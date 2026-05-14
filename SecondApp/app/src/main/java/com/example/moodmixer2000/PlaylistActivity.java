package com.example.moodmixer2000;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        String mood = getIntent().getStringExtra("mood");
        if (mood == null) mood = "HAPPY";

        TextView tvTitle = findViewById(R.id.tv_playlist_title);
        if (tvTitle != null) {
            tvTitle.setText(getMoodTitle(mood));
        }

        List<Song> songs = getSongsForMood(mood);

        if (songs.size() >= 3) {
            bindSong(songs.get(0), R.id.tv_song1_title, R.id.tv_song1_artist, R.id.btn_yt1, R.id.btn_heart1);
            bindSong(songs.get(1), R.id.tv_song2_title, R.id.tv_song2_artist, R.id.btn_yt2, R.id.btn_heart2);
            bindSong(songs.get(2), R.id.tv_song3_title, R.id.tv_song3_artist, R.id.btn_yt3, R.id.btn_heart3);
        }

        Button btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                SoundHelper.play(this, R.raw.click_sound);
                finish();
            });
        }
    }

    private void bindSong(Song song, int titleId, int artistId, int ytBtnId, int heartBtnId) {
        TextView tvTitle  = findViewById(titleId);
        TextView tvArtist = findViewById(artistId);
        Button   btnYt    = findViewById(ytBtnId);
        ImageButton btnHeart = findViewById(heartBtnId);

        if (tvTitle != null) tvTitle.setText(song.getTitle());
        if (tvArtist != null) tvArtist.setText(song.getArtist());

        if (btnHeart != null) {
            updateHeartIcon(btnHeart, FavoritesManager.isFavorite(this, song));
            btnHeart.setOnClickListener(v -> {
                SoundHelper.play(this, R.raw.click_sound);
                boolean currentlyFav = FavoritesManager.isFavorite(this, song);
                if (currentlyFav) {
                    FavoritesManager.removeFavorite(this, song);
                    updateHeartIcon(btnHeart, false);
                    Toast.makeText(this, "Removed from faves 💔", Toast.LENGTH_SHORT).show();
                } else {
                    FavoritesManager.addFavorite(this, song);
                    updateHeartIcon(btnHeart, true);
                    Toast.makeText(this, "Added to faves! 💖", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // YOUTUBE INTENT
        if (btnYt != null) {
            btnYt.setOnClickListener(v -> {
                SoundHelper.play(this, R.raw.click_sound);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(song.getYoutubeUrl()));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
            });
        }
    }

    private void updateHeartIcon(ImageButton btn, boolean isFavorite) {
        btn.setImageResource(isFavorite
                ? android.R.drawable.btn_star_big_on
                : android.R.drawable.btn_star_big_off);

        int tintColor = isFavorite
                ? getResources().getColor(R.color.pink_primary, getTheme())
                : getResources().getColor(R.color.pink_text, getTheme());

        btn.setColorFilter(tintColor);
    }

    private String getMoodTitle(String mood) {
        switch (mood) {
            case "HAPPY":     return "Your Happy Vibe! 😄";
            case "SAD":       return "Feeling Blue... 😢";
            case "CRUSH":     return "Crush Mode ON 😍";
            case "ANGRY":     return "Let It Out! 😤";
            case "NOSTALGIC": return "Total Throwback 🦋";
            default:          return "Your Vibe! ✨";
        }
    }

    private List<Song> getSongsForMood(String mood) {
        List<Song> list = new ArrayList<>();
        switch (mood) {
            case "HAPPY":
                list.add(new Song("Hit Me Baby One More Time", "Britney Spears", "https://www.youtube.com/results?search_query=baby+one+more+time+britney", "HAPPY"));
                list.add(new Song("Get The Party Started", "P!nk", "https://www.youtube.com/results?search_query=get+the+party+started+pink", "HAPPY"));
                list.add(new Song("I Want It That Way", "Backstreet Boys", "https://www.youtube.com/results?search_query=i+want+it+that+way+backstreet", "HAPPY"));
                break;
            case "SAD":
                list.add(new Song("Cry Me A River", "Justin Timberlake", "https://www.youtube.com/results?search_query=cry+me+a+river+justin", "SAD"));
                list.add(new Song("I'm With You", "Avril Lavigne", "https://www.youtube.com/results?search_query=im+with+you+avril", "SAD"));
                list.add(new Song("Behind These Hazel Eyes", "Kelly Clarkson", "https://www.youtube.com/results?search_query=behind+these+hazel+eyes+kelly", "SAD"));
                break;
            case "CRUSH":
                list.add(new Song("Bye Bye Bye", "*NSYNC", "https://www.youtube.com/results?search_query=bye+bye+bye+nsync", "CRUSH"));
                list.add(new Song("All The Small Things", "Blink-182", "https://www.youtube.com/results?search_query=all+the+small+things+blink", "CRUSH"));
                list.add(new Song("Lovefool", "The Cardigans", "https://www.youtube.com/results?search_query=lovefool+cardigans", "CRUSH"));
                break;
            case "ANGRY":
                list.add(new Song("In The End", "Link Park", "https://www.youtube.com/results?search_query=in+the+end+linkin+park", "ANGRY"));
                list.add(new Song("Bring Me To Life", "Evanescence", "https://www.youtube.com/results?search_query=bring+me+to+life+evanescence", "ANGRY"));
                list.add(new Song("Sk8er Boi", "Avril Lavigne", "https://www.youtube.com/results?search_query=sk8er+boi+avril", "ANGRY"));
                break;
            case "NOSTALGIC":
            default:
                list.add(new Song("Genie In A Bottle", "Christina Aguilera", "https://www.youtube.com/results?search_query=genie+in+a+bottle+christina", "NOSTALGIC"));
                list.add(new Song("Mambo No. 5", "Lou Bega", "https://www.youtube.com/results?search_query=mambo+number+5+lou+bega", "NOSTALGIC"));
                list.add(new Song("Wannabe", "Spice Girls", "https://www.youtube.com/results?search_query=wannabe+spice+girls", "NOSTALGIC"));
                break;
        }
        return list;
    }
}