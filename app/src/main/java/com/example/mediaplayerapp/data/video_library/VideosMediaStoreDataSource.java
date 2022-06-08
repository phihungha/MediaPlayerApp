package com.example.mediaplayerapp.data.video_library;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mediaplayerapp.data.MediaStoreDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class VideosMediaStoreDataSource extends MediaStoreDataSource {

    public VideosMediaStoreDataSource(Context context) {
        super(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * Get videos that satisfy certain conditions
     *
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @param sortOrder Sort order
     * @return List of videos
     */
    public Single<List<Video>> getVideos(
            String selection, String[] selectionArgs, String sortOrder) {

        return Single.fromCallable(() -> {
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

            Cursor cursor = getMediaItems(
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int resolutionColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION);
            int dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN);

            if (cursor.moveToFirst()) {
                do {
                    Uri videoUri = getMediaItemUri(cursor.getLong(idColumn));
                    videoList.add(new Video(
                            videoUri,
                            cursor.getString(nameColumn),
                            cursor.getInt(durationColumn),
                            cursor.getString(pathColumn),
                            cursor.getLong(sizeColumn),
                            cursor.getString(resolutionColumn),
                            cursor.getLong(dateTakenColumn)));
                } while (cursor.moveToNext());
                cursor.close();
            }

            return videoList;
        });
    }
}
