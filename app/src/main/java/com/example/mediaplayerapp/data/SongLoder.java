package com.example.mediaplayerapp.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class SongLoder {

    public List<Song> getAllSongs(Context context) {

        List<Song> songList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,//0
                MediaStore.Audio.Media.TITLE,//1
                MediaStore.Audio.Media.ALBUM_ID,//2
                MediaStore.Audio.Media.ALBUM,//3
                MediaStore.Audio.Media.ARTIST_ID,//4
                MediaStore.Audio.Media.ARTIST,//5
                MediaStore.Audio.Media.DURATION,//6
                MediaStore.Audio.Media.TRACK//7

        };

        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                songList.add(new Song(cursor.getLong(0), cursor.getString(1), cursor.getLong(2), cursor.getString(3),
                        cursor.getLong(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7)));
            } while (cursor.moveToNext());

            if (cursor != null) {
                cursor.close();
            }

        }

        return songList;
    }
}
