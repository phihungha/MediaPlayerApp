package com.example.mediaplayerapp.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

public class VideoLibraryRepository {
    private final MutableLiveData<List<Video>> mAllVideos;

    public VideoLibraryRepository(Context context) throws IOException {
        mAllVideos = VideoDataSource.getAllVideos(context);
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return mAllVideos;
    }
}
