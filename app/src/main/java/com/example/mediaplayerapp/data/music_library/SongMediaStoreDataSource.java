package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class SongMediaStoreDataSource extends MediaStoreDataSource {
    public SongMediaStoreDataSource(Context context) {
        super(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * Get songs that satisfy selection conditions
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @return List of Song objects
     */
    public List<Song> getSongs(String selection, String[] selectionArgs) {
        List<Song> songs = new ArrayList<>();

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
        };

        Cursor cursor = getMediaItems(
                projection,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int artist = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int duration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

        int orderIndex = 0;
        if (cursor.moveToFirst()) {
            do {
                songs.add(new Song(
                        getMediaItemUri(cursor.getLong(idColumn)),
                        cursor.getString(titleColumn),
                        cursor.getString(albumColumn),
                        cursor.getString(artist),
                        cursor.getInt(duration),
                        orderIndex));
                orderIndex++;
            } while (cursor.moveToNext());
            cursor.close();
        }

        return songs;
    }
}
