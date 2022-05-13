package com.example.mediaplayerapp.data.playlist.playlist_details;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PlaylistMedia.class}, version = 1, exportSchema = false)
public abstract class PlaylistMediaRoomDatabase extends RoomDatabase {
    public abstract PlaylistMediaDao playlistMediaDao();

    private static volatile PlaylistMediaRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PlaylistMediaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlaylistMediaRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlaylistMediaRoomDatabase.class, "media_table")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
