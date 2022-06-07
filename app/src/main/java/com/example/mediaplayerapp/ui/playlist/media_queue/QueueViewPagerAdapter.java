package com.example.mediaplayerapp.ui.playlist.media_queue;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mediaplayerapp.ui.playlist.media_queue.music_queue.MusicQueueFragment;
import com.example.mediaplayerapp.ui.playlist.media_queue.video_queue.VideoQueueFragment;

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
