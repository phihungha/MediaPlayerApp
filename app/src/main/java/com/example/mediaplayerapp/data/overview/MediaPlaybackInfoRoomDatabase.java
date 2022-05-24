package com.example.mediaplayerapp.data.overview;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MediaPlaybackInfo.class}, version = 1, exportSchema = false)
public abstract class MediaPlaybackInfoRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile MediaPlaybackInfoRoomDatabase INSTANCE;

    static MediaPlaybackInfoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MediaPlaybackInfoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MediaPlaybackInfoRoomDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MediaPlaybackInfoDao mediaPlaybackInfoDao();
}
