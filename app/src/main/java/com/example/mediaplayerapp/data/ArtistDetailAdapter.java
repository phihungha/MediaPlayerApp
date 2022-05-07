package com.example.mediaplayerapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;

import java.util.List;

public class ArtistDetailAdapter  extends RecyclerView.Adapter<ArtistDetailAdapter.AD>  {
    private Context context;
    private List<Song> artistSongList;
    public ArtistDetailAdapter(Context context,List<Song> artistSongList){
        this.context=context;
        this.artistSongList=artistSongList;
    }

    @NonNull
    @Override
    public ArtistDetailAdapter.AD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AD(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistDetailAdapter.AD holder, int position) {
        Song song = artistSongList.get(position);

        if (song!=null) {

            holder.atv.setText(song.songTitle);
            holder.dtv.setText(song.songArtist);
            int trackN = song.trackNumber;
            if (trackN==0) {
                holder.ntv.setText("-");
            }else  holder.ntv.setText(String.valueOf(trackN));
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class AD extends RecyclerView.ViewHolder{
        private TextView atv,ntv,dtv;
        public AD(@NonNull View view) {
            super(view);
            atv = view.findViewById(R.id.songTitle);
            ntv = view.findViewById(R.id.number);
            dtv = view.findViewById(R.id.detail);
        }
    }
}
