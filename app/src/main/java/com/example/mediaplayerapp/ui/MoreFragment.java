package com.example.mediaplayerapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mediaplayerapp.databinding.FragmentMoreBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {


    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMoreBinding binding = FragmentMoreBinding.inflate(inflater, container, false);

        binding.moreOpenPlaylist.setOnClickListener(
                view -> Navigation.findNavController(view)
                        .navigate(MoreFragmentDirections.actionNavigationMoreToNavigationPlaylist()));

        binding.moreOpenSettings.setOnClickListener(
                view -> Navigation.findNavController(view)
                        .navigate(MoreFragmentDirections.actionNavigationMoreToNavigationSettings()));
        binding.moreOpenWatchLater.setOnClickListener(
                view -> Navigation.findNavController(view)
                        .navigate(MoreFragmentDirections.actionNavigationMoreToMediaQueueFragment()));

        binding.moreOpenFavorites.setOnClickListener(
                view -> Navigation.findNavController(view)
                        .navigate(MoreFragmentDirections.actionNavigationMoreToFavouriteFragment()));
        return binding.getRoot();
    }
}