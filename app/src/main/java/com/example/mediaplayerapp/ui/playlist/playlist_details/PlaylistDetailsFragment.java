package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueViewModel;
import com.example.mediaplayerapp.data.playlist.playlist_details.MediaItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.MediaItemViewModel;
import com.example.mediaplayerapp.databinding.FragmentPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;

public class PlaylistDetailsFragment extends Fragment implements View.OnClickListener {
    private Playlist playlist;
    private FragmentPlaylistDetailsBinding binding;
    private MediaItemAdapter adapter;
    private MediaItemViewModel mediaItemViewModel;

    private boolean isASC = false;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false);
        mediaItemViewModel = new ViewModelProvider(this).get(MediaItemViewModel.class);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            playlist = (Playlist) bundle.getSerializable(PlaylistConstants.KEY_TRANSFER_PLAYLIST);
        }

        adapter = new MediaItemAdapter(new MediaItemAdapter.PlaylistMediaDiff());
        adapter.setContext(getContext());
        adapter.setApplication(requireActivity().getApplication());
        mediaItemViewModel.getAllPlaylistMediasWithID(playlist.getId()).observe(
                getViewLifecycleOwner(),
                media -> adapter.submitList(media)
        );
        binding.rcvPlaylistsDetails.setAdapter(adapter);
        binding.imgThumbnailPlaylistDetails.setImageResource(playlist.getIdResource());

        setListener();
        refresh();
    }

    public void refresh() {
        int count = mediaItemViewModel.getCountPlaylistWithID(playlist.getId());
        String textNumber = "Playlist " + count + " ";

        if (playlist.getId()==1){
            if (count <= 1) {
                textNumber += "media";
            } else
                textNumber += "medias";
        }
        else
        {
            if (playlist.isVideo()) {
                if (count <= 1) {
                    textNumber += "video";
                } else
                    textNumber += "videos";
            } else {
                if (count <= 1) {
                    textNumber += "song";
                } else
                    textNumber += "songs";
            }
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
            Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlist.getId(), position);
            if (playlist.isVideo()){
                VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            }
            else {
                MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            }
        });

        // click play bottom sheet
        adapter.setBsPlayListener((view, position) -> {
            Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlist.getId(), 0);
            if (playlist.isVideo()){
                VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            }
            else {
                MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            }
        });
        // click delete bottom sheet
        adapter.setBsDeleteListener((view, position) -> {
            MediaItem media = adapter.getPlaylistMediaItemAt(position);
            PlaylistDetailsDeleteDialog dialog = PlaylistDetailsDeleteDialog.newInstance(media);
            dialog.setPlaylistDetailsFragment(this);
            dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_BS_DETAIL_DELETE_DIALOG);
        });
        
        //click add to queue bottom sheet
        adapter.setBsAddQueueListener((view, position) -> {
            MediaItem media=adapter.getPlaylistMediaItemAt(position);

            MediaQueueViewModel mediaQueueViewModel=new ViewModelProvider(requireActivity())
                    .get(MediaQueueViewModel.class);

            MediaQueue mediaQueue=new MediaQueue(media.getMediaUri(),media.getName());
            mediaQueueViewModel.insert(mediaQueue);
        });

        //click properties bottom sheet
        adapter.setBsPropertiesListener((view, position) -> {
            MediaItem media = adapter.getPlaylistMediaItemAt(position);

            MediaInfo mediaInfo=MediaUtils.getInfoWithUri(requireContext(),
                    Uri.parse(media.getMediaUri()));

            PlaylistDetailsPropertiesDialog dialog = PlaylistDetailsPropertiesDialog.newInstance(mediaInfo);
            dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_BS_DETAIL_PROPERTY_DIALOG);
        });
    }

    private void PlayAll() {
        Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlist.getId(), 0);
        if (playlist.isVideo()){
            VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        }
        else {
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        }
    }

    private void PlayShuffleAll() {
        Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlist.getId(), 0);
        if (playlist.isVideo()){
            VideoPlayerActivity.launchWithUriAndShuffleAll(requireActivity(), playbackUri);
        }
        else {
            MusicPlayerActivity.launchWithUriAndShuffleAll(requireActivity(), playbackUri);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_addMore) {
            AddMoreMedia();
        } else if (id == R.id.layout_playAll) {
            PlayAll();
        } else if (id == R.id.layout_shuffleAll) {
            PlayShuffleAll();
        }
    }

    private void AddMoreMedia() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (playlist.getId()==1){
            intent.setType("*/*");
        }else {
            if (playlist.isVideo()) {
                intent.setType("video/*");
            } else {
                intent.setType("audio/*");
            }
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
                        MediaItem media = new MediaItem(
                                playlist.getId(),
                                uri.toString(),
                                MediaUtils.getMediaNameFromURI(requireContext(), uri)
                        );
                        mediaItemViewModel.insert(media);
                    }
                } else {
                    //pick single media file
                    Uri uri = data.getData();
                    MediaItem media = new MediaItem(
                            playlist.getId(),
                            uri.toString(),
                            MediaUtils.getMediaNameFromURI(requireContext(), uri)
                    );
                    mediaItemViewModel.insert(media);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        refresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.playlist_options_menu, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setIconified(true);
        searchView.setQueryHint(PlaylistConstants.STRING_HINT_SEARCH);

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Searching(s);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort)
            SortByName();
        return super.onOptionsItemSelected(item);
    }

    private void Searching(String s){
        if (s.equals("")) {
            mediaItemViewModel.getAllPlaylistMediasWithID(playlist.getId()).observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        } else {
            mediaItemViewModel.getAllMediaSearching(s).observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        }
    }

    private void SortByName(){
        if (isASC) {
            mediaItemViewModel.sortAllMediaByNameDESCWithID(playlist.getId()).observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        } else {
            mediaItemViewModel.sortAllMediaByNameASCWithID(playlist.getId()).observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        }
        isASC = !isASC;
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