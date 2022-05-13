package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;

public class MediaQueueAdapter extends ListAdapter<MediaQueue,MediaQueueViewHolder> {
    private Context mContext;
    private IOnItemClickListener deleteItemListener;

    public void setDeleteItemListener(IOnItemClickListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    protected MediaQueueAdapter(@NonNull DiffUtil.ItemCallback<MediaQueue> diffCallback) {
        super(diffCallback);
    }
    @NonNull
    @Override
    public MediaQueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MediaQueueViewHolder.create(parent,mContext,deleteItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaQueueViewHolder holder, int position) {
        MediaQueue current= getItemAt(position);
        if (current==null){
            return;
        }

        holder.setBinding(current);
    }

    public static class MediaQueueDiff extends DiffUtil.ItemCallback<MediaQueue> {
        @Override
        public boolean areItemsTheSame(@NonNull MediaQueue oldItem, @NonNull MediaQueue newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MediaQueue oldItem, @NonNull MediaQueue newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri());
        }
    }

    public MediaQueue getItemAt(int position){
        return getItem(position);
    }
}
