package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;


public class PlaylistDetailsViewHolder extends RecyclerView.ViewHolder {
    private static Context mContext;
    private ItemPlaylistDetailsBinding binding;
    public PlaylistDetailsViewHolder(@NonNull ItemPlaylistDetailsBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
    }

    public void setBinding(String name, String thumb) {
        binding.tvPlaylistNamePlaylistDetails.setText(name);

        Glide.with(mContext)
                .load(thumb)
                .skipMemoryCache(false)
                .error(R.drawable.ic_round_error_24)
                .into(binding.imgThumbnailPlaylistDetails);
    }

    static PlaylistDetailsViewHolder create(ViewGroup parent,
                                            Context context) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistDetailsBinding binding = ItemPlaylistDetailsBinding.inflate(inflater, parent, false);

        mContext=context;
        return new PlaylistDetailsViewHolder(binding);
    }
}
