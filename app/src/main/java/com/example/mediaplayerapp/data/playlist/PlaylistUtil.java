package com.example.mediaplayerapp.data.playlist;

import android.app.Application;

import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;

public class PlaylistUtil {
    public static int getNumberItemOfPlaylistWithID(Application application, int id){
        PlaylistItemViewModel playlistItemViewModel=new PlaylistItemViewModel(application);
        return playlistItemViewModel.getCountPlaylistWithID(id);
    }
}
