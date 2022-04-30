package com.example.mediaplayerapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.VH> {
    private List<Song> songList;
    public SongAdapter(List<Song> songList)
    {
        this.songList=songList;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Song song = songList.get(position);
        if(song !=null)
        {
            holder.titl.setText(song.title);
            holder.art.setText(song.artistName);

        }

    }



    @Override
    public int getItemCount() {
        return songList!=null?songList.size():0;
    }

    public class VH extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titl,art;
        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
