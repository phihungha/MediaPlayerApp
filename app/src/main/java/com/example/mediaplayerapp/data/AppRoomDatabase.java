package com.example.mediaplayerapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistDao;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.PlaylistItemDao;

@Database(entities = {Playlist.class, PlaylistItem.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract PlaylistDao playlistDao();
    public abstract PlaylistItemDao playlistItemDao();
    private static volatile AppRoomDatabase instance;

    public static AppRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppRoomDatabase.class,
                                    "database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
