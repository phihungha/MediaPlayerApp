package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.data.music_library.AlbumsRepository;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AlbumsViewModel extends AndroidViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final AlbumsRepository albumsRepository;

    public AlbumsViewModel(@NonNull Application application) {
        super(application);
        albumsRepository = new AlbumsRepository(application);
    }

    public LiveData<List<Album>> getAllAlbums(AlbumsRepository.SortBy sortBy, SortOrder sortOrder) {
        MutableLiveData<List<Album>> albums = new MutableLiveData<>();
        Disposable disposable = albumsRepository.getAllAlbums(sortBy, sortOrder)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(albums::setValue);
        disposables.add(disposable);
        return albums;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
