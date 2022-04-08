package com.example.mediaplayerapp.ui.video_library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideoLibraryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VideoLibraryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is video library fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}