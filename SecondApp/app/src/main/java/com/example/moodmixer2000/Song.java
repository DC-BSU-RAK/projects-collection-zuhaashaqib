package com.example.moodmixer2000;

import org.json.JSONException;
import org.json.JSONObject;

public class Song {
    private String title;
    private String artist;
    private String youtubeUrl;
    private String mood;

    public Song(String title, String artist, String youtubeUrl, String mood) {
        this.title = title;
        this.artist = artist;
        this.youtubeUrl = youtubeUrl;
        this.mood = mood;
    }


    public String getTitle()      { return title; }
    public String getArtist()     { return artist; }
    public String getYoutubeUrl() { return youtubeUrl; }
    public String getMood()       { return mood; }

    // ---- JSON Serialization (for SharedPreferences storage) ----
    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("artist", artist);
        obj.put("youtubeUrl", youtubeUrl);
        obj.put("mood", mood);
        return obj;
    }

    public static Song fromJson(JSONObject obj) throws JSONException {
        return new Song(
                obj.getString("title"),
                obj.getString("artist"),
                obj.getString("youtubeUrl"),
                obj.getString("mood")
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song s = (Song) o;
        return title.equals(s.title) && artist.equals(s.artist);
    }
}