package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;

public class PlaylistDetailsDeleteDialog extends AppCompatDialogFragment {

    private PlaylistItem mMedia;
    private PlaylistDetailsFragment playlistDetailsFragment;
    private Playlist playlist;

    public void setPlaylistDetailsFragment(PlaylistDetailsFragment playlistDetailsFragment) {
        this.playlistDetailsFragment = playlistDetailsFragment;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public static PlaylistDetailsDeleteDialog newInstance(PlaylistItem media) {
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
        mMedia=(PlaylistItem) getArguments().getSerializable(PlaylistConstants.KEY_PLAYLIST_DETAIL);
        PlaylistItemViewModel viewModel= new ViewModelProvider(this)
                .get(PlaylistItemViewModel.class);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= requireActivity().getLayoutInflater().inflate(R.layout.dialog_delete_playlist_layout,null);

        builder.setView(view)
                .setNegativeButton("cancel", (dialogInterface, i) -> {
                })
                .setPositiveButton("delete", (dialogInterface, i) -> {
                    viewModel.delete(mMedia);
                    Toast.makeText(getActivity(), "Item deleted!", Toast.LENGTH_SHORT).show();

                    playlist.setCount(playlist.getCount()-1);
                    if (playlist.getCount()==0)
                        playlist.setFirstMediaUri(null);

                    PlaylistViewModel playlistViewModel=new ViewModelProvider(this).get(PlaylistViewModel.class);
                    playlistViewModel.update(playlist);
                    playlistDetailsFragment.refresh(playlist);
                });

        return builder.create();
    }
}
