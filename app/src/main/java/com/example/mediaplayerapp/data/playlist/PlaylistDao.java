package com.example.mediaplayerapp.data.playlist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaylistDao {

    @Insert
    Completable insert(Playlist playlist);

    @Update
    Completable update(Playlist playlist);

    @Query("UPDATE Playlists SET Name = :name WHERE PlaylistId = :id")
    Completable updateName(int id, String name);

    @Delete
    Completable delete(Playlist playlist);

    @Query("DELETE FROM Playlists WHERE PlaylistId = :id")
    Completable delete(int id);

    @Query("DELETE FROM Playlists")
    Completable deleteAll();

    @Query("SELECT * FROM Playlists WHERE Name LIKE '%' || :text || '%'")
    Flowable<List<Playlist>> getPlaylistsByNameMatching(String text);

    @Query("SELECT * FROM Playlists ORDER BY Name ASC")
    Flowable<List<Playlist>> getAllPlaylistsSortedByNameAsc();

    @Query("SELECT * FROM Playlists ORDER BY Name DESC")
    Flowable<List<Playlist>> getAllPlaylistsSortedByNameDesc();

    @Query("SELECT * FROM Playlists ORDER BY ItemCount ASC")
    Flowable<List<Playlist>> getAllPlaylistsSortedByItemCountAsc();

    @Query("SELECT * FROM Playlists ORDER BY ItemCount DESC")
    Flowable<List<Playlist>> getAllPlaylistsSortedByItemCountDesc();

    @Query("SELECT * from Playlists WHERE PlaylistId = :id")
    Flowable<List<Playlist>> getPlaylist(int id);
}
