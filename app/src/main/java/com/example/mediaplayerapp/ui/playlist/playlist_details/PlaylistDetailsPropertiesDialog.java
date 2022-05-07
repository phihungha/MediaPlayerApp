package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;

public class PlaylistDetailsPropertiesDialog extends AppCompatDialogFragment {
    private MediaInfo mInfo;

    public static PlaylistDetailsPropertiesDialog newInstance(MediaInfo info) {
        PlaylistDetailsPropertiesDialog f = new PlaylistDetailsPropertiesDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(PlaylistConstants.KEY_PLAYLIST_DETAIL,info);
        f.setArguments(args);

        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        mInfo=(MediaInfo) getArguments().getSerializable(PlaylistConstants.KEY_PLAYLIST_DETAIL);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= getActivity().getLayoutInflater().inflate(R.layout.dialog_properties_playlist_playout,null);

        TextView tvName=view.findViewById(R.id.tv_nameProp);
  /*      TextView tvDuration=view.findViewById(R.id.tv_durationProp);
        TextView tvSize=view.findViewById(R.id.tv_fileSizeProp);
        TextView tvLocation=view.findViewById(R.id.tv_locationProp);
        TextView tvDate=view.findViewById(R.id.tv_dateProp);*/

        tvName.setText(mInfo.getFileName());

        builder.setView(view)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                });
        return builder.create();
    }

}
