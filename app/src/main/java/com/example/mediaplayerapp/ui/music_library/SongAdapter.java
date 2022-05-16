package com.example.mediaplayerapp.ui.music_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongItemViewHolder> implements Filterable {
    SongsFragment.DisplayMode displayMode;
    Context context;

    List<Song> displayedSongs;
    List<Song> songs;

    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.displayedSongs = songs;
        this.songs = songs;
    }

    public void setDisplayMode(SongsFragment.DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (displayMode == SongsFragment.DisplayMode.GRID) {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.song_grid_item,
                    parent,
                    false);
        } else {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.song_list_item,
                    parent,
                    false);
        }
        return new SongItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        Song currentSong = displayedSongs.get(position);
        holder.updateCurrentSong(currentSong);
    }

    @Override
    public int getItemCount() {
        return displayedSongs.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Song> filteredSongs = songs.stream()
                        .filter(s -> s.getTitle()
                                .toLowerCase()
                                .contains(charSequence))
                        .collect(Collectors.toList());
                FilterResults filterResults = new FilterResults();
                filterResults.values= filteredSongs;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //noinspection unchecked
                displayedSongs = (List<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SongItemViewHolder extends RecyclerView.ViewHolder  {
        Song currentSong;
        private final ShapeableImageView songThumbnail;
        private final TextView songTitle;
        private final TextView artistName;
        
        public SongItemViewHolder(@NonNull View itemView) {
            super(itemView);

            songThumbnail = itemView.findViewById(R.id.song_thumbnail);
            songTitle = itemView.findViewById(R.id.song_title);
            artistName = itemView.findViewById(R.id.song_artist);
            
            ImageButton contextMenuBtn = itemView.findViewById(R.id.context_menu_btn);

            contextMenuBtn.setOnClickListener(this::openContextMenu);
        }

        /**
         * Set current song and update the views.
         * @param song Current song
         */
        public void updateCurrentSong(Song song) {
            this.currentSong = song;
            updateViewsWithCurrentSong();
        }

        /**
         * Update the views using current song's info.
         */
        private void updateViewsWithCurrentSong() {
            songTitle.setText(currentSong.getTitle());
            artistName.setText(currentSong.getArtistName());
            updateThumbnailWithCurrentSong();
        }

        /**
         * Update item thumbnail with current song's artwork.
         */
        private void updateThumbnailWithCurrentSong() {
            try {
                Bitmap songArtwork = context.getApplicationContext().getContentResolver()
                        .loadThumbnail(currentSong.getUri(),
                                new Size(500, 500),
                                null);
                songThumbnail.setImageBitmap(songArtwork);
            } catch (IOException e) {
                songThumbnail.setImageBitmap(null);
            }
        }

        private void openContextMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(context, view, Gravity.END);
            popupMenu.getMenuInflater().inflate(
                    R.menu.song_context_menu,
                    popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.song_detail)
                    showSongDetails();
                return true;
            });

            popupMenu.show();
        }

        private void showSongDetails() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Song detail")
                    .setMessage("Song title: "+ currentSong.getTitle()
                            + "\nArtist name: " + currentSong.getArtistName()
                            + "\nAlbum name: "+ currentSong.getAlbumName()
                            + "\nDuration: "+ MediaTimeUtils.getFormattedTime(currentSong.getDuration()));
            builder.setCancelable(true);
            builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            builder.create().show();
        }
    }
}
