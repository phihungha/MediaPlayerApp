package com.example.mediaplayerapp.data.video;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class VideoLibraryRepository {
    private final MutableLiveData<List<Video>> mAllVideos;

    public VideoLibraryRepository(Application application) {
        VideoDataSource videoDataSource = new VideoDataSource(application);
        mAllVideos = videoDataSource.getAllVideos();
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return mAllVideos;
    }

}
