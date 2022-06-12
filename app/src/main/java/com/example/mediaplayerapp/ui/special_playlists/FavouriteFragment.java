package com.example.mediaplayerapp.ui.special_playlists;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.FragmentFavouriteBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class FavouriteFragment extends Fragment {
    private FragmentFavouriteBinding binding;
    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = binding.favouriteViewpager;

        FavouriteViewPagerAdapter adapter = new FavouriteViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setSaveEnabled(false);
        new TabLayoutMediator(binding.favouriteTabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.video_queue);
                            break;
                        case 1:
                            tab.setText(R.string.music_queue);
                            break;
                    }
                }).attach();
        binding.tvTitleFavourite.setText(R.string.my_favourite);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}