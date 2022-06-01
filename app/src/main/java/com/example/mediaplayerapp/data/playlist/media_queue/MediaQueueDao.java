package com.example.mediaplayerapp.data.playlist.media_queue;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaQueueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MediaQueue media);

    @Delete
    void delete(MediaQueue media);

    @Query("DELETE FROM mediaQueue_table")
    void deleteAll();

    @Query("DELETE FROM mediaQueue_table WHERE mediaQueue_table.MediaQueueUri=:uri")
    void deleteItemWithUri(String uri);

    @Query("SELECT * FROM mediaQueue_table ORDER BY MediaQueueId ASC")
    LiveData<List<MediaQueue>> getAllMediaQueue();

    @Query("SELECT * FROM mediaQueue_table WHERE Type=:type ORDER BY MediaQueueId ASC")
    LiveData<List<MediaQueue>> getAllQueueWithType(int type);

    @Query("SELECT COUNT(*) FROM mediaQueue_table")
    int getCountMediaQueue();
}
