package com.example.mediaplayerapp.ui.setting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mediaplayerapp.MainActivity;
import com.example.mediaplayerapp.databinding.FragmentSettingBinding;
import com.example.mediaplayerapp.utils.SharedPrefs;


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
        LinearLayout eng = fragmentSettingBinding.english;
        SharedPrefs sharedPreferences = new SharedPrefs(getActivity());
        eng.setOnClickListener(view -> {
            sharedPreferences.setLocale("en");
            getActivity().recreate();
        });
        LinearLayout vi = fragmentSettingBinding.vietnamese;
        vi.setOnClickListener(view -> {
            sharedPreferences.setLocale("vi");
            getActivity().recreate();
        });
        return fragmentSettingBinding.getRoot();
    }

}