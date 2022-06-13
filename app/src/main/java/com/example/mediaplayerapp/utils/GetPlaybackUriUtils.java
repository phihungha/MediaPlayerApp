package com.example.mediaplayerapp.utils;

import android.net.Uri;

import com.example.mediaplayerapp.data.music_library.SongsRepository;
import com.example.mediaplayerapp.data.video_library.VideosRepository;

/**
 * Utility methods to get URI to request playback within the app.
 */
public class GetPlaybackUriUtils {
    public static final String PLAYBACK_URI_SCHEME = "mediaplayerapp";
    public static final String PLAYLIST_URI_SEGMENT = "playlist";
    public static final String SPECIAL_PLAYLIST_URI_SEGMENT = "special_playlist";
    public static final String FAVORITES_URI_SEGMENT = "favorites";
    public static final String WATCH_LATER_URI_SEGMENT = "watch_later";
    public static final String MUSIC_LIBRARY_URI_SEGMENT = "music_library";
    public static final String SONG_URI_SEGMENT = "song";
    public static final String ALBUM_URI_SEGMENT = "album";
    public static final String ARTIST_URI_SEGMENT = "artist";
    public static final String VIDEO_LIBRARY_URI_SEGMENT = "video_library";

    /**
     * Get URI to play a playlist starting from an index.
     * @param id Playlist ID
     * @param index Index of the media item to start
     * @return URI for playback
     */
    public static Uri forPlaylist(long id, int index) {
        return new Uri.Builder()
                .scheme(PLAYBACK_URI_SCHEME)
                .appendPath(PLAYLIST_URI_SEGMENT)
                .appendPath(String.valueOf(id))
                .appendPath(String.valueOf(index))
                .build();
    }

    /**
     * Get URI to play favorites playlist starting from an index.
     * @param index Index of the media item to start
     * @return URI for playback
     */
    public static Uri forFavorites(int index) {
        return new Uri.Builder()
                .scheme(PLAYBACK_URI_SCHEME)
                .appendPath(SPECIAL_PLAYLIST_URI_SEGMENT)
                .appendPath(FAVORITES_URI_SEGMENT)
                .appendPath(String.valueOf(index))
                .build();
    }

    /**
     * Get URI to play watch later playlist starting from an index.
     * @param index Index of the media item to start
     * @return URI for playback
     */
    public static Uri forWatchLater(int index) {
        return new Uri.Builder()
                .scheme(PLAYBACK_URI_SCHEME)
                .appendPath(SPECIAL_PLAYLIST_URI_SEGMENT)
                .appendPath(WATCH_LATER_URI_SEGMENT)
                .appendPath(String.valueOf(index))
                .build();
    }

    /**
     * Get URI to play from video library starting from an index
     * @param index Index of the video item to start
     * @return URI for playback
     */
    public static Uri forVideoLibrary(VideosRepository.SortBy sortBy, SortOrder sortOrder, int index) {
        return new Uri.Builder()
                .scheme(PLAYBACK_URI_SCHEME)
                .appendPath(VIDEO_LIBRARY_URI_SEGMENT)
                .appendPath(sortBy.getUriSegmentName())
                .appendPath(sortOrder.getUriSegmentName())
                .appendPath(String.valueOf(index))
                .build();
    }

    /**
     * Get URI to play from music library starting from an index
     * @param index Index of the song to start
     * @return URI for playback
     */
    public static Uri forMusicLibrary(SongsRepository.SortBy sortBy, SortOrder sortOrder, int index) {
        return new Uri.Builder()
                .scheme(PLAYBACK_URI_SCHEME)
                .appendPath(MUSIC_LIBRARY_URI_SEGMENT)
                .appendPath(SONG_URI_SEGMENT)
                .appendPath(sortBy.getUriSegmentName())
                .appendPath(sortOrder.getUriSegmentName())
                .appendPath(String.valueOf(index))
                .build();
    }

    /**
     * Get URI to play songs from an artist starting from an index.
     * @param id Artist ID
     * @param index Index of the song to start
     * @return URI for playback
     */
    public static Uri forArtist(long id, int index) {
        return new Uri.Builder()
                .scheme(PLAYBACK_URI_SCHEME)
                .appendPath(MUSIC_LIBRARY_URI_SEGMENT)
                .appendPath(ARTIST_URI_SEGMENT)
                .appendPath(String.valueOf(id))
                .appendPath(String.valueOf(index))
                .build();
    }

    /**
     * Get URI to play an album starting from an index.
     * @param id Album ID
     * @param index Index of the song to start
     * @return URI for playback
     */
    public static Uri forAlbum(long id, int index) {
        return new Uri.Builder()
                .scheme(PLAYBACK_URI_SCHEME)
                .appendPath(MUSIC_LIBRARY_URI_SEGMENT)
                .appendPath(ALBUM_URI_SEGMENT)
                .appendPath(String.valueOf(id))
                .appendPath(String.valueOf(index))
                .build();
    }
}
