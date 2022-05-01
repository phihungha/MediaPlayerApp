package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Video;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;


public class PlaylistDetailsViewHolder extends RecyclerView.ViewHolder {
    private static Context mContext;
    private ItemPlaylistDetailsBinding binding;
    public PlaylistDetailsViewHolder(@NonNull ItemPlaylistDetailsBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
    }

    public void setBinding(Video video) {
        binding.tvPlaylistNamePlaylistDetails.setText(video.getName());

        Glide.with(mContext)
                .load(video.getUri().toString())
                .skipMemoryCache(false)
                .error(R.drawable.ic_round_error_24)
                .centerCrop()
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
