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
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK,
        };

        Cursor cursor = getMediaItems(
                projection,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
        int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int artistId = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID);
        int artist = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int duration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int track = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK);

        if (cursor.moveToFirst()) {
            do {
                songs.add(new Song(
                        cursor.getLong(idColumn),
                        getMediaItemUri(cursor.getLong(idColumn)),
                        cursor.getString(titleColumn),
                        cursor.getLong(albumIdColumn),
                        cursor.getString(albumColumn),
                        cursor.getLong(artistId),
                        cursor.getString(artist),
                        cursor.getInt(duration),
                        cursor.getInt(track)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return songs;
    }
}
