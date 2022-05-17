package com.example.mediaplayerapp.utils;

import android.net.Uri;

public class MediaUriUtils {
    public static String PLAYLIST_URI_SCHEME = "playlist";
    public static String LIBRARY_URI_SCHEME = "library";

    public static Uri getPlaylistUri(long id) {
        return new Uri.Builder()
                .scheme(PLAYLIST_URI_SCHEME)
                .path(String.valueOf(id))
                .build();
    }

    /**
     * Get a special URI that contains the index of the media item to play.
     * This method helps skipping between library media items possible in players.
     * @param index Index of the media item in the library
     * @return Uri with the index
     */
    public static Uri getLibraryUri(int index) {
        return new Uri.Builder()
                .scheme(LIBRARY_URI_SCHEME)
                .path(String.valueOf(index))
                .build();
    }
}
