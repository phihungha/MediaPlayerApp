package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.data.music_library.AlbumsRepository;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongsRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AlbumDetailsViewModel extends AndroidViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final AlbumsRepository albumRepository;
    private final SongsRepository songsRepository;

    public AlbumDetailsViewModel(@NonNull Application application) {
        super(application);
        albumRepository  = new AlbumsRepository(application);
        songsRepository  = new SongsRepository(application);
    }

    public LiveData<List<Song>> getAlbumSongs(long albumId) {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();
        Disposable disposable = songsRepository.getAllSongsFromAlbum(albumId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs::setValue);
        disposables.add(disposable);
        return songs;
    }

    public LiveData<Album> getAlbum(long albumId) {
        MutableLiveData<Album> album = new MutableLiveData<>();
        Disposable disposable = albumRepository.getAlbum(albumId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(album::setValue);
        disposables.add(disposable);
        return album;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
