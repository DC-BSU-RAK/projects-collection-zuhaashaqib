package com.example.moodmixer2000;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private FavoritesAdapter adapter;
    private List<Song> favorites;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        tvEmpty = findViewById(R.id.tv_no_favorites);
        RecyclerView rv = findViewById(R.id.rv_favorites);
        rv.setLayoutManager(new LinearLayoutManager(this));

        favorites = FavoritesManager.getFavorites(this);
        adapter   = new FavoritesAdapter(favorites);
        rv.setAdapter(adapter);

        updateEmptyState();

        Button btnBack = findViewById(R.id.btn_favorites_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void updateEmptyState() {
        if (favorites.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.VH> {

        private final List<Song> songs;
        FavoritesAdapter(List<Song> songs) { this.songs = songs; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_song, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Song song = songs.get(position);
            holder.tvTitle.setText(song.getTitle());
            holder.tvArtist.setText(song.getArtist() + " • " + song.getMood());

            holder.btnYt.setOnClickListener(v -> {
                SoundHelper.play(FavoritesActivity.this, R.raw.click_sound);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(song.getYoutubeUrl()));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
            });

            holder.btnRemove.setOnClickListener(v -> {
                SoundHelper.play(FavoritesActivity.this, R.raw.click_sound);
                int pos = holder.getAdapterPosition();
                Song removed = songs.get(pos);
                FavoritesManager.removeFavorite(FavoritesActivity.this, removed);
                songs.remove(pos);
                notifyItemRemoved(pos);
                Toast.makeText(FavoritesActivity.this, removed.getTitle() + " removed 💔", Toast.LENGTH_SHORT).show();
                updateEmptyState();
            });
        }

        @Override
        public int getItemCount() { return songs.size(); }

        class VH extends RecyclerView.ViewHolder {
            TextView tvTitle, tvArtist;
            Button   btnYt, btnRemove;

            VH(View itemView) {
                super(itemView);
                tvTitle   = itemView.findViewById(R.id.item_song_title);
                tvArtist  = itemView.findViewById(R.id.item_song_artist);
                btnYt     = itemView.findViewById(R.id.item_btn_yt);
                btnRemove = itemView.findViewById(R.id.item_btn_remove);
            }
        }
    }
}