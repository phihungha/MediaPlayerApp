package com.example.mediaplayerapp.services;

import android.net.Uri;

public class PlaylistUriUtils {
    public static String PLAYLIST_URI_SCHEME = "playlist";
    
    public static Uri getPlaylistUri(Long id) {
        return new Uri.Builder()
                .scheme(PLAYLIST_URI_SCHEME)
                .path(String.valueOf(id))
                .build();
    }
}
