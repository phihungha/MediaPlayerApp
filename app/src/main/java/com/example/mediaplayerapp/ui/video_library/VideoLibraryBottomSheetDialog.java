package com.example.mediaplayerapp.ui.video_library;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.mediaplayerapp.data.video.Video;
import com.example.mediaplayerapp.databinding.DialogBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VideoLibraryBottomSheetDialog extends BottomSheetDialogFragment {

    private Video currentVideo;
    private DialogBottomSheetBinding binding;
    private VideoLibraryViewModel videoLibraryViewModel;

    public VideoLibraryBottomSheetDialog(Video video) {
        currentVideo = video;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DialogBottomSheetBinding.inflate(inflater, container, false);
        videoLibraryViewModel  = new VideoLibraryViewModel(requireActivity().getApplication());

        TextView textView = binding.videoNameBottomSheetTextview;
        textView.setText(currentVideo.getName());

        LinearLayout bottomSheetOptionInfo = binding.bottomSheetOptionInfo;
        bottomSheetOptionInfo.setOnClickListener(view1 -> {

        });

        LinearLayout bottomSheetOptionAddPlaylist = binding.bottomSheetOptionAddPlaylist;
        bottomSheetOptionAddPlaylist.setOnClickListener(view1 -> {

        });

        LinearLayout bottomSheetOptionRename = binding.bottomSheetOptionRename;
        bottomSheetOptionRename.setOnClickListener(view1 -> {

        });

        LinearLayout bottomSheetOptionShare = binding.bottomSheetOptionShare;
        bottomSheetOptionShare.setOnClickListener(view1 -> {

        });

        LinearLayout bottomSheetOptionDelete = binding.bottomSheetOptionDelete;
        bottomSheetOptionDelete.setOnClickListener(view1 -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

            builder.setTitle("Delete video ?")
                    .setMessage("Are you sure you want to delete this video ?")
                    .setPositiveButton("Delete", (dialogInterface, i) ->
                            videoLibraryViewModel.deleteVideo(currentVideo))
                    .setNegativeButton("Cancel",null)
                    .show();

        });

        return binding.getRoot();
    }
}
