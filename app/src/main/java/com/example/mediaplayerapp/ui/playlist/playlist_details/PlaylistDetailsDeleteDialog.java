package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMedia;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMediaViewModel;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;

public class PlaylistDetailsDeleteDialog extends AppCompatDialogFragment {

    private PlaylistMedia mMedia;

    public static PlaylistDetailsDeleteDialog newInstance(PlaylistMedia media) {
        PlaylistDetailsDeleteDialog f = new PlaylistDetailsDeleteDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(PlaylistConstants.KEY_PLAYLIST_DETAIL,media);
        f.setArguments(args);

        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        mMedia=(PlaylistMedia) getArguments().getSerializable(PlaylistConstants.KEY_PLAYLIST_DETAIL);
        PlaylistMediaViewModel viewModel= new ViewModelProvider(this)
                .get(PlaylistMediaViewModel.class);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_playlist_layout,null);

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.delete(mMedia);
                        Toast.makeText(getActivity(), "Item deleted!", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
