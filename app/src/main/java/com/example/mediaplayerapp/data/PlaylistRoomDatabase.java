package com.example.mediaplayerapp.data;

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
                            PlaylistRoomDatabase.class, "playlist_database")
                            //.addCallback(sRoomDatabaseCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


 /*   private static RoomDatabase.Callback sRoomDatabaseCallBack= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                PlaylistDao dao = INSTANCE.playlistDao();

                Playlist playlist = new Playlist(1, R.drawable.img_for_test,"Hello",1,true,10,11);
                dao.insert(playlist);
                playlist = new Playlist(2, R.drawable.img_for_test,"World",1,true,10,11);
                dao.insert(playlist);
            });
        }
    };*/
}
