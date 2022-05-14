package com.example.mediaplayerapp.ui.music_library;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Album;
import com.example.mediaplayerapp.data.GridSpacingItemDecoration;
import com.example.mediaplayerapp.data.MusicLibraryRepository;

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
            setHasOptionsMenu(true);
            recyclerView = view.findViewById(R.id.ar);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            albums= (ArrayList<Album>) MusicLibraryRepository.AlbumLoader.albumList(getActivity());
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                albumAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                albumAdapter.getFilter().filter(s);
                return false;
            }
        });

    }
}