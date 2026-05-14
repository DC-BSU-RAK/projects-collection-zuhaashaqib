package com.example.moodmixer2000;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        TextView tvWelcome = findViewById(R.id.tv_welcome);
        String username = ThemeHelper.getUsername(this);
        if (tvWelcome != null && !username.isEmpty()) {
            tvWelcome.setText("Hey, " + username + "! 🌟 Pick your vibe:");
        }


        setupMoodButton(R.id.btn_mood_happy, "HAPPY");
        setupMoodButton(R.id.btn_mood_sad, "SAD");
        setupMoodButton(R.id.btn_mood_crush, "CRUSH");
        setupMoodButton(R.id.btn_mood_angry, "ANGRY");
        setupMoodButton(R.id.btn_mood_nostalgic, "NOSTALGIC");


        findViewById(R.id.btn_nav_favorites).setOnClickListener(v -> {
            SoundHelper.play(this, R.raw.click_sound);
            startActivity(new Intent(this, FavoritesActivity.class));
        });

        findViewById(R.id.btn_nav_settings).setOnClickListener(v -> {
            SoundHelper.play(this, R.raw.click_sound);
            startActivity(new Intent(this, SettingsActivity.class));
        });

        if (ThemeHelper.isFirstLaunch(this)) {
            ThemeHelper.setFirstLaunchDone(this);
            showVibeDialog();
        }
    }

    private void setupMoodButton(int id, String mood) {
        Button btn = findViewById(id);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                SoundHelper.play(this, R.raw.click_sound);
                Intent intent = new Intent(this, PlaylistActivity.class);
                intent.putExtra("mood", mood);
                startActivity(intent);
            });
        }
    }

    private void showVibeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_vibe);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        Button btnGotIt = dialog.findViewById(R.id.btn_dialog_gotit);
        if (btnGotIt != null) btnGotIt.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}