package com.example.mediaplayerapp.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mediaplayerapp.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.CustomActionProvider;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.util.List;

public class BackgroundMusicService extends MediaBrowserServiceCompat {

    private static final String ACTION_PLAY_PLAYLIST = "PLAY_PLAYLIST";
    private static final String ACTION_PLAY_PLAYLIST_ID = "PLAYLIST_ID";
    private static final String NOTIFICATION_CHANNEL_ID = "com.example.mediaplayerapp.services.MUSIC_NOW_PLAYING";
    private static final int NOTIFICATION_ID = 1;

    ExoPlayer player;
    private MediaSessionCompat mediaSession;
    private PlayerNotificationManager notificationManager;
    Bitmap currentAlbumArtwork;

    @Override
    public void onCreate() {
        super.onCreate();

        player = new ExoPlayer.Builder(this).build();

        mediaSession = new MediaSessionCompat(this, "BackgroundMusicService");
        setSessionToken(mediaSession.getSessionToken());

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
                MediaMetadata metadata = new MediaMetadata.Builder()
                        .build();
                MediaItem item = new MediaItem.Builder()
                        .setUri(uri)
                        .setMediaMetadata(metadata)
                        .build();
                player.setMediaItem(item);
                player.setPlayWhenReady(playWhenReady);
                player.prepare();
            }

            @Override
            public boolean onCommand(@NonNull Player player, @NonNull String command, @Nullable Bundle extras, @Nullable ResultReceiver cb) {
                return false;
            }
        };

        // Supports playing from playlist
        CustomActionProvider customActionProvider =
                new CustomActionProvider() {
                    @Override
                    public void onCustomAction(@NonNull Player player, @NonNull String action, @Nullable Bundle extras) {
                        if (action.equals(ACTION_PLAY_PLAYLIST) && extras != null) {
                                String playlistId = extras.getString(ACTION_PLAY_PLAYLIST_ID);
                                if (playlistId != null) {
                                    // TODO: Load media items from playlist via PlaylistRepository
                                }
                        }
                    }

                    @Override
                    public PlaybackStateCompat.CustomAction getCustomAction(@NonNull Player player) {
                        return new PlaybackStateCompat.CustomAction.Builder(
                                ACTION_PLAY_PLAYLIST,
                                "Play a playlist",
                                R.drawable.ic_playlist_play_white_24dp).build();
                    }
                };

        // Supports navigating media items
        MediaSessionConnector.QueueNavigator queueNavigator
                = new TimelineQueueNavigator(mediaSession) {
            @NonNull
            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                MediaItem currentMediaItem = player.getCurrentMediaItem();
                if (currentMediaItem != null) {
                    return new MediaDescriptionCompat.Builder()
                            .setTitle(currentMediaItem.mediaMetadata.title)
                            .setDescription(currentMediaItem.mediaMetadata.description)
                            .setSubtitle(currentMediaItem.mediaMetadata.subtitle)
                            .build();
                }
                return new MediaDescriptionCompat.Builder().build();
            }
        };

        // Controls playback via notification and plays music in background
        PlayerNotificationManager.NotificationListener notificationListener
                = new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                stopForeground(true);
                stopSelf();
            }

            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                startService(new Intent(BackgroundMusicService.this, BackgroundMusicService.class));
                startForeground(notificationId, notification);
            }
        };

        // Provide media information to display on notification
        PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter
                = new PlayerNotificationManager.MediaDescriptionAdapter() {
            @Override
            public CharSequence getCurrentContentTitle(Player player) {
                MediaItem currentMediaItem = player.getCurrentMediaItem();
                if (currentMediaItem != null)
                    return currentMediaItem.mediaMetadata.title;
                return null;
            }

            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(Player player) {
                return mediaSession.getController().getSessionActivity();
            }

            @Nullable
            @Override
            public CharSequence getCurrentContentText(Player player) {
                MediaItem currentMediaItem = player.getCurrentMediaItem();
                if (currentMediaItem != null)
                    return currentMediaItem.mediaMetadata.description;
                return null;
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                MediaItem currentMediaItem = player.getCurrentMediaItem();
                if (currentMediaItem != null) {
                    Uri albumArtwork = currentMediaItem.mediaMetadata.artworkUri;
                    if (currentAlbumArtwork == null && albumArtwork != null)
                        loadBitmap(albumArtwork, callback);
                    else
                        return currentAlbumArtwork;
                }
                return null;
            }
        };

        notificationManager = new PlayerNotificationManager.Builder(this,
                        NOTIFICATION_ID,
                        NOTIFICATION_CHANNEL_ID)
                        .setNotificationListener(notificationListener)
                        .setMediaDescriptionAdapter(descriptionAdapter)
                        .setChannelNameResourceId(R.string.notification_channel_name)
                        .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .build();
        notificationManager.setPlayer(player);
        notificationManager.setMediaSessionToken(mediaSession.getSessionToken());

        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setCustomActionProviders(customActionProvider);
        mediaSessionConnector.setPlaybackPreparer(playbackPreparer);
        mediaSessionConnector.setQueueNavigator(queueNavigator);
        mediaSessionConnector.setPlayer(player);

        mediaSession.setActive(true);

    }

    /**
     * Load bitmap of album artwork asynchronously
     * @param artworkUri Uri of the artwork
     * @param bitmapCallback Callback to send the result
     */
    private void loadBitmap(Uri artworkUri, PlayerNotificationManager.BitmapCallback bitmapCallback) {
        Glide.with(BackgroundMusicService.this)
                .asBitmap()
                .load(artworkUri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmapCallback.onBitmap(resource);
                        currentAlbumArtwork = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
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