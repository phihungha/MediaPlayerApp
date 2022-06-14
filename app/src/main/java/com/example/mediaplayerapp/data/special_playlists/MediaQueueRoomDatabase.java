package com.example.mediaplayerapp.data.special_playlists;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MediaQueue.class}, version = 3, exportSchema = false)
public abstract class MediaQueueRoomDatabase extends RoomDatabase {
    public abstract MediaQueueDao mediaQueueDao();

    private static volatile MediaQueueRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MediaQueueRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MediaQueueRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MediaQueueRoomDatabase.class, "mediaQueue_table")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
