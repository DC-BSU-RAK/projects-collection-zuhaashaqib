package com.example.moodmixer2000;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class ThemeHelper {

    public static final String PREFS_NAME   = "MoodMixerPrefs";
    public static final String KEY_THEME    = "theme";
    public static final String KEY_SOUND    = "sound_enabled";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FIRST_LAUNCH = "first_launch";

    // Theme constants — match these in settings
    public static final String THEME_PINK  = "pink";
    public static final String THEME_BLUE  = "blue";
    public static final String THEME_GREEN = "green";

    /**
     * Call this at the TOP of every Activity's onCreate(), before setContentView().
     */
    public static void applyTheme(AppCompatActivity activity) {
        String theme = getSavedTheme(activity);
        switch (theme) {
            case THEME_BLUE:
                activity.setTheme(R.style.Theme_MoodMixer2000_Blue);
                break;
            case THEME_GREEN:
                activity.setTheme(R.style.Theme_MoodMixer2000_Green);
                break;
            default: // THEME_PINK
                activity.setTheme(R.style.Theme_MoodMixer2000_Pink);
                break;
        }
    }

    public static String getSavedTheme(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_THEME, THEME_PINK);
    }

    public static void saveTheme(Context ctx, String theme) {
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_THEME, theme).apply();
    }

    public static boolean isSoundEnabled(Context ctx) {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_SOUND, true);
    }

    public static void setSoundEnabled(Context ctx, boolean enabled) {
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_SOUND, enabled).apply();
    }

    public static String getUsername(Context ctx) {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_USERNAME, "");
    }

    public static void saveUsername(Context ctx, String name) {
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_USERNAME, name).apply();
    }

    public static boolean isFirstLaunch(Context ctx) {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public static void setFirstLaunchDone(Context ctx) {
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }
}