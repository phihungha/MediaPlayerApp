package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongRepository;

import java.util.List;

public class SongsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Song>> songs = new MutableLiveData<>();

    public SongsViewModel(Application application) {
        super(application);
        SongRepository songRepository = new SongRepository(application.getApplicationContext());
        songs.setValue(songRepository.getAllSongs());
    }

    public LiveData<List<Song>> getAllSongs() {
        return songs;
    }
}
