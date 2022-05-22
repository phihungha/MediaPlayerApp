package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.utils.IStartPlayback;
import com.example.mediaplayerapp.utils.MediaThumbnailUtils;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressLint("NotifyDataSetChanged")
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongItemViewHolder> implements Filterable {

    private DisplayMode displayMode = DisplayMode.LIST;
    private final Context context;

    private final IStartPlayback playbackStartMethod;
    private List<Song> songs = new ArrayList<>();
    private List<Song> displayedSongs = new ArrayList<>();

    public SongAdapter(Context context, IStartPlayback playbackStartMethod) {
        this.context = context;
        this.playbackStartMethod = playbackStartMethod;
    }

    public void updateSongs(List<Song> newSongs) {
        songs = newSongs;
        displayedSongs.clear();
        displayedSongs.addAll(songs);
        notifyDataSetChanged();
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
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
        BottomSheetDialog bottomSheetDialog;
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
            contextMenuBtn.setOnClickListener(this::openBottomSheetDialog);

            itemView.setOnClickListener(view -> playbackStartMethod.play(currentSong.getOrderIndex()));
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
            try {
                Bitmap thumbnail = MediaThumbnailUtils.getThumbnailFromUri(context, currentSong.getUri());
                songThumbnail.setImageBitmap(thumbnail);
            } catch (IOException e) {
                songThumbnail.setImageDrawable(
                        ContextCompat.getDrawable(context,
                                R.drawable.default_song_artwork));
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
        private void openBottomSheetDialog(View view){
            bottomSheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetTheme);
            View bsView = LayoutInflater.from(view.getContext()).inflate(R.layout.bottom_sheet_song,
                    view.findViewById(R.id.bs_song));
            TextView tv_name = bsView.findViewById(R.id.bottom_sheet_song_name_textview);
            tv_name.setText(currentSong.getTitle());
            bsView.findViewById(R.id.bottom_sheet_song_detail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSongDetails();
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(bsView);
            bottomSheetDialog.show();
        }
        private void showSongDetails() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Song detail")
                    .setMessage("Song title: "+ currentSong.getTitle()
                            + "\nArtist name: " + currentSong.getArtistName()
                            + "\nAlbum name: "+ currentSong.getAlbumName()
                            + "\nGenre: "+currentSong.getGenre(context)
                            + "\nDuration: "+ MediaTimeUtils.getFormattedTime(currentSong.getDuration()));
            builder.setCancelable(true);
            builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
            builder.create().show();
        }
    }
}
