package com.example.mediaplayerapp.ui.music_player;

import android.content.ComponentName;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;
import com.example.mediaplayerapp.services.BackgroundMusicService;

import java.io.File;

public class MusicPlayerActivity extends AppCompatActivity {

    private MediaBrowserCompat mediaBrowser;
    private final MediaBrowserCompat.ConnectionCallback connectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                    MediaControllerCompat mediaController =
                            new MediaControllerCompat(MusicPlayerActivity.this, token);
                    MediaControllerCompat.setMediaController(MusicPlayerActivity.this, mediaController);
                    setupTransportControls();
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

    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    binding.musicTitle.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE));
                    binding.musicArtist.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_ARTIST));
                    binding.musicDuration.setText(Long.toString(metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)));
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    if (state.getState() == PlaybackStateCompat.STATE_PLAYING)
                        binding.musicPlayPauseBtn.setImageLevel(1);
                    else
                        binding.musicPlayPauseBtn.setImageLevel(0);
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

        controller.registerCallback(controllerCallback);
    }

    private void disableTransportControls() {
        binding.musicPlayPauseBtn.setEnabled(false);
        binding.musicSkipNextBtn.setEnabled(false);
        binding.musicSkipPrevBtn.setEnabled(false);
        binding.repeatBtn.setEnabled(false);
        binding.shuffleBtn.setEnabled(false);
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