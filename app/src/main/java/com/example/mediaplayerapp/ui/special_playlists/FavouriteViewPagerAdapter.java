package com.example.mediaplayerapp.ui.special_playlists;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mediaplayerapp.ui.special_playlists.music_fav.MusicFavouriteFragment;
import com.example.mediaplayerapp.ui.special_playlists.video_fav.VideoFavouriteFragment;

public class FavouriteViewPagerAdapter extends FragmentStateAdapter {
    public FavouriteViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment=null;
        switch (position) {
            case 0:
                fragment=new VideoFavouriteFragment();
                break;
            case 1:
                fragment=new MusicFavouriteFragment();
                break;
        }
        assert fragment != null;
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
