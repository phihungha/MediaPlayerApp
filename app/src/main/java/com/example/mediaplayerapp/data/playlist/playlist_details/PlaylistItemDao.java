package com.example.mediaplayerapp.data.playlist.playlist_details;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlaylistItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaylistItem media);

    @Update
    void update(PlaylistItem media);

    @Delete
    void delete(PlaylistItem media);

    @Query("DELETE FROM media_table")
    void deleteAll();

    @Query("DELETE FROM media_table WHERE media_table.MediaUri=:uri")
    void deleteItemWithUri(String uri);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId= :id ORDER BY OrderSort ASC")
    LiveData<List<PlaylistItem>> getAllPlaylistMediasWithID(int id);

    @Query("SELECT * FROM media_table ORDER BY OrderSort ASC")
    LiveData<List<PlaylistItem>> getAllPlaylistMedias();

    @Query("SELECT * FROM media_table WHERE media_table.MediaName LIKE '%' || :text || '%' ORDER BY OrderSort ASC")
    LiveData<List<PlaylistItem>> getAllMediaSearching(String text);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId=:id ORDER BY MediaName ASC,OrderSort ASC")
    LiveData<List<PlaylistItem>> sortAllMediaByNameASCWithID(int id);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId=:id ORDER BY MediaName DESC, OrderSort ASC")
    LiveData<List<PlaylistItem>> sortAllMediaByNameDESCWithID(int id);

    @Query("DELETE FROM media_table WHERE media_table.MediaId=:id")
    void deleteAllWithID(int id);

    @Query("SELECT COUNT(*) FROM media_table WHERE media_table.MediaId=:id")
    int getCountPlaylistWithID(int id);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId = :id ORDER BY OrderSort ASC")
    PlaylistItem findByItemId(int id);
}
