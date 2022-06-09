package com.example.mediaplayerapp.ui.special_playlists;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mediaplayerapp.ui.special_playlists.music_queue.MusicQueueFragment;
import com.example.mediaplayerapp.ui.special_playlists.video_queue.VideoQueueFragment;

public class QueueViewPagerAdapter extends FragmentStateAdapter {
    public QueueViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment=null;
        switch (position) {
            case 0:
                fragment=new VideoQueueFragment();
                break;
            case 1:
                fragment=new MusicQueueFragment();
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
