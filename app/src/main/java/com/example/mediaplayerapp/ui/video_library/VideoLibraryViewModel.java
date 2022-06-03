package com.example.mediaplayerapp.ui.video_library;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mediaplayerapp.data.video_library.Video;
import com.example.mediaplayerapp.data.video_library.VideosRepository;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VideoLibraryViewModel extends AndroidViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Video>> videos = new MutableLiveData<>();
    private final VideosRepository videosRepository;

    public VideoLibraryViewModel(Application application) {
        super(application);
        videosRepository = new VideosRepository(application);
    }

    public MutableLiveData<List<Video>> getAllVideos() {
        return videos;
    }

    public void loadAllVideos(VideosRepository.SortBy sortBy, SortOrder sortOrder) {
        Disposable disposable = videosRepository.getAllVideos(sortBy, sortOrder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos::setValue);
        disposables.add(disposable);
    }
}