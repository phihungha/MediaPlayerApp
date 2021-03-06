package com.example.mediaplayerapp.ui.music_library.artist_tab;

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
import com.example.mediaplayerapp.data.music_library.Artist;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.MusicLibraryFragmentDirections;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressLint("NotifyDataSetChanged")
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistItemViewHolder> implements Filterable {
    private DisplayMode displayMode = DisplayMode.LIST;
    private final Context context;

    private List<Artist> artists = new ArrayList<>();
    private List<Artist> displayedArtists = new ArrayList<>();

    public ArtistAdapter(Context context) {
        this.context = context;
    }

    public void updateArtists(List<Artist> newArtists) {
        artists = newArtists;
        displayedArtists.clear();
        displayedArtists.addAll(artists);
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
    public ArtistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (displayMode == DisplayMode.GRID) {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.item_artist_grid,
                    parent,
                    false);
        } else {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.item_artist_list,
                    parent,
                    false);
        }
        return new ArtistItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistItemViewHolder holder, int position) {
        Artist currentArtist = displayedArtists.get(position);
        holder.updateCurrentArtist(currentArtist);
    }

    @Override
    public int getItemCount() {
        return displayedArtists.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String match = charSequence.toString().toLowerCase().trim();
                List<Artist> filteredArtists = artists.stream()
                        .filter(s -> s.getArtistName()
                                .toLowerCase()
                                .contains(match))
                        .collect(Collectors.toList());
                FilterResults filterResults = new FilterResults();
                filterResults.values= filteredArtists;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // Java can't check generic types
                //noinspection unchecked
                displayedArtists = (List<Artist>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ArtistItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Artist currentArtist;
        private final ImageView artistThumbnail;
        private final TextView artistName;

        public ArtistItemViewHolder(View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_name);
            artistThumbnail = itemView.findViewById(R.id.artist_thumbnail);
            itemView.setOnClickListener(this);
        }

        /**
         * Set current artist and update the views.
         * @param artist Current artist
         */
        public void updateCurrentArtist(Artist artist) {
            currentArtist = artist;
            updateViewsWithCurrentArtist();
        }

        /**
         * Update the views using current artist's info.
         */
        private void updateViewsWithCurrentArtist() {
            artistName.setText(currentArtist.getArtistName());
            updateThumbnailWithCurrentAlbum();
        }

        /**
         * Update thumbnail with artist's first song's artwork.
         */
        private void updateThumbnailWithCurrentAlbum() {
            artistThumbnail.setImageDrawable(
                    MediaMetadataUtils.getThumbnail(
                            context,
                            currentArtist.getThumbnailUri(),
                            R.drawable.default_album_artwork
                    )
            );
        }

        @Override
        public void onClick(View view) {
            Navigation.findNavController(view)
                    .navigate(MusicLibraryFragmentDirections
                            .actionNavigationMusicLibraryToArtistDetailFragment(currentArtist.getId()));
        }
    }
}
