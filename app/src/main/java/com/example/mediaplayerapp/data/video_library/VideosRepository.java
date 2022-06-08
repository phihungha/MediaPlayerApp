package com.example.mediaplayerapp.data.video_library;

import android.content.Context;
import android.provider.MediaStore;

import com.example.mediaplayerapp.utils.SortOrder;

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

    private final VideosMediaStoreDataSource mediaStore;

    public VideosRepository(Context context) {
        mediaStore = new VideosMediaStoreDataSource(context);
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

        return mediaStore.getVideos(null, null, sortQuery);
    }
}
