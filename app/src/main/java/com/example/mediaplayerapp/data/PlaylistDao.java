package com.example.mediaplayerapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert
    void insert(Playlist playlist);

    @Update
    void update(Playlist playlist);

    @Delete
    void delete(Playlist playlist);

    @Query("DELETE FROM playlist_table")
    void deleteAll();

    @Query("SELECT * FROM playlist_table ORDER BY PlaylistID ASC")
    LiveData<List<Playlist>> getAllPlaylists();

  /*  @Query("SELECT * from playlist_table WHERE PlaylistID=:id")
    LiveData<List<Playlist>> getPlayListWithID(int id);*/

}
