package com.clearlee.playwhiledownloadmusic;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.clearlee.playwhiledownloadmusic.proxy.MediaPlayerProxy;
import com.clearlee.playwhiledownloadmusic.util.Common;
import com.clearlee.playwhiledownloadmusic.util.LogTool;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //音源地址可能过期，导致无法播放，请换成自己的地址测试
    public static final String TEST_URL = "http://dl.stream.qqmusic.qq.com/C400002qU5aY3Qu24y.m4a?fromtag=38&vkey=170BABBE23148CD14BEEC17094FC2816B8B852EFD5D29A6E43760B7F6FFDDE10934AACE139C2E9E6284BE58111B46BFCC1E9C4B69A283D6D&guid=9164044848";

    public static final int STATE_NORMAL = 0;
    public static final int STATE_PLAY = 1;

    public MediaPlayer mediaPlayer;
    public MediaPlayerProxy mediaPlayerHttpProxy;//当前播放器的在线播放代理（用于边播边存等处理）
    private ImageView ivMusicPlay;
    private SeekBar sb_music;
    private TextView currTime, totalTime;

    private Handler refreshPlayDegreeHandler = new Handler();
    private int currentState;
    private int currMusicProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
        initMediaPlayer();
    }

    private void initView() {
        ivMusicPlay = (ImageView) findViewById(R.id.iv_music_play);
        sb_music = (SeekBar) findViewById(R.id.sb_music);
        currTime = (TextView) findViewById(R.id.tv_curr_play_time);
        totalTime = (TextView) findViewById(R.id.tv_total_play_time);
        ivMusicPlay.setOnClickListener(this);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.reset();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                totalTime.setText(Common.getSecDuration2HMSFormatString(mediaPlayer.getDuration() / 1000));
                mediaPlayer.start();
                refreshPlayDegreeTask();
            }
        });
    }

    private void refreshPlayDegreeTask() {
        refreshPlayDegreeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updatePlayingTime();
            }
        }, 1000);
    }

    private void updatePlayingTime() {
        try {
            int poistion = mediaPlayer.getCurrentPosition();
            currTime.setText(Common.getSecDuration2HMSFormatString(poistion / 1000));
            currMusicProgress = (int) (Common.div(poistion, mediaPlayer.getDuration(), 5) * 100);
            if (currMusicProgress <= 100) {
                sb_music.setProgress(currMusicProgress);
                refreshPlayDegreeTask();
            }
        } catch (Exception e) {
            LogTool.ex(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_music_play:
                startPlay();
                break;
        }
    }

    private void startPlay() {
        switch (currentState) {
            case STATE_NORMAL:
                playMusic();
                break;
            case STATE_PLAY:
                stopMusic();
                break;
        }
    }

    private void stopMusic() {
        ivMusicPlay.setImageResource(R.mipmap.music_button_play);
        currentState = STATE_NORMAL;
        mediaPlayer.pause();
    }

    private void playMusic() {
        ivMusicPlay.setImageResource(R.mipmap.music_button_pause);
        currentState = STATE_PLAY;
        if (mediaPlayer != null && mediaPlayer.getCurrentPosition() > 0) {
            mediaPlayerHttpProxy = null;
            mediaPlayer.start();
        } else {
            try {
                mediaPlayerHttpProxy = new MediaPlayerProxy("青花瓷", false);
                mediaPlayerHttpProxy.setOnCaChedProgressUpdateListener(new MediaPlayerProxy.OnCaChedProgressUpdateListener() {
                    @Override
                    public void updateCachedProgress(int progress) {
                        sb_music.setSecondaryProgress(progress);
                    }
                });
                String localProxyUrl = mediaPlayerHttpProxy.getLocalURLAndSetRemotSocketAddr(TEST_URL);
                mediaPlayerHttpProxy.startProxy();
                mediaPlayer.setDataSource(localProxyUrl);
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                LogTool.ex(e);
            }
        }
    }
}
