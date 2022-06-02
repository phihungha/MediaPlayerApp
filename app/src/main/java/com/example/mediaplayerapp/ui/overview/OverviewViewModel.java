package com.example.mediaplayerapp.ui.overview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mediaplayerapp.data.overview.MediaPlaybackInfo;
import com.example.mediaplayerapp.data.overview.MediaPlaybackInfoRepository;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {

    private final MediaPlaybackInfoRepository repository;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaPlaybackInfoRepository(application);
    }

    public LiveData<List<MediaPlaybackInfo>> getRecentVideos(int mediaShownCount) {
        return repository.getRecentVideos (mediaShownCount);
    }

    public LiveData<List<MediaPlaybackInfo>> getMostWatchedVideos(int mediaShownCount) {
        return repository.getMostWatchedVideos(mediaShownCount);
    }

    public LiveData<List<MediaPlaybackInfo>> getRecentSongs(int mediaShownCount) {
        return repository.getRecentSongs(mediaShownCount);
    }

    public LiveData<List<MediaPlaybackInfo>> getMostListenedSongs(int mediaShownCount) {
        return repository.getMostListenedSongs(mediaShownCount);
    }

    public void insert(MediaPlaybackInfo mediaPlaybackInfo) {
        repository.insert(mediaPlaybackInfo);
    }

    public void updatePlaybackCount(MediaPlaybackInfo mediaPlaybackInfo) {
        repository.updatePlaybackCount(mediaPlaybackInfo);
    }

    public void update(MediaPlaybackInfo mediaPlaybackInfo) {
        repository.update(mediaPlaybackInfo);
    }

    /**
     * This method will call another method in Repository to check if there already exists a record
     * for this mediaPlaybackInfo. If a record already exists, update, otherwise insert
     *
     * @param mediaPlaybackInfo The mediaPlaybackInfo that user wishes to insert or update
     */
    public void insertOrUpdate(MediaPlaybackInfo mediaPlaybackInfo) {
        repository.insertOrUpdate(mediaPlaybackInfo);
    }
}
