package com.example.mediaplayerapp.ui.playlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlaylistViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PlaylistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is playlist fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}