package com.example.mediaplayerapp.ui.video_library;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mediaplayerapp.data.Video;
import com.example.mediaplayerapp.data.VideoLibraryRepository;

import java.util.List;

public class VideoLibraryViewModel extends ViewModel {

    private VideoLibraryRepository mVideoLibraryRepository;
    private final MutableLiveData<List<Video>> mAllVideos;

    public VideoLibraryViewModel(Context context) {
        mVideoLibraryRepository = new VideoLibraryRepository(context);
        mAllVideos = mVideoLibraryRepository.getAllVideos();
        //mText.setValue("This is video library fragment");
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return mAllVideos;
    }
}