package com.example.mediaplayerapp.ui.music_player;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.core.content.ContextCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mediaplayerapp.MainActivity;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ActivityMainBinding;
import com.example.mediaplayerapp.databinding.BottomMusicPlayerBinding;
import com.example.mediaplayerapp.services.MusicPlaybackService;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;

/**
 * Logic for bottom music player UI.
 */
public class BottomMusicPlayerComponent implements DefaultLifecycleObserver {

    private static final String LOG_TAG = BottomMusicPlayerComponent.class.getSimpleName();
    private static final int AUTOSCROLL_DELAY = 4500;
    private boolean isDisplayed = false;
    private boolean firstDisplayTime = true;
    private boolean isUsingDefaultArtwork = true;

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
                    if (MediaControllerCompat
                            .getMediaController(activity)
                            .getPlaybackState()
                            .getState() == PlaybackStateCompat.STATE_NONE)
                        return;

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
                        showUI();
                    }
                    else if (state.getState() == PlaybackStateCompat.STATE_PAUSED)
                        binding.bottomMusicPlayerPlayPauseBtn.setImageLevel(0);
                    else if (state.getState() == PlaybackStateCompat.STATE_NONE)
                        hideUI();

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
    ActivityMainBinding activityMainBinding;
    BottomMusicPlayerBinding binding;

    public BottomMusicPlayerComponent(MainActivity activity, ActivityMainBinding binding) {
        this.activity = activity;
        this.activityMainBinding = binding;
        this.binding = binding.bottomMusicPlayer;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        mediaBrowser = new MediaBrowserCompat(activity,
                new ComponentName(activity, MusicPlaybackService.class),
                connectionCallback,
                null);

        binding.getRoot().setVisibility(View.INVISIBLE);
        binding.getRoot().setOnClickListener(view -> {
                    Intent intent = new Intent(activity, MusicPlayerActivity.class);
                    Bundle extras = null;
                    if (!isUsingDefaultArtwork) {
                        extras = ActivityOptions
                                .makeSceneTransitionAnimation(activity,
                                        binding.bottomMusicPlayerSongArtwork,
                                        "song_artwork").toBundle();
                    }
                    activity.startActivity(intent, extras);
                }
        );

        // Delay text auto-scroll
        Handler handler = new Handler(activity.getMainLooper());
        handler.postDelayed(() -> {
            binding.bottomMusicPlayerSongTitle.setSelected(true);
            binding.bottomMusicPlayerSongArtist.setSelected(true);
        }, AUTOSCROLL_DELAY);
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
                view -> MediaControllerCompat.getMediaController(activity)
                        .getTransportControls()
                        .stop()
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
     * Show the player's UI with slide up animation.
     */
    private void showUI()
    {
        if (firstDisplayTime) {
            binding.getRoot().setTranslationY(binding.getRoot().getHeight());
            firstDisplayTime = false;
        }

        if (!isDisplayed) {
            binding.getRoot()
                    .animate()
                    .translationY(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            binding.getRoot().setVisibility(View.VISIBLE);
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
        if (isDisplayed) {
            binding.getRoot()
                    .animate()
                    .translationY(binding.getRoot().getHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            binding.getRoot().setVisibility(View.INVISIBLE);
                        }
                    });
            isDisplayed = false;
        }
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
                        setViewsColors(resource);
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

        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(
                new BlurTransformation(6, 5),
                new BrightnessFilterTransformation(-0.15f));
        Glide.with(activity)
                .load(artworkBitmap)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(binding.bottomMusicPlayerSongArtworkBackground);
        Log.d(LOG_TAG, "Song's artwork loaded from bitmap");

        setViewsColors(artworkBitmap);

        isUsingDefaultArtwork = false;
    }

    /**
     * Set default artwork.
     */
    private void setDefaultArtwork() {
        Drawable defaultArtwork = AppCompatResources.getDrawable(activity,
                R.drawable.default_song_artwork_bottom_music_player);
        binding.bottomMusicPlayerSongArtwork.setImageDrawable(defaultArtwork);
        binding.bottomMusicPlayerSongArtworkBackground.setImageDrawable(null);
        setDefaultViewsColors();
        isUsingDefaultArtwork = true;
    }

    /**
     * Set colors of views on the UI based on song's artwork.
     * @param artworkBitmap Artwork's bitmap
     */
    private void setViewsColors(Bitmap artworkBitmap) {
        Palette.from(artworkBitmap).generate(palette -> {
            if (palette == null)
                return;
            int color = palette.getLightVibrantColor(
                    ContextCompat.getColor(activity, R.color.white));
            binding.bottomMusicPlayerSongTitle.setTextColor(color);
            binding.bottomMusicPlayerSongArtist.setTextColor(color);
            binding.bottomMusicPlayerStopBtn.setColorFilter(color);
            binding.bottomMusicPlayerPlayPauseBtn.setColorFilter(color);
            binding.bottomMusicPlayerSkipNextBtn.setColorFilter(color);
            binding.bottomMusicPlayerSkipPrevBtn.setColorFilter(color);
            Log.d(LOG_TAG, "Updated views' colors using song artwork's palette");
        });
    }

    /**
     * Set views' colors back to default values.
     */
    private void setDefaultViewsColors() {
        int color = ContextCompat.getColor(activity, R.color.black);
        binding.bottomMusicPlayerSongTitle.setTextColor(color);
        binding.bottomMusicPlayerSongArtist.setTextColor(color);
        binding.bottomMusicPlayerStopBtn.setColorFilter(color);
        binding.bottomMusicPlayerPlayPauseBtn.setColorFilter(color);
        binding.bottomMusicPlayerSkipNextBtn.setColorFilter(color);
        binding.bottomMusicPlayerSkipPrevBtn.setColorFilter(color);
        Log.d(LOG_TAG, "Updated views' colors to default");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        mediaBrowser.connect();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if (MediaControllerCompat.getMediaController(activity) != null)
            MediaControllerCompat.getMediaController(activity).unregisterCallback(controllerCallback);
        mediaBrowser.disconnect();
    }
}
