package com.example.mediaplayerapp.ui.playlist.playlist_details;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMedia;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;

public class PlaylistDetailsAdapter extends ListAdapter<PlaylistMedia,PlaylistDetailsViewHolder> {
    private Context mContext;
    private IOnItemClickListener itemClickListener;
    private IOnItemClickListener bsPlayListener;
    private IOnItemClickListener bsDeleteListener;
    private IOnItemClickListener bsPropertiesListener;
    private IOnItemClickListener bsAddQueueListener;

    public void setBsAddQueueListener(IOnItemClickListener bsAddQueueListener) {
        this.bsAddQueueListener = bsAddQueueListener;
    }

    public PlaylistDetailsAdapter(@NonNull DiffUtil.ItemCallback<PlaylistMedia> diffCallback) {
        super(diffCallback);
    }

    public void setBsPropertiesListener(IOnItemClickListener bsPropertiesListener) {
        this.bsPropertiesListener = bsPropertiesListener;
    }

    public void setItemClickListener(IOnItemClickListener listener){
        itemClickListener=listener;
    }

    public void setBsPlayListener(IOnItemClickListener bsPlayListener) {
        this.bsPlayListener = bsPlayListener;
    }

    public void setBsDeleteListener(IOnItemClickListener bsDeleteListener) {
        this.bsDeleteListener = bsDeleteListener;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PlaylistDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlaylistDetailsViewHolder.create(parent,mContext,itemClickListener,
                bsPlayListener,bsDeleteListener,bsPropertiesListener,bsAddQueueListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistDetailsViewHolder holder, int position) {
        PlaylistMedia current= getPlaylistMediaItemAt(position);
        if (current==null){
            return;
        }

        holder.setBinding(current);
    }

    public static class PlaylistMediaDiff extends DiffUtil.ItemCallback<PlaylistMedia> {
        @Override
        public boolean areItemsTheSame(@NonNull PlaylistMedia oldItem, @NonNull PlaylistMedia newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlaylistMedia oldItem, @NonNull PlaylistMedia newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri())
                    && oldItem.getId()==newItem.getId();
        }
    }

    public PlaylistMedia getPlaylistMediaItemAt(int position){
        return getItem(position);
    }
}
