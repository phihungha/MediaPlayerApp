package com.example.mediaplayerapp.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class VideoLibraryRepository {
    private List<Video> mAllVideos;


    public VideoLibraryRepository(Context context) {
        mAllVideos = VideoDataSource.getAllVideos(context);
    }

    public List<Video> getAllVideos() {
        return mAllVideos;
    }
}
