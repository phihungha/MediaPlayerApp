package com.example.mediaplayerapp.ui.video_player;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideoPlayerViewModel extends ViewModel {
    private final MutableLiveData<String> text;

    public VideoPlayerViewModel() {
        text = new MutableLiveData<>();
        text.setValue("This is video player fragment");
    }

    public LiveData<String> getText() {
        return text;
    }
}