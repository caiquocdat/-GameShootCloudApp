package com.caiquocdat.shootclouds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.caiquocdat.shootclouds.databinding.ActivityMainBinding;
import com.caiquocdat.shootclouds.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding activitySettingBinding;
    private static final String PREFS_NAME = "app_prefs";
    private static final String STATE_KEY = "turn_state";
    public static MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = activitySettingBinding.getRoot();
        setContentView(view);
        hideSystemUI();
        boolean isOn = getState();  // Đọc trạng thái ban đầu
        updateUI(isOn);
        activitySettingBinding.monkeyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra("icon",1);
                startActivity(intent);
                stopMusic();
            }
        });
        activitySettingBinding.pineappleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra("icon",2);
                startActivity(intent);
                stopMusic();
            }
        });
        activitySettingBinding.beeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra("icon",3);
                startActivity(intent);
                stopMusic();
            }
        });
        activitySettingBinding.shitImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra("icon",4);
                startActivity(intent);
                stopMusic();
            }
        });

        activitySettingBinding.turnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newState = !getState();  // Đảo trạng thái
                saveState(newState);  // Lưu trạng thái mới
                updateUI(newState);   // Cập nhật giao diện theo trạng thái mới
            }
        });
    }
    private void updateUI(boolean isOn) {
        // Cập nhật giao diện tùy vào giá trị của isOn.
        // Ví dụ: nếu là hình ảnh, bạn có thể thay đổi hình ảnh tương ứng.
        if (isOn) {
            playMusic();
            activitySettingBinding.turnImg.setImageResource(R.drawable.img_music_on);
        } else {
            stopMusic();
            activitySettingBinding.turnImg.setImageResource(R.drawable.img_music_off);
        }
    }
    private void saveState(boolean isOn) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(STATE_KEY, isOn);
        editor.apply();
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
    protected void onResume() {
        super.onResume();
        hideSystemUI();
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

    }
}