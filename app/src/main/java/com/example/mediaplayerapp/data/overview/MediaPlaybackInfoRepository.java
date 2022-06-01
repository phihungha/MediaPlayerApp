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

    public void updateRelevantInfo(String mediaUri, long lastPlaybackTime, long lastPlaybackPosition) {
        MediaPlaybackInfoRoomDatabase.databaseWriteExecutor.execute(
                () -> mediaPlaybackInfoDao.updateRelevantInfo(
                        mediaUri, lastPlaybackTime, lastPlaybackPosition));
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
                        updateRelevantInfo(
                                mediaPlaybackInfo.getMediaUri(),
                                mediaPlaybackInfo.getLastPlaybackTime(),
                                mediaPlaybackInfo.getLastPlaybackPosition());
                }
        );
    }

    public LiveData<List<MediaPlaybackInfo>> getRecentVideos(int mediaShownCount) {
        return mediaPlaybackInfoDao.getRecentVideos(mediaShownCount);
    }

    public LiveData<List<MediaPlaybackInfo>> getMostWatchedVideos(int mediaShownCount) {
        return mediaPlaybackInfoDao.getMostWatchedVideos(mediaShownCount);
    }

    public LiveData<List<MediaPlaybackInfo>> getRecentSongs(int mediaShownCount) {
        return mediaPlaybackInfoDao.getRecentSongs(mediaShownCount);
    }

    public LiveData<List<MediaPlaybackInfo>> getMostListenedSongs(int mediaShownCount) {
        return mediaPlaybackInfoDao.getMostListenedSongs(mediaShownCount);
    }
    public LiveData<List<MediaPlaybackInfo>> getAllMediaPlaybackInfo() {
        return allMediaPlaybackInfo;
    }
}
