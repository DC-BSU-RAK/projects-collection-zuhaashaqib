package com.example.moodmixer2000;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION_MS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen Mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        SoundHelper.play(this, R.raw.startup_sound);

        ImageView logo = findViewById(R.id.splash_logo);
        TextView loadingText = findViewById(R.id.splash_loading_text);
        TextView titleText = findViewById(R.id.splash_title);

        logo.setAlpha(0f);
        logo.setScaleX(0.7f);
        logo.setScaleY(0.7f);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0.7f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.7f, 1f);

        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.playTogether(fadeIn, scaleX, scaleY);
        logoAnim.setDuration(1000);
        logoAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        logoAnim.start();

        if (titleText != null) {
            titleText.setAlpha(0f);
            titleText.setTranslationY(100f);
            titleText.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(500).start();
        }

        if (loadingText != null) {
            loadingText.setAlpha(0f);
            loadingText.animate().alpha(1f).setDuration(500).setStartDelay(1000).start();
            startRetroFlicker(loadingText);
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String username = ThemeHelper.getUsername(this);
            Intent intent = (username == null || username.isEmpty())
                    ? new Intent(this, LoginActivity.class)
                    : new Intent(this, MoodActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DURATION_MS);
    }

    private void startRetroFlicker(TextView tv) {
        final Handler flickerHandler = new Handler(Looper.getMainLooper());
        flickerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) return; // Stop if activity is closing
                tv.setAlpha(0.4f);
                new Handler(Looper.getMainLooper()).postDelayed(() -> tv.setAlpha(1.0f), 100);
                flickerHandler.postDelayed(this, 400);
            }
        });
    }
}