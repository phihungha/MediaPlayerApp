package com.example.mediaplayerapp.ui.playlist.media_queue;

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
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;
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
                Toast.makeText(getActivity(), "queue", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Favourite", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }


    /*    adapter = new MediaQueueAdapter(new MediaQueueAdapter.MediaQueueDiff());
        adapter.setContext(getContext());
        adapter.setApplication(requireActivity().getApplication());

        binding.rcvQueue.setAdapter(adapter);
        setListeners();
        mediaQueueViewModel.getAllMediaQueue().observe(
                getViewLifecycleOwner(),
                mediaQueues -> adapter.submitList(mediaQueues)
        );*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    
/*
    private void setListeners() {
        binding.layoutClearQueue.setOnClickListener(this);
        binding.layoutPlayQueue.setOnClickListener(this);

        adapter.setDeleteItemListener(this::DeleteItemQueue);

        adapter.setItemClickListener(this::ClickItem);
    }

    private void ClickItem(View view, int position) {
        MediaQueue media = adapter.getItemAt(position);
        Uri playbackUri = Uri.parse(media.getMediaUri());
        if (media.isVideo()) {
            VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        } else {
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_clearQueue:
                ClearQueue();
                break;

            case R.id.layout_playQueue:
                PlayQueue();
                break;
        }
    }

    private void PlayQueue() {
       *//* List<MediaQueue> listMedia = adapter.getCurrentList();
        List<Uri> uriList = new ArrayList<>();
        listMedia.forEach(item -> {
            Uri uri = Uri.parse(item.getMediaUri());
            uriList.add(uri);
        });*//*

        Uri playbackUri = GetPlaybackUriUtils.forPlaylist(PlaylistConstants.TYPE_VIDEO_QUEUE, 0);
   *//*     if (playlist.isVideo()) {
            VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        } else {
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        }*//*
        makeToast("Play all");

    }*/
/*
    private void DeleteItemQueue(View view, int position) {
        MediaQueue mediaQueue = adapter.getItemAt(position);
        MediaQueueDeleteDialog dialog = MediaQueueDeleteDialog.newInstance(mediaQueue);
        dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_DELETE_DIALOG_QUEUE);
    }

    private void ClearQueue() {
        MediaQueueDeleteDialog dialog = MediaQueueDeleteDialog.newInstance(null);
        dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_DELETE_DIALOG_ALL_QUEUE);
    }

    private void makeToast(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }*/
}