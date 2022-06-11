package com.example.mediaplayerapp.ui.video_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.data.video_library.Video;
import com.example.mediaplayerapp.databinding.BottomSheetVideoBinding;
import com.example.mediaplayerapp.databinding.DialogVideoInfoBinding;
import com.example.mediaplayerapp.ui.music_library.song_tab.SongBottomSheet;
import com.example.mediaplayerapp.ui.playlist.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.ui.special_playlists.MediaQueueUtil;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.MessageUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class VideoLibraryBottomSheetDialog extends BottomSheetDialogFragment {

    private final Video currentVideo;
    private static final String LOG_TAG = SongBottomSheet.class.getSimpleName();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public VideoLibraryBottomSheetDialog(Video video) {
        currentVideo = video;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BottomSheetVideoBinding binding
                = BottomSheetVideoBinding.inflate(inflater, container, false);

        PlaylistViewModel playlistViewModel
                = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);
        PlaylistItemViewModel playlistItemViewModel
                = new ViewModelProvider(requireActivity()).get(PlaylistItemViewModel.class);

        binding.bottomSheetVideoNameTextview.setText(currentVideo.getTitle());

        LinearLayout optionInfo = binding.bottomSheetOptionInfo;
        optionInfo.setOnClickListener(view1 -> {

            DialogVideoInfoBinding videoInfoBinding
                    = DialogVideoInfoBinding.inflate(inflater, container, false);

            videoInfoBinding.dialogVideoInfoVideoNameTextview.setText(currentVideo.getTitle());

            int duration = currentVideo.getDuration();
            String durationFormatted = String.format(
                    Locale.US,
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % 60
            );
            videoInfoBinding.dialogVideoInfoVideoLengthTextview.setText(durationFormatted);

            videoInfoBinding.dialogVideoInfoVideoPathTextview.setText(currentVideo.getLocation());

            videoInfoBinding.dialogVideoInfoVideoResolutionTextview
                    .setText(currentVideo.getResolution());

            videoInfoBinding.dialogVideoInfoVideoDateTakenTextview
                    .setText(MediaTimeUtils.getFormattedTimeFromZonedDateTime(currentVideo.getTimeAdded()));

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(videoInfoBinding.getRoot()).show();
        });

        List<Playlist> videoPlaylists = new ArrayList<>();
        playlistViewModel.getAllVideoPlaylists()
                .observe(requireActivity(),
                        playlists -> {
                            videoPlaylists.clear();
                            videoPlaylists.addAll(playlists);
                        }
                );

        binding.bottomSheetOptionAddPlaylist.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(R.string.choose_a_playlist)
                    .setItems(
                            videoPlaylists.stream().map(Playlist::getName).toArray(CharSequence[]::new),
                            (dialogInterface, i) -> {
                                PlaylistItem newPlaylistItem =
                                        new PlaylistItem(
                                                videoPlaylists.get(i).getId(),
                                                currentVideo.getUri().toString());
                                Disposable disposable
                                        = playlistItemViewModel.addPlaylistItem(newPlaylistItem)
                                        .subscribe(
                                                () -> MessageUtils.makeShortToast(requireContext(), R.string.added_to_playlist),
                                                e -> {
                                                    String message = e.getMessage();
                                                    if (message != null && message.contains("UNIQUE"))
                                                        MessageUtils.makeShortToast(requireContext(), R.string.item_already_added);
                                                    else
                                                        MessageUtils.displayError(
                                                                requireContext(),
                                                                LOG_TAG,
                                                                e.getMessage());
                                                }
                                        );
                                disposables.add(disposable);
                            })
                    .show();
        });

        binding.bottomSheetOptionAddToFavorites.setOnClickListener(
                view -> MediaQueueUtil.insertVideoToFavourite(
                        requireActivity().getApplication(),
                        currentVideo.getUri().toString()
                ));

        binding.bottomSheetOptionAddToWatchLater.setOnClickListener(
                view -> MediaQueueUtil.insertVideoToWatchLater(
                        requireActivity().getApplication(),
                        currentVideo.getUri().toString()
                ));

        return binding.getRoot();
    }
}
