package com.example.mediaplayerapp.ui.music_library;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MusicLibraryViewModel extends ViewModel {
    private final MutableLiveData<String> text;


    public MusicLibraryViewModel() {
        text = new MutableLiveData<>();
        text.setValue("This is music library fragment");

    }

    public LiveData<String> getText() {
        return text;
    }
}