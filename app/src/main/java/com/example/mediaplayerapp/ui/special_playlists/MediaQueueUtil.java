package com.example.mediaplayerapp.ui.special_playlists;


import android.app.Application;

import com.example.mediaplayerapp.data.special_playlists.MediaQueue;
import com.example.mediaplayerapp.data.special_playlists.MediaQueueViewModel;

public class MediaQueueUtil {

    /** Insert video file to watch later
     * @param application Application
     * @param uriString value of uri convert to String
     *
     * */
    public static void insertVideoToWatchLater(Application application, String uriString){
        MediaQueueViewModel viewModel= new MediaQueueViewModel(application);
        long order= MediaUtils.generateOrderSort();
        MediaQueue mediaQueue = new MediaQueue(uriString, true, PlaylistConstants.TYPE_VIDEO_QUEUE,order);
        viewModel.insert(mediaQueue);
    }

    /** Insert song file to watch later
     * @param application Application
     * @param uriString value of uri convert to String
     *
     * */
    public static void insertSongToWatchLater(Application application, String uriString){
        MediaQueueViewModel viewModel= new MediaQueueViewModel(application);
        long order= MediaUtils.generateOrderSort();
        MediaQueue mediaQueue = new MediaQueue(uriString, false, PlaylistConstants.TYPE_MUSIC_QUEUE,order);
        viewModel.insert(mediaQueue);
    }

    /** Insert video file to favourite
     * @param application Application
     * @param uriString value of uri convert to String
     *
     * */
    public static void insertVideoToFavourite(Application application, String uriString){
        MediaQueueViewModel viewModel= new MediaQueueViewModel(application);
        long order= MediaUtils.generateOrderSort();
        MediaQueue mediaQueue = new MediaQueue(uriString, true, PlaylistConstants.TYPE_VIDEO_FAVOURITE,order);
        viewModel.insert(mediaQueue);
    }

    /** Insert song file to favourite
     * @param application Application
     * @param uriString value of uri convert to String
     *
     * */
    public static void insertSongToFavourite(Application application, String uriString){
        MediaQueueViewModel viewModel= new MediaQueueViewModel(application);
        long order= MediaUtils.generateOrderSort();
        MediaQueue mediaQueue = new MediaQueue(uriString,false, PlaylistConstants.TYPE_MUSIC_FAVOURITE,order);
        viewModel.insert(mediaQueue);
    }
}