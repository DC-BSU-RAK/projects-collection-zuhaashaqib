package com.example.moodmixer2000;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {

    private static final String KEY_FAVORITES = "favorites_json";

    public static List<Song> getFavorites(Context ctx) {
        List<Song> songs = new ArrayList<>();
        SharedPreferences prefs = ctx.getSharedPreferences(
                ThemeHelper.PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_FAVORITES, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                songs.add(Song.fromJson(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songs;
    }

    public static void addFavorite(Context ctx, Song song) {
        List<Song> songs = getFavorites(ctx);
        for (Song s : songs) {
            if (s.equals(song)) return;
        }
        songs.add(song);
        saveFavorites(ctx, songs);
    }

    public static void removeFavorite(Context ctx, Song song) {
        List<Song> songs = getFavorites(ctx);
        songs.removeIf(s -> s.equals(song));
        saveFavorites(ctx, songs);
    }

    public static boolean isFavorite(Context ctx, Song song) {
        for (Song s : getFavorites(ctx)) {
            if (s.equals(song)) return true;
        }
        return false;
    }

    private static void saveFavorites(Context ctx, List<Song> songs) {
        JSONArray arr = new JSONArray();
        for (Song s : songs) {
            try { arr.put(s.toJson()); } catch (JSONException e) { e.printStackTrace(); }
        }
        ctx.getSharedPreferences(ThemeHelper.PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_FAVORITES, arr.toString()).apply();
    }
}