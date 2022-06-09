package com.example.mediaplayerapp.data.video_library;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mediaplayerapp.data.MediaStoreDataSource;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.RESOLUTION,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.SIZE
            };

            Cursor cursor = mediaStore.getMediaItems(
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            );

            int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int resolutionColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION);
            int durationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int dateAddedColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
            int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

            if (cursor.moveToFirst()) {
                do {
                    Uri videoUri = mediaStore.getMediaItemUri(cursor.getLong(idColumnIndex));
                    String title = cursor.getString(nameColumnIndex);
                    videoList.add(new Video(
                            videoUri,
                            title,
                            cursor.getString(resolutionColumnIndex),
                            cursor.getInt(durationColumnIndex),
                            MediaTimeUtils.getZonedDateTimeFromLong(cursor.getLong(dateAddedColumnIndex)),
                            title,
                            cursor.getString(dataColumnIndex),
                            cursor.getLong(sizeColumnIndex),
                            0));
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

    public Single<Video> getVideo(Uri uri) {
        return Single.fromCallable(() -> {
            String[] projection = new String[]{
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.RESOLUTION,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.SIZE
            };

            Cursor cursor = mediaStore.getMediaItem(uri, projection);

            int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int resolutionColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION);
            int durationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int dateAddedColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
            int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

            Video video = null;
            if (cursor.moveToFirst()) {
                Uri videoUri = mediaStore.getMediaItemUri(cursor.getLong(idColumnIndex));
                String title = cursor.getString(nameColumnIndex);
                video = new Video(
                        videoUri,
                        title,
                        cursor.getString(resolutionColumnIndex),
                        cursor.getInt(durationColumnIndex),
                        MediaTimeUtils.getZonedDateTimeFromLong(cursor.getLong(dateAddedColumnIndex)),
                        title,
                        cursor.getString(dataColumnIndex),
                        cursor.getLong(sizeColumnIndex),
                        0
                );
                cursor.close();
            }

            return video;
        }).subscribeOn(Schedulers.io());
    }
}
