package com.example.mediaplayerapp.ui.overview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.example.mediaplayerapp.data.playback_history.PlaybackHistoryEntry;
import com.example.mediaplayerapp.data.playback_history.PlaybackHistoryRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;

public class OverviewViewModel extends AndroidViewModel {

    private final PlaybackHistoryRepository repository;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaybackHistoryRepository(application);
    }

    public LiveData<List<PlaybackHistoryEntry>> getRecentVideos(int displayCount) {
        return LiveDataReactiveStreams
                .fromPublisher(repository.getRecentVideos(displayCount));
    }

    public LiveData<List<PlaybackHistoryEntry>> getMostWatchedVideos(int displayCount) {
        return LiveDataReactiveStreams
                .fromPublisher(repository.getMostWatchedVideos(displayCount));
    }

    public LiveData<List<PlaybackHistoryEntry>> getRecentSongs(int displayCount) {
        return LiveDataReactiveStreams
                .fromPublisher(repository.getRecentSongs(displayCount));
    }

    public LiveData<List<PlaybackHistoryEntry>> getMostListenedSongs(int displayCount) {
        return LiveDataReactiveStreams
                .fromPublisher(repository.getMostListenedSongs(displayCount));
    }

    public Completable clearHistory() {
        return repository.clearHistory().observeOn(AndroidSchedulers.mainThread());
    }
}
