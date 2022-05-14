package com.example.mediaplayerapp.data;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.ui.music_library.AlbumDetailFragment;
import com.example.mediaplayerapp.ui.music_library.ArtistDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>{
    private Context context;
    private ArrayList<Album> albumList;
    private ArrayList<Album> albumListOld;


    public AlbumAdapter(Context context, ArrayList<Album> albumList) {
        this.albumList = albumList;
        this.context = context;
        this.albumListOld=albumList;
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
    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView img;
        private TextView albumT,albumA;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumT = (TextView)itemView.findViewById(R.id.album_title);
            albumA = (TextView)itemView.findViewById(R.id.album_artist);
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
            transaction.add(R.id.container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

}
