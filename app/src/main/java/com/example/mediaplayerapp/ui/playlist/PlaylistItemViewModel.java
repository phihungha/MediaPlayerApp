package com.example.mediaplayerapp.ui.playlist;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongsRepository;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.PlaylistItemRepository;
import com.example.mediaplayerapp.data.video_library.Video;
import com.example.mediaplayerapp.data.video_library.VideosRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class PlaylistItemViewModel extends AndroidViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final PlaylistItemRepository playlistItemRepository;
    private final VideosRepository videosRepository;
    private final SongsRepository songsRepository;

    public PlaylistItemViewModel(@NonNull Application application) {
        super(application);
        playlistItemRepository = new PlaylistItemRepository(application);
        videosRepository = new VideosRepository(application);
        songsRepository = new SongsRepository(application);
    }

    /**
     * Get all items of a playlist.
     *
     * @param playlistId Playlist id
     * @return List of playlist items
     */
    public LiveData<List<PlaylistItem>> getAllItemsOfPlaylist(int playlistId) {
        return LiveDataReactiveStreams
                .fromPublisher(playlistItemRepository.getAllItemsOfPlaylist(playlistId));
    }

    public Completable addPlaylistItem(PlaylistItem item) {
        return playlistItemRepository.addPlaylistItem(item)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addPlaylistItems(List<PlaylistItem> items) {
        return playlistItemRepository.addPlaylistItems(items)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updatePlaylistItems(List<PlaylistItem> items) {
        return playlistItemRepository.updatePlaylistItems(items)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deletePlaylistItem(PlaylistItem item) {
        return playlistItemRepository.deletePlaylistItem(item)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public LiveData<PlaylistItem> getFirstItemOfPlaylist(int id) {
        return LiveDataReactiveStreams.fromPublisher(playlistItemRepository.getFirstItemOfPlaylist(id));
    }

    public LiveData<Video> getVideoMetadata(Uri uri) {
        MutableLiveData<Video> liveData = new MutableLiveData<>();
        Disposable disposable = videosRepository.getVideo(uri)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(liveData::setValue);
        disposables.add(disposable);
        return liveData;
    }

    public LiveData<Song> getSongMetadata(Uri uri) {
        MutableLiveData<Song> liveData = new MutableLiveData<>();
        Disposable disposable = songsRepository.getSong(uri)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(liveData::setValue);
        disposables.add(disposable);
        return liveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
