package com.example.mediaplayerapp.ui.music_player;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.databinding.FragmentMusicPlayerControlBinding;
import com.example.mediaplayerapp.services.MusicPlaybackService;
import com.example.mediaplayerapp.ui.playlist.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.ui.special_playlists.MediaQueueUtil;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.MessageUtils;
import com.google.android.exoplayer2.ui.TimeBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;

public class MusicPlayerControlFragment extends Fragment {

    private static final String LOG_TAG = MusicPlayerControlFragment.class.getSimpleName();
    private static final int AUTOSCROLL_DELAY = 4500;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final List<Playlist> playlists = new ArrayList<>();
    private PlaylistItemViewModel playlistItemViewModel;

    private MediaMetadataCompat currentMediaMetadata;
    private int currentPlaylistId = -1;
    private String currentMediaUri;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted)
                            bindAudioVisualizerToAudio();
                        else
                            showAudioVisualizerPermissionDeniedNotice();
                    });

    FragmentMusicPlayerControlBinding binding;

    public MusicPlayerControlFragment() {
        // Required empty public constructor
    }

    public static MusicPlayerControlFragment newInstance() {
        return new MusicPlayerControlFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMusicPlayerControlBinding.inflate(inflater, container, false);

        playlistItemViewModel = new ViewModelProvider(this).get(PlaylistItemViewModel.class);
        PlaylistViewModel playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        playlistViewModel.getAllSongPlaylists()
                        .observe(getViewLifecycleOwner(),
                                newPlaylists -> {
                            playlists.clear();
                            playlists.addAll(newPlaylists);
                        });

        beginObservingMediaControllerData();

        setupTransportControls();
        binding.musicPlayerCloseBtn.setOnClickListener(view -> requireActivity().finishAfterTransition());
        binding.musicPlayerMenuBtn.setOnClickListener(view -> openMenu());
        binding.musicPlayerVisualizer.setDensity(70);

        // Delay text auto-scroll
        Handler handler = new Handler(requireActivity().getMainLooper());
        handler.postDelayed(() -> {
            binding.musicPlayerSongTitle.setSelected(true);
            binding.musicPlayerSongArtist.setSelected(true);
        }, AUTOSCROLL_DELAY);

        ((MusicPlayerActivity) requireActivity()).enterEdgeToEdgeUIMode();

        return binding.getRoot();
    }

    private void beginObservingMediaControllerData() {
        MusicPlayerViewModel musicPlayerViewModel
                = new ViewModelProvider(requireActivity()).get(MusicPlayerViewModel.class);

        musicPlayerViewModel.getCurrentMediaMetadata().observe(
                getViewLifecycleOwner(), this::setMediaMetadata
        );

        musicPlayerViewModel.getCurrentPlaybackState().observe(
                getViewLifecycleOwner(), this::setPlaybackState
        );

        musicPlayerViewModel.getCurrentRepeatMode().observe(
                getViewLifecycleOwner(), this::setRepeatMode
        );

        musicPlayerViewModel.getCurrentShuffleMode().observe(
                getViewLifecycleOwner(), this::setShuffleMode
        );

        musicPlayerViewModel.getCurrentPlaylistId().observe(
                getViewLifecycleOwner(), this::setCurrentPlaylistId
        );

        musicPlayerViewModel.getEnableTransportControls().observe(
                getViewLifecycleOwner(),
                enableTransportControls -> {
                    if (enableTransportControls)
                        enablePlaybackControlCapability();
                    else
                        disableTransportControls();
                }
        );
    }

    /**
     * Set current media metadata.
     * @param metadata New media metadata
     */
    private void setMediaMetadata(MediaMetadataCompat metadata) {
        currentMediaMetadata = metadata;
        currentMediaUri = metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI);

        updateMediaMetadataViews();
    }

    /**
     * Set current playback state.
     * @param playbackState New playback state
     */
    private void setPlaybackState(PlaybackStateCompat playbackState) {
        if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            // Bind visualizer to new audio session id if it changes.
            if (checkAudioVisualizerPermission())
                bindAudioVisualizerToAudio();
            binding.musicPlayerPlayPauseBtn.setImageLevel(1);
        } else
            binding.musicPlayerPlayPauseBtn.setImageLevel(0);
    }

    /**
     * Set current repeat mode.
     * @param repeatMode New repeat mode
     */
    private void setRepeatMode(int repeatMode) {
        if (repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE)
            binding.musicPlayerRepeatBtn.setImageLevel(0);
        else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL)
            binding.musicPlayerRepeatBtn.setImageLevel(1);
        else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE)
            binding.musicPlayerRepeatBtn.setImageLevel(2);
    }

    /**
     * Set current shuffle mode.
     * @param shuffleMode New shuffle mode
     */
    private void setShuffleMode(int shuffleMode) {
        if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE)
            binding.musicPlayerShuffleBtn.setImageLevel(0);
        else if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL)
            binding.musicPlayerShuffleBtn.setImageLevel(1);
    }

    /**
     * Set id of current playlist.
     * @param id New playlist id
     */
    private void setCurrentPlaylistId(int id) {
        currentPlaylistId = id;
    }

    /**
     * Enable all transport controls and time indicators on the UI.
     */
    private void enablePlaybackControlCapability() {
        binding.musicPlayerPlayPauseBtn.setEnabled(true);
        binding.musicPlayerSkipPrevBtn.setEnabled(true);
        binding.musicPlayerSkipNextBtn.setEnabled(true);
        binding.musicPlayerRepeatBtn.setEnabled(true);
        binding.musicPlayerShuffleBtn.setEnabled(true);
        binding.musicPlayerSeekbar.setEnabled(true);
        startTimeIndicators();
    }

    /**
     * Disable all transport controls on the UI in case
     * we lost connection to the music playback service.
     */
    private void disableTransportControls() {
        binding.musicPlayerPlayPauseBtn.setEnabled(false);
        binding.musicPlayerSkipPrevBtn.setEnabled(false);
        binding.musicPlayerSkipNextBtn.setEnabled(false);
        binding.musicPlayerRepeatBtn.setEnabled(false);
        binding.musicPlayerShuffleBtn.setEnabled(false);
        binding.musicPlayerSeekbar.setEnabled(false);
    }

    /**
     * Open more button's menu.
     */
    private void openMenu() {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), binding.musicPlayerMenuBtn);
        popupMenu.inflate(R.menu.music_player_menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.music_player_add_to_playlist)
                openAddToPlaylistDialog();
            else if (itemId == R.id.music_player_playlist)
                openPlaylistEditScreen();
            else if (itemId == R.id.music_player_add_to_favorites)
                addToFavorites();
            else if (itemId == R.id.music_player_add_to_listen_later)
                addToWatchLater();
            return true;
        });
        popupMenu.setForceShowIcon(true);
        popupMenu.show();
    }

    private void openAddToPlaylistDialog() {
        CharSequence[] playlistOptions = playlists
                .stream()
                .map(Playlist::getName)
                .toArray(CharSequence[]::new);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.choose_a_playlist)
                .setItems(
                        playlistOptions,
                        (dialogInterface, i) -> {
                            PlaylistItem newPlaylistItem
                                    = new PlaylistItem(
                                            playlists.get(i).getId(),
                                            currentMediaUri);
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
                        });
        builder.show();
    }

    private void openPlaylistEditScreen() {
        ((MusicPlayerActivity) requireActivity()).exitEdgeToEdgeUIMode();
        Navigation.findNavController(binding.getRoot())
                .navigate(MusicPlayerControlFragmentDirections
                        .actionMusicPlayerControlFragmentToPlaylistDetailsFragment(currentPlaylistId));
    }

    private void addToFavorites() {
        MediaQueueUtil.insertSongToFavourite(requireActivity().getApplication(), currentMediaUri);
        Toast.makeText(requireActivity(), "Added to favorites!", Toast.LENGTH_SHORT).show();
    }

    private void addToWatchLater() {
        MediaQueueUtil.insertSongToWatchLater(requireActivity().getApplication(), currentMediaUri);
        Toast.makeText(requireActivity(), "Added to favorites!", Toast.LENGTH_SHORT).show();
    }


    /**
     * Set up transport controls on the UI.
     */
    private void setupTransportControls() {
        binding.musicPlayerPlayPauseBtn.setOnClickListener(view -> {
            MediaControllerCompat controller = getMediaController();
            int playbackState = controller.getPlaybackState().getState();
            if (playbackState == PlaybackStateCompat.STATE_PLAYING)
                controller.getTransportControls().pause();
            else if (playbackState == PlaybackStateCompat.STATE_PAUSED)
                controller.getTransportControls().play();
        });

        binding.musicPlayerRepeatBtn.setOnClickListener(view -> {
            MediaControllerCompat controller = getMediaController();
            int repeatMode = controller.getRepeatMode();
            if (repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE)
                controller.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL);
            else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL)
                controller.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
            else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE)
                controller.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
        });

        binding.musicPlayerShuffleBtn.setOnClickListener(view -> {
            MediaControllerCompat controller = getMediaController();
            int shuffleMode = controller.getShuffleMode();
            if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE)
                controller.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
            else if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL)
                controller.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
        });

        binding.musicPlayerSkipNextBtn.setOnClickListener(
                view -> getMediaController()
                        .getTransportControls()
                        .skipToNext()
        );

        binding.musicPlayerSkipPrevBtn.setOnClickListener(
                view -> getMediaController()
                        .getTransportControls()
                        .skipToPrevious()
        );

        binding.musicPlayerSeekbar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {

            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                timeBar.setPosition(position);
                binding.musicPlayerSongCurrentPosition.setText(MediaTimeUtils.getFormattedTimeFromLong(position));
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                getMediaController()
                        .getTransportControls()
                        .seekTo(position);
            }
        });
    }

    /**
     * Begin updating current playback position and seekbar progress.
     */
    private void startTimeIndicators() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null)
                    return;
                long currentPlaybackPosition = getMediaController()
                        .getPlaybackState()
                        .getPosition();
                binding.musicPlayerSeekbar.setPosition(currentPlaybackPosition);
                binding.musicPlayerSongCurrentPosition.setText(
                        MediaTimeUtils.getFormattedTimeFromLong(currentPlaybackPosition)
                );
                handler.postDelayed(this, 100);
            }
        };
        handler.post(runnable);
    }

    /**
     * Connect audio visualizer to the current music audio stream.
     */
    private void bindAudioVisualizerToAudio() {
        int audioSessionId = getMediaController()
                .getExtras()
                .getInt(MusicPlaybackService.AUDIO_SESSION_ID_KEY);
        binding.musicPlayerVisualizer.setPlayer(audioSessionId);
    }

    /**
     * Check RECORD_AUDIO permission for audio visualizer
     * and request it if necessary.
     *
     * @return True if RECORD_AUDIO permission is granted.
     */
    private boolean checkAudioVisualizerPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)
            return true;

        if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
            dialogBuilder.setMessage(R.string.audio_visualizer_permission_rationale_message)
                    .setTitle(R.string.audio_visualizer_permission_rationale_title)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    });
            dialogBuilder.create().show();
        } else
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);

        return false;
    }

    /**
     * Show that the audio visualizer has been disabled because
     * RECORD_AUDIO permission is denied.
     */
    private void showAudioVisualizerPermissionDeniedNotice() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        dialogBuilder.setMessage(R.string.audio_visualizer_permission_denied_notice_message)
                .setTitle(R.string.audio_visualizer_permission_denied_notice_title)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                });
        dialogBuilder.create().show();
    }

    /**
     * Update views that display media metadata.
     */
    private void updateMediaMetadataViews() {
        binding.musicPlayerSongTitle.setText(currentMediaMetadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE));
        binding.musicPlayerSongArtist.setText(currentMediaMetadata.getText(MediaMetadataCompat.METADATA_KEY_ARTIST));

        long duration = currentMediaMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        binding.musicPlayerSongDuration.setText(MediaTimeUtils.getFormattedTimeFromLong(duration));
        binding.musicPlayerSeekbar.setDuration(duration);

        String artworkUri = currentMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI);
        Bitmap artworkBitmap = currentMediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART);
        if (artworkBitmap != null)
            setArtworkFromBitmap(artworkBitmap);
        else if (artworkUri != null)
            setArtworkFromArtworkUri(Uri.parse(artworkUri));
        else
            setDefaultArtwork();
        Log.d(LOG_TAG, "Media metadata views updated");
    }

    /**
     * Set song's artwork from artwork's uri.
     *
     * @param artworkUri Artwork's uri
     */
    private void setArtworkFromArtworkUri(Uri artworkUri) {
        Glide.with(this)
                .asBitmap()
                .load(artworkUri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        setArtworkFromBitmap(resource);
                        Log.d(LOG_TAG, "Song's artwork loaded from uri");
                        setViewsColors(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * Set song's artwork from artwork's bitmap.
     *
     * @param artworkBitmap Artwork's bitmap
     */
    private void setArtworkFromBitmap(Bitmap artworkBitmap) {
        binding.musicPlayerSongArtwork.setImageBitmap(artworkBitmap);

        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(
                new BlurTransformation(6, 5),
                new BrightnessFilterTransformation(-0.2f));
        Glide.with(this)
                .load(artworkBitmap)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .placeholder(R.drawable.shape_default_artwork_background)
                .into(binding.musicPlayerSongArtworkBackground);
        Log.d(LOG_TAG, "Loaded song's artwork from bitmap");

        setViewsColors(artworkBitmap);
    }

    /**
     * Set default artwork.
     */
    private void setDefaultArtwork() {
        binding.musicPlayerSongArtwork.setImageDrawable(null);
        binding.musicPlayerSongArtworkBackground.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(),
                        R.drawable.shape_default_artwork_background));
        Log.d(LOG_TAG, "Loaded default song's artwork");
        setDefaultViewsColors();
    }

    /**
     * Set colors of views on the UI based on song's artwork.
     *
     * @param artworkBitmap Artwork's bitmap
     */
    private void setViewsColors(Bitmap artworkBitmap) {
        Palette.from(artworkBitmap).generate(palette -> {
            if (palette == null)
                return;
            int color = palette.getLightVibrantColor(
                    ContextCompat.getColor(requireActivity(), R.color.white));
            int darkerColor = palette.getDarkMutedColor(
                    ContextCompat.getColor(requireActivity(), R.color.bright_grey));
            binding.musicPlayerSongTitle.setTextColor(color);
            binding.musicPlayerSongArtist.setTextColor(color);
            binding.musicPlayerRepeatBtn.setColorFilter(color);
            binding.musicPlayerShuffleBtn.setColorFilter(color);
            binding.musicPlayerSongDuration.setTextColor(color);
            binding.musicPlayerSongCurrentPosition.setTextColor(color);
            binding.musicPlayerPlayPauseBtn.setColorFilter(color);
            binding.musicPlayerSkipNextBtn.setColorFilter(color);
            binding.musicPlayerSkipPrevBtn.setColorFilter(color);
            binding.musicPlayerSeekbar.setPlayedColor(color);
            binding.musicPlayerSeekbar.setScrubberColor(color);
            binding.musicPlayerSeekbar.setUnplayedColor(darkerColor);
            binding.musicPlayerScreenTitle.setTextColor(color);
            binding.musicPlayerMenuBtn.setColorFilter(color);
            binding.musicPlayerCloseBtn.setColorFilter(color);
            binding.musicPlayerVisualizer.setColor(color);
            Log.d(LOG_TAG, "Updated views' colors using song artwork's palette");
        });
    }

    /**
     * Set views' colors back to default values.
     */
    private void setDefaultViewsColors() {
        int color = ContextCompat.getColor(requireActivity(), R.color.white);
        int darkerColor = ContextCompat.getColor(requireActivity(), R.color.bright_grey);
        binding.musicPlayerSongTitle.setTextColor(color);
        binding.musicPlayerSongArtist.setTextColor(color);
        binding.musicPlayerRepeatBtn.setColorFilter(color);
        binding.musicPlayerShuffleBtn.setColorFilter(color);
        binding.musicPlayerSongDuration.setTextColor(color);
        binding.musicPlayerSongCurrentPosition.setTextColor(color);
        binding.musicPlayerPlayPauseBtn.setColorFilter(color);
        binding.musicPlayerSkipNextBtn.setColorFilter(color);
        binding.musicPlayerSkipPrevBtn.setColorFilter(color);
        binding.musicPlayerSeekbar.setPlayedColor(color);
        binding.musicPlayerSeekbar.setScrubberColor(color);
        binding.musicPlayerSeekbar.setUnplayedColor(darkerColor);
        binding.musicPlayerScreenTitle.setTextColor(color);
        binding.musicPlayerMenuBtn.setColorFilter(color);
        binding.musicPlayerCloseBtn.setColorFilter(color);
        binding.musicPlayerVisualizer.setColor(color);
        Log.d(LOG_TAG, "Updated views' colors to default");
    }

    private MediaControllerCompat getMediaController() {
        return MediaControllerCompat.getMediaController(requireActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}