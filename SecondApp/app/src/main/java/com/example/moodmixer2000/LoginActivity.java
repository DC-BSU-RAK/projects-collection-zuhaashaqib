package com.example.moodmixer2000;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etName = findViewById(R.id.et_screen_name);
        Button   btnGo  = findViewById(R.id.btn_lets_go);


        String existingName = ThemeHelper.getUsername(this);
        if (!existingName.isEmpty()) {
            etName.setText(existingName);
        }

        btnGo.setOnClickListener(v -> {
            SoundHelper.play(this, R.raw.click_sound);
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Gotta enter a screen name! 💅", Toast.LENGTH_SHORT).show();
                return;
            }
            ThemeHelper.saveUsername(this, name);
            Intent intent = new Intent(this, MoodActivity.class);
            startActivity(intent);
            finish();
        });
    }
}