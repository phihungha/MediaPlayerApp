package com.example.mediaplayerapp.ui.playlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mediaplayerapp.R;

import java.util.ArrayList;
import java.util.List;

/*public class PlaylistViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PlaylistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is playlist fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}*/

public class PlaylistViewModel extends ViewModel {

    private MutableLiveData<List<Playlist>> mPlaylistsLiveData;
    private List<Playlist> mPlaylists;

    public PlaylistViewModel() {
        mPlaylistsLiveData = new MutableLiveData<>();
        init();
    }

    private void init(){
        mPlaylists=new ArrayList<>();
     /*   mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"New playlist...",""));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 1","1 video"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 2","2 videos"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 3","3 videos"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 4","4 videos"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 5","5 videos"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 6","6 videos"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 7","7 videos"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 8","8 videos"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 9","9 videos"));
        mPlaylists.add(new Playlist(R.drawable.ic_launcher_background,"Name 10","10 videos"));*/

        mPlaylistsLiveData.setValue(mPlaylists);
    }

    public MutableLiveData<List<Playlist>> getPlaylistsLiveData() {
        return mPlaylistsLiveData;
    }

}
