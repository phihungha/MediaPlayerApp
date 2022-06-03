package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;

import com.example.mediaplayerapp.databinding.ItemMediaQueueBinding;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;
import com.example.mediaplayerapp.ui.playlist.playlist_details.ItemTouchHelperViewHolder;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaInfo;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaUtils;

public class MediaQueueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
    public final ItemMediaQueueBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static IOnItemClickListener deleteItemListener;
    private static IOnItemClickListener itemClickListener;
    @SuppressLint("StaticFieldLeak")
    private static MediaQueueAdapter mAdapter;

    public MediaQueueViewHolder(@NonNull ItemMediaQueueBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.imgBtnDeleteMediaQueue.setOnClickListener(this);
        this.binding.getRoot().setOnClickListener(this);
    }

    public void setBinding(MediaQueue media) {
        binding.tvNameMediaQueue.setText(media.getName());

        MediaInfo mediaInfo = MediaUtils.getInfoWithUri(mContext, Uri.parse(media.getMediaUri()));
        String duration = MediaUtils.convertDuration(mediaInfo.getDuration());
        binding.tvDurationMediaQueue.setText(String.valueOf(media.getOrderSort()));

        if (media.isVideo()) {
            Glide.with(mContext)
                    .load(media.getMediaUri())
                    .skipMemoryCache(false)
                    .error(R.drawable.ic_error_24dp)
                    .centerCrop()
                    .into(binding.imgThumbnailMediaQueue);
        } else {
            Bitmap thumb = MediaUtils.loadThumbnail(mContext, Uri.parse(media.getMediaUri()));
            if (thumb != null) {
                binding.imgThumbnailMediaQueue.setImageBitmap(thumb);
            } else {
                binding.imgThumbnailMediaQueue.setImageDrawable(
                        ContextCompat.getDrawable(mContext,
                                R.drawable.default_song_artwork));
            }
        }

    }

    static MediaQueueViewHolder create(ViewGroup parent,
                                       Context context,
                                       IOnItemClickListener _deleteItemListener,
                                       IOnItemClickListener _itemClickListener,
                                       MediaQueueAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMediaQueueBinding binding = ItemMediaQueueBinding.inflate(inflater, parent, false);
        deleteItemListener = _deleteItemListener;
        itemClickListener = _itemClickListener;
        mContext = context;
        mAdapter = adapter;
        return new MediaQueueViewHolder(binding);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

    @Override
    public void onItemSelected() {
        itemView.setBackgroundResource(R.color.bright_grey);
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);

        if (mAdapter.getListPosSize() < 2)
            return;

        mAdapter.swapItem();
        mAdapter.clearListPos();
    }
}
