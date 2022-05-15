package com.example.mediaplayerapp.ui.music_library;


import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Song;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> implements Filterable {
    ArrayList<Song> SongList = new ArrayList<>();
    ArrayList<Song> SongListOld = new ArrayList<>();
    Context context;


    @Override
    public int getItemViewType(int position) {
        Song song = SongList.get(position);
        return song.getTypeDisplay();
    }

    public static Uri getImage(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }
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
        switch (viewType)
        {
            case Song.TYPE_GRID:view = LayoutInflater.from(context).inflate(R.layout.song_list_grid,
                    parent, false);
                break;
            case Song.TYPE_LIST:view = LayoutInflater.from(context).inflate(R.layout.song_list,
                    parent, false);
                break;
        }
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongHolder holder, int position) {
        final Song songInfoModel = SongList.get(position);
        holder.sogname.setText(songInfoModel.songTitle);
        holder.artistname.setText(songInfoModel.songArtist);
        Glide.with(context).load(getImage(songInfoModel.getAlbumId())).skipMemoryCache(true).into(holder.albumart);
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
                SongList= (ArrayList<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SongHolder extends RecyclerView.ViewHolder  {
        TextView sogname;
        TextView artistname;
        ImageView contextmenu;
        ImageView albumart;
        private PopupMenu popup;

        public SongHolder(@NonNull View itemView)    {
            super(itemView);
            albumart=itemView.findViewById(R.id.songthumb);
            sogname = itemView.findViewById(R.id.sogname);
            artistname= itemView.findViewById(R.id.artistname);
            contextmenu=itemView.findViewById(R.id.contextmenu);
            //Click event of context menu
            contextmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup = new PopupMenu(context, view, Gravity.END);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.song_option, popup.getMenu());
                    popup.show();
                    //Click event of item of song context menu
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.add_playlist:

                                    break;
                                case R.id.song_detail:
                                    String name = sogname.getText().toString();
                                    Song selectSong = new Song();
                                    for(Song song : SongList)
                                    {
                                        if(name == song.songTitle)
                                        {
                                            selectSong=song;
                                            break;
                                        }
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Song detail")
                                            .setMessage("Song title: "+ selectSong.songTitle
                                            +"\nArtist name: " + selectSong.songArtist
                                            +"\nAlbum name: "+selectSong.albumName
                                            +"\nDuration: "+convertDurationToAudioTime(selectSong.duration));
                                    builder.setCancelable(true);
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Cancel
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    break;
                                case R.id.delete_song:
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });
        }
        private String convertDurationToAudioTime(long duration) {
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
        }
    }
}
