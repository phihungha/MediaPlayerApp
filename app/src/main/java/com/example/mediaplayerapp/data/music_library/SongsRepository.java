package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.provider.MediaStore;

import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * Get songs.
 */
public class SongsRepository {

    public enum SortBy
    {
        TITLE,
        DURATION,
        TIME_ADDED
    }

    SongsMediaStoreDataSource mediaStore;

    /**
     * Construct song repository.
     * @param context Application context
     */
    public SongsRepository(Context context) {
        this.mediaStore = new SongsMediaStoreDataSource(context);
    }

    /**
     * Get all songs from media store.
     * @return List of all songs
     */
    public Observable<List<Song>> getAllSongs(SortBy sortBy, SortOrder sortOrder) {
        String sortQuery = "";
        switch (sortBy) {
            case TITLE:
                sortQuery = MediaStore.Audio.Media.TITLE;
                break;
            case DURATION:
                sortQuery = MediaStore.Audio.Media.DURATION;
                break;
            case TIME_ADDED:
                sortQuery = MediaStore.Audio.Media.DATE_ADDED;
                break;
        }

        switch (sortOrder) {
            case ASC:
                sortQuery += " ASC";
                break;
            case DESC:
                sortQuery += " DESC";
                break;
        }

        return mediaStore.getSongs(null, null, sortQuery);
    }

    /**
     * Get all songs from an artist.
      * @param artistId Id of the artist
     * @return List of Song objects
     */
    public Observable<List<Song>> getAllSongsFromArtist(long artistId) {
        return mediaStore.getSongs(MediaStore.Audio.Media.ARTIST_ID + " = ?",
                new String[] { String.valueOf(artistId) },
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    /**
     * Get all songs from an album.
     * @param albumId Id of the album
     * @return List of Song objects
     */
    public Observable<List<Song>> getAllSongsFromAlbum(long albumId) {
        return mediaStore.getSongs(MediaStore.Audio.Media.ALBUM_ID + " = ?",
                new String[] { String.valueOf(albumId) },
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }
}
