package com.example.mediaplayerapp.ui.special_playlists;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.special_playlists.MediaQueue;
import com.example.mediaplayerapp.data.special_playlists.MediaQueueViewModel;

public class MediaQueueDeleteDialog extends AppCompatDialogFragment {
    private MediaQueue mMedia;

    public static MediaQueueDeleteDialog newInstance(MediaQueue media) {
        MediaQueueDeleteDialog f = new MediaQueueDeleteDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(PlaylistConstants.KEY_PLAYLIST_QUEUE, media);
        f.setArguments(args);

        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mMedia = (MediaQueue) getArguments().getSerializable(PlaylistConstants.KEY_PLAYLIST_QUEUE);
        }

        MediaQueueViewModel viewModel = new ViewModelProvider(this)
                .get(MediaQueueViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_delete_item_queue, null);

        TextView tv_title = view.findViewById(R.id.tv_title_delete_queue);
        if (mMedia == null) {
            tv_title.setText(PlaylistConstants.STRING_DELETE_ALL_QUEUE);
        } else {
            tv_title.setText(PlaylistConstants.STRING_DELETE_ITEM_QUEUE);
        }

        builder.setView(view)
                .setNegativeButton("cancel", (dialogInterface, i) -> {
                })
                .setPositiveButton("delete", (dialogInterface, i) -> {
                    if (mMedia != null) {
                        viewModel.delete(mMedia);
                        Toast.makeText(getActivity(), "Item deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        viewModel.deleteAll();
                        Toast.makeText(getActivity(), "All items deleted!", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
