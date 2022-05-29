package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.provider.MediaStore;

import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * Get artists.
 */
public class ArtistsRepository {

    public enum SortBy
    {
        NAME,
        NUMBER_OF_ALBUMS,
        NUMBER_OF_TRACKS
    }

    ArtistsMediaStoreDataSource dataSource;

    /**
     * Construct artist repository.
     * @param context Application context
     */
    public ArtistsRepository(Context context) {
        this.dataSource = new ArtistsMediaStoreDataSource(context);
    }

    /**
     * Get all artists from media store.
     * @return List of all artists
     */
    public Observable<List<Artist>> getAllArtists(SortBy sortBy, SortOrder sortOrder) {
        String sortQuery = "";
        switch (sortBy) {
            case NAME:
                sortQuery = MediaStore.Audio.Artists.ARTIST;
                break;
            case NUMBER_OF_ALBUMS:
                sortQuery = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS;
                break;
            case NUMBER_OF_TRACKS:
                sortQuery = MediaStore.Audio.Artists.NUMBER_OF_TRACKS;
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

        return dataSource.getArtists(null, null, sortQuery);
    }

    public Observable<Artist> getArtist(long id) {
        return dataSource.getArtists(MediaStore.Audio.Artists._ID + " = ?",
                new String[] { String.valueOf(id) },
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER)
                .map(i -> i.get(0));
    }
}
