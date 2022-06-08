package com.example.mediaplayerapp.data.video_library;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mediaplayerapp.data.MediaStoreDataSource;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class VideosRepository {

    public enum SortBy {
        NAME("name"),
        DURATION("duration"),
        TIME_ADDED("time_added");

        private final String uriSegmentName;

        SortBy(String uriSegmentName) {
            this.uriSegmentName = uriSegmentName;
        }

        public String getUriSegmentName() {
            return uriSegmentName;
        }
    }

    private final MediaStoreDataSource mediaStore;

    public VideosRepository(Context context) {
        mediaStore = new MediaStoreDataSource(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
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

            Cursor cursor = mediaStore.getMediaItems(
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            );

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int resolutionColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION);
            int dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN);

            if (cursor.moveToFirst()) {
                do {
                    Uri videoUri = mediaStore.getMediaItemUri(cursor.getLong(idColumn));
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

    public Single<List<Video>> getAllVideos(SortBy sortBy, SortOrder sortOrder) {
        String sortQuery = "";
        switch (sortBy) {
            case NAME:
                sortQuery = MediaStore.Audio.Media.TITLE;
                break;
            case DURATION:
                sortQuery = MediaStore.Audio.Media.DURATION;
                break;
            case TIME_ADDED:
                sortQuery = MediaStore.Audio.Media.DATE_ADDED;
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

        return getVideos(null, null, sortQuery);
    }
}
