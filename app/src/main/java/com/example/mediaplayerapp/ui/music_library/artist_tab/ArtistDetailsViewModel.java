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

public class ArtistDetailsViewModel extends AndroidViewModel {

    private final ArtistsRepository artistRepository;
    private final SongsRepository songRepository;

    private final MutableLiveData<List<Song>> artistSongs = new MutableLiveData<>();
    private final MutableLiveData<String> artistName = new MutableLiveData<>();
    private final MutableLiveData<String> numberOfSongs = new MutableLiveData<>();
    private final MutableLiveData<String> numberOfAlbums = new MutableLiveData<>();

    public ArtistDetailsViewModel(@NonNull Application application) {
        super(application);
        artistRepository  = new ArtistsRepository(application);
        songRepository  = new SongsRepository(application);
    }

    public LiveData<List<Song>> getArtistSongs() {
        return artistSongs;
    }

    public LiveData<String> getArtistName() {
        return artistName;
    }

    public LiveData<String> getNumberOfSongs() {
        return numberOfSongs;
    }

    public LiveData<String> getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public void setCurrentArtistId(long id) {
        Artist currentArtist = artistRepository.getArtist(id);
        artistName.setValue(currentArtist.getArtistName());
        numberOfSongs.setValue(String.valueOf(currentArtist.getNumberOfSongs()));
        numberOfAlbums.setValue(String.valueOf(currentArtist.getNumberOfAlbums()));
        artistSongs.setValue(songRepository.getAllSongsFromArtist(id));
    }
}
