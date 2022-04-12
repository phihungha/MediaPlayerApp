package com.example.mediaplayerapp.ui.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;


import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private List<Playlist> mPlaylists;
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
        holder.binding.tvPlaylistNumbers.setText(playlist.getNumbers());
    }

    @Override
    public int getItemCount() {
        if (mPlaylists!=null){
            return mPlaylists.size();
        }
        return 0;
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

}
