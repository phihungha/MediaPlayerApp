package com.example.mediaplayerapp.ui.playlist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.Playlist;

public class PlaylistAdapter extends ListAdapter<Playlist,PlaylistViewHolder> {
    private IOnPlaylistItemClickListener mListener;
    private IOnBottomSheetRenameClick mBSRenameListener;
    private IOnBottomSheetDeleteClick mBSDeleteListener;

    protected PlaylistAdapter(@NonNull DiffUtil.ItemCallback<Playlist> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlaylistViewHolder.create(parent, mListener,mBSRenameListener,mBSDeleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist current=getItem(position);
        holder.setBinding(current.getIdResource(),current.getName());
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

    public void setBSDeleteListener(IOnBottomSheetDeleteClick listener){
        mBSDeleteListener=listener;
    }

    public void setBSRenameListener(IOnBottomSheetRenameClick listener){
        mBSRenameListener=listener;
    }

    public void setListener(IOnPlaylistItemClickListener listener){
        mListener=listener;
    }

    public Playlist getPlaylistItemAt(int position){
        return getItem(position);
    }
/*
    public void setData(List<Playlist> list){
        this.mPlaylists=list;
        notifyDataSetChanged();
    }
*/




/*
    public void filter(String queryText)
    {
        listFilter.clear();

        if(queryText.isEmpty())
        {
            listFilter.addAll(mPlaylists);
        }
        else
        {

            for(Playlist playlist: mPlaylists)
            {
                if(playlist.getName().toLowerCase().contains(queryText.toLowerCase()))
                {
                    listFilter.add(playlist);
                }
            }
        }

        notifyDataSetChanged();
    }*/

}
