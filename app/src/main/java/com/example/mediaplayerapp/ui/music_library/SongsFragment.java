package com.example.mediaplayerapp.ui.music_library;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Song;
import com.example.mediaplayerapp.data.SongAdapter;

import java.util.ArrayList;


public class SongsFragment extends Fragment {
    private SongAdapter songAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Song> SongList = new ArrayList<Song>();
    private Song s;


    public SongsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_songs,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.sr);
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        Song s= new Song(01,"Cheap Thrills","This Is Acting","Sia",
                "http://mp3fb.com/wp-includes/inc/down.php?id=hyCQomZzosfGBvVON0tRV7xfiL2GdtKXbTsBrZh_3NM&t=Sia%2B-%2BCheap%2BThrils%2B%D0%A0%D0%B5%D0%BC%D0%B8%D0%BA%D1%81.mp3&hash=true");
        SongList.add(s);
        songAdapter= new SongAdapter(getContext(),SongList);
        recyclerView.setAdapter(songAdapter);
        return view;
    }


}