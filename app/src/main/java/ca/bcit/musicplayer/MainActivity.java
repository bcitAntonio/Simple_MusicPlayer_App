package ca.bcit.musicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity {

    SeekBar musicBar;
    Button playButton;
    Button prevButton;
    Button nextButton;
    SeekBar volumeBar;
    TextView startTime;
    TextView endTime;
    int playListIndex;
    ArrayList<Integer> playList;

    MediaPlayer mediaPlayer;
    int musicLength;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicBar = findViewById(R.id.musicBar);
        playButton = findViewById(R.id.playButton);
        prevButton = findViewById(R.id.preButton);
        nextButton = findViewById(R.id.nextButton);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        playList = new ArrayList<>();
        add();

        mediaPlayer = MediaPlayer.create(this, playList.get(playListIndex));
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(.5F, 0.5F);
        musicLength = mediaPlayer.getDuration();


        musicBar.setMax(musicLength);
        musicBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                            musicBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeChanged = progress / 100f;
                        mediaPlayer.setVolume(volumeChanged, volumeChanged);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            musicBar.setProgress(currentPosition);

            String startTime_ = timeLabel(currentPosition);
            endTime.setText(startTime_);

            String endTime_ = timeLabel(musicLength - currentPosition);
            startTime.setText("-" + endTime_);
        }
    };

    public String timeLabel(int time) {
        String timeLabel_ = "";
        int minimum = time / 1000 / 60;
        int second = time / 1000 % 60;

        timeLabel_ = minimum + ":";

        if (second < 10) timeLabel_ += "0";
        timeLabel_ += second;

        return timeLabel_;
    }


    public void playButton(final View view) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playButton.setBackgroundResource(R.drawable.stop);
        } else {
            mediaPlayer.pause();
            playButton.setBackgroundResource(R.drawable.play);
        }
    }

    public void preButton(final View view) {
        if (!mediaPlayer.isPlaying()) {
            prev_playlist_loop();
        } else {
            mediaPlayer.pause();
            prev_playlist_loop();
        }
    }

    public void prev_playlist_loop()
    {
        if(playListIndex != 0)
        {
            playListIndex--;
            mediaPlayer = MediaPlayer.create(this, playList.get(playListIndex));
        }
        else
        {
            playListIndex = (playList.size() - 1);
            mediaPlayer = MediaPlayer.create(this, playList.get(playListIndex));
        }
        mediaPlayer.start();
    }

    public void nextButton(final View view) {
        if (!mediaPlayer.isPlaying()) {
            next_playlist_loop();
        } else {
            mediaPlayer.pause();
            next_playlist_loop();
        }
    }

    public void next_playlist_loop()
    {
        if(playListIndex != (playList.size() - 1))
        {
            playListIndex++;
            mediaPlayer = MediaPlayer.create(this, playList.get(playListIndex));
        }
        else
        {
            playListIndex = 0;
            mediaPlayer = MediaPlayer.create(this, playList.get(playListIndex));
        }
        mediaPlayer.start();
    }

    public void add()
    {
        playListIndex = 2;
        playList.add(R.raw.a);
        playList.add(R.raw.b);
        playList.add(R.raw.c);
        playList.add(R.raw.d);
        playList.add(R.raw.e);
        playList.add(R.raw.f);
    }

}