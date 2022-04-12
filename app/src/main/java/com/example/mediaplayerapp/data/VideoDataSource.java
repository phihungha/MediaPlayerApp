package com.example.mediaplayerapp.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class VideoDataSource {

    public static MutableLiveData<List<Video>> getAllVideos(Context context) {
        List<Video> videoList = new ArrayList<>();

        String[] projection = new String[]{
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

            do {
                long videoId = cursor.getLong(idColumnIndex);
                Uri videoUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
                String videoName = cursor.getString(nameColumnIndex);
                int videoDuration = cursor.getInt(durationColumnIndex);

                videoList.add(new Video(videoUri, videoName, videoDuration));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return new MutableLiveData<>(videoList);
    }
}
