package com.example.mediaplayerapp.ui.video_player;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.preference.PreferenceManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playback_history.PlaybackHistoryRepository;
import com.example.mediaplayerapp.data.playlist.PlaylistItemRepository;
import com.example.mediaplayerapp.data.video_library.Video;
import com.example.mediaplayerapp.data.video_library.VideosRepository;
import com.example.mediaplayerapp.databinding.ActivityVideoPlayerBinding;
import com.example.mediaplayerapp.utils.GetMediaItemsUtils;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.MessageUtils;
import com.example.mediaplayerapp.utils.SortOrder;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VideoPlayerActivity extends AppCompatActivity {

    private final static String LOG_TAG = VideoPlayerActivity.class.getSimpleName();
    private static final String SHUFFLE_MODE_ALL_KEY =
            "com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity.SHUFFLE_MODE_KEY";
    private static final String SEEK_TO_POSITION_KEY =
            "com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity.SEEK_TO_POSITION_KEY";

    private final CompositeDisposable disposables = new CompositeDisposable();

    private ExoPlayer player;
    private MediaSessionCompat mediaSession;

    // These information are not available
    // on media item transition so we need to
    // cache them.
    private long lastPlaybackPosition = 0;
    private Uri lastMediaUri = Uri.EMPTY;

    private VideosRepository videoLibraryRepository;
    private PlaylistItemRepository playlistItemRepository;
    private PlaybackHistoryRepository playbackHistoryRepository;

    ActivityVideoPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoLibraryRepository = new VideosRepository(getApplication());
        playlistItemRepository = new PlaylistItemRepository(getApplication());
        playbackHistoryRepository = new PlaybackHistoryRepository(getApplication());

        enterFullScreenMode();

        player = new ExoPlayer.Builder(this).build();
        binding.videoPlayer.setPlayer(player);
        setupPlayerEventListener();

        mediaSession = new MediaSessionCompat(this, LOG_TAG);
        mediaSession.setMediaButtonReceiver(null);
        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
            @NonNull
            @Override
            public MediaDescriptionCompat getMediaDescription(@NonNull Player player, int windowIndex) {
                return new MediaDescriptionCompat.Builder().build();
            }
        });
        mediaSessionConnector.setPlayer(player);

        loadMediaItemsFromIntent();
        play();

        beginLastPlaybackPositionRecording();
    }

    private void beginLastPlaybackPositionRecording() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (player.getPlaybackState() == Player.STATE_READY)
                    lastPlaybackPosition = player.getCurrentPosition();
                handler.postDelayed(this, 1000);
            }
        });
    }

    /**
     * Sequentially play video(s) specified by an URI with VideoPlayerActivity.
     * @param context Current context
     * @param uri URI of the media item to play
     */
    public static void launchWithUri(Context context, Uri uri) {
        Intent playbackIntent = new Intent(context, VideoPlayerActivity.class);
        playbackIntent.setData(uri);
        context.startActivity(playbackIntent);
    }

    /**
     * Sequentially play video(s) specified by an URI with VideoPlayerActivity.
     * @param context Current context
     * @param uri URI of the media item to play
     * @param seekToPosition Position to seek to
     */
    public static void launchWithUriAndSeekTo(Context context, Uri uri, long seekToPosition) {
        Intent playbackIntent = new Intent(context, VideoPlayerActivity.class);
        playbackIntent.setData(uri);
        playbackIntent.putExtra(SEEK_TO_POSITION_KEY, seekToPosition);
        context.startActivity(playbackIntent);
    }

    /**
     * Randomly play video(s) specified by an URI with VideoPlayerActivity.
     * @param context Current context
     * @param uri URI of the media item to play
     */
    public static void launchWithUriAndShuffleAll(Context context, Uri uri) {
        Intent playbackIntent = new Intent(context, VideoPlayerActivity.class);
        playbackIntent.setData(uri);
        playbackIntent.putExtra(SHUFFLE_MODE_ALL_KEY, true);
        context.startActivity(playbackIntent);
    }

    private void setupPlayerEventListener() {
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                if (mediaMetadata.mediaUri != null) {
                    lastMediaUri = mediaMetadata.mediaUri;
                    resumeLastPosition(mediaMetadata.mediaUri);
                }
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                recordHistory();
            }
        });
    }

    /**
     * Resume playback at last saved position if there is.
     * @param mediaUri Media URI
     */
    private void resumeLastPosition(Uri mediaUri) {
        if (!PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("resume_last_position", false))
            return;

        Disposable disposable = playbackHistoryRepository.getLastPlaybackPosition(mediaUri)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lastPlaybackPosition -> {
                    if (lastPlaybackPosition != -1)
                        askToResumeLastPosition(lastPlaybackPosition);
                });
        disposables.add(disposable);
    }

    private void askToResumeLastPosition(long lastPlaybackPosition) {
        Snackbar.make(binding.getRoot(), R.string.resume_last_playback_position, 5000)
                .setAction(R.string.yes,
                        view -> player.seekTo(lastPlaybackPosition))
                .show();
    }

    /**
     * Record current media item and its last playback position into history.
     */
    private void recordHistory() {
        if (lastPlaybackPosition == 0)
            return;

        long playbackPosition = lastPlaybackPosition;
        if (playbackPosition == player.getDuration())
            playbackPosition = 0;

        Disposable disposable =
                playbackHistoryRepository.recordVideoHistory(lastMediaUri, playbackPosition)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {},
                                e -> MessageUtils.displayError(
                                        getApplicationContext(),
                                        LOG_TAG,
                                        e.getMessage()));
        disposables.add(disposable);
    }

    /**
     * Load media items from intent into player.
     */
    private void loadMediaItemsFromIntent() {
        Uri uri = getIntent().getData();
        if (uri == null)
            return;

        player.clearMediaItems();

        if (uri.getScheme().equals(GetPlaybackUriUtils.PLAYBACK_URI_SCHEME))
            loadMediaItemsFromPlaybackUri(uri);
        else
            player.setMediaItem(GetMediaItemsUtils.getMediaItemFromUri(uri));

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            if (extras.getBoolean(SHUFFLE_MODE_ALL_KEY))
                player.setShuffleModeEnabled(true);
    }

    /**
     * Load video media items specified by a playback URI.
     * @param uri Playback URI
     */
    private void loadMediaItemsFromPlaybackUri(Uri uri) {
        List<String> uriSegments = uri.getPathSegments();
        String type = uriSegments.get(0);

        if (type.equals(GetPlaybackUriUtils.VIDEO_LIBRARY_URI_SEGMENT))
            loadMediaItemsFromLibrary(uriSegments);
        else if (type.equals(GetPlaybackUriUtils.PLAYLIST_URI_SEGMENT))
            loadMediaItemsFromVideoPlaylist(uriSegments);
    }

    /**
     * Load media items from the entire video library
     * with provided sort order in the playback URI.
     * @param uriSegments Playback URI segments
     */
    private void loadMediaItemsFromLibrary(List<String> uriSegments) {
        String sortByUriSegment = uriSegments.get(1);
        String sortOrderUriSegment = uriSegments.get(2);
        int playbackStartIndex = Integer.parseInt(uriSegments.get(3));

        VideosRepository.SortBy sortBy;
        if (sortByUriSegment.equals(VideosRepository.SortBy.DURATION.getUriSegmentName()))
            sortBy = VideosRepository.SortBy.DURATION;
        else if (sortByUriSegment.equals(VideosRepository.SortBy.TIME_ADDED.getUriSegmentName()))
            sortBy = VideosRepository.SortBy.TIME_ADDED;
        else
            sortBy = VideosRepository.SortBy.NAME;

        SortOrder sortOrder;
        if (sortOrderUriSegment.equals(SortOrder.DESC.getUriSegmentName()))
            sortOrder = SortOrder.DESC;
        else
            sortOrder = SortOrder.ASC;

        asyncLoadMediaItems(
                videoLibraryRepository.getAllVideos(sortBy, sortOrder),
                playbackStartIndex
        );
    }

    /**
     * Asynchronously load media items from video library.
     * @param videos RxJava Single observable that emits a list of videos.
     * @param playbackStartIndex Index of first media item to play
     */
    private void asyncLoadMediaItems(Single<List<Video>> videos, int playbackStartIndex) {
        Disposable disposable = videos.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newVideos -> {
                    player.clearMediaItems();
                    player.addMediaItems(GetMediaItemsUtils.fromLibraryVideos(newVideos));
                    player.seekTo(playbackStartIndex, C.TIME_UNSET);
                });
        disposables.add(disposable);
    }

    /**
     * Load video media items from playlist specified by playback URI.
     * @param uriSegments Playback URI segments
     */
    private void loadMediaItemsFromVideoPlaylist(List<String> uriSegments) {
        int playlistId = Integer.parseInt(uriSegments.get(1));
        int playbackStartIndex = Integer.parseInt(uriSegments.get(2));
        Disposable disposable = playlistItemRepository.getAllItemsOfPlaylist(playlistId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playlistItem -> {
                    player.clearMediaItems();
                    player.addMediaItems(GetMediaItemsUtils.fromPlaylistItems(playlistItem));
                    player.seekTo(playbackStartIndex, C.TIME_UNSET);
                });
        disposables.add(disposable);
    }

    /**
     * Begin playback of loaded media items.
     */
    private void play() {
        player.prepare();
        player.play();
    }

    /**
     * Hide system bars to enter full screen mode.
     */
    private void enterFullScreenMode() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null)
            return;

        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        player.stop();
        loadMediaItemsFromIntent();
        play();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (player.isPlaying())
            enterPictureInPictureMode(new PictureInPictureParams.Builder().build());
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        binding.videoPlayer.setUseController(!isInPictureInPictureMode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaSession.setActive(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaSession.setActive(false);
        player.pause();
        recordHistory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaSession.release();
        player.release();
        disposables.dispose();
    }
}