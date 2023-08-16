package com.caiquocdat.shootclouds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.caiquocdat.shootclouds.data.ScoreDataSource;
import com.caiquocdat.shootclouds.databinding.ActivityGameOverBinding;
import com.caiquocdat.shootclouds.databinding.ActivityHomeBinding;

public class GameOverActivity extends AppCompatActivity {
    ActivityGameOverBinding activityGameOverBinding;
    private int point;
    private static final String PREFS_NAME = "app_prefs";
    private static final String STATE_KEY = "turn_state";
    private MediaPlayer mediaPlayer;
    private ScoreDataSource scoreDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGameOverBinding = ActivityGameOverBinding.inflate(getLayoutInflater());
        View view = activityGameOverBinding.getRoot();
        setContentView(view);
        hideSystemUI();
        setUpDataPoint();
        activityGameOverBinding.playAgainImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(GameOverActivity.this,MainActivity.class);
                startActivity(intent);
                stopMusic();
            }
        });
    }

    private void setUpDataPoint() {
        scoreDataSource = new ScoreDataSource(this);
        scoreDataSource.open();
        Intent intent=getIntent();
        point=intent.getIntExtra("point",0);
        activityGameOverBinding.pointTv.setText(point+"");
        scoreDataSource.checkAndUpdateScores(point);

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
    private boolean getState() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(STATE_KEY, false); // Giá trị mặc định là false (off)
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        scoreDataSource.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        checkMusic();
    }

    @Override
    public void onBackPressed() {

    }
}