package com.example.mediaplayerapp.ui.setting;

import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.mediaplayerapp.databinding.FragmentChangeLanguageBinding;
import com.example.mediaplayerapp.utils.SharedPrefs;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class ChangeLanguage  extends BottomSheetDialogFragment {

    SharedPrefs sharedPreferences = new SharedPrefs(getActivity());
    public ChangeLanguage() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentChangeLanguageBinding fragmentChangeLanguageBinding =  FragmentChangeLanguageBinding.inflate(inflater,container,false);
        LinearLayout english = fragmentChangeLanguageBinding.bottomSheetEnglish;
        LinearLayout vietnam = fragmentChangeLanguageBinding.bottomSheetVietnam;
        english.setOnClickListener(view -> {
            sharedPreferences.setLocale("en");
            getActivity().recreate();
        });
        vietnam.setOnClickListener(view -> {
            sharedPreferences.setLocale("vi");
            getActivity().recreate();
        });

        return fragmentChangeLanguageBinding.getRoot();
    }
}