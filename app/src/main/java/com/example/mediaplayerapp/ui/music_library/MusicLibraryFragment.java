package com.example.mediaplayerapp.ui.music_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.FragmentMusicLibraryBinding;
import com.google.android.material.tabs.TabLayoutMediator;


public class MusicLibraryFragment extends Fragment {

    private FragmentMusicLibraryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMusicLibraryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        ViewPager2 viewPager = binding.musicLibraryViewpager;
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.musicLibraryTabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.songs);
                            break;
                        case 1:
                            tab.setText(R.string.albums);
                            break;
                        case 2:
                            tab.setText(R.string.artists);
                            break;
                    }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}