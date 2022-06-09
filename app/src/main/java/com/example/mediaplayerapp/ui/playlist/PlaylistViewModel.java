package com.example.mediaplayerapp.ui.playlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistRepository;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;

public class PlaylistViewModel extends AndroidViewModel {

    private final PlaylistRepository playlistRepository;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        playlistRepository =new PlaylistRepository(application);
    }

    public LiveData<List<Playlist>> getAllPlaylists(PlaylistRepository.SortBy sortBy, SortOrder sortOrder) {
        return LiveDataReactiveStreams
                .fromPublisher(playlistRepository.getAllPlaylists(sortBy, sortOrder));
    }

    public LiveData<Playlist> getPlaylist(int id) {
        return LiveDataReactiveStreams
                .fromPublisher(playlistRepository.getPlaylist(id));
    }

    public LiveData<List<Playlist>> getPlaylistsByNameMatching(String name){
        return LiveDataReactiveStreams
                .fromPublisher(playlistRepository.getPlaylistsByNameMatching(name));
    }

    public Completable insertPlaylist(Playlist playlist){
        return playlistRepository.addPlaylist(playlist)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updatePlaylist(Playlist playlist){
        return playlistRepository.updatePlaylist(playlist)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable renamePlaylist(int playlistId, String newName) {
        return playlistRepository.renamePlaylist(playlistId, newName)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deletePlaylist(int playlistId) {
        return playlistRepository.deletePlaylist(playlistId)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
