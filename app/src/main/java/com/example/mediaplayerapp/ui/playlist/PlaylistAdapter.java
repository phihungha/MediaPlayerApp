package com.example.mediaplayerapp.ui.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;


import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private List<Playlist> mPlaylists;
    private List<Playlist> listFilter=new ArrayList<>();
    private OnPlaylistItemClickListener listener;

    public interface OnPlaylistItemClickListener{
        void onClick(View view, int position);
    }
    public PlaylistAdapter(List<Playlist> mPlaylists) {
        this.mPlaylists = mPlaylists;
    }

    public void setListener(OnPlaylistItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Playlist> list){
        this.mPlaylists=list;
        notifyDataSetChanged();
    }

    public Playlist getPlaylistItemAt(int position){
        return mPlaylists.get(position);
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemPlaylistBinding binding=ItemPlaylistBinding.inflate(inflater,parent,false);
        return new PlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist=mPlaylists.get(position);
        if (playlist==null){
            return;
        }
        holder.binding.imgThumbnail.setImageResource(playlist.getIdResource());
        holder.binding.tvPlaylistName.setText(playlist.getName());
        holder.binding.tvPlaylistNumbers.setText(String.valueOf(playlist.getNumbers()));
    }

    @Override
    public int getItemCount() {
        if (mPlaylists!=null){
            return mPlaylists.size();
        }
        return 0;
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

    public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemPlaylistBinding binding;
        public PlaylistViewHolder(@NonNull ItemPlaylistBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getBindingAdapterPosition());
        }

    }

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
    }

}
