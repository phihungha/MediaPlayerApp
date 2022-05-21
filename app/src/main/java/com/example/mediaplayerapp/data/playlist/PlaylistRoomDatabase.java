package com.example.mediaplayerapp.data.playlist;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mediaplayerapp.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Playlist.class}, version = 1, exportSchema = false)
public abstract class PlaylistRoomDatabase extends RoomDatabase {
    public abstract PlaylistDao playlistDao();

    private static volatile PlaylistRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PlaylistRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlaylistRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlaylistRoomDatabase.class, "playlist_table")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallBack= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                PlaylistDao dao = INSTANCE.playlistDao();

                Playlist favourite = new Playlist(R.drawable.ic_favorite_24dp,"My Favourite",true,0,null);
                dao.insert(favourite);
            });
        }
    };
}
