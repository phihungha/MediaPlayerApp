package com.example.mediaplayerapp.ui.music_library;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Album;
import com.example.mediaplayerapp.data.AlbumAdapter;
import com.example.mediaplayerapp.data.GridSpacingItemDecoration;

import java.util.ArrayList;


public class AlbumFragment extends Fragment {
    private ArrayList<Album> albums= new ArrayList<Album>();
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    public AlbumFragment() {
        // Required empty public constructor
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null)
        {
            view = inflater.inflate(R.layout.fragment_album, container, false);
            recyclerView = view.findViewById(R.id.ar);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            String[] projection = new String[]{
                    MediaStore.Audio.Albums._ID,//0
                    MediaStore.Audio.Albums.ALBUM,//1
                    MediaStore.Audio.Albums.ARTIST_ID,//2
                    MediaStore.Audio.Albums.ARTIST,//3
                    MediaStore.Audio.Albums.NUMBER_OF_SONGS,//4
                    MediaStore.Audio.Albums.FIRST_YEAR,//5
            };
            Cursor albumCursor = getActivity().getContentResolver().query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
            while (albumCursor.moveToNext()) {
                albums.add(convertToAlbum(albumCursor));
            }
            albumAdapter = new AlbumAdapter(getContext(),albums);
            recyclerView.setAdapter(albumAdapter);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }
        // Inflate the layout for this fragment
        return view;
    }
    private Album convertToAlbum(Cursor cursor) {
        Album album = new Album();
        album.setId(cursor.getLong(0));
        album.setAlbumName(cursor.getString(1));
        album.setArtistId(cursor.getLong(2));
        album.setArtistName(cursor.getString(3));
        album.setNumSong(cursor.getInt(4));
        album.setYear(cursor.getInt(5));
        return album;
    }
}