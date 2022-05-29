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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongsRepository;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemRepository;
import com.example.mediaplayerapp.utils.GetMediaItemsUtils;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.SortOrder;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MusicPlaybackService extends MediaBrowserServiceCompat {

    private static final String LOG_TAG = MusicPlaybackService.class.getSimpleName();
    public static final String AUDIO_SESSION_ID_KEY = "com.example.mediaplayerapp.services.MusicPlaybackService.AudioSessionId";
    private static final String NOTIFICATION_CHANNEL_ID = "com.example.mediaplayerapp.services.MusicPlaybackService.MUSIC_PLAYBACK";
    private static final int NOTIFICATION_ID = 1;

    private ExoPlayer player;
    private MediaSessionCompat mediaSession;
    private PlayerNotificationManager notificationManager;
    private SongsRepository songsRepository;
    private PlaylistItemRepository playlistItemRepository;
    private boolean isForeground = false;

    @Override
    public void onCreate() {
        super.onCreate();

        player = new ExoPlayer.Builder(this).build();

        mediaSession = new MediaSessionCompat(this, "MusicPlaybackService");
        setSessionToken(mediaSession.getSessionToken());

        songsRepository = new SongsRepository(getApplicationContext());
        playlistItemRepository = new PlaylistItemRepository(getApplication());

        setAudioSessionIdOnMediaSession();
        setupAnalyticsListener();
        setupMetadataSyncListener();
        setupMediaSessionConnector();
        setupNotification();
        mediaSession.setActive(true);
    }

    private void setAudioSessionIdOnMediaSession() {
        Bundle extras = new Bundle();
        extras.putInt(AUDIO_SESSION_ID_KEY, player.getAudioSessionId());
        mediaSession.setExtras(extras);
    }

    private void setupAnalyticsListener() {
        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onAudioSessionIdChanged(@NonNull EventTime eventTime, int audioSessionId) {
                setAudioSessionIdOnMediaSession();
            }
        });
    }

    private void setupMetadataSyncListener() {
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                MediaMetadataCompat.Builder metadataCompatBuilder = new MediaMetadataCompat.Builder();
                putStringIntoMediaMetadataCompat(metadataCompatBuilder,
                        MediaMetadataCompat.METADATA_KEY_TITLE,
                        mediaMetadata.title);
                putStringIntoMediaMetadataCompat(metadataCompatBuilder,
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        mediaMetadata.artist);
                putStringIntoMediaMetadataCompat(metadataCompatBuilder,
                        MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST,
                        mediaMetadata.albumArtist);
                putStringIntoMediaMetadataCompat(metadataCompatBuilder,
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
                Log.d(LOG_TAG, "New media metadata found");
            }
        });
    }

    private void putStringIntoMediaMetadataCompat(
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
                        player.clearMediaItems();

                        if (uri.getScheme().equals(GetPlaybackUriUtils.PLAYBACK_URI_SCHEME)) {
                            if (uri.getPathSegments()
                                    .get(0).equals(GetPlaybackUriUtils.PLAYLIST_URI_SEGMENT))
                                loadMediaItemsFromPlaylist(uri);
                            else
                                loadMediaItemsFromLibrary(uri);
                            Log.i(LOG_TAG, "Loaded media from playback URI: " + uri);
                        } else {
                            player.setMediaItem(MediaItem.fromUri(uri));
                            Log.i(LOG_TAG, "Loaded media from URI: " + uri);
                        }

                        player.setPlayWhenReady(playWhenReady);
                        player.prepare();
                        Log.i(LOG_TAG, "ExoPlayer prepared");
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
            public MediaDescriptionCompat getMediaDescription(@NonNull Player player, int windowIndex) {
                return new MediaDescriptionCompat.Builder().build();
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
                isForeground = false;
                stopSelf();
                Log.i(LOG_TAG, "Stopped foreground service");
            }

            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                if (ongoing && !isForeground) {
                    startService(new Intent(MusicPlaybackService.this, MusicPlaybackService.class));
                    startForeground(notificationId, notification);
                    isForeground = true;
                    Log.i(LOG_TAG, "Started foreground service");
                }
            }
        };

        notificationManager = new PlayerNotificationManager.Builder(this,
                NOTIFICATION_ID,
                NOTIFICATION_CHANNEL_ID)
                .setNotificationListener(notificationListener)
                .setChannelNameResourceId(R.string.notification_channel_name)
                .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .build();
        notificationManager.setUseStopAction(true);
        notificationManager.setPlayer(player);
        notificationManager.setMediaSessionToken(mediaSession.getSessionToken());
    }

    /**
     * Load media items specified by this app's playback URI from library.
     * @param uri Playback URI
     */
    private void loadMediaItemsFromLibrary(Uri uri) {
        List<Song> songsToPlay = new ArrayList<>();
        int playbackStartIndex = 0;
        List<String> uriSegments = uri.getPathSegments();
        String type = uriSegments.get(0);

        if (type.equals(GetPlaybackUriUtils.LIBRARY_URI_SEGMENT)) {
            songsToPlay = songsRepository.getAllSongs(SongsRepository.SortBy.TITLE, SortOrder.ASC);
            playbackStartIndex = Integer.parseInt(uriSegments.get(1));
        } else if (type.equals(GetPlaybackUriUtils.ARTIST_URI_SEGMENT)) {
            long artistId = Long.parseLong(uriSegments.get(1));
            songsToPlay = songsRepository.getAllSongsFromArtist(artistId);
            playbackStartIndex = Integer.parseInt(uriSegments.get(2));
        } else if (type.equals(GetPlaybackUriUtils.ALBUM_URI_SEGMENT)) {
            long albumId = Long.parseLong(uriSegments.get(1));
            songsToPlay = songsRepository.getAllSongsFromAlbum(albumId);
            playbackStartIndex = Integer.parseInt(uriSegments.get(2));
        }

        player.addMediaItems(GetMediaItemsUtils.fromLibrarySongs(songsToPlay));
        player.seekTo(playbackStartIndex, C.TIME_UNSET);
    }

    /**
     * Load media items specified by this app's playback URI from playlist.
     * @param uri Playback URI
     */
    private void loadMediaItemsFromPlaylist(Uri uri) {
        int playlistId = Integer.parseInt(uri.getPathSegments().get(1));
        int playbackStartIndex = Integer.parseInt(uri.getPathSegments().get(2));
        playlistItemRepository.getAllPlaylistMediasWithID(playlistId)
                        .observeForever(playlistItems -> {
                            player.addMediaItems(GetMediaItemsUtils.fromPlaylistItems(playlistItems));
                            player.seekTo(playbackStartIndex, C.TIME_UNSET);
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
        Log.d(LOG_TAG, "Get root requested");
        return new BrowserRoot("empty_root", null);
    }

    /**
     * Does not support browsing media library via this service
     */
    @Override
    public void onLoadChildren(@NonNull final String parentMediaId,
                               @NonNull final Result<List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result) {
        Log.d(LOG_TAG, "Load children requested");
    }
}