package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongsRepository;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SongsViewModel extends AndroidViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SongsRepository songsRepository;

    public SongsViewModel(Application application) {
        super(application);
        songsRepository = new SongsRepository(application);
    }

    public LiveData<List<Song>> getAllSongs(SongsRepository.SortBy sortBy, SortOrder sortOrder) {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();
        Disposable disposable = songsRepository.getAllSongs(sortBy, sortOrder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs::setValue);
        disposables.add(disposable);
        return songs;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
