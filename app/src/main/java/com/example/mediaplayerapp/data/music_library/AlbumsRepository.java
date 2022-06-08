package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mediaplayerapp.data.MediaStoreDataSource;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.ArrayList;
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

    private final MediaStoreDataSource mediaStore;

    /**
     * Construct album repository.
     * @param context Application context
     */
    public AlbumsRepository(Context context) {
        mediaStore = new MediaStoreDataSource(context, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI);
    }

    /**
     * Get albums that satisfy selection conditions
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @param sortOrder Sort order
     * @return List of Album objects
     */
    public Single<List<Album>> getAlbums(String selection, String[] selectionArgs, String sortOrder) {
        return Single.fromCallable(() -> {
            List<Album> albums = new ArrayList<>();

            String[] projection = new String[]{
                    MediaStore.Audio.Albums._ID,
                    MediaStore.Audio.Albums.ALBUM,
                    MediaStore.Audio.Albums.ARTIST,
                    MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                    MediaStore.Audio.Albums.FIRST_YEAR
            };

            Cursor cursor = mediaStore.getMediaItems(
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);

            int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID);
            int albumColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM);
            int artistColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST);
            int numberOfSongsColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
            int firstYearColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.FIRST_YEAR);

            if (cursor.moveToFirst()) {
                do {
                    albums.add(new Album(
                            cursor.getLong(idColumnIndex),
                            mediaStore.getMediaItemUri(cursor.getLong(albumColumnIndex)),
                            cursor.getString(albumColumnIndex),
                            cursor.getString(artistColumnIndex),
                            cursor.getInt(numberOfSongsColumnIndex),
                            cursor.getInt(firstYearColumnIndex))
                    );
                } while (cursor.moveToNext());
                cursor.close();
            }
            return albums;
        });
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

        return getAlbums(null, null, sortQuery);
    }

    public Single<Album> getAlbum(long id) {
        return getAlbums(MediaStore.Audio.Albums._ID + " = ?",
                new String[] { String.valueOf(id) },
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER)
                .map(i -> i.get(0));
    }
}
