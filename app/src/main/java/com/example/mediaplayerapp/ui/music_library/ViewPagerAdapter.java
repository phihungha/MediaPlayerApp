package com.example.mediaplayerapp.ui.music_library;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mediaplayerapp.ui.music_library.album_tab.AlbumsFragment;
import com.example.mediaplayerapp.ui.music_library.artist_tab.ArtistsFragment;
import com.example.mediaplayerapp.ui.music_library.song_tab.SongsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SongsFragment();
            case 1:
                return new AlbumsFragment();
            case 2:
                return new ArtistsFragment();
        }
        return new Fragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
