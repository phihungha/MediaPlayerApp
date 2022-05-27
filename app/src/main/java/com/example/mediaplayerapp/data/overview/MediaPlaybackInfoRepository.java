package com.example.mediaplayerapp.data.overview;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MediaPlaybackInfoRepository {
    private final MediaPlaybackInfoDao mediaPlaybackInfoDao;
    private final LiveData<List<MediaPlaybackInfo>> allMediaPlaybackInfo;

    public MediaPlaybackInfoRepository(Application application) {
        MediaPlaybackInfoRoomDatabase database
                = MediaPlaybackInfoRoomDatabase.getDatabase(application);
        mediaPlaybackInfoDao = database.mediaPlaybackInfoDao();
        allMediaPlaybackInfo = mediaPlaybackInfoDao.getAllMediaPlaybackInfo();
    }

    public void insert(MediaPlaybackInfo mediaPlaybackInfo) {
        MediaPlaybackInfoRoomDatabase.databaseWriteExecutor.execute(
                () -> mediaPlaybackInfoDao.insert(mediaPlaybackInfo));
    }

    public void update(MediaPlaybackInfo mediaPlaybackInfo) {
        MediaPlaybackInfoRoomDatabase.databaseWriteExecutor.execute(
                () -> mediaPlaybackInfoDao.update(mediaPlaybackInfo));
    }

    public void updatePlaybackAmount(MediaPlaybackInfo mediaPlaybackInfo) {
        MediaPlaybackInfoRoomDatabase.databaseWriteExecutor.execute(
                () -> mediaPlaybackInfoDao.updatePlaybackAmount(mediaPlaybackInfo.getId()));
    }

    public void insertOrUpdate(MediaPlaybackInfo mediaPlaybackInfo) {

        MediaPlaybackInfoRoomDatabase.databaseWriteExecutor.execute(
                () ->
                {
                    MediaPlaybackInfo mediaPlaybackInfoFromRepo = mediaPlaybackInfoDao
                            .getMediaPlayBackInfoByUri(mediaPlaybackInfo.getMediaUri());
                    if (mediaPlaybackInfoFromRepo == null)
                        insert(mediaPlaybackInfo);
                    else
                        update(mediaPlaybackInfo);
                }
        );
    }

    public LiveData<List<MediaPlaybackInfo>> get5RecentVideos() {
        return mediaPlaybackInfoDao.get5RecentVideos();
    }

    public LiveData<List<MediaPlaybackInfo>> getAllMediaPlaybackInfo() {
        return allMediaPlaybackInfo;
    }
}
