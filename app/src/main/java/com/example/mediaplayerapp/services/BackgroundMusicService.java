package com.example.mediaplayerapp.services;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.media.MediaBrowserServiceCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

import java.util.List;

public class BackgroundMusicService extends MediaBrowserServiceCompat {

    private MediaSessionCompat mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();

        ExoPlayer player = new ExoPlayer.Builder(this).build();

        mediaSession = new MediaSessionCompat(this, "BackgroundMusicService");
        setSessionToken(mediaSession.getSessionToken());
        PlaybackStateCompat.Builder playbackState = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(playbackState.build());
        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setPlayer(player);

        setSessionToken(mediaSession.getSessionToken());
        mediaSession.setActive(true);
    }

    @Override
    public void onDestroy() {
        mediaSession.release();
    }

    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid,
                                 Bundle rootHints) {
        return new BrowserRoot("empty_root", null);
    }

    @Override
    public void onLoadChildren(@NonNull final String parentMediaId,
                               @NonNull final Result<List<MediaItem>> result) {
    }
}