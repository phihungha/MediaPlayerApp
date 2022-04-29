package com.example.mediaplayerapp.ui.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;


import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends ListAdapter<Playlist,PlaylistViewHolder> {
    private IOnPlaylistItemClickListener mListener;
    private IOnBottomSheetClick mBSListener;
    protected PlaylistAdapter(@NonNull DiffUtil.ItemCallback<Playlist> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlaylistViewHolder.create(parent, mListener,mBSListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist current=getItem(position);
        holder.setBinding(current.getIdResource(),current.getName(),current.getNumbers());
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
    public void setBSListener(IOnBottomSheetClick listener){
        mBSListener=listener;
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
