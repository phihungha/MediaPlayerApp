package com.example.mediaplayerapp.ui.playlist.playlist_details;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class PlaylistDetailsAdapter extends ListAdapter<PlaylistMedia,PlaylistDetailsViewHolder> {
    private Context mContext;
    private IOnPlaylistDetailsItemClickListener itemClickListener;

    protected PlaylistDetailsAdapter(@NonNull DiffUtil.ItemCallback<PlaylistMedia> diffCallback) {
        super(diffCallback);
    }

    public void setItemClickListener(IOnPlaylistDetailsItemClickListener listener){
        itemClickListener=listener;
    }
    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PlaylistDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlaylistDetailsViewHolder.create(parent,mContext,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistDetailsViewHolder holder, int position) {
        PlaylistMedia current= getPlaylistMediaItemAt(position);
        if (current==null){
            return;
        }

        holder.setBinding(current);
    }

    static class PlaylistMediaDiff extends DiffUtil.ItemCallback<PlaylistMedia> {
        @Override
        public boolean areItemsTheSame(@NonNull PlaylistMedia oldItem, @NonNull PlaylistMedia newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlaylistMedia oldItem, @NonNull PlaylistMedia newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri());
        }
    }

    public PlaylistMedia getPlaylistMediaItemAt(int position){
        return getItem(position);
    }
}
