package com.example.mediaplayerapp.ui.music_player;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mediaplayerapp.MainActivity;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ActivityMainBinding;
import com.example.mediaplayerapp.services.MusicPlaybackService;

/**
 * Logic for bottom music player UI.
 */
public class BottomMusicPlayerComponent implements DefaultLifecycleObserver {

    private static final String LOG_TAG = BottomMusicPlayerComponent.class.getSimpleName();
    private boolean isDisplayed = false;

    private MediaBrowserCompat mediaBrowser;
    MediaBrowserCompat.ConnectionCallback connectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    Log.i(LOG_TAG, "Connected to music playback service");
                    MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                    MediaControllerCompat mediaController =
                            new MediaControllerCompat(activity, token);
                    MediaControllerCompat.setMediaController(activity, mediaController);
                    setupTransportControls();
                    MediaMetadataCompat metadata = mediaController.getMetadata();
                    PlaybackStateCompat playbackState = mediaController.getPlaybackState();
                    if (metadata != null)
                        controllerCallback.onMetadataChanged(metadata);
                    if (playbackState != null)
                        controllerCallback.onPlaybackStateChanged(playbackState);
                }

                @Override
                public void onConnectionSuspended() {
                    Log.i(LOG_TAG, "Suspended connection to music playback service");
                    disableTransportControls();
                }

                @Override
                public void onConnectionFailed() {
                    Log.i(LOG_TAG, "Connection to music playback service failed");
                    Toast.makeText(activity,
                            "Music playback service error!",
                            Toast.LENGTH_SHORT).show();
                }
            };
    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    binding.bottomMusicPlayerSongTitle.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE));
                    binding.bottomMusicPlayerSongArtist.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_ARTIST));

                    String artworkUri = metadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI);
                    Bitmap artworkBitmap = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART);
                    if (artworkBitmap != null)
                        setArtworkFromBitmap(artworkBitmap);
                    else if (artworkUri != null)
                        setArtworkFromArtworkUri(Uri.parse(artworkUri));
                    else
                        setDefaultArtwork();
                    Log.d(LOG_TAG, "Media metadata displays updated");
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                        binding.bottomMusicPlayerPlayPauseBtn.setImageLevel(1);
                        displayUI();
                    }
                    else if (state.getState() == PlaybackStateCompat.STATE_PAUSED)
                        binding.bottomMusicPlayerPlayPauseBtn.setImageLevel(0);

                    Log.d(LOG_TAG, "Playback state changed to " + state.getState());
                }

                @Override
                public void onSessionDestroyed() {
                    super.onSessionDestroyed();
                    mediaBrowser.disconnect();
                    Log.i(LOG_TAG, "Music playback service disconnected");
                }
            };

    MainActivity activity;
    ActivityMainBinding binding;

    public BottomMusicPlayerComponent(MainActivity activity) {
        this.activity = activity;
        binding = activity.getBinding();
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        mediaBrowser = new MediaBrowserCompat(activity,
                new ComponentName(activity, MusicPlaybackService.class),
                connectionCallback,
                null);

        binding.bottomMusicPlayer.setOnClickListener(view -> {
                    Intent intent = new Intent(activity, MusicPlayerActivity.class);
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(activity,
                                    binding.bottomMusicPlayerSongArtwork,
                                    "song_artwork");
                    activity.startActivity(intent, options.toBundle());
                }
        );
    }

    /**
     * Setup transport controls for the bottom player UI.
     */
    private void setupTransportControls() {
        binding.bottomMusicPlayerPlayPauseBtn.setOnClickListener(view -> {
            MediaControllerCompat controller = MediaControllerCompat.getMediaController(activity);
            int playbackState = controller.getPlaybackState().getState();
            if (playbackState == PlaybackStateCompat.STATE_PLAYING)
                controller.getTransportControls().pause();
            else if (playbackState == PlaybackStateCompat.STATE_PAUSED)
                controller.getTransportControls().play();
        });

        binding.bottomMusicPlayerSkipNextBtn.setOnClickListener(
                view -> MediaControllerCompat.getMediaController(activity)
                        .getTransportControls()
                        .skipToNext()
        );

        binding.bottomMusicPlayerSkipPrevBtn.setOnClickListener(
                view -> MediaControllerCompat.getMediaController(activity)
                        .getTransportControls()
                        .skipToPrevious()
        );

        binding.bottomMusicPlayerStopBtn.setOnClickListener(
                view -> {
                    MediaControllerCompat
                            .getMediaController(activity)
                            .getTransportControls()
                            .stop();
                    hideUI();
                }
        );

        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(activity);
        mediaController.registerCallback(controllerCallback);
    }

    private void disableTransportControls() {
        binding.bottomMusicPlayerPlayPauseBtn.setEnabled(false);
        binding.bottomMusicPlayerSkipPrevBtn.setEnabled(false);
        binding.bottomMusicPlayerSkipNextBtn.setEnabled(false);
    }

    /**
     * Display the player's UI with slide up animation.
     */
    private void displayUI()
    {
        if (!isDisplayed) {
            binding.bottomMusicPlayer.animate().translationY(-binding.navView.getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            binding.bottomMusicPlayer.setVisibility(View.VISIBLE);
                        }
                    });
            isDisplayed = true;
        }
    }

    /**
     * Hide the player's UI with slide down animation.
     */
    private void hideUI()
    {
        binding.bottomMusicPlayer.animate()
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.bottomMusicPlayer.setVisibility(View.GONE);
                    }
                });
        isDisplayed = false;
    }

    /**
     * Set song's artwork from artwork's uri.
     * @param artworkUri Artwork's uri
     */
    private void setArtworkFromArtworkUri(Uri artworkUri) {
        Glide.with(activity)
                .asBitmap()
                .load(artworkUri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        setArtworkFromBitmap(resource);
                        Log.d(LOG_TAG, "Song's artwork loaded from uri");
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * Set song's artwork from artwork's bitmap.
     * @param artworkBitmap Artwork's bitmap
     */
    private void setArtworkFromBitmap(Bitmap artworkBitmap) {
        binding.bottomMusicPlayerSongArtwork.setImageBitmap(artworkBitmap);
        Log.d(LOG_TAG, "Song's artwork loaded from bitmap");
    }

    /**
     * Set default artwork.
     */
    private void setDefaultArtwork() {
        Drawable defaultArtwork = AppCompatResources.getDrawable(activity, R.drawable.ic_music_note_white_24dp);
        binding.bottomMusicPlayerSongArtwork.setImageDrawable(defaultArtwork);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        mediaBrowser.connect();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if (MediaControllerCompat.getMediaController(activity) != null)
            MediaControllerCompat.getMediaController(activity).unregisterCallback(controllerCallback);
        mediaBrowser.disconnect();
    }
}
