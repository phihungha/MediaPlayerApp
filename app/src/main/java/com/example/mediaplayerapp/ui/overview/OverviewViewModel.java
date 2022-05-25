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

    public LiveData<List<MediaPlaybackInfo>> get5RecentVideos(){
        return repository.get5RecentVideos();
    }
}
