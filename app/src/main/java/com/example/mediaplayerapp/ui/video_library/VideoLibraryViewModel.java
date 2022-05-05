package com.example.mediaplayerapp.ui.video_library;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.video.Video;
import com.example.mediaplayerapp.data.video.VideoLibraryRepository;

import java.util.List;

public class VideoLibraryViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Video>> mAllVideos;
    private final VideoLibraryRepository mVideoLibraryRepository;

    public VideoLibraryViewModel(Application application) {
        super(application);
        mVideoLibraryRepository = new VideoLibraryRepository(application);
        mAllVideos = mVideoLibraryRepository.getAllVideos();
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return mAllVideos;
    }
}