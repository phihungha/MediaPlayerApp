package com.example.mediaplayerapp.ui.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;

public class PlaylistViewHolder extends RecyclerView.ViewHolder {

    private ItemPlaylistBinding binding;
    public PlaylistViewHolder(@NonNull ItemPlaylistBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
        //this.binding.getRoot().setOnClickListener(this);
    }


    public void bind(String text) {
        binding.tvPlaylistName.setText(text);
    }

    static PlaylistViewHolder create(ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemPlaylistBinding binding=ItemPlaylistBinding.inflate(inflater,parent,false);
        return new PlaylistViewHolder(binding);
    }

   /* @Override
    public void onClick(View view) {
        listener.onClick(view,getBindingAdapterPosition());
    }*/
}
