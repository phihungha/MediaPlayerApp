package com.example.mediaplayerapp.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MusicLibraryRepository {
    @SuppressLint("Range")
    public static ArrayList<Song> getMusicInfos(Context context) {

        ArrayList<Song> musicInfos = new ArrayList<Song>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return null;
        }


        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();


            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

            if (isMusic != 0) {
                Song music = new Song();


                music.data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                if (!new File(music.data).exists()) {
                    continue;
                }


                music.songId = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

                music.songTitle = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));


                music.songTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));


                music.albumName = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));


                music.songArtist = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                music.duration = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(music.data);

                mmr.release();

                musicInfos.add(music);
            }
        }

        return musicInfos;
    }
}



