package com.example.mediaplayerapp.data;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaPlayer;
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


import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    ArrayList<Song> SongList = new ArrayList<>();
    Context context;
    MediaPlayer mediaPlayer;
    Song obj;

    public SongAdapter(Context context, ArrayList<Song> SongList) {

        this.context = context;
        this.SongList = SongList;

    }
    @NonNull
    @Override
    public SongAdapter.SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_list,
                parent, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongHolder holder, int position) {
        final Song songInfoModel = SongList.get(position);
        holder.sogname.setText(songInfoModel.songTitle);
        holder.artistname.setText(songInfoModel.songArtist);
    }

    @Override
    public int getItemCount() {
        return SongList.size();
    }
    public class SongHolder extends RecyclerView.ViewHolder  {
        TextView sogname;
        TextView artistname;



        public SongHolder(View itemView)    {

            super(itemView);
            sogname = (TextView)itemView.findViewById(R.id.sogname);
            artistname= (TextView)itemView.findViewById(R.id.artistname);
        }

    }
}
