package com.example.mediaplayerapp.ui.music_library;


import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Album;
import com.google.android.material.imageview.ShapeableImageView;


import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> implements Filterable {
    private final Context context;
    private ArrayList<Album> albumList;
    private final ArrayList<Album> albumListOld;

    @Override
    public int getItemViewType(int position) {
        Album album = albumList.get(position);
        return album.getTypeDisplay();
    }

    public static Uri getImage(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }
    public AlbumAdapter(Context context, ArrayList<Album> albumList) {
        this.albumList = albumList;
        this.context = context;
        this.albumListOld=albumList;
    }
    @NonNull
    @Override
    public AlbumAdapter.AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_list,
                parent, false);
        switch (viewType){
            case Album.TYPE_GRID:view = LayoutInflater.from(context).inflate(R.layout.album_list,
                    parent, false);
                break;
            case Album.TYPE_LIST:view = LayoutInflater.from(context).inflate(R.layout.album_list2,
                    parent, false);
                break;
        }
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        if (album != null) {
            holder.albumT.setText(album.albumName);
            holder.albumA.setText(album.artistName);
            Glide.with(context).load(getImage(album.getId())).skipMemoryCache(true).into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strsearch= charSequence.toString();
                if (strsearch.isEmpty())
                {
                    albumList=albumListOld;
                }else {
                    ArrayList<Album> NewList = new ArrayList<>();
                    for(Album album : albumListOld){
                        if(album.getAlbumName().toLowerCase().contains(strsearch.toLowerCase()))
                        {
                            NewList.add(album);
                        }
                    }
                    albumList=NewList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=albumList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                albumList= (ArrayList<Album>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ShapeableImageView img;
        private final TextView albumT;
        private final TextView albumA;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumT = (TextView)itemView.findViewById(R.id.album_title);
            albumA = (TextView)itemView.findViewById(R.id.album_artist);
            img=itemView.findViewById(R.id.albumimg);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            long albumId = albumList.get(getAbsoluteAdapterPosition()).id;
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment;
            transaction.setCustomAnimations(R.anim.layout_fad_in, R.anim.layout_fad_out,
                    R.anim.layout_fad_in, R.anim.layout_fad_out);
            fragment = AlbumDetailFragment.newInstance(albumId);
            String tag = fragment.getTag();
            transaction.add(R.id.music_container,fragment);
            transaction.addToBackStack(tag);
            transaction.commit();
        }
    }

}
