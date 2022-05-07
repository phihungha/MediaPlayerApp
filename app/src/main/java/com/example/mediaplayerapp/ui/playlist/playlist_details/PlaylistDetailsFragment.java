package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
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
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            playlist = (Playlist) bundle.getSerializable(PlaylistConstants.KEY_TRANSFER_PLAYLIST);

            refresh();
        }
        adapter = new PlaylistDetailsAdapter(new PlaylistDetailsAdapter.PlaylistMediaDiff());
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
        setListener();
    }

    public void refresh() {
        int count = playlistMediaViewModel.getCountPlaylistWithID(playlist.getId());
        String textNumber = "Playlist " + count + " ";
        if (playlist.isVideo()) {
            if (count <= 1) {
                textNumber += "video";
            } else
                textNumber += "videos";
        } else {
            if (count <= 1) {
                textNumber += "audio";
            } else
                textNumber += "audios";
        }
        binding.tvPlaylistDetailsName.setText(playlist.getName());
        binding.tvPlaylistDetailsNumbers.setText(textNumber);
    }

    private void setListener() {
        binding.btnAddMore.setOnClickListener(this);
        binding.layoutPlayAll.setOnClickListener(this);
        binding.layoutShuffleAll.setOnClickListener(this);
        //item detail (media) click
        adapter.setItemClickListener((v, position) -> {
            PlaylistMedia media=adapter.getPlaylistMediaItemAt(position);
            if (playlist.isVideo()){
                /**
                 *
                 *
                 *        CLICK TO OPEN VIDEO HERE
                 *
                 *
                 *
                 * */
            }
            else {
                /**
                 *
                 *
                 *        CLICK TO OPEN SONG HERE
                 *
                 *
                 *
                 * */
            }
            Toast.makeText(getActivity(), "click + pos " + position, Toast.LENGTH_SHORT).show();
        });

        // click play bottom sheet
        adapter.setBsPlayListener((view, position) -> {
            PlaylistMedia media=adapter.getPlaylistMediaItemAt(position);
            if (playlist.isVideo()){
                /**
                 *
                 *
                 *        CLICK TO OPEN VIDEO HERE
                 *
                 *
                 *
                 * */
            }
            else {
                /**
                 *
                 *
                 *        CLICK TO OPEN SONG HERE
                 *
                 *
                 *
                 * */
            }
            Toast.makeText(getActivity(), "Play media pos " + position, Toast.LENGTH_SHORT).show();
        });
        // click delete bottom sheet
        adapter.setBsDeleteListener((view, position) -> {
            PlaylistMedia media = adapter.getPlaylistMediaItemAt(position);
            PlaylistDetailsDeleteDialog dialog = PlaylistDetailsDeleteDialog.newInstance(media);
            dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_BS_DETAIL_DELETE_DIALOG);
        });

        //click properties bottom sheet
        adapter.setBsPropertiesListener((view, position) -> {
            PlaylistMedia media = adapter.getPlaylistMediaItemAt(position);
            MediaInfo mediaInfo=MediaUtils.getInfoWithUri(getContext(),Uri.parse(media.getMediaUri()));

            PlaylistDetailsPropertiesDialog dialog = PlaylistDetailsPropertiesDialog.newInstance(mediaInfo);
            dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_BS_DETAIL_PROPERTY_DIALOG);
        });
    }


    private void PlayAll() {
        if (playlist.isVideo()){
            /**
             *
             *
             *        CLICK TO OPEN VIDEO HERE
             *
             *
             *
             * */
        }
        else {
            /**
             *
             *
             *        CLICK TO OPEN SONG HERE
             *
             *
             *
             * */
        }
        Toast.makeText(getActivity(), "Play all", Toast.LENGTH_SHORT).show();
    }

    private void PlayShuffleAll() {
        if (playlist.isVideo()){
            /**
             *
             *
             *        CLICK TO OPEN VIDEO HERE
             *
             *
             *
             * */
        }
        else {
            /**
             *
             *
             *        CLICK TO OPEN SONG HERE
             *
             *
             *
             * */
        }
        Toast.makeText(getActivity(), "Play Shuffle All ", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addMore:
                AddMoreMedia();
                break;

            case R.id.layout_playAll:
                PlayAll();
                break;

            case R.id.layout_shuffleAll:
                PlayShuffleAll();
                break;
        }
    }

    private void AddMoreMedia() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (playlist.isVideo()) {
            intent.setType("video/*");
        } else {
            intent.setType("audio/*");
        }
        startActivityForResult(intent, PlaylistConstants.REQUEST_CODE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PlaylistConstants.REQUEST_CODE_GALLERY
                && resultCode == Activity.RESULT_OK
                && data != null) {
            try {
                if (data.getClipData() != null) {
                    //pick multiple media file
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        PlaylistMedia media = new PlaylistMedia(
                                playlist.getId(),
                                uri.toString(),
                                MediaUtils.getMediaNameFromURI(requireContext(), uri)
                        );
                        playlistMediaViewModel.insert(media);
                    }
                } else {
                    //pick single media file
                    Uri uri = data.getData();
                    PlaylistMedia media = new PlaylistMedia(
                            playlist.getId(),
                            uri.toString(),
                            MediaUtils.getMediaNameFromURI(requireContext(), uri)
                    );
                    playlistMediaViewModel.insert(media);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

   /* public final ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Intent data = result.getData();
                    Log.d("TAG", "activityLauncher");
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null) {
                        try {
                            if (result.getData().getClipData() != null) {
                                //pick multiple media file
                                int count = result.getData().getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                                    PlaylistMedia media = new PlaylistMedia(
                                            playlist.getId(),
                                            uri.toString(),
                                            MediaUtils.getMediaNameFromURI(requireContext(), uri)
                                    );
                                    playlistMediaViewModel.insert(media);
                                }
                            } else {
                                //pick single media file
                                Uri uri = result.getData().getData();
                                PlaylistMedia media = new PlaylistMedia(
                                        playlist.getId(),
                                        uri.toString(),
                                        MediaUtils.getMediaNameFromURI(requireContext(), uri)
                                );
                                playlistMediaViewModel.insert(media);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
    );*/
}