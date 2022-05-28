package com.example.mediaplayerapp.utils;

import android.net.Uri;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.video_library.Video;
import com.google.android.exoplayer2.MediaItem;

import java.util.List;
import java.util.stream.Collectors;

public class GetMediaItemsUtils {

    /**
     * Get MediaItem objects from list of Song objects.
     * @param songs Song objects
     * @return MediaItem objects
     */
    public static List<MediaItem> fromLibrarySongs(List<Song> songs) {
        return songs.stream()
                .map(s -> MediaItem.fromUri(s.getUri()))
                .collect(Collectors.toList());
    }

    /**
     * Get MediaItem objects from list of Song objects.
     * @param videos Video objects
     * @return MediaItem objects
     */
    public static List<MediaItem> fromLibraryVideos(List<Video> videos) {
        return videos.stream()
                .map(s -> MediaItem.fromUri(s.getUri()))
                .collect(Collectors.toList());
    }

    /**
     * Get MediaItem objects from list of playlist's MediaItem objects.
     * @param items Playlist's MediaItem objects
     * @return MediaItem objects
     */
    public static List<MediaItem> fromPlaylistItems(
            List<PlaylistItem> items) {
        return items.stream()
                .map(i -> MediaItem.fromUri(Uri.parse(i.getMediaUri())))
                .collect(Collectors.toList());
    }
}
