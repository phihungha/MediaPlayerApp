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
import android.widget.SearchView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.data.music_library.AlbumRepository;


import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends Fragment {
    private List<Album> albums = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private int currentType= Album.TYPE_GRID;
    public AlbumFragment() {
        // Required empty public constructor
    }
    View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlbumRepository albumRepository = new AlbumRepository(requireActivity().getApplicationContext());
        if(view==null)
        {
            view = inflater.inflate(R.layout.fragment_album, container, false);
            setHasOptionsMenu(true);
            recyclerView = view.findViewById(R.id.ar);
            gridLayoutManager = new GridLayoutManager(getActivity(),2);
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(gridLayoutManager);
            albums = albumRepository.getAllAlbums();
            setTypeDisplayRecycleView(Album.TYPE_GRID);
            albumAdapter = new AlbumAdapter(getContext(),albums);
            recyclerView.setAdapter(albumAdapter);
            if(getActivity() != null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }
        // Inflate the layout for this fragment
        return view;
    }
    private void setTypeDisplayRecycleView(int typeDisplay){
        if(albums == null || albums.isEmpty()){
            return;
        }
        currentType=typeDisplay;
        for(Album album : albums){
            album.setTypeDisplay(typeDisplay);
        }
    }
    private void onClickChangeTypeDisplay() {
        if(currentType==Album.TYPE_LIST){
            setTypeDisplayRecycleView(Album.TYPE_GRID);
            recyclerView.setLayoutManager(gridLayoutManager);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }else {
            setTypeDisplayRecycleView(Album.TYPE_LIST);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.removeItemDecorationAt(0);
        }
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id == R.id.change_display_mode){
            onClickChangeTypeDisplay();
        }
        return true;
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