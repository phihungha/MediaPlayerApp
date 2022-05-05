package com.example.mediaplayerapp.data.video;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class VideoLibraryRepository {
    private final MutableLiveData<List<Video>> mAllVideos;

    public VideoLibraryRepository(Context context) {
        mAllVideos = VideoDataSource.getAllVideos(context);
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return mAllVideos;
    }
}
