package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.MusicLibraryFragmentDirections;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;

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
        notifyDataSetChanged();
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
                    R.layout.item_album_grid,
                    parent,
                    false);
        } else {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.item_album_list,
                    parent,
                    false);
        }
        return new AlbumItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumItemViewHolder holder, int position) {
        Album currentAlbum = displayedAlbums.get(position);
        holder.updateCurrentAlbum(currentAlbum);
    }

    @Override
    public int getItemCount() {
        return displayedAlbums.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String match = charSequence.toString().toLowerCase().trim();
                List<Album> filteredAlbums = albums.stream()
                        .filter(s -> s.getAlbumName()
                                .toLowerCase()
                                .contains(match))
                        .collect(Collectors.toList());
                FilterResults filterResults = new FilterResults();
                filterResults.values= filteredAlbums;
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
        private final ImageView albumThumbnail;
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
            updateViewsWithCurrentAlbum();
        }

        /**
         * Update the views using current album's info.
         */
        private void updateViewsWithCurrentAlbum() {
            albumName.setText(currentAlbum.getAlbumName());
            albumArtistName.setText(currentAlbum.getArtistName());
            updateThumbnailWithCurrentAlbum();
        }

        /**
         * Update item thumbnail with current album's artwork.
         */
        private void updateThumbnailWithCurrentAlbum() {
            albumThumbnail.setImageDrawable(
                    MediaMetadataUtils.getThumbnail(
                            context,
                            currentAlbum.getUri(),
                            R.drawable.default_album_artwork
                    )
            );
        }

        @Override
        public void onClick(View view) {
            Navigation.findNavController(view)
                    .navigate(MusicLibraryFragmentDirections
                            .actionNavigationMusicLibraryToAlbumDetailFragment(currentAlbum.getId()));
        }
    }

}
