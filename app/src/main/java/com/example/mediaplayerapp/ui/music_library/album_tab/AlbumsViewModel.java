package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.data.music_library.AlbumRepository;

import java.util.List;

public class AlbumsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Album>> albums = new MutableLiveData<>();

    public AlbumsViewModel(@NonNull Application application) {
        super(application);
        AlbumRepository albumRepository = new AlbumRepository(application);
        albums.setValue(albumRepository.getAllAlbums());
    }

    public LiveData<List<Album>> getAllAlbums() {
        return albums;
    }
}
