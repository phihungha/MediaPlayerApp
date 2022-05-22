package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.data.music_library.AlbumsRepository;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongsRepository;

import java.util.List;

public class AlbumDetailsViewModel extends AndroidViewModel {

    private final AlbumsRepository albumRepository;
    private final SongsRepository songRepository;

    private final MutableLiveData<List<Song>> albumSongs = new MutableLiveData<>();
    private final MutableLiveData<String> albumName = new MutableLiveData<>();
    private final MutableLiveData<Uri> albumUri = new MutableLiveData<>();
    private final MutableLiveData<String> numberOfSongs = new MutableLiveData<>();
    private final MutableLiveData<String> totalDuration = new MutableLiveData<>();

    public AlbumDetailsViewModel(@NonNull Application application) {
        super(application);
        albumRepository  = new AlbumsRepository(application);
        songRepository  = new SongsRepository(application);
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
        Album currentAlbum = albumRepository.getAlbum(id);
        albumName.setValue(currentAlbum.getArtistName());
        albumUri.setValue(currentAlbum.getUri());
        numberOfSongs.setValue(String.valueOf(currentAlbum.getNumberOfSongs()));
        albumSongs.setValue(songRepository.getAllSongsFromAlbum(id));
        totalDuration.setValue(songRepository.getAlbumDurationById(id));
    }
}
