package com.example.mediaplayerapp.ui.playlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.databinding.FragmentPlaylistDetailsBinding;

public class PlaylistDetailsFragment extends Fragment {

    private FragmentPlaylistDetailsBinding binding;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentPlaylistDetailsBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel viewModel=new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(),item -> {
            binding.tvIdPlaylist.setText("Playlist id: " + item.getId() +"\n" +
                    "Name: " + item.getName() + "\n"+
                    "Number: " + item.getNumbers() + "\n"+
                    "Song: " + item.getSongID() + "\n"+
                    "Video: " + item.getVideoID() + "\n"+
                    "IsVideo: " + item.isVideo() + "\n");
        });
    }
}