package com.example.mediaplayerapp.ui.music_library.artist_tab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Artist;
import com.example.mediaplayerapp.data.music_library.ArtistsRepository;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArtistsViewModel extends AndroidViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Artist>> artists = new MutableLiveData<>();
    private final ArtistsRepository artistsRepository;

    public ArtistsViewModel(@NonNull Application application) {
        super(application);
        artistsRepository = new ArtistsRepository(application.getApplicationContext());
    }

    public void loadAllArtists(ArtistsRepository.SortBy sortBy, SortOrder sortOrder) {
        Disposable disposable = artistsRepository.getAllArtists(sortBy, sortOrder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artists::setValue);
        disposables.add(disposable);
    }

    public LiveData<List<Artist>> getAllArtists() {
        return artists;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
