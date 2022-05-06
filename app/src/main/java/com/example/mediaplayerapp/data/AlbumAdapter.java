package com.example.mediaplayerapp.data;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>{
    private Context context;
    private List<Album> albumList;


    public AlbumAdapter(Context context, List<Album> albumList) {
        this.albumList = albumList;
        this.context = context;
    }
    @NonNull
    @Override
    public AlbumAdapter.AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumAdapter.AlbumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        if (album != null) {
            holder.albumT.setText(album.albumName);
            holder.albumA.setText(album.artistName);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
    public class AlbumViewHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView albumT,albumA;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumT = (TextView)itemView.findViewById(R.id.album_title);
            albumA = (TextView)itemView.findViewById(R.id.album_artist);

        }


    }

}
