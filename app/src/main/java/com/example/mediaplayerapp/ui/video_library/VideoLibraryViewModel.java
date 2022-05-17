package com.example.mediaplayerapp.ui.video_library;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.video.Video;
import com.example.mediaplayerapp.data.video.VideoLibraryRepository;

import java.util.List;

public class VideoLibraryViewModel extends AndroidViewModel {

    private final VideoLibraryRepository videoLibraryRepository;
    private final MutableLiveData<List<Video>> allVideos;

    public VideoLibraryViewModel(Application application) {
        super(application);
        videoLibraryRepository = new VideoLibraryRepository(application);
        allVideos = videoLibraryRepository.getAllVideos();
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return allVideos;
    }

    public MutableLiveData<List<Video>> getVideosSortByNameASC() {
        return videoLibraryRepository.getVideosSortByNameASC();
    }

    public MutableLiveData<List<Video>> getVideosSortByNameDESC() {
        return videoLibraryRepository.getVideosSortByNameDESC();
    }

    public MutableLiveData<List<Video>> getVideosSortByDurationASC() {
        return videoLibraryRepository.getVideosSortByDurationASC();
    }

    public MutableLiveData<List<Video>> getVideosSortByDurationDESC() {
        return videoLibraryRepository.getVideosSortByDurationDESC();
    }
}