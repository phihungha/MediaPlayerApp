package com.example.mediaplayerapp.ui.playlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistViewModel;

public class PlaylistRenameDialog extends AppCompatDialogFragment {
    private EditText edtRename;
    private Playlist playlist;

    public static PlaylistRenameDialog newInstance(Playlist playlist) {
        PlaylistRenameDialog f = new PlaylistRenameDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(PlaylistConstants.KEY_PLAYLIST,playlist);
        f.setArguments(args);

        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        playlist=(Playlist) getArguments().getSerializable(PlaylistConstants.KEY_PLAYLIST);

        PlaylistViewModel viewModel= new ViewModelProvider(this)
                .get(PlaylistViewModel.class);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        View view= requireActivity().getLayoutInflater().inflate(R.layout.dialog_rename_playlist_layout,null);
        edtRename=view.findViewById(R.id.edt_renamePlaylist);
        edtRename.setText(playlist.getName());

        builder.setView(view)
                .setNegativeButton("cancel", (dialogInterface, i) -> {
                })
                .setPositiveButton("rename", (dialogInterface, i) -> {
                    String name=edtRename.getText().toString().trim();
                    if (!name.isEmpty()){
                        playlist.setName(edtRename.getText().toString());
                        viewModel.update(playlist);
                        Toast.makeText(getActivity(), "Playlist updated!", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
