package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.provider.MediaStore;

import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * Get albums.
 */
public class AlbumsRepository {

    public enum SortBy {
        NAME,
        NUMBER_OF_SONGS,
        FIRST_YEAR
    }

    AlbumsMediaStoreDataSource mediaStore;

    /**
     * Construct album repository.
     * @param context Application context
     */
    public AlbumsRepository(Context context) {
        this.mediaStore = new AlbumsMediaStoreDataSource(context);
    }

    public Single<List<Album>> getAllAlbums(SortBy sortBy, SortOrder sortOrder) {
        String sortQuery = "";
        switch (sortBy) {
            case NAME:
                sortQuery = MediaStore.Audio.Albums.ALBUM;
                break;
            case NUMBER_OF_SONGS:
                sortQuery = MediaStore.Audio.Albums.NUMBER_OF_SONGS;
                break;
            case FIRST_YEAR:
                sortQuery = MediaStore.Audio.Albums.FIRST_YEAR;
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

        return mediaStore.getAlbums(null, null, sortQuery);
    }

    public Single<Album> getAlbum(long id) {
        return mediaStore.getAlbums(MediaStore.Audio.Albums._ID + " = ?",
                new String[] { String.valueOf(id) },
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER)
                .map(i -> i.get(0));
    }
}
