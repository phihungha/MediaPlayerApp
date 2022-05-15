package com.example.mediaplayerapp.ui.music_library;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.GridSpacingItemDecoration;
import com.example.mediaplayerapp.data.MusicLibraryRepository;
import com.example.mediaplayerapp.data.Song;

import java.util.ArrayList;


public class SongsFragment extends Fragment {
    private SongAdapter songAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Song> SongList = new ArrayList<Song>();
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private int currentType=Song.TYPE_GRID;
    public SongsFragment() {
        // Required empty public constructor
    }


    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view==null) {
            view = inflater.inflate(R.layout.fragment_songs, container, false);
            setHasOptionsMenu(true);
            recyclerView = (RecyclerView) view.findViewById(R.id.sr);
            gridLayoutManager= new GridLayoutManager(getActivity(),2);
            linearLayoutManager= new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(gridLayoutManager);
            SongList= (ArrayList<Song>) MusicLibraryRepository.SongLoder.getAllSongs(getActivity());
            setTypeDisplayRecycleView(Song.TYPE_GRID);
            songAdapter = new SongAdapter(getContext(), SongList);
            recyclerView.setAdapter(songAdapter);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }
        return view;
    }
    private void setTypeDisplayRecycleView(int typeDisplay){
        if(SongList == null || SongList.isEmpty()){
            return;
        }
        currentType=typeDisplay;
        for(Song song : SongList){
            song.setTypeDisplay(typeDisplay);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        Button switch_view = (Button) menu.findItem(R.id.switch_view).getActionView();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id == R.id.switch_view){
            onClickChangeTypeDisplay();
        }
        return true;
    }

    private void onClickChangeTypeDisplay() {
        if(currentType==Song.TYPE_LIST){
            setTypeDisplayRecycleView(Song.TYPE_GRID);
            recyclerView.setLayoutManager(gridLayoutManager);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }else {
            setTypeDisplayRecycleView(Song.TYPE_LIST);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.removeItemDecorationAt(0);
        }
        songAdapter.notifyDataSetChanged();
    }
}