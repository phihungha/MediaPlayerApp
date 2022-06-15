package com.example.mediaplayerapp.ui.music_player;

import android.app.Application;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MusicPlayerViewModel extends AndroidViewModel {

    private final MutableLiveData<MediaMetadataCompat> currentMediaMetadata = new MutableLiveData<>();
    private final MutableLiveData<PlaybackStateCompat> currentPlaybackState = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentRepeatMode = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentShuffleMode = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPlaylistId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> enableTransportControls = new MutableLiveData<>();

    public MusicPlayerViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<MediaMetadataCompat> getCurrentMediaMetadata() {
        return currentMediaMetadata;
    }

    public void setCurrentMediaMetadata(MediaMetadataCompat mediaMetadata) {
        currentMediaMetadata.setValue(mediaMetadata);
    }

    public LiveData<PlaybackStateCompat> getCurrentPlaybackState() {
        return currentPlaybackState;
    }

    public void setCurrentPlaybackState(PlaybackStateCompat playbackState) {
        currentPlaybackState.setValue(playbackState);
    }

    public LiveData<Integer> getCurrentRepeatMode() {
        return currentRepeatMode;
    }

    public void setCurrentRepeatMode(int repeatMode) {
        currentRepeatMode.setValue(repeatMode);
    }

    public LiveData<Integer> getCurrentShuffleMode() {
        return currentShuffleMode;
    }

    public void setCurrentShuffleMode(int shuffleMode) {
        currentShuffleMode.setValue(shuffleMode);
    }

    public LiveData<Integer> getCurrentPlaylistId() {
        return currentPlaylistId;
    }

    public void setCurrentPlaylistId(int playlistId) {
        currentPlaylistId.setValue(playlistId);
    }

    public LiveData<Boolean> getEnableTransportControls() {
        return enableTransportControls;
    }

    public void setEnableTransportControls(boolean enable) {
        enableTransportControls.setValue(enable);
    }
}
