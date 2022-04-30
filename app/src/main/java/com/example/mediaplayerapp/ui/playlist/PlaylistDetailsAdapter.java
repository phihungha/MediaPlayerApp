package com.example.mediaplayerapp.ui.playlist;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;

import java.util.ArrayList;

public class PlaylistDetailsAdapter extends RecyclerView.Adapter<PlaylistDetailsAdapter.PlaylistDetailsViewHolder>{
    private ArrayList<Uri> uriArrayList;

    public PlaylistDetailsAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public PlaylistDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      /*  View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist_details,parent,false);

        ItemPlaylistDetailsBinding binding= ItemPlaylistDetailsBinding.bind(view);*/
        return new PlaylistDetailsViewHolder(ItemPlaylistDetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistDetailsViewHolder holder, int position) {
        Uri uri=uriArrayList.get(position);
        if (uri==null){
            return;
        }


        holder.binding.imgThumbnailPlaylistDetails.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        if (uriArrayList!=null){
            return uriArrayList.size();
        }
        return 0;
    }

    public class PlaylistDetailsViewHolder extends RecyclerView.ViewHolder{
        private ItemPlaylistDetailsBinding binding;
        public PlaylistDetailsViewHolder(@NonNull ItemPlaylistDetailsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
