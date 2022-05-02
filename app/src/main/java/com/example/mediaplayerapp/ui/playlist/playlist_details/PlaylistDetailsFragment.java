package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.Activity;
import android.app.TaskInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.FragmentPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;

import java.util.List;

public class PlaylistDetailsFragment extends Fragment implements View.OnClickListener {
    private Playlist playlist;
    private FragmentPlaylistDetailsBinding binding;
    private PlaylistDetailsAdapter adapter;
    private PlaylistMediaViewModel playlistMediaViewModel;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false);
        playlistMediaViewModel = new ViewModelProvider(this).get(PlaylistMediaViewModel.class);

        /*SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(),
                new Observer<Playlist>() {
                    @Override
                    public void onChanged(Playlist p) {
                        Log.d("TAG","onChanged_start");
                        playlist = p;
                        id=p.getId();
                        binding.tvPlaylistDetailsName.setText(playlist.getName() + ", ID "+ playlist.getId());
                        binding.tvPlaylistDetailsNumbers.setText("Play list 0 media");
                        Log.d("TAG","onChanged_end");
                    }

            *//*binding.tvIdPlaylist.setText("Playlist id: " + item.getId() +"\n" +
                    "Name: " + item.getName() + "\n"+
                    "Number: " + item.getNumbers() + "\n"+
                    "Song: " + item.getSongID() + "\n"+
                    "Video: " + item.getVideoID() + "\n"+
                    "IsVideo: " + item.isVideo() + "\n");*//*

                });*/

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {

            playlist = (Playlist) bundle.getSerializable(PlaylistConstants.KEY_TRANSFER_PLAYLIST);

            binding.tvPlaylistDetailsName.setText(playlist.getName() + ", ID " + playlist.getId());
            binding.tvPlaylistDetailsNumbers.setText("Play list 0 media");
            bundle.clear();
        }

        // arrayListMedias = new ArrayList<>();
        adapter = new PlaylistDetailsAdapter(new PlaylistDetailsAdapter.PlaylistMediaDiff());
        //adapter.submitList(arrayListMedias);
        adapter.setContext(getContext());
        binding.rcvPlaylistsDetails.setAdapter(adapter);

        playlistMediaViewModel.getAllPlaylistMediasWithID(playlist.getId()).observe(
                getViewLifecycleOwner(),
                new Observer<List<PlaylistMedia>>() {
                    @Override
                    public void onChanged(List<PlaylistMedia> media) {
                        adapter.submitList(media);
                    }
                }
        );

        //item detail (media) click
        adapter.setItemClickListener((v, position) -> {
            /**
             *
             *
             *        CLICK TO OPEN VIDEO OR SONG HERE
             *
             *
             *
             * */
            Toast.makeText(getActivity(), "click + pos "+position, Toast.LENGTH_SHORT).show();
        });

        binding.btnAddMore.setOnClickListener(this);
    }

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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (playlist.isVideo()) {
            intent.setType("video/*");
            pickerLauncher.launch(Intent.createChooser(intent, "Select Video(s)"));
        } else {
            intent.setType("audio/*");
            pickerLauncher.launch(Intent.createChooser(intent, "Select Audio(s)"));
        }
    }

    ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Intent data = result.getData();
                    try {
                        if (result.getData().getClipData() != null) {
                            //pick multiple media file
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                                PlaylistMedia media = new PlaylistMedia(
                                        playlist.getId(),
                                        uri.toString(),
                                        MediaUtils.getMediaNameFromURI(getContext(), uri)
                                );
                                playlistMediaViewModel.insert(media);
                            }
                        } else {
                            //pick single media file
                            Uri uri = result.getData().getData();
                            PlaylistMedia media = new PlaylistMedia(
                                    playlist.getId(),
                                    uri.toString(),
                                    MediaUtils.getMediaNameFromURI(getContext(), uri)
                            );
                            playlistMediaViewModel.insert(media);
                        }
                    } finally {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );

}