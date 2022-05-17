package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.ThumbnailUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressLint("NotifyDataSetChanged")
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumItemViewHolder> implements Filterable {
    DisplayMode displayMode = DisplayMode.LIST;
    Context context;

    List<Album> albums = new ArrayList<>();
    List<Album> displayedAlbums = new ArrayList<>();

    public AlbumAdapter(Context context) {
        this.context = context;
    }

    public void updateAlbums(List<Album> newAlbums) {
        albums = newAlbums;
        displayedAlbums.clear();
        displayedAlbums.addAll(albums);
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
    public AlbumItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (displayMode == DisplayMode.GRID) {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.album_grid_item,
                    parent,
                    false);
        } else {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.album_list_item,
                    parent,
                    false);
        }
        return new AlbumItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumItemViewHolder holder, int position) {
        Album currentAlbum = albums.get(position);
        holder.updateCurrentAlbum(currentAlbum);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Album> filteredSongs = albums.stream()
                        .filter(s -> s.getAlbumName()
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
                displayedAlbums = (List<Album>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AlbumItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Album currentAlbum;
        private final ShapeableImageView albumThumbnail;
        private final TextView albumName;
        private final TextView albumArtistName;

        public AlbumItemViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.album_title);
            albumArtistName = itemView.findViewById(R.id.album_artist);
            albumThumbnail = itemView.findViewById(R.id.album_thumbnail);
            itemView.setOnClickListener(this);
        }

        /**
         * Set current album and update the views.
         * @param album Current album
         */
        public void updateCurrentAlbum(Album album) {
            currentAlbum = album;
            updateViewsWithCurrentArtist();
        }

        /**
         * Update the views using current album's info.
         */
        private void updateViewsWithCurrentArtist() {
            albumName.setText(currentAlbum.getAlbumName());
            albumArtistName.setText(currentAlbum.getArtistName());
            updateThumbnailWithCurrentSong();
        }

        /**
         * Update item thumbnail with current album's artwork.
         */
        private void updateThumbnailWithCurrentSong() {
            try {
                Bitmap thumbnail = ThumbnailUtils.getThumbnailFromUri(context, currentAlbum.getUri());
                albumThumbnail.setImageBitmap(thumbnail);
            } catch (IOException e) {
                albumThumbnail.setImageDrawable(
                        ContextCompat.getDrawable(context,
                                R.drawable.default_song_artwork));
            }
        }

        @Override
        public void onClick(View view) {
            long albumId = albums.get(getAbsoluteAdapterPosition()).getId();
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment;
            transaction.setCustomAnimations(R.anim.layout_fad_in, R.anim.layout_fad_out,
                    R.anim.layout_fad_in, R.anim.layout_fad_out);
            fragment = AlbumDetailFragment.newInstance(albumId);
            transaction.replace(R.id.nav_host_fragment_activity_main,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

}
