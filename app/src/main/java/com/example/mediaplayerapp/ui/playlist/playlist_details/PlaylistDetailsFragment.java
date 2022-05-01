package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.FragmentPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.ui.playlist.SharedViewModel;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;

import java.util.ArrayList;

public class PlaylistDetailsFragment extends Fragment implements View.OnClickListener {
    private Playlist playlist;
    private FragmentPlaylistDetailsBinding binding;
    private PlaylistDetailsAdapter adapter;
    private ArrayList<PlaylistMedia> arrayListMedias;
    private PlaylistViewModel playlistViewModel;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false);
        playlistViewModel=new ViewModelProvider(this).get(PlaylistViewModel.class);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddMore.setOnClickListener(this);

        arrayListMedias = new ArrayList<>();
        adapter = new PlaylistDetailsAdapter(new PlaylistDetailsAdapter.PlaylistMediaDiff());
       adapter.submitList(arrayListMedias);
        adapter.setContext(getActivity().getApplicationContext());
        binding.rcvPlaylistsDetails.setAdapter(adapter);

/*
        playlistViewModel.getAllPlaylists().observe(
                getViewLifecycleOwner(),
                arrayListMedias -> {
                    // Update the cached copy of the playlist in the adapter.
                    adapter.submitList(arrayListMedias);
                });
*/

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(), item -> {

            /*binding.tvIdPlaylist.setText("Playlist id: " + item.getId() +"\n" +
                    "Name: " + item.getName() + "\n"+
                    "Number: " + item.getNumbers() + "\n"+
                    "Song: " + item.getSongID() + "\n"+
                    "Video: " + item.getVideoID() + "\n"+
                    "IsVideo: " + item.isVideo() + "\n");*/

            playlist = item;
          /*  binding.tvPlaylistDetailsName.setText(item.getName().toString());
            binding.tvPlaylistDetailsNumbers.setText("Play list " + String.valueOf(item.getNumbers()));*/

            init();
        });


        //item detail (media) click
        adapter.setItemClickListener((v,position)->{
                /**
                 *
                 *
                 *        CLICK TO OPEN VIDEO OR SONG HERE
                 *
                 *
                 *
                 * */
        });


    }

    private void init() {
        binding.tvPlaylistDetailsName.setText(playlist.getName());
        binding.tvPlaylistDetailsNumbers.setText("Play list " + String.valueOf(playlist.getNumbers()));
    }

    ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //Intent data = result.getData();
                        try {
                            if (result.getData().getClipData() != null) {
                                //pick multiple media file
                                int count = result.getData().getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                                    PlaylistMedia video=MediaUtils.getVideoFromURI(getContext(),
                                            uri);
                                    arrayListMedias.add(video);


                                    /**
                                     *  add to database
                                     *
                                     * */
                                    playlist.setMediaUri(uri.toString());
                                    playlist.setNumbers(playlist.getNumbers()+count);
                                    playlistViewModel.update(playlist);
                                }
                            } else {
                                //pick single media file
                                Uri uri = result.getData().getData();
                                PlaylistMedia video=MediaUtils.getVideoFromURI(getContext(),
                                    uri);

                                arrayListMedias.add(video);
                            }

                        } finally {
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }
    );

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addMore:
                AddMoreMedia();
                break;
        }
    }

    private void AddMoreMedia() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickerLauncher.launch(Intent.createChooser(intent, "Select Video(s)"));
    }
}