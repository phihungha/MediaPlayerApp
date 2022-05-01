package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;


public class PlaylistDetailsViewHolder extends RecyclerView.ViewHolder {
    private ItemPlaylistDetailsBinding binding;
    public PlaylistDetailsViewHolder(@NonNull ItemPlaylistDetailsBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
    }

    public void setBinding(String name, String thumb) {
        binding.tvPlaylistNamePlaylistDetails.setText(name);

        Glide.with(itemView.getContext())
                .load("file://"+ thumb)
                .skipMemoryCache(false)
                .into(binding.imgThumbnailPlaylistDetails);
    }

    static PlaylistDetailsViewHolder create(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistDetailsBinding binding = ItemPlaylistDetailsBinding.inflate(inflater, parent, false);

        return new PlaylistDetailsViewHolder(binding);
    }
}
