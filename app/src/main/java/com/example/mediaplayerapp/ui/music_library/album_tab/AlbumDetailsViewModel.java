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

    private final MutableLiveData<Album> album = new MutableLiveData<>();
    private final MutableLiveData<String> albumDuration = new MutableLiveData<>();
    private final MutableLiveData<List<Song>> albumSongs = new MutableLiveData<>();

    public AlbumDetailsViewModel(@NonNull Application application) {
        super(application);
        albumRepository  = new AlbumsRepository(application);
        songsRepository  = new SongsRepository(application);
    }

    public LiveData<List<Song>> getAlbumSongs() {
        return albumSongs;
    }

    public LiveData<Album> getAlbum() {
        return album;
    }

    public LiveData<String> getAlbumDuration() {
        return albumDuration;
    }

    public void setCurrentAlbumId(long id) {
        Disposable disposable1 = albumRepository.getAlbum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(album::setValue);

        Disposable disposable2 = songsRepository.getAllSongsFromAlbum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> {
                    albumSongs.setValue(songs);
                    long totalDurationLong = songs.stream().mapToLong(Song::getDuration).sum();
                    albumDuration.setValue(MediaTimeUtils.getFormattedTimeFromLong(totalDurationLong));
                });

        disposables.addAll(disposable1, disposable2);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
