package com.example.mediaplayerapp.ui.special_playlists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.FragmentMediaQueueBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MediaQueueFragment extends Fragment {
    private FragmentMediaQueueBinding binding;

    public MediaQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMediaQueueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            String key = bundle.getString(PlaylistConstants.TRANS);

            if (key.equals(PlaylistConstants.TRANS_QUEUE)) {
                ViewPager2 viewPager = binding.mediaQueueViewpager;

                QueueViewPagerAdapter adapter = new QueueViewPagerAdapter(this);
                viewPager.setAdapter(adapter);
                viewPager.setSaveEnabled(false);
                new TabLayoutMediator(binding.mediaQueueTabLayout, viewPager,
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
                binding.tvTitleMediaQueue.setText(R.string.watch_later);
            } else if (key.equals(PlaylistConstants.TRANS_FAVOURITE)) {
                ViewPager2 viewPager = binding.mediaQueueViewpager;

                FavouriteViewPagerAdapter adapter = new FavouriteViewPagerAdapter(this);
                viewPager.setAdapter(adapter);
                viewPager.setSaveEnabled(false);
                new TabLayoutMediator(binding.mediaQueueTabLayout, viewPager,
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
                binding.tvTitleMediaQueue.setText(R.string.my_favourite);
            }
            else Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}