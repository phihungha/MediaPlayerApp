package com.example.mediaplayerapp.data.video;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class VideoLibraryRepository {
    private final MutableLiveData<List<Video>> mAllVideos;
    private final VideoDataSource videoDataSource;

    public VideoLibraryRepository(Application application) {
        videoDataSource = new VideoDataSource(application);
        mAllVideos = videoDataSource.getAllVideos();
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return mAllVideos;
    }

    public void deleteVideo(Video video){videoDataSource.deleteVideo(video);}
}
