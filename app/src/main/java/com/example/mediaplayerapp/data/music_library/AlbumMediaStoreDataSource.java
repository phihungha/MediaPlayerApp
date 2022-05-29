package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * MediaStore data source for albums.
 */
public class AlbumMediaStoreDataSource extends MediaStoreDataSource {
    /**
     * Construct album MediaStore data source.
     * @param context Application context
     */
    public AlbumMediaStoreDataSource(Context context) {
        super(context, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI);
    }

    /**
     * Get albums that satisfy selection conditions
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @param sortOrder Sort order
     * @return List of Album objects
     */
    public List<Album> getAlbums(String selection, String[] selectionArgs, String sortOrder) {
        List<Album> albums = new ArrayList<>();

        String[] projection = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.FIRST_YEAR
        };

        Cursor cursor = getMediaItems(
                projection,
                selection,
                selectionArgs,
                sortOrder);

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID);
        int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM);
        int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST);
        int numberOfSongsColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
        int firstYearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.FIRST_YEAR);

        if (cursor.moveToFirst()) {
            do {
                albums.add(new Album(
                        cursor.getLong(idColumn),
                        getMediaItemUri(cursor.getLong(idColumn)),
                        cursor.getString(albumColumn),
                        cursor.getString(artistColumn),
                        cursor.getInt(numberOfSongsColumn),
                        cursor.getInt(firstYearColumn))
                );
            } while (cursor.moveToNext());
            cursor.close();
        }
        return albums;
    }
}
