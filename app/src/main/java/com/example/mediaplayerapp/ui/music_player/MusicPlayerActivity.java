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
                    buildTransportControls();
                }

                @Override
                public void onConnectionSuspended() {
                    // The Service has crashed. Disable transport controls until it automatically reconnects
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
                public void onMetadataChanged(MediaMetadataCompat metadata) {}

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {}

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
        binding.musicPlayPauseBtn.setOnClickListener(view -> binding.musicPlayPauseBtn.setImageLevel(1));

    }

    private void buildTransportControls() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(this);
        int playbackState = controller.getPlaybackState().getState();
        MediaControllerCompat.TransportControls transportControls = controller.getTransportControls();

        String path = Environment.getExternalStorageDirectory().getPath();
        transportControls.playFromUri(Uri.fromFile(new File(path + "/Download/music_sample.flac")), null);

        binding.musicPlayPauseBtn.setOnClickListener(view -> {
            if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
                transportControls.pause();
                binding.musicPlayPauseBtn.setImageLevel(0);
            } else if (playbackState == PlaybackStateCompat.STATE_PAUSED) {
                transportControls.play();
                binding.musicPlayPauseBtn.setImageLevel(1);
            }
        });

        controller.registerCallback(controllerCallback);
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