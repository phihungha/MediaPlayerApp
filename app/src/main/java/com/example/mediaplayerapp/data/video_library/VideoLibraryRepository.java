package com.example.mediaplayerapp.data.video_library;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class VideoLibraryRepository {

    private final VideoMediaStoreDataSource videoDataSource;
    private final MutableLiveData<List<Video>> allVideos;

    public VideoLibraryRepository(Application application) {
        videoDataSource = new VideoMediaStoreDataSource(application);
        allVideos = videoDataSource.getAllVideos();
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return allVideos;
    }

    public MutableLiveData<List<Video>> getVideosSortByNameASC() {
        return videoDataSource.getVideosSortByNameASC();
    }

    public MutableLiveData<List<Video>> getVideosSortByNameDESC() {
        return videoDataSource.getVideosSortByNameDESC();
    }

    public MutableLiveData<List<Video>> getVideosSortByDurationASC() {
        return videoDataSource.getVideosSortByDurationASC();
    }

    public MutableLiveData<List<Video>> getVideosSortByDurationDESC() {
        return videoDataSource.getVideosSortByDurationDESC();
    }

    public MutableLiveData<List<Video>> getVideoByUri(Uri uri){
        return videoDataSource.getVideoByUri(uri);
    }
}
