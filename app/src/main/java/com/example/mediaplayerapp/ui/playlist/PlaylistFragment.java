package com.example.mediaplayerapp.ui.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.FragmentPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class PlaylistFragment extends Fragment {
    private FragmentPlaylistBinding binding;
    private PlaylistDetailsFragment detailsFragment=new PlaylistDetailsFragment();
    private PlaylistAdapter adapter;
    private PlaylistViewModel playlistViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        playlistViewModel=new ViewModelProvider(this).get(PlaylistViewModel.class);

        playlistViewModel.getAllPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                Toast.makeText(getActivity(), "onChanged", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  SharedViewModel viewModel=new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Playlist> mPlaylists =new ArrayList<>();*/
/*
        mPlaylists.add(new Playlist(1,R.drawable.img_for_test,"Name 1",1,true,10,11));
        mPlaylists.add(new Playlist(2,R.drawable.img_for_test,"Name 2",1,true,10,11));
        mPlaylists.add(new Playlist(3,R.drawable.img_for_test,"Name 3",1,true,10,11));
        mPlaylists.add(new Playlist(4,R.drawable.img_for_test,"Name 4",1,true,10,11));
        mPlaylists.add(new Playlist(5,R.drawable.img_for_test,"Name 5",1,true,10,11));
        mPlaylists.add(new Playlist(6,R.drawable.img_for_test,"Name 6",1,true,10,11));
        mPlaylists.add(new Playlist(7,R.drawable.img_for_test,"Name 7",1,true,10,11));
        mPlaylists.add(new Playlist(8,R.drawable.img_for_test,"Name 8",1,true,10,11));
        mPlaylists.add(new Playlist(9,R.drawable.img_for_test,"Name 9",1,true,10,11));
*/

        adapter=new PlaylistAdapter(new PlaylistAdapter.PlaylistDiff());
        binding.rcvPlaylists.setAdapter(adapter);
        playlistViewModel.getAllPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(playlists);
        });
        /*adapter.setListener((v, position) -> {
            viewModel.setSelected(adapter.getPlaylistItemAt(position));
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main,detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });*/

        binding.btnDeleteALl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistViewModel.deleteAll();
            }
        });

        binding.layoutItemAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlaylist();
            }
        });
    }


    private void makeToast(String mess){
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    private void addPlaylist(){
        Playlist playlist = new Playlist(R.drawable.img_for_test,
                "Name 23",100,true,10,11);
        playlistViewModel.insert(playlist);

       // makeToast("ADD Playlist");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
