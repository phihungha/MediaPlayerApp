package com.example.mediaplayerapp.ui.setting;

import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.mediaplayerapp.databinding.FragmentSettingBinding;



public class SettingFragment extends Fragment {




    public SettingFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSettingBinding fragmentSettingBinding = FragmentSettingBinding.inflate(inflater,container,false);
        LinearLayout changelanguage = fragmentSettingBinding.changelanguage;
        changelanguage.setOnClickListener(view -> {
            ChangeLanguage changeLanguageBottomSheet = new ChangeLanguage();
            changeLanguageBottomSheet.show(getActivity().getSupportFragmentManager(),
                    changeLanguageBottomSheet.getTag());
        });
        return fragmentSettingBinding.getRoot();
    }

}