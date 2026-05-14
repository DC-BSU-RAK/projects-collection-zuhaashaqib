package com.example.moodmixer2000;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class SoundHelper {

    public static void play(Context ctx, int rawResId) {

        if (!ThemeHelper.isSoundEnabled(ctx)) {
            Log.d("SoundHelper", "Sound blocked: Disabled in Settings");
            return;
        }

        try {
            MediaPlayer mp = MediaPlayer.create(ctx, rawResId);
            if (mp != null) {

                mp.setOnCompletionListener(MediaPlayer::release);
                mp.start();
                Log.d("SoundHelper", "Playing resource: " + rawResId);
            } else {
                Log.e("SoundHelper", "Failed to create MediaPlayer - check if file exists in res/raw");
            }
        } catch (Exception e) {
            Log.e("SoundHelper", "Error playing sound", e);
        }
    }
}