package com.example.mediaplayerapp.utils;

import android.net.Uri;

/**
 * Utility methods to get URI to request playback within the app.
 */
public class GetPlaybackUriUtils {
    public static String PLAYBACK_URI_SCHEME = "mediaplayerapp";
    public static String PLAYLIST_URI_SEGMENT = "playlist";
    public static String LIBRARY_URI_SEGMENT = "library";
    public static String ALBUM_URI_SEGMENT = "album";
    public static String ARTIST_URI_SEGMENT = "artist";

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
     * Get URI to play from library starting from an index
     * @param index Index of the media item to start
     * @return URI for playback
     */
    public static Uri forLibrary(int index) {
        return new Uri.Builder()
                .scheme(PLAYBACK_URI_SCHEME)
                .appendPath(LIBRARY_URI_SEGMENT)
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
                .appendPath(ALBUM_URI_SEGMENT)
                .appendPath(String.valueOf(id))
                .appendPath(String.valueOf(index))
                .build();
    }
}
