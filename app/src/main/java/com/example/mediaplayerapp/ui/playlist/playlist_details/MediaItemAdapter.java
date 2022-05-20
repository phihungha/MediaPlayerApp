package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;

public class MediaItemAdapter extends ListAdapter<PlaylistItem, MediaItemViewHolder> {
    private Context mContext;
    private IOnItemClickListener itemClickListener;
    private IOnItemClickListener bsPlayListener;
    private IOnItemClickListener bsDeleteListener;
    private IOnItemClickListener bsPropertiesListener;
    private IOnItemClickListener bsAddQueueListener;
    private Application mApplication;

    public void setBsAddQueueListener(IOnItemClickListener bsAddQueueListener) {
        this.bsAddQueueListener = bsAddQueueListener;
    }

    public void setApplication(Application mApplication) {
        this.mApplication = mApplication;
    }

    public MediaItemAdapter(@NonNull DiffUtil.ItemCallback<PlaylistItem> diffCallback) {
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
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MediaItemViewHolder.create(parent,mContext,itemClickListener,
                bsPlayListener,bsDeleteListener,bsPropertiesListener,bsAddQueueListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemViewHolder holder, int position) {
        PlaylistItem current= getPlaylistMediaItemAt(position);
        if (current==null){
            return;
        }
        Uri uri=Uri.parse(current.getMediaUri());
        if (MediaUtils.isUriExists(mContext,uri)){
            holder.setBinding(current);
        } else {
            PlaylistItemViewModel playlistItemViewModel =new PlaylistItemViewModel(mApplication);
            playlistItemViewModel.deleteItemWithUri(uri.toString());
        }
    }

    public static class PlaylistMediaDiff extends DiffUtil.ItemCallback<PlaylistItem> {
        @Override
        public boolean areItemsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri())
                    && oldItem.getId()==newItem.getId();
        }
    }

    public PlaylistItem getPlaylistMediaItemAt(int position){
        return getItem(position);
    }
}
