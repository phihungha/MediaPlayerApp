package com.example.mediaplayerapp.ui.music_library.artist_tab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Artist;
import com.example.mediaplayerapp.data.music_library.ArtistRepository;

import java.util.List;

public class ArtistsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Artist>> artists = new MutableLiveData<>();

    public ArtistsViewModel(@NonNull Application application) {
        super(application);
        ArtistRepository artistRepository = new ArtistRepository(application.getApplicationContext());
        artists.setValue(artistRepository.getAllArtists());
    }

    public LiveData<List<Artist>> getAllArtists() {
        return artists;
    }

}
