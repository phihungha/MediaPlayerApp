package com.example.mediaplayerapp.ui.music_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.FragmentMusicLibraryBinding;
import com.google.android.material.tabs.TabLayout;


public class MusicLibraryFragment extends Fragment {


    private FragmentMusicLibraryBinding binding;
    private MusicLibraryViewModel viewModel;

    public static MusicLibraryFragment newInstance() {
        return new MusicLibraryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(MusicLibraryViewModel.class);
        binding = FragmentMusicLibraryBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

@Override
public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

    ViewPager viewPager = view.findViewById(R.id.viewpager);
    TabLayout tabLayout = view.findViewById(R.id.tablayout);

    // attach tablayout with viewpager
    tabLayout.setupWithViewPager(viewPager);
    ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
    // add your fragments
    adapter.addFrag(new SongsFragment(), "Song");
    adapter.addFrag(new AlbumFragment(), "Album");
    adapter.addFrag(new ArtistFragment(), "Artist");
    // set adapter on viewpager
    viewPager.setAdapter(adapter);

}

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}