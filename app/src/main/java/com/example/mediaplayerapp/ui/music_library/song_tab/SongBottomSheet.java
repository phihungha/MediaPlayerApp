package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.databinding.BottomSheetSongBinding;
import com.example.mediaplayerapp.databinding.FragmentSongDetailsBinding;
import com.example.mediaplayerapp.ui.playlist.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.ui.special_playlists.MediaQueueUtil;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.MessageUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;


public class SongBottomSheet extends BottomSheetDialogFragment {

    private static final String LOG_TAG = SongBottomSheet.class.getSimpleName();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final Song currentSong;

    public SongBottomSheet(Song song) {
        currentSong = song;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BottomSheetSongBinding binding =
                BottomSheetSongBinding.inflate(inflater,container,false);
        binding.bottomSheetSongNameTextview.setText(currentSong.getTitle());

        PlaylistViewModel playlistViewModel
                = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);
        PlaylistItemViewModel playlistItemViewModel
                = new ViewModelProvider(requireActivity()).get(PlaylistItemViewModel.class);

        LinearLayout SongDetail = binding.bottomSheetSongDetail;
        SongDetail.setOnClickListener(view -> {
            FragmentSongDetailsBinding songDetailBinding = FragmentSongDetailsBinding.inflate(inflater,container,false);
            songDetailBinding.songInfoTitle.setText(currentSong.getTitle());
            songDetailBinding.songInfoArtist.setText(currentSong.getArtistName());
            songDetailBinding.songInfoAlbum.setText(currentSong.getAlbumName());
            songDetailBinding.songInfoGenre.setText(currentSong.getGenre());
            songDetailBinding.songInfoDuration.setText(MediaTimeUtils.getFormattedTimeFromLong(currentSong.getDuration()));
            songDetailBinding.songInfoTimeadded.setText(MediaTimeUtils.getFormattedTimeFromZonedDateTime(currentSong.getTimeAdded()));
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(songDetailBinding.getRoot()).show();
        });

        List<Playlist> allSongPlaylists = new ArrayList<>();
        playlistViewModel.getAllSongPlaylists()
                .observe(requireActivity(),
                         playlists -> {
                             allSongPlaylists.clear();
                             allSongPlaylists.addAll(playlists);
                         }
                );

        binding.bottomSheetAddSongPlaylist.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(R.string.choose_a_playlist)
                    .setItems(
                            allSongPlaylists.stream().map(Playlist::getName).toArray(CharSequence[]::new),
                            (dialogInterface, i) -> {
                                PlaylistItem newPlaylistItem =
                                        new PlaylistItem(
                                            allSongPlaylists.get(i).getId(),
                                            currentSong.getUri().toString()
                                        );
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

        binding.bottomSheetAddSongToFavorites.setOnClickListener(
                view -> MediaQueueUtil.insertSongToFavourite(
                        requireActivity().getApplication(),
                        currentSong.getUri().toString()
                )
        );

        binding.bottomSheetAddSongToWatchLater.setOnClickListener(
                view -> MediaQueueUtil.insertSongToWatchLater(
                        requireActivity().getApplication(),
                        currentSong.getUri().toString()
                )
        );

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}