package com.example.mediaplayerapp.ui.music_library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MusicLibraryViewModel extends ViewModel {
    private final MutableLiveData<String> text;

    public MusicLibraryViewModel() {
        text = new MutableLiveData<>();
        text.setValue("This is music library fragment");
    }

    public LiveData<String> getText() {
        return text;
    }
}