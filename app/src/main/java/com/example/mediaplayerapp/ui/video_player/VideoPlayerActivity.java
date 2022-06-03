package com.example.mediaplayerapp.ui.video_player;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemRepository;
import com.example.mediaplayerapp.data.video_library.VideoLibraryRepository;
import com.example.mediaplayerapp.databinding.ActivityVideoPlayerBinding;
import com.example.mediaplayerapp.utils.GetMediaItemsUtils;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;

import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    private final static String LOG_TAG = VideoPlayerActivity.class.getSimpleName();
    private static final String SHUFFLE_MODE_ALL_KEY =
            "com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity.SHUFFLE_MODE_KEY";

    ActivityVideoPlayerBinding binding;

    private ExoPlayer player;
    private MediaSessionCompat mediaSession;
    private VideoLibraryRepository videoLibraryRepository;
    private PlaylistItemRepository playlistItemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoLibraryRepository = new VideoLibraryRepository(getApplication());
        playlistItemRepository = new PlaylistItemRepository(getApplication());

        enterFullScreenMode();

        player = new ExoPlayer.Builder(this).build();
        binding.videoPlayer.setPlayer(player);

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
            player.setMediaItem(MediaItem.fromUri(uri));

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
            loadMediaItemsFromVideoLibrary(Integer.parseInt(uriSegments.get(1)));
        else if (type.equals(GetPlaybackUriUtils.PLAYLIST_URI_SEGMENT))
            loadMediaItemsFromVideoPlaylist(
                    Integer.parseInt(uriSegments.get(1)),
                    Integer.parseInt(uriSegments.get(2))
            );
    }

    /**
     * Load video media items from video library and start playing
     * from the specified index.
     * @param playbackStartIndex Index of item to start playback from
     */
    private void loadMediaItemsFromVideoLibrary(int playbackStartIndex) {
        videoLibraryRepository.getAllVideos().observe(this, videos -> {
            player.addMediaItems(GetMediaItemsUtils.fromLibraryVideos(videos));
            player.seekTo(playbackStartIndex, C.TIME_UNSET);
        });
    }

    /**
     * Load video media items from video playlist and start playing
     * from the specified index.
     * @param playlistId id of playlist to play
     * @param playbackStartIndex Index of item to start playback from
     */
    private void loadMediaItemsFromVideoPlaylist(int playlistId, int playbackStartIndex) {
        playlistItemRepository.getAllPlaylistMediasWithID(playlistId)
                .observe(this, playlistItem -> {
                    player.addMediaItems(GetMediaItemsUtils.fromPlaylistItems(playlistItem));
                    player.seekTo(playbackStartIndex, C.TIME_UNSET);
                });
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaSession.release();
        player.release();
    }
}