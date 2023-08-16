package com.caiquocdat.shootclouds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.caiquocdat.shootclouds.databinding.ActivityHomeBinding;
import com.caiquocdat.shootclouds.databinding.ActivityMainBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding activityHomeBinding;
    private static final String PREFS_NAME = "app_prefs";
    private static final String STATE_KEY = "turn_state";
    public static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = activityHomeBinding.getRoot();
        setContentView(view);
        hideSystemUI();
        activityHomeBinding.playImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                stopMusic();
            }
        });
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