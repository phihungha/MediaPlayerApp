package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.data.music_library.AlbumsRepository;
import com.example.mediaplayerapp.data.music_library.Artist;

import java.util.List;

public class AlbumsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Album>> albums = new MutableLiveData<>();
    private final AlbumsRepository albumRepository;
    public AlbumsViewModel(@NonNull Application application) {
        super(application);
        albumRepository = new AlbumsRepository(application);
        albums.setValue(albumRepository.getAllAlbums());
    }

    public LiveData<List<Album>> getAllAlbums() {
        return albums;
    }

    public MutableLiveData<List<Album>> getAlbumSortByNameASC() {
        return albumRepository.getArtistSortbyNameASC();
    }

    public MutableLiveData<List<Album>> getAlbumSortByNameDESC() {
        return albumRepository.getArtistSortbyNameDESC();
    }
}
