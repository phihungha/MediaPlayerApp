package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;


public class PlaylistDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static Context mContext;
    private ItemPlaylistDetailsBinding binding;
    private static IOnPlaylistDetailsItemClickListener itemClickListener;

    public PlaylistDetailsViewHolder(@NonNull ItemPlaylistDetailsBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
        this.binding.getRoot().setOnClickListener(this);
    }

    public void setBinding(PlaylistMedia video) {
        binding.tvPlaylistNamePlaylistDetails.setText(video.getName());

        Glide.with(mContext)
                .load(video.getVideoUri())
                .skipMemoryCache(false)
                .error(R.drawable.ic_round_error_24)
                .centerCrop()
                .into(binding.imgThumbnailPlaylistDetails);
    }

    static PlaylistDetailsViewHolder create(ViewGroup parent,
                                            Context context,
                                            IOnPlaylistDetailsItemClickListener clickListener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistDetailsBinding binding = ItemPlaylistDetailsBinding.inflate(inflater, parent, false);
        itemClickListener=clickListener;
        mContext=context;
        return new PlaylistDetailsViewHolder(binding);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(itemView,getBindingAdapterPosition());
    }
}
