package com.example.mediaplayerapp.data.playlist.media_queue;


import android.app.Application;

import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;

public class MediaQueueUtil {

    /** Insert video file to watch later
     * @param application Application
     * @param uriString value of uri convert to String
     * @param name name of video
     * */
    public static void insertVideoToWatchLater(Application application, String uriString, String name){
        MediaQueueViewModel viewModel= new MediaQueueViewModel(application);
        int count= viewModel.getCountMediaQueue();
        MediaQueue mediaQueue = new MediaQueue(uriString, name, true, PlaylistConstants.TYPE_VIDEO_QUEUE,count+1);
        viewModel.insert(mediaQueue);
    }


    /** Insert song file to watch later
     * @param application Application
     * @param uriString value of uri convert to String
     * @param name name of song
     * */
    public static void insertSongToWatchLater(Application application, String uriString, String name){
        MediaQueueViewModel viewModel= new MediaQueueViewModel(application);
        int count= viewModel.getCountMediaQueue();
        MediaQueue mediaQueue = new MediaQueue(uriString, name, false, PlaylistConstants.TYPE_MUSIC_QUEUE,count+1);
        viewModel.insert(mediaQueue);
    }

    /** Insert video file to favourite
     * @param application Application
     * @param uriString value of uri convert to String
     * @param name name of video
     * */
    public static void insertVideoToFavourite(Application application, String uriString, String name){
        MediaQueueViewModel viewModel= new MediaQueueViewModel(application);
        int count= viewModel.getCountMediaQueue();
        MediaQueue mediaQueue = new MediaQueue(uriString, name, true, PlaylistConstants.TYPE_VIDEO_FAVOURITE,count+1);
        viewModel.insert(mediaQueue);
    }

    /** Insert song file to favourite
     * @param application Application
     * @param uriString value of uri convert to String
     * @param name name of song
     * */
    public static void insertSongToFavourite(Application application, String uriString, String name){
        MediaQueueViewModel viewModel= new MediaQueueViewModel(application);
        int count= viewModel.getCountMediaQueue();
        MediaQueue mediaQueue = new MediaQueue(uriString, name, false, PlaylistConstants.TYPE_MUSIC_FAVOURITE,count+1);
        viewModel.insert(mediaQueue);
    }
}
