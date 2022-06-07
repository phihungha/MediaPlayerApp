package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;
import com.example.mediaplayerapp.utils.StartPlaybackCallback;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressLint("NotifyDataSetChanged")
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongItemViewHolder> implements Filterable {

    private DisplayMode displayMode = DisplayMode.LIST;
    private final Context context;

    private final StartPlaybackCallback startPlaybackCallback;
    private List<Song> songs = new ArrayList<>();
    private List<Song> displayedSongs = new ArrayList<>();

    public SongAdapter(Context context, StartPlaybackCallback startPlaybackCallback) {
        this.context = context;
        this.startPlaybackCallback = startPlaybackCallback;
    }

    public void updateSongs(List<Song> newSongs) {
        songs = newSongs;
        displayedSongs.clear();
        displayedSongs.addAll(songs);
        notifyDataSetChanged();
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return displayMode.getValue();
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (displayMode == DisplayMode.GRID) {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.item_song_grid,
                    parent,
                    false);
        } else {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.item_song_list,
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

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // Java can't check generic types
                //noinspection unchecked
                displayedSongs = (List<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SongItemViewHolder extends RecyclerView.ViewHolder  {
        private Song currentSong;
        private final ShapeableImageView songThumbnail;
        private final TextView songTitle;
        private final TextView songArtist;
        
        public SongItemViewHolder(@NonNull View itemView) {
            super(itemView);

            songThumbnail = itemView.findViewById(R.id.song_thumbnail);
            songTitle = itemView.findViewById(R.id.song_title);
            songArtist = itemView.findViewById(R.id.song_artist);

            ImageButton contextMenuBtn = itemView.findViewById(R.id.context_menu_btn);
            contextMenuBtn.setOnClickListener(view -> {
                SongBottomSheet songBottomSheet = new SongBottomSheet(currentSong);
                songBottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(),
                        songBottomSheet.getTag());
            });
            itemView.setOnClickListener(view -> startPlaybackCallback.play(currentSong.getOrderIndex()));
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
            songArtist.setText(currentSong.getArtistName());
            updateThumbnailWithCurrentSong();
        }

        /**
         * Update item thumbnail with current song's artwork.
         */
        private void updateThumbnailWithCurrentSong() {
            songThumbnail.setImageBitmap(
                    MediaMetadataUtils.getThumbnail(
                                    context,
                                    currentSong.getUri(),
                                    R.drawable.default_song_artwork
                            )
            );
        }
    }
}
