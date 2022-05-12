package com.example.mediaplayerapp.data.playlist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.mediaplayerapp.ui.playlist.playlist_details.PlaylistMedia;

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

    @Query("SELECT * FROM playlist_table WHERE playlist_table.Name LIKE '%' || :text || '%'")
    LiveData<List<Playlist>> getAllPlaylistSearching(String text);

    @Query("SELECT * FROM playlist_table ORDER BY playlist_table.Name ASC")
    LiveData<List<Playlist>> sortPlaylistByNameASC();

    @Query("SELECT * FROM playlist_table ORDER BY playlist_table.Name DESC")
    LiveData<List<Playlist>> sortPlaylistByNameDESC();

    @Query("SELECT * from playlist_table WHERE PlaylistID=:id")
    LiveData<List<Playlist>> getPlayListWithID(int id);

}
