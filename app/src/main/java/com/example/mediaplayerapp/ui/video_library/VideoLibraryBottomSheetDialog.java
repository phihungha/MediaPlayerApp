package com.example.mediaplayerapp.ui.video_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.DialogBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VideoLibraryBottomSheetDialog extends BottomSheetDialogFragment {

    private DialogBottomSheetBinding binding;

    public VideoLibraryBottomSheetDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet, container, false);

        return view;
    }
}
