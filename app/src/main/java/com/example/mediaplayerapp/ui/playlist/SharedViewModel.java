package com.example.mediaplayerapp.ui.playlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mediaplayerapp.data.playlist.Playlist;

/** Share data between Fragment Playlist and Fragment Playlist Details
 * */
public class SharedViewModel extends ViewModel {
    private MutableLiveData<Playlist> selected=new MutableLiveData<>();

    public void setSelected(Playlist playlist) {
        selected.setValue(playlist);
    }

    public MutableLiveData<Playlist> getSelected() {
        return selected;
    }
}
