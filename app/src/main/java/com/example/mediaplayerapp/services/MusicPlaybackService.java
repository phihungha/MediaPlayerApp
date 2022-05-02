package com.example.mediaplayerapp.services;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.example.mediaplayerapp.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.lang.reflect.Array;
import java.util.List;

public class MusicPlaybackService extends MediaBrowserServiceCompat {

    private static final String NOTIFICATION_CHANNEL_ID = "com.example.mediaplayerapp.services.MUSIC_PLAYBACK";
    private static final int NOTIFICATION_ID = 1;

    ExoPlayer player;
    private MediaSessionCompat mediaSession;
    private PlayerNotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        player = new ExoPlayer.Builder(this).build();

        mediaSession = new MediaSessionCompat(this, "MusicPlaybackService");
        setSessionToken(mediaSession.getSessionToken());

        setupMetadataSyncListener();
        setupMediaSessionConnector();
        setupNotification();
        mediaSession.setActive(true);
    }

    private void setupMetadataSyncListener() {
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                MediaMetadataCompat.Builder metadataCompatBuilder = new MediaMetadataCompat.Builder();
                putValueIntoMediaMetadataCompat(metadataCompatBuilder,
                        MediaMetadataCompat.METADATA_KEY_TITLE,
                        mediaMetadata.title);
                putValueIntoMediaMetadataCompat(metadataCompatBuilder,
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        mediaMetadata.artist);
                putValueIntoMediaMetadataCompat(metadataCompatBuilder,
                        MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST,
                        mediaMetadata.albumArtist);
                putValueIntoMediaMetadataCompat(metadataCompatBuilder,
                        MediaMetadataCompat.METADATA_KEY_ALBUM,
                        mediaMetadata.albumTitle);
                metadataCompatBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
                        player.getDuration());

                if (mediaMetadata.artworkUri != null)
                    metadataCompatBuilder.putString(MediaMetadataCompat.METADATA_KEY_ART_URI,
                            mediaMetadata.artworkUri.toString());

                if (mediaMetadata.artworkData != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(
                            mediaMetadata.artworkData,
                            0,
                            Array.getLength(mediaMetadata.artworkData));
                    metadataCompatBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, bitmap);
                }

                mediaSession.setMetadata(metadataCompatBuilder.build());
            }
        });
    }

    private void putValueIntoMediaMetadataCompat(
            MediaMetadataCompat.Builder metadataCompat,
            String key,
            CharSequence value) {
        if (value != null)
            metadataCompat.putString(key, value.toString());
    }

    /**
     * Connect media session with player
     */
    private void setupMediaSessionConnector() {
        // Supports playing from uri or search term
        MediaSessionConnector.PlaybackPreparer playbackPreparer =
                new MediaSessionConnector.PlaybackPreparer() {
                    @Override
                    public long getSupportedPrepareActions() {
                        return PlaybackStateCompat.ACTION_PREPARE_FROM_URI |
                                PlaybackStateCompat.ACTION_PLAY_FROM_URI |
                                PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH |
                                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
                    }

                    @Override
                    public void onPrepare(boolean playWhenReady) {
                        player.setPlayWhenReady(playWhenReady);
                    }

                    @Override
                    public void onPrepareFromMediaId(@NonNull String mediaId, boolean playWhenReady, @Nullable Bundle extras) {

                    }

                    @Override
                    public void onPrepareFromSearch(@NonNull String query, boolean playWhenReady, @Nullable Bundle extras) {

                    }

                    @Override
                    public void onPrepareFromUri(@NonNull Uri uri, boolean playWhenReady, @Nullable Bundle extras) {
                        player.setMediaItem(MediaItem.fromUri(uri));
                        player.setPlayWhenReady(playWhenReady);
                        player.prepare();
                    }

                    @Override
                    public boolean onCommand(@NonNull Player player, @NonNull String command, @Nullable Bundle extras, @Nullable ResultReceiver cb) {
                        return false;
                    }
                };

        // Supports navigating media items
        MediaSessionConnector.QueueNavigator queueNavigator
                = new TimelineQueueNavigator(mediaSession) {
            @NonNull
            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                MediaItem mediaItem = player.getMediaItemAt(windowIndex);
                return new MediaDescriptionCompat.Builder()
                        .setTitle(mediaItem.mediaMetadata.title)
                        .setDescription(mediaItem.mediaMetadata.description)
                        .setSubtitle(mediaItem.mediaMetadata.subtitle)
                        .build();
            }
        };

        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setPlaybackPreparer(playbackPreparer);
        mediaSessionConnector.setQueueNavigator(queueNavigator);
        mediaSessionConnector.setPlayer(player);
    }

    /**
     * Setup playback notification and foreground service
     */
    private void setupNotification() {
        PlayerNotificationManager.NotificationListener notificationListener
                = new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                stopForeground(true);
                stopSelf();
            }

            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                startService(new Intent(MusicPlaybackService.this, MusicPlaybackService.class));
                startForeground(notificationId, notification);
            }
        };

        notificationManager = new PlayerNotificationManager.Builder(this,
                NOTIFICATION_ID,
                NOTIFICATION_CHANNEL_ID)
                .setNotificationListener(notificationListener)
                .setChannelNameResourceId(R.string.notification_channel_name)
                .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .build();
        notificationManager.setPlayer(player);
        notificationManager.setMediaSessionToken(mediaSession.getSessionToken());
    }

    @Override
    public void onDestroy() {
        mediaSession.release();
        notificationManager.setPlayer(null);
        player.release();
    }

    /**
     * Does not support browsing media library via this service
     */
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid,
                                 Bundle rootHints) {
        return new BrowserRoot("empty_root", null);
    }

    /**
     * Does not support browsing media library via this service
     */
    @Override
    public void onLoadChildren(@NonNull final String parentMediaId,
                               @NonNull final Result<List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result) {
    }
}