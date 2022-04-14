package com.example.mediaplayerapp.ui.video_library;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.Video;
import com.example.mediaplayerapp.data.VideoLibraryRepository;

import java.io.IOException;
import java.util.List;

public class VideoLibraryViewModel extends AndroidViewModel {

    private VideoLibraryRepository mVideoLibraryRepository;
    private final MutableLiveData<List<Video>> mAllVideos;

    public VideoLibraryViewModel(Application application) {
        super(application);
        mVideoLibraryRepository = new VideoLibraryRepository(application);
        mAllVideos = mVideoLibraryRepository.getAllVideos();
        //mText.setValue("This is video library fragment");
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return mAllVideos;
    }
}