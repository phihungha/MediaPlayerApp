package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ItemMediaQueueBinding;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaInfo;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaUtils;

public class MediaQueueViewHolder extends RecyclerView.ViewHolder {
    private ItemMediaQueueBinding binding;
    private static Context mContext;
    public MediaQueueViewHolder(@NonNull ItemMediaQueueBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
    }

    public void setBinding(MediaQueue media) {
        binding.tvNameMediaQueue.setText(media.getName());
        MediaInfo mediaInfo= MediaUtils.getInfoWithUri(mContext, Uri.parse(media.getMediaUri()));
        String duration=MediaUtils.convertDuration(mediaInfo.getDuration());
        binding.tvDurationMediaQueue.setText(duration);

        Glide.with(mContext)
                .load(media.getMediaUri())
                .skipMemoryCache(false)
                .error(R.drawable.ic_round_error_24)
                .centerCrop()
                .into(binding.imgThumbnailMediaQueue);
    }

    static MediaQueueViewHolder create(ViewGroup parent,
                                            Context context) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMediaQueueBinding binding = ItemMediaQueueBinding.inflate(inflater, parent, false);

        mContext=context;
        return new MediaQueueViewHolder(binding);
    }
}
