package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.AlbumsRepository;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongsRepository;
import com.example.mediaplayerapp.utils.MediaTimeUtils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlbumDetailsViewModel extends AndroidViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final AlbumsRepository albumRepository;
    private final SongsRepository songsRepository;

    private final MutableLiveData<List<Song>> albumSongs = new MutableLiveData<>();
    private final MutableLiveData<String> albumName = new MutableLiveData<>();
    private final MutableLiveData<Uri> albumUri = new MutableLiveData<>();
    private final MutableLiveData<String> numberOfSongs = new MutableLiveData<>();
    private final MutableLiveData<String> totalDuration = new MutableLiveData<>();

    public AlbumDetailsViewModel(@NonNull Application application) {
        super(application);
        albumRepository  = new AlbumsRepository(application);
        songsRepository  = new SongsRepository(application);
    }

    public LiveData<List<Song>> getAlbumSongs() {
        return albumSongs;
    }

    public LiveData<String> getAlbumName() {
        return albumName;
    }

    public LiveData<String> getNumberOfSongs() {
        return numberOfSongs;
    }

    public LiveData<Uri> getAlbumUri() {
        return albumUri;
    }

    public LiveData<String> getTotalDuration() {
        return totalDuration;
    }

    public void setCurrentAlbumId(long id) {
        Disposable disposable1 = albumRepository.getAlbum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentAlbum -> {
                    albumName.setValue(currentAlbum.getArtistName());
                    albumUri.setValue(currentAlbum.getUri());
                    numberOfSongs.setValue(String.valueOf(currentAlbum.getNumberOfSongs()));
                });

        Disposable disposable2 = songsRepository.getAllSongsFromAlbum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    albumSongs.setValue(songs);
                    long totalDurationLong = songs.stream().mapToLong(Song::getDuration).sum();
                    totalDuration.setValue(MediaTimeUtils.getFormattedTime(totalDurationLong));
                });

        disposables.addAll(disposable1, disposable2);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
