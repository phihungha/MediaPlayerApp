package com.example.mediaplayerapp.ui.playlist.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.ui.playlist.PlaylistViewModel;

public class PlaylistDeleteDialog extends AppCompatDialogFragment {

    private static final String PLAYLIST_ID_KEY = "PlaylistId";

    public static PlaylistDeleteDialog newInstance(int playlistId) {
        PlaylistDeleteDialog dialog = new PlaylistDeleteDialog();

        Bundle args = new Bundle();
        args.putInt(PLAYLIST_ID_KEY, playlistId);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int playlistId = requireArguments().getInt(PLAYLIST_ID_KEY);
        PlaylistViewModel viewModel =
                new ViewModelProvider(this).get(PlaylistViewModel.class);

        Activity activity = requireActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View dialogContent = activity
                .getLayoutInflater()
                .inflate(R.layout.dialog_playlist_delete,null);

        builder.setView(dialogContent)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {})
                .setPositiveButton(R.string.delete,
                        (dialogInterface, i) -> viewModel.delete(playlistId).subscribe()
                );

        return builder.create();
    }
}
