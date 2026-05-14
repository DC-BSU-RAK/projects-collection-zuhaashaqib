package com.example.moodmixer2000;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat; // FIXED: Use SwitchCompat to match your XML

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView tvUsername = findViewById(R.id.tv_settings_username);
        if (tvUsername != null) {
            tvUsername.setText("Screen name: " + ThemeHelper.getUsername(this) + " ✨");
        }


        Button btnPink  = findViewById(R.id.btn_theme_pink);
        Button btnBlue  = findViewById(R.id.btn_theme_blue);
        Button btnGreen = findViewById(R.id.btn_theme_green);

        if (btnPink != null && btnBlue != null && btnGreen != null) {
            highlightCurrentTheme(btnPink, btnBlue, btnGreen);

            btnPink.setOnClickListener(v -> selectTheme(ThemeHelper.THEME_PINK,  btnPink, btnBlue, btnGreen));
            btnBlue.setOnClickListener(v -> selectTheme(ThemeHelper.THEME_BLUE,  btnPink, btnBlue, btnGreen));
            btnGreen.setOnClickListener(v -> selectTheme(ThemeHelper.THEME_GREEN, btnPink, btnBlue, btnGreen));
        }


        SwitchCompat switchSound = findViewById(R.id.switch_sound);
        if (switchSound != null) {
            switchSound.setChecked(ThemeHelper.isSoundEnabled(this));
            switchSound.setOnCheckedChangeListener((btn, isChecked) -> {
                ThemeHelper.setSoundEnabled(this, isChecked);
                Toast.makeText(this,
                        isChecked ? "Sound ON 🔊" : "Sound OFF 🔇",
                        Toast.LENGTH_SHORT).show();
            });
        }


        Button btnChangeName = findViewById(R.id.btn_change_name);
        if (btnChangeName != null) {
            btnChangeName.setOnClickListener(v -> {
                ThemeHelper.saveUsername(this, "");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }


        Button btnBack = findViewById(R.id.btn_settings_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void selectTheme(String theme, Button pink, Button blue, Button green) {
        SoundHelper.play(this, R.raw.click_sound);
        ThemeHelper.saveTheme(this, theme);
        Toast.makeText(this, "Theme saved! Restarting... ✨", Toast.LENGTH_SHORT).show();
        highlightCurrentTheme(pink, blue, green);
        recreate(); // Applies the theme immediately
    }

    private void highlightCurrentTheme(Button pink, Button blue, Button green) {
        String current = ThemeHelper.getSavedTheme(this);
        pink.setAlpha(current.equals(ThemeHelper.THEME_PINK)  ? 1.0f : 0.5f);
        blue.setAlpha(current.equals(ThemeHelper.THEME_BLUE)  ? 1.0f : 0.5f);
        green.setAlpha(current.equals(ThemeHelper.THEME_GREEN) ? 1.0f : 0.5f);
    }
}