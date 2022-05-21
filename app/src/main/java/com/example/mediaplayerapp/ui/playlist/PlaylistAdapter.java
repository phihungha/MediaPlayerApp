package com.example.mediaplayerapp.ui.playlist;

import android.app.Application;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.playlist.playlist_details.PlaylistDetailsFragment;

public class PlaylistAdapter extends ListAdapter<Playlist, PlaylistViewHolder> {
    private IOnItemClickListener mListener;
    private Context mContext;
    private IOnItemClickListener mBSRenameListener;
    private IOnItemClickListener mBSDeleteListener;
    private IOnItemClickListener mBSPlayListener;

    protected PlaylistAdapter(@NonNull DiffUtil.ItemCallback<Playlist> diffCallback) {
        super(diffCallback);
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlaylistViewHolder.create(parent, mListener, mBSRenameListener, mBSDeleteListener, mBSPlayListener,
                mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist current = getItem(position);
        holder.setBinding(current);
    }

    static class PlaylistDiff extends DiffUtil.ItemCallback<Playlist> {
        @Override
        public boolean areItemsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }

    public void setBSPlayListener(IOnItemClickListener mBSPlayListener) {
        this.mBSPlayListener = mBSPlayListener;
    }

    public void setBSDeleteListener(IOnItemClickListener listener) {
        mBSDeleteListener = listener;
    }

    public void setBSRenameListener(IOnItemClickListener listener) {
        mBSRenameListener = listener;
    }

    public void setListener(IOnItemClickListener listener) {
        mListener = listener;
    }

    public Playlist getPlaylistItemAt(int position) {
        return getItem(position);
    }
}
