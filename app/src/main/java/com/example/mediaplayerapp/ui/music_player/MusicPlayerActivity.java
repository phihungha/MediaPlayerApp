package com.example.mediaplayerapp.ui.music_player;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;
import com.example.mediaplayerapp.services.BackgroundMusicService;
import com.google.android.exoplayer2.ui.TimeBar;

import java.io.File;

public class MusicPlayerActivity extends AppCompatActivity {

    private static final String PLAYBACK_TIME_FORMAT = "%02d:%02d";

    private MediaBrowserCompat mediaBrowser;
    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    binding.musicTitle.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE));
                    binding.musicArtist.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_ARTIST));

                    long duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
                    binding.musicDuration.setText(getFormattedPlaybackPosition(duration));
                    binding.musicSeekbar.setDuration(duration);

                    String albumArtUri = metadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
                    if (albumArtUri != null) {
                        Glide.with(MusicPlayerActivity.this)
                                .asBitmap()
                                .load(Uri.parse(albumArtUri))
                                .into(binding.albumArt);
                    }
                    Bitmap albumArtBitmap = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART);
                    if (albumArtBitmap != null)
                        binding.albumArt.setImageBitmap(albumArtBitmap);
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    if (state.getState() == PlaybackStateCompat.STATE_PLAYING)
                        binding.musicPlayPauseBtn.setImageLevel(1);
                    else
                        binding.musicPlayPauseBtn.setImageLevel(0);
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                    if (repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE)
                        binding.repeatBtn.setImageLevel(0);
                    else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL)
                        binding.repeatBtn.setImageLevel(1);
                    else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE)
                        binding.repeatBtn.setImageLevel(2);
                }

                @Override
                public void onShuffleModeChanged(int shuffleMode) {
                    if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE)
                        binding.shuffleBtn.setImageLevel(0);
                    else if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL)
                        binding.shuffleBtn.setImageLevel(1);
                }

                @Override
                public void onSessionDestroyed() {
                    super.onSessionDestroyed();
                    mediaBrowser.disconnect();
                }
            };

    ActivityMusicPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MediaBrowserCompat.ConnectionCallback connectionCallback =
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {
                        MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                        MediaControllerCompat mediaController =
                                new MediaControllerCompat(MusicPlayerActivity.this, token);
                        MediaControllerCompat.setMediaController(MusicPlayerActivity.this, mediaController);
                        setupTransportControls();
                        setupTimeIndicators();
                    }

                    @Override
                    public void onConnectionSuspended() {
                        disableTransportControls();
                    }

                    @Override
                    public void onConnectionFailed() {
                        Toast.makeText(MusicPlayerActivity.this,
                                "Music playback service error!",
                                Toast.LENGTH_SHORT).show();
                    }
                };

        mediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, BackgroundMusicService.class),
                connectionCallback,
                null);

        binding.musicCloseBtn.setOnClickListener(view -> finish());
        binding.musicSeekbar.setDuration(100);
        binding.musicSeekbar.setPosition(50);

        binding.repeatBtn.setImageLevel(1);
    }

    private void setupTransportControls() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(this);
        MediaControllerCompat.TransportControls transportControls = controller.getTransportControls();

        String path = Environment.getExternalStorageDirectory().getPath();
        transportControls.playFromUri(Uri.fromFile(new File(path + "/Download/music_sample.flac")), null);

        binding.musicPlayPauseBtn.setOnClickListener(view -> {
            int playbackState = controller.getPlaybackState().getState();
            if (playbackState == PlaybackStateCompat.STATE_PLAYING)
                transportControls.pause();
            else if (playbackState == PlaybackStateCompat.STATE_PAUSED)
                transportControls.play();
        });
        binding.repeatBtn.setOnClickListener(view -> {
            int repeatMode = controller.getRepeatMode();
            if (repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE)
                transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL);
            else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL)
                transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
            else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE)
                transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
        });
        binding.shuffleBtn.setOnClickListener(view -> {
            int shuffleMode = controller.getShuffleMode();
            if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE)
                transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
            else if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL)
                transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
        });
        binding.musicSkipNextBtn.setOnClickListener(view -> transportControls.skipToNext());
        binding.musicSkipPrevBtn.setOnClickListener(view -> transportControls.skipToPrevious());
        binding.musicSeekbar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {

            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                timeBar.setPosition(position);
                binding.musicCurrentPosition.setText(getFormattedPlaybackPosition(position));
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                transportControls.seekTo(position);
            }
        });

        controller.registerCallback(controllerCallback);
    }

    private void setupTimeIndicators() {
        Handler handler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MediaControllerCompat controller = MediaControllerCompat.getMediaController(MusicPlayerActivity.this);
                long currentPlaybackPosition = controller.getPlaybackState().getPosition();
                binding.musicSeekbar.setPosition(currentPlaybackPosition);
                binding.musicCurrentPosition.setText(getFormattedPlaybackPosition(currentPlaybackPosition));
                handler.postDelayed(this, 100);
            }
        });
    }

    /**
     * Get formatted playback position from a milisecond value for display.
     * @param position Playback position as miliseconds
     * @return String Formatted playback position
     */
    @SuppressLint("DefaultLocale")
    private String getFormattedPlaybackPosition(long position) {
        long playedSeconds = position / 1000;
        return String.format(PLAYBACK_TIME_FORMAT, (playedSeconds % 3600) / 60, playedSeconds % 60);
    }

    private void disableTransportControls() {
        binding.musicPlayPauseBtn.setEnabled(false);
        binding.musicSkipNextBtn.setEnabled(false);
        binding.musicSkipPrevBtn.setEnabled(false);
        binding.repeatBtn.setEnabled(false);
        binding.shuffleBtn.setEnabled(false);
        binding.musicSeekbar.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaBrowser.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MediaControllerCompat.getMediaController(this) != null)
            MediaControllerCompat.getMediaController(this).unregisterCallback(controllerCallback);
        mediaBrowser.disconnect();
    }
}