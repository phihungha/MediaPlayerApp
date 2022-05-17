package com.example.mediaplayerapp.utils;

import com.example.mediaplayerapp.data.music_library.Song;
import com.google.android.exoplayer2.MediaItem;

import java.util.List;
import java.util.stream.Collectors;

public class MediaItemUtils {

    /**
     * Get MediaItem objects from list of Song objects.
     * @param songs Song objects
     * @return MediaItem objects
     */
    public static List<MediaItem> getMediaItemsFromSongs(List<Song> songs) {
        return songs.stream()
                .map(s -> MediaItem.fromUri(s.getUri()))
                .collect(Collectors.toList());
    }
}
