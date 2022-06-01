package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueViewModel;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaUtils;

public class MediaQueueAdapter extends ListAdapter<MediaQueue,MediaQueueViewHolder> {
    private Context mContext;
    private IOnItemClickListener deleteItemListener;
    private IOnItemClickListener itemClickListener;
    private Application mApplication;

    public void setApplication(Application mApplication) {
        this.mApplication = mApplication;
    }

    public void setItemClickListener(IOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setDeleteItemListener(IOnItemClickListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public MediaQueueAdapter(@NonNull DiffUtil.ItemCallback<MediaQueue> diffCallback) {
        super(diffCallback);
    }
    @NonNull
    @Override
    public MediaQueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MediaQueueViewHolder.create(parent,mContext,deleteItemListener,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaQueueViewHolder holder, int position) {
        MediaQueue current= getItemAt(position);
        if (current==null){
            return;
        }
        Uri uri=Uri.parse(current.getMediaUri());
        if (MediaUtils.isUriExists(mContext,uri)){
            holder.setBinding(current);
        } else {
            MediaQueueViewModel mediaQueueViewModel=new MediaQueueViewModel(mApplication);
            mediaQueueViewModel.deleteItemWithUri(uri.toString());
        }
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
