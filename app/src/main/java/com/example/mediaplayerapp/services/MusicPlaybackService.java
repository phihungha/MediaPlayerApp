package com.example.mediaplayerapp.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.mediaplayerapp.data.overview.MediaPlaybackInfo;
import com.example.mediaplayerapp.data.overview.MediaPlaybackInfoRepository;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemRepository;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
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
import com.google.android.exoplayer2.ui.DefaultMediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MusicPlaybackService extends MediaBrowserServiceCompat {

    private static final String LOG_TAG = MusicPlaybackService.class.getSimpleName();
    public static final String AUDIO_SESSION_ID_KEY
            = "com.example.mediaplayerapp.services.MusicPlaybackService.AudioSessionId";
    private static final String NOTIFICATION_CHANNEL_ID
            = "com.example.mediaplayerapp.services.MusicPlaybackService.MusicPlayback";
    private static final int NOTIFICATION_ID = 1;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private ExoPlayer player;
    private MediaSessionCompat mediaSession;
    private PlayerNotificationManager notificationManager;
    private boolean isForeground = false;

    private SongsRepository songsRepository;
    private PlaylistItemRepository playlistItemRepository;
    private MediaPlaybackInfoRepository playbackInfoRepository;

    private long lastPlaybackPosition = -1;
    private Uri currentMediaUri = null;

    @Override
    public void onCreate() {
        super.onCreate();

        player = new ExoPlayer.Builder(this).build();

        mediaSession = new MediaSessionCompat(this, "MusicPlaybackService");
        mediaSession.setSessionActivity(mediaSession.getController().getSessionActivity());
        setSessionToken(mediaSession.getSessionToken());

        songsRepository = new SongsRepository(getApplicationContext());
        playlistItemRepository = new PlaylistItemRepository(getApplication());
        playbackInfoRepository = new MediaPlaybackInfoRepository(getApplication());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (player.getCurrentPosition() != 0)
                    lastPlaybackPosition = player.getCurrentPosition();
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        setAudioSessionIdOnMediaSession();
        setupAnalyticsListener();
        setupPlayerEventListener();
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

    private void setupPlayerEventListener() {
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

                if (mediaMetadata.mediaUri != null) {
                    currentMediaUri = mediaMetadata.mediaUri;
                    metadataCompatBuilder.putString(
                            MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                            mediaMetadata.mediaUri.toString()
                    );
                }

                if (mediaMetadata.artworkUri != null)
                    metadataCompatBuilder.putString(
                            MediaMetadataCompat.METADATA_KEY_ART_URI,
                            mediaMetadata.artworkUri.toString()
                    );

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

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED && currentMediaUri != null)
                    playbackInfoRepository.insertOrUpdate(new MediaPlaybackInfo(
                            currentMediaUri.toString(),
                            Calendar.getInstance().getTimeInMillis(),
                            1,
                            false,
                            lastPlaybackPosition
                    ));
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                if (currentMediaUri != null) {
                    playbackInfoRepository.insertOrUpdate(new MediaPlaybackInfo(
                            currentMediaUri.toString(),
                            Calendar.getInstance().getTimeInMillis(),
                            1,
                            false,
                            lastPlaybackPosition
                    ));
                }
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
                    public void onPrepareFromMediaId(@NonNull String mediaId,
                                                     boolean playWhenReady,
                                                     @Nullable Bundle extras) {

                    }

                    @Override
                    public void onPrepareFromSearch(@NonNull String query,
                                                    boolean playWhenReady,
                                                    @Nullable Bundle extras) {
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
                    public boolean onCommand(@NonNull Player player,
                                             @NonNull String command,
                                             @Nullable Bundle extras,
                                             @Nullable ResultReceiver cb) {
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

        PendingIntent openMusicPlayerScreenIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MusicPlayerActivity.class),
                PendingIntent.FLAG_IMMUTABLE);

        PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter
                = new DefaultMediaDescriptionAdapter(openMusicPlayerScreenIntent);

        notificationManager = new PlayerNotificationManager.Builder(this,
                NOTIFICATION_ID,
                NOTIFICATION_CHANNEL_ID)
                .setNotificationListener(notificationListener)
                .setMediaDescriptionAdapter(descriptionAdapter)
                .setChannelNameResourceId(R.string.notification_channel_name)
                .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .build();
        notificationManager.setUseStopAction(true);
        notificationManager.setPlayer(player);
        notificationManager.setMediaSessionToken(mediaSession.getSessionToken());
    }

    /**
     * Load media items specified by the playback URI from system's music library.
     * @param uri Playback URI
     */
    private void loadMediaItemsFromLibrary(Uri uri) {
        List<String> uriSegments = uri.getPathSegments();
        String libraryType = uriSegments.get(1);

        switch (libraryType) {
            case GetPlaybackUriUtils.SONG_URI_SEGMENT:
                loadMediaItemsFromEntireLibrary(uriSegments);
                break;
            case GetPlaybackUriUtils.ARTIST_URI_SEGMENT:
                loadMediaItemsFromArtist(uriSegments);
                break;
            case GetPlaybackUriUtils.ALBUM_URI_SEGMENT:
                loadMediaItemsFromAlbum(uriSegments);
                break;
        }
    }

    /**
     * Load media items from the entire music library according to the playback URI.
     * @param uriSegments Playback URI segments
     */
    private void loadMediaItemsFromEntireLibrary(List<String> uriSegments) {
        String sortByUriSegment = uriSegments.get(2);
        String sortOrderUriSegment = uriSegments.get(3);
        int playbackStartIndex = Integer.parseInt(uriSegments.get(4));

        SongsRepository.SortBy sortBy;
        if (sortByUriSegment.equals(SongsRepository.SortBy.DURATION.getUriSegmentName()))
            sortBy = SongsRepository.SortBy.DURATION;
        else if (sortByUriSegment.equals(SongsRepository.SortBy.TIME_ADDED.getUriSegmentName()))
            sortBy = SongsRepository.SortBy.TIME_ADDED;
        else
            sortBy = SongsRepository.SortBy.TITLE;

        SortOrder sortOrder;
        if (sortOrderUriSegment.equals(SortOrder.DESC.getUriSegmentName()))
            sortOrder = SortOrder.DESC;
        else
            sortOrder = SortOrder.ASC;

        asyncLoadMediaItems(
                songsRepository.getAllSongs(sortBy, sortOrder),
                playbackStartIndex
        );
    }

    /**
     * Load media items from songs of an artist specified by the playback URI.
     * @param uriSegments Playback URI segments
     */
    private void loadMediaItemsFromArtist(List<String> uriSegments) {
        long artistId = Long.parseLong(uriSegments.get(2));
        int playbackStartIndex = Integer.parseInt(uriSegments.get(3));
        asyncLoadMediaItems(
                songsRepository.getAllSongsFromArtist(artistId),
                playbackStartIndex
        );
    }

    /**
     * Load media items from songs in an album specified by the playback URI.
     * @param uriSegments Playback URI segments
     */
    private void loadMediaItemsFromAlbum(List<String> uriSegments) {
        long albumId = Long.parseLong(uriSegments.get(2));
        int playbackStartIndex = Integer.parseInt(uriSegments.get(3));
        asyncLoadMediaItems(
                songsRepository.getAllSongsFromAlbum(albumId),
                playbackStartIndex
        );
    }

    /**
     * Asynchronously load media items from music library.
     * @param songs RxJava Observable that emits Song objects
     * @param playbackStartIndex Index of first media item to play
     */
    private void asyncLoadMediaItems(Observable<List<Song>> songs, int playbackStartIndex) {
        Disposable disposable = songs.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newSongs -> {
                player.addMediaItems(GetMediaItemsUtils.fromLibrarySongs(newSongs));
                player.seekTo(playbackStartIndex, C.TIME_UNSET);
            });
        disposables.add(disposable);
    }

    /**
     * Load music media items from playlist specified by playback URI.
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
        disposables.dispose();
    }

    /**
     * Does not support browsing media library via this service
     */
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid,
                                 Bundle rootHints) {
        Log.i(LOG_TAG, "Get root requested");
        return new BrowserRoot("empty_root", null);
    }

    /**
     * Does not support browsing media library via this service
     */
    @Override
    public void onLoadChildren(@NonNull final String parentMediaId,
                               @NonNull final Result<List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result) {
        Log.d(LOG_TAG, "Load children requested");
        result.sendResult(null);
    }
}