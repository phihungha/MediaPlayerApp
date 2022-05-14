package com.example.mediaplayerapp.ui.music_library;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.MusicLibraryRepository;
import com.example.mediaplayerapp.data.Song;

import java.util.ArrayList;


public class SongsFragment extends Fragment {
    private SongAdapter songAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Song> SongList = new ArrayList<Song>();
    private Song s;


    public SongsFragment() {
        // Required empty public constructor
    }


    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view==null) {

            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_songs, container, false);
            setHasOptionsMenu(true);
            recyclerView = (RecyclerView) view.findViewById(R.id.sr);
            LinearLayoutManager linearLayoutManager = new
                    LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            SongList= (ArrayList<Song>) MusicLibraryRepository.SongLoder.getAllSongs(getActivity());
            songAdapter = new SongAdapter(getContext(), SongList);
            recyclerView.setAdapter(songAdapter);

        }
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                songAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                songAdapter.getFilter().filter(s);
                return false;
            }
        });

    }
}