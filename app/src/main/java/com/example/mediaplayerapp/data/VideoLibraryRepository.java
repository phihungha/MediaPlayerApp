package com.example.mediaplayerapp.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class VideoLibraryRepository {
    private MutableLiveData<List<Video>> mAllVideos;

    public VideoLibraryRepository(Context context) {
        mAllVideos = VideoDataSource.getAllVideos(context);
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return mAllVideos;
    }
}
