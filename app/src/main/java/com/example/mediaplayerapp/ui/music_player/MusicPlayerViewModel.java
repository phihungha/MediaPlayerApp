package com.example.mediaplayerapp.ui.music_player;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MusicPlayerViewModel extends ViewModel {
    private final MutableLiveData<String> text;

    public MusicPlayerViewModel() {
        text = new MutableLiveData<>();
        text.setValue("This is music player fragment");
    }

    public LiveData<String> getText() {
        return text;
    }
}