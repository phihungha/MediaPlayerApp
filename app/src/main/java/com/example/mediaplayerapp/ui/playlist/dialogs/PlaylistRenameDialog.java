package com.example.mediaplayerapp.ui.playlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.ui.playlist.PlaylistViewModel;

public class PlaylistRenameDialog extends AppCompatDialogFragment {

    private static final String PLAYLIST_ID_KEY = "PlaylistId";
    private static final String PLAYLIST_CURRENT_NAME_KEY = "PlaylistCurrentName";

    public static PlaylistRenameDialog newInstance(int playlistId, String currentName) {
        PlaylistRenameDialog dialog = new PlaylistRenameDialog();

        Bundle args = new Bundle();
        args.putInt(PLAYLIST_ID_KEY, playlistId);
        args.putString(PLAYLIST_CURRENT_NAME_KEY, currentName);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int playlistId = requireArguments().getInt(PLAYLIST_ID_KEY);
        String playlistCurrentName = requireArguments().getString(PLAYLIST_CURRENT_NAME_KEY);
        PlaylistViewModel viewModel =
                new ViewModelProvider(this).get(PlaylistViewModel.class);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        View view= requireActivity().getLayoutInflater().inflate(R.layout.dialog_playlist_rename,null);

        EditText nameEditText = view.findViewById(R.id.playlist_rename_dialog_new_name);
        nameEditText.setText(playlistCurrentName);

        builder.setView(view)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {})
                .setPositiveButton(R.string.rename, (dialogInterface, i) -> {
                    String name= nameEditText.getText().toString().trim();
                    if (!name.isEmpty())
                        viewModel.updateName(playlistId,
                                             nameEditText.getText().toString())
                                .subscribe();
                });

        return builder.create();
    }
}
