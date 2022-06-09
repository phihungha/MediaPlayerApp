package com.example.mediaplayerapp.ui.video_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.PlaylistRepository;
import com.example.mediaplayerapp.data.video_library.Video;
import com.example.mediaplayerapp.databinding.BottomSheetVideoBinding;
import com.example.mediaplayerapp.databinding.DialogVideoInfoBinding;
import com.example.mediaplayerapp.ui.special_playlists.MediaQueueUtil;
import com.example.mediaplayerapp.ui.playlist.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.SortOrder;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VideoLibraryBottomSheetDialog extends BottomSheetDialogFragment {

    private final Video currentVideo;


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

            videoInfoBinding.dialogVideoInfoVideoSizeTextview
                    .setText(convertFileSize(currentVideo.getSize()));

            videoInfoBinding.dialogVideoInfoVideoResolutionTextview
                    .setText(currentVideo.getResolution());

            videoInfoBinding.dialogVideoInfoVideoDateTakenTextview
                    .setText(MediaTimeUtils.getFormattedTimeFromZonedDateTime(currentVideo.getTimeAdded()));

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(videoInfoBinding.getRoot()).show();
        });

        PlaylistViewModel playlistViewModel
                = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        // Get all playlists that contain videos
        //**** MUST **** do this before setOnClickListener for optionAddPlaylist, or else the first
        // time user clicks, dialog only shows title, NO ITEMS
        List<Playlist> allVideoPlaylists = new ArrayList<>();
        playlistViewModel.getAllPlaylists(PlaylistRepository.SortBy.NAME, SortOrder.ASC)
                .observe(
                    requireActivity(),
                        playlists -> {
                            for (Playlist playlist : playlists) {
                                if (playlist.isVideo())
                                    allVideoPlaylists.add(playlist);
                            }
                        }
                 );

        LinearLayout optionAddPlaylist = binding.bottomSheetOptionAddPlaylist;
        optionAddPlaylist.setOnClickListener(view1 -> {
            PlaylistItemViewModel PlaylistItemViewModel
                    = new ViewModelProvider(requireActivity()).get(PlaylistItemViewModel.class);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder
                    .setTitle("Choose a playlist: ")
                    .setItems(
                            allVideoPlaylists.stream().map(Playlist::getName).toArray(CharSequence[]::new),
                            (dialogInterface, i) -> {
                                PlaylistItem newPlaylistItem =
                                        new PlaylistItem(
                                            allVideoPlaylists.get(i).getId(),
                                            currentVideo.getUri().toString()
                                );
                                PlaylistItemViewModel.addPlaylistItem(newPlaylistItem);
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

    /**
     * Convert a file size from type "long" to an easy-to-look "String"
     *
     * @param size The file size that needs converting
     * @return The result string
     */
    private String convertFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#")
                .format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
