package com.caiquocdat.shootclouds;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.caiquocdat.shootclouds.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView movingImage;
    private ValueAnimator animator;
    private Handler handler = new Handler();
    private ActivityMainBinding activityMainBinding;
    private boolean isGameOver = false;
    private int pendingExplosions = 0;
    private List<ValueAnimator> cloudAnimators = new ArrayList<>();
    private CountDownTimer countDownTimer;
    private int countPoint=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        hideSystemUI();
        createAndAnimateImage();
        startCountdown();
        activityMainBinding.gameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySpringEffect();
                shootBullet();
            }
        });
        activityMainBinding.rankImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(MainActivity.this,RankActivity.class);
                startActivity(intent);
            }
        });
        activityMainBinding.settingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });


    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(30000, 1000) { // Đếm ngược từ 10 giây, mỗi tick 1 giây
            @Override
            public void onTick(long millisUntilFinished) {
                activityMainBinding.timeTv.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                activityMainBinding.timeTv.setText("0s");
                // Các hành động khác khi thời gian hết (nếu cần)
                isGameOver = true;
                Toast.makeText(MainActivity.this, "Bạn đã hết thời gian", Toast.LENGTH_SHORT).show();
                animator.cancel();
                handler.removeCallbacksAndMessages(null);  // Ngăn chặn việc tạo thêm img_cloud
                clearAllClouds();
                Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
                intent.putExtra("point",countPoint);
                startActivity(intent);

            }
        }.start();
    }

    private void shootBullet() {
        final ImageView bullet = new ImageView(this);
        bullet.setImageResource(R.drawable.img_bullet_1);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bullet.setLayoutParams(params);
        activityMainBinding.gameLayout.addView(bullet);
        bullet.setTranslationY(0);  // Đặt vị trí ban đầu ở dưới cùng của màn hình

        float destination = -activityMainBinding.gameLayout.getHeight(); // Điểm đích cho viên đạn
        ValueAnimator bulletAnimator = ValueAnimator.ofFloat(0, destination);
        bulletAnimator.setDuration(2000);  // Thời gian viên đạn di chuyển
        bulletAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            bullet.setTranslationY(value);

            // Kiểm tra va chạm tại đây
            checkCollision(bullet,bulletAnimator);
        });
        bulletAnimator.start();
    }

    private void checkCollision(ImageView bullet,ValueAnimator bulletAnimator) {
        for (int i = 0; i < activityMainBinding.gameLayout.getChildCount(); i++) {
            View child = activityMainBinding.gameLayout.getChildAt(i);
            if (child instanceof ImageView && ((ImageView) child).getDrawable().getConstantState() == getResources().getDrawable(R.drawable.img_cloud_new).getConstantState()) {
                // Kiểm tra nếu bullet va chạm với img_cloud
                if (isViewsOverlapping(bullet, child)) {
                    // Hiệu ứng nổ tại đây và xóa img_cloud
                    bulletAnimator.cancel();
                    explodeAndRemoveCloud((ImageView) child);

                    // Xóa img_bullet khỏi layout
                    activityMainBinding.gameLayout.removeView(bullet);

                    return;  // Dừng việc kiểm tra nếu đã tìm thấy va chạm
                }
            }
        }
    }
    private void applySpringEffect() {
        // Lò xo dãn hết mức
        activityMainBinding.imgSpring.setImageResource(R.drawable.img_spring_default);
        handler.postDelayed(() -> {
            // Lò xo ép hết mức
            activityMainBinding.imgSpring.setImageResource(R.drawable.img_spring_compressed);
            handler.postDelayed(() -> {
                // Lò xo dãn ra 1 phần nhỏ
                activityMainBinding.imgSpring.setImageResource(R.drawable.img_spring_partially_stretched);
                handler.postDelayed(() -> {
                    // Lò xo dãn hết mức
                    activityMainBinding.imgSpring.setImageResource(R.drawable.img_spring_default);
                }, 200); // Thời gian chờ trước khi chuyển đến hình tiếp theo
            }, 200);
        }, 200);
    }


    private void explodeAndRemoveCloud(ImageView cloud) {
        ValueAnimator cloudAnimator = (ValueAnimator) cloud.getTag();
        if (cloudAnimator != null && cloudAnimator.isRunning()) {
            cloudAnimator.cancel();
        }
        cloud.setImageResource(R.drawable.img_explosion);
        pendingExplosions++;
        countPoint++;

        // After a short duration, remove the cloud
        new Handler().postDelayed(() -> {
            activityMainBinding.gameLayout.removeView(cloud);
            pendingExplosions--;  // Decrease the number of clouds that are currently exploding
        }, 500);
    }


    private boolean isViewsOverlapping(View view1, View view2) {
        int[] firstPos = new int[2];
        int[] secondPos = new int[2];

        view1.getLocationOnScreen(firstPos);
        view2.getLocationOnScreen(secondPos);

        return firstPos[0] < secondPos[0] + view2.getWidth() &&
                firstPos[0] + view1.getWidth() > secondPos[0] &&
                firstPos[1] < secondPos[1] + view2.getHeight() &&
                firstPos[1] + view1.getHeight() > secondPos[1];
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

    private void createAndAnimateImage() {
        if (isGameOver) {
            return;
        }
        final ImageView movingImage = new ImageView(this);
        movingImage.setImageResource(R.drawable.img_cloud_new);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        movingImage.setLayoutParams(params);

        activityMainBinding.gameLayout.addView(movingImage, 0);

        movingImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                movingImage.getViewTreeObserver().removeOnPreDrawListener(this);
                movingImage.setTranslationY(-movingImage.getHeight());

                animator = ValueAnimator.ofFloat(0, 1);
                movingImage.setTag(animator);
                cloudAnimators.add(animator);
                animator.setDuration(20000);
                animator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    movingImage.setTranslationY(activityMainBinding.gameLayout.getHeight() * value);

                    if (movingImage.getTranslationY() > activityMainBinding.gameLayout.getHeight() - movingImage.getHeight() && pendingExplosions == 0) {
                        if(!isGameOver) {
                            isGameOver = true;
                            Toast.makeText(MainActivity.this, "Bạn đã thua", Toast.LENGTH_SHORT).show();
                            animator.cancel();
                            handler.removeCallbacksAndMessages(null);  // Ngăn chặn việc tạo thêm img_cloud
                            clearAllClouds();
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
                            intent.putExtra("point",countPoint);
                            startActivity(intent);
                        }
                    }
                });
                animator.start();
                return true;
            }
        });

        handler.postDelayed(this::createAndAnimateImage, 1000);
    }
    private boolean isAnyCloudLeft() {
        for (int i = 0; i < activityMainBinding.gameLayout.getChildCount(); i++) {
            View child = activityMainBinding.gameLayout.getChildAt(i);
            if (child instanceof ImageView && ((ImageView) child).getDrawable().getConstantState() == getResources().getDrawable(R.drawable.img_cloud_new).getConstantState()) {
                return true;
            }
        }
        return false;
    }

    private void clearAllClouds() {
        for (int i = 0; i < activityMainBinding.gameLayout.getChildCount(); i++) {
            View child = activityMainBinding.gameLayout.getChildAt(i);
            if (child instanceof ImageView && ((ImageView) child).getDrawable().getConstantState() == getResources().getDrawable(R.drawable.img_cloud_new).getConstantState()) {
                activityMainBinding.gameLayout.removeViewAt(i);
                i--; // giảm biến đếm để tránh bỏ sót khi xóa view
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animator != null) {
            animator.cancel();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
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