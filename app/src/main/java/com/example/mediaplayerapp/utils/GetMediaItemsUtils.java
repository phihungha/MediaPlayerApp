package com.example.mediaplayerapp.utils;

import android.net.Uri;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.data.special_playlists.MediaQueue;
import com.example.mediaplayerapp.data.video_library.Video;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;

import java.util.List;
import java.util.stream.Collectors;

public class GetMediaItemsUtils {

    /**
     * Get an Exoplayer's MediaItem from an URI with URI metadata included.
     * @param uri URI
     * @return Exoplayer's MediaItem object
     */
    public static MediaItem getMediaItemFromUri(Uri uri) {
        return new MediaItem.Builder()
                .setUri(uri)
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setMediaUri(uri)
                                .build())
                .build();
    }

    /**
     * Get Exoplayer's MediaItem objects from list of Song objects.
     * @param songs Song objects
     * @return MediaItem objects
     */
    public static List<MediaItem> fromLibrarySongs(List<Song> songs) {
        return songs.stream()
                .map(i -> getMediaItemFromUri(i.getUri()))
                .collect(Collectors.toList());
    }

    /**
     * Get Exoplayer's MediaItem objects from list of Song objects.
     * @param videos Video objects
     * @return MediaItem objects
     */
    public static List<MediaItem> fromLibraryVideos(List<Video> videos) {
        return videos.stream()
                .map(i -> getMediaItemFromUri(i.getUri()))
                .collect(Collectors.toList());
    }

    /**
     * Get Exoplayer's MediaItem objects from playlist items.
     * @param items Playlist's MediaItem objects
     * @return MediaItem objects
     */
    public static List<MediaItem> fromPlaylistItems(List<PlaylistItem> items) {
        return items.stream()
                .map(i -> getMediaItemFromUri(Uri.parse(i.getMediaUri())))
                .collect(Collectors.toList());
    }

    /**
     * Get Exoplayer's MediaItem objects from special playlists' items.
     * @param items Playlist's MediaItem objects
     * @return MediaItem objects
     */
    public static List<MediaItem> fromSpecialPlaylistItems(List<MediaQueue> items) {
        return items.stream()
                .map(i -> getMediaItemFromUri(Uri.parse(i.getMediaUri())))
                .collect(Collectors.toList());
    }
}
