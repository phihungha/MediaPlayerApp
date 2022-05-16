package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.databinding.ItemMediaQueueBinding;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaInfo;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaUtils;

public class MediaQueueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ItemMediaQueueBinding binding;
    private static Context mContext;
    private static IOnItemClickListener deleteItemListener;
    private static IOnItemClickListener itemClickListener;
    public MediaQueueViewHolder(@NonNull ItemMediaQueueBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
        binding.imgBtnDeleteMediaQueue.setOnClickListener(this);
        binding.getRoot().setOnClickListener(this);
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
                                            Context context,
                                       IOnItemClickListener _deleteItemListener,
                                       IOnItemClickListener _itemClickListener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMediaQueueBinding binding = ItemMediaQueueBinding.inflate(inflater, parent, false);
        deleteItemListener=_deleteItemListener;
        itemClickListener=_itemClickListener;
        mContext=context;
        return new MediaQueueViewHolder(binding);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_Delete_MediaQueue:
                DeleteItemQueue();
                break;

            case R.id.layoutItem_mediaQueue:
                QueueItemClick();
                break;
        }

    }

    private void QueueItemClick() {
        if (itemClickListener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
            itemClickListener.onClick(itemView.getRootView(), getBindingAdapterPosition());
        }
    }

    private void DeleteItemQueue() {
        if (deleteItemListener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
            deleteItemListener.onClick(itemView.getRootView(), getBindingAdapterPosition());
        }
    }
}
