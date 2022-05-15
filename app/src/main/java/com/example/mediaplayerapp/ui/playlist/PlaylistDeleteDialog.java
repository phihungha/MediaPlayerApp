package com.example.mediaplayerapp.ui.playlist;

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
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.data.playlist.playlist_details.MediaItemViewModel;

public class PlaylistDeleteDialog extends AppCompatDialogFragment {
    private Playlist playlist;
    public static PlaylistDeleteDialog newInstance(Playlist playlist) {
        PlaylistDeleteDialog f = new PlaylistDeleteDialog();

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
                        MediaItemViewModel mediaItemViewModel =new ViewModelProvider(getActivity())
                                .get(MediaItemViewModel.class);
                        mediaItemViewModel.deleteAllWithID(playlist.getId());

                        viewModel.delete(playlist);
                        Toast.makeText(getActivity(), "Playlist deleted!", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
