package com.caiquocdat.shootclouds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.caiquocdat.shootclouds.databinding.ActivityGameOverBinding;
import com.caiquocdat.shootclouds.databinding.ActivityHomeBinding;

public class GameOverActivity extends AppCompatActivity {
    ActivityGameOverBinding activityGameOverBinding;
    private int point;

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
            }
        });
    }

    private void setUpDataPoint() {
        Intent intent=getIntent();
        point=intent.getIntExtra("point",0);
        activityGameOverBinding.pointTv.setText(point+"");
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
    }

    @Override
    public void onBackPressed() {

    }
}