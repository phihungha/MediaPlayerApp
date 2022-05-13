package com.example.mediaplayerapp.ui.playlist;

import android.app.Application;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMediaViewModel;

public class PlaylistAdapter extends ListAdapter<Playlist,PlaylistViewHolder> {
    private IOnItemClickListener mListener;
    private Application application;
    private IOnItemClickListener mBSRenameListener;
    private IOnItemClickListener mBSDeleteListener;
    private IOnItemClickListener mBSPlayListener;
    protected PlaylistAdapter(@NonNull DiffUtil.ItemCallback<Playlist> diffCallback) {
        super(diffCallback);
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlaylistViewHolder.create(parent, mListener,mBSRenameListener,mBSDeleteListener,mBSPlayListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist current=getItem(position);

        PlaylistMediaViewModel playlistMediaViewModel=new PlaylistMediaViewModel(application);
        int count=playlistMediaViewModel.getCountPlaylistWithID(current.getId());
        String textNumber=count+" ";
        if (current.isVideo()){
            if (count<=1){
                textNumber+="video";
            }else
                textNumber+="videos";
        }
        else {
            if (count<=1){
                textNumber+="audio";
            }else
                textNumber+="audios";
        }
        holder.setBinding(current,textNumber);
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

    public void setBSDeleteListener(IOnItemClickListener listener){
        mBSDeleteListener=listener;
    }

    public void setBSRenameListener(IOnItemClickListener listener){
        mBSRenameListener=listener;
    }

    public void setListener(IOnItemClickListener listener){
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
