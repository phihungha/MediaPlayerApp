package com.example.mediaplayerapp.ui.playlist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.Playlist;

public class PlaylistAdapter extends ListAdapter<Playlist,PlaylistViewHolder> {
    private IOnPlaylistItemClickListener mListener;

    private IOnBottomSheetSelectionClick mBSRenameListener;
    private IOnBottomSheetSelectionClick mBSDeleteListener;
    private IOnBottomSheetSelectionClick mBSPlayListener;
    protected PlaylistAdapter(@NonNull DiffUtil.ItemCallback<Playlist> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlaylistViewHolder.create(parent, mListener,mBSRenameListener,mBSDeleteListener,mBSPlayListener);
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

    public void setBSPlayListener(IOnBottomSheetSelectionClick mBSPlayListener) {
        this.mBSPlayListener = mBSPlayListener;
    }

    public void setBSDeleteListener(IOnBottomSheetSelectionClick listener){
        mBSDeleteListener=listener;
    }

    public void setBSRenameListener(IOnBottomSheetSelectionClick listener){
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
