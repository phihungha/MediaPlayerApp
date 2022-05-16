package com.example.mediaplayerapp.ui.music_library.artist_tab;

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
import android.widget.SearchView;


import com.example.mediaplayerapp.R;

import com.example.mediaplayerapp.data.music_library.Artist;
import com.example.mediaplayerapp.data.music_library.ArtistRepository;
import com.example.mediaplayerapp.ui.music_library.GridSpacingItemDecoration;


import java.util.ArrayList;

public class ArtistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private ArrayList<Artist> artists = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private int currentType= Artist.TYPE_GRID;

    public ArtistFragment() {
        // Required empty public constructor
    }

    View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArtistRepository artistRepository = new ArtistRepository(requireActivity().getApplicationContext());
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_artist, container, false);
            setHasOptionsMenu(true);
            recyclerView = view.findViewById(R.id.arr);
            gridLayoutManager= new GridLayoutManager(getActivity(),2);
            linearLayoutManager= new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(gridLayoutManager);
            artists = (ArrayList<Artist>) artistRepository.getAllArtists();
            setTypeDisplayRecycleView(Artist.TYPE_GRID);
            artistAdapter = new ArtistAdapter(getContext(), artists);
            recyclerView.setAdapter(artistAdapter);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }
        return view;
    }

    private void setTypeDisplayRecycleView(int typeDisplay){
        if(artists == null || artists.isEmpty()){
            return;
        }
        currentType=typeDisplay;
        for(Artist artist : artists){
            artist.setTypeDisplay(typeDisplay);
        }
    }
    private void onClickChangeTypeDisplay() {
        if(currentType==Artist.TYPE_LIST){
            setTypeDisplayRecycleView(Artist.TYPE_GRID);
            recyclerView.setLayoutManager(gridLayoutManager);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }else {
            setTypeDisplayRecycleView(Artist.TYPE_LIST);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.removeItemDecorationAt(0);
        }
        artistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.music_library_options_menu, menu);
        SearchManager searchManager = (SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                artistAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                artistAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id == R.id.change_display_mode){
            onClickChangeTypeDisplay();
        }
        return true;
    }
}