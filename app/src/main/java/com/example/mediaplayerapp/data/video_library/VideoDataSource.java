package com.example.mediaplayerapp.data.video_library;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;
import androidx.loader.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;

public class VideoDataSource {

    private final Context context;

    public VideoDataSource(Context context) {
        this.context = context;
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return getVideosWithSomeCondition(null, null, null);
    }

    public MutableLiveData<List<Video>> getVideosSortByNameASC() {
        return getVideosWithSomeCondition(null, null,
                MediaStore.MediaColumns.DISPLAY_NAME + " ASC");
    }

    public MutableLiveData<List<Video>> getVideosSortByNameDESC() {
        return getVideosWithSomeCondition(null, null,
                MediaStore.MediaColumns.DISPLAY_NAME + " DESC");
    }

    public MutableLiveData<List<Video>> getVideosSortByDurationASC() {
        return getVideosWithSomeCondition(null, null,
                MediaStore.Video.Media.DURATION + " ASC");

    }

    public MutableLiveData<List<Video>> getVideosSortByDurationDESC() {
        return getVideosWithSomeCondition(null, null,
                MediaStore.Video.Media.DURATION + " DESC");
    }

    /**
     * Get videos that satisfy certain conditions
     *
     * @param selection     sql "where" clause with placeholder variables
     * @param selectionArgs values of placeholder variables
     * @param sortOrder     sql "order by" clause
     * @return The MutableLiveData form for list of video that satisfies the conditions
     */
    private MutableLiveData<List<Video>> getVideosWithSomeCondition(
            String selection, String[] selectionArgs, String sortOrder) {

        List<Video> videoList = new ArrayList<>();

        String[] projection = new String[]{
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATE_TAKEN,
        };
        //Using a cursorLoader with loadInBackground() method to do the loading in a worker thread,
        //this reduces freezing when loading videos from MediaStore
        CursorLoader cursorLoader = new CursorLoader(
                context,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int pathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int durationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int resolutionColumnIndex
                    = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION);
            int dateTakenColumnIndex
                    = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN);
            do {
                long videoId = cursor.getLong(idColumnIndex);
                Uri videoUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);

                String videoName = cursor.getString(nameColumnIndex);
                int videoDuration = cursor.getInt(durationColumnIndex);
                String videoPath = cursor.getString(pathColumnIndex);
                long videoSize = cursor.getLong(sizeColumnIndex);
                String videoResolution = cursor.getString(resolutionColumnIndex);
                int videoDateTaken = cursor.getInt(dateTakenColumnIndex);

                videoList.add(new Video(videoUri, videoName, videoDuration
                        , videoPath, videoSize, videoResolution, videoDateTaken));

            } while (cursor.moveToNext());
            cursor.close();
        }

        return new MutableLiveData<>(videoList);
    }
}