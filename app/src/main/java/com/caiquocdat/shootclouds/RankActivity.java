package com.caiquocdat.shootclouds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.caiquocdat.shootclouds.adapter.ScoreAdapter;
import com.caiquocdat.shootclouds.data.ScoreDataSource;
import com.caiquocdat.shootclouds.databinding.ActivityMainBinding;
import com.caiquocdat.shootclouds.databinding.ActivityRankBinding;
import com.caiquocdat.shootclouds.model.ScoreModel;

import java.util.List;

public class RankActivity extends AppCompatActivity {
    private ActivityRankBinding activityRankBinding;
    private static final String PREFS_NAME = "app_prefs";
    private static final String STATE_KEY = "turn_state";
    private MediaPlayer mediaPlayer;
    private ScoreAdapter adapter;
    private List<ScoreModel> scores;
    private ScoreDataSource scoreDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRankBinding = ActivityRankBinding.inflate(getLayoutInflater());
        View view = activityRankBinding.getRoot();
        setContentView(view);
        hideSystemUI();
        setUpRcv();
    }

    private void setUpRcv() {
        activityRankBinding.rankRcv.setLayoutManager(new LinearLayoutManager(this));
        scoreDataSource = new ScoreDataSource(this);
        scoreDataSource.open();  // Mở kết nối tới database
        scores = scoreDataSource.getAllScores();
        adapter = new ScoreAdapter(this, scores);
        activityRankBinding.rankRcv.setAdapter(adapter);
    }

    private void checkMusic() {
        boolean isOn = getState();
        if (isOn) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    private void playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.musicgame);
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    private boolean getState() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(STATE_KEY, false); // Giá trị mặc định là false (off)
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        checkMusic();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopMusic();
    }
}