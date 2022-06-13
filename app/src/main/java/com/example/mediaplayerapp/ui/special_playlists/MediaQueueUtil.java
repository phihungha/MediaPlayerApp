package com.example.mediaplayerapp.ui.special_playlists;


import android.app.Application;
import android.widget.Toast;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.special_playlists.MediaQueue;

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
        Toast.makeText(application, R.string.added_to_watch_later, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(application, R.string.added_to_listen_later, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(application, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(application, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
    }
}
