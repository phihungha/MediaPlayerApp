package com.example.mediaplayerapp.ui.music_library;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Song;


import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> implements Filterable {
    ArrayList<Song> SongList = new ArrayList<>();
    ArrayList<Song> SongListOld = new ArrayList<>();
    Context context;


    public SongAdapter(Context context, ArrayList<Song> SongList) {
        this.context = context;
        this.SongList = SongList;
        this.SongListOld=SongList;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strsearch= charSequence.toString();
                if (strsearch.isEmpty())
                {
                    SongList=SongListOld;
                }else {
                    ArrayList<Song> NewList = new ArrayList<>();
                    for(Song song : SongListOld){
                        if(song.getSongTitle().toLowerCase().contains(strsearch.toLowerCase()))
                        {
                            NewList.add(song);
                        }
                    }
                    SongList=NewList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=SongList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                SongList=(ArrayList<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SongHolder extends RecyclerView.ViewHolder  {
        TextView sogname;
        TextView artistname;
        ImageView contextmenu;
        private PopupMenu popup;
        public SongHolder(View itemView)    {

            super(itemView);
            sogname = (TextView)itemView.findViewById(R.id.sogname);
            artistname= (TextView)itemView.findViewById(R.id.artistname);
            contextmenu=itemView.findViewById(R.id.contextmenu);
            contextmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup = new PopupMenu(context, view, Gravity.END);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.song_option, popup.getMenu());
                    popup.show();
                }
            });

        }
    }
}
