package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongsRepository;


import java.util.List;

public class SongsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Song>> songs = new MutableLiveData<>();
    private final SongsRepository songsRepository;
    public SongsViewModel(Application application) {
        super(application);
        songsRepository = new SongsRepository(application);
        songs.setValue(songsRepository.getAllSongs());
    }

    public LiveData<List<Song>> getAllSongs() {
        return songs;
    }

    public MutableLiveData<List<Song>> getSongsSortByTitleASC() {
        return songsRepository.getSongSortbyTitleASC();
    }
    public MutableLiveData<List<Song>> getSongsSortByTitleDESC() {
        return songsRepository.getSongSortbyTitleDESC();
    }
}
