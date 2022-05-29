package com.example.mediaplayerapp.ui.music_library.artist_tab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Artist;
import com.example.mediaplayerapp.data.music_library.ArtistsRepository;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongsRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArtistDetailsViewModel extends AndroidViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final ArtistsRepository artistsRepository;
    private final SongsRepository songsRepository;

    private final MutableLiveData<Artist> artist = new MutableLiveData<>();
    private final MutableLiveData<List<Song>> artistSongs = new MutableLiveData<>();

    public ArtistDetailsViewModel(@NonNull Application application) {
        super(application);
        artistsRepository  = new ArtistsRepository(application);
        songsRepository  = new SongsRepository(application);
    }

    public LiveData<List<Song>> getArtistSongs() {
        return artistSongs;
    }

    public LiveData<Artist> getArtist() {
        return artist;
    }

    public void setCurrentArtistId(long id) {
        Disposable disposable1 = artistsRepository.getArtist(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artist::setValue);

        Disposable disposable2 = songsRepository.getAllSongsFromArtist(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artistSongs::setValue);

        disposables.addAll(disposable1, disposable2);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
