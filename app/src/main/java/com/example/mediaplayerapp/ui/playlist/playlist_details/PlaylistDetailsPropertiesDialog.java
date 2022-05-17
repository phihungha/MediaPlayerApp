package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;

public class PlaylistDetailsPropertiesDialog extends AppCompatDialogFragment {

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
        MediaInfo mInfo = (MediaInfo) getArguments().getSerializable(PlaylistConstants.KEY_PLAYLIST_DETAIL);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= requireActivity().getLayoutInflater().inflate(R.layout.dialog_properties_playlist_layout,null);

        TextView tvName=view.findViewById(R.id.tv_nameProp);
        TextView tvDuration=view.findViewById(R.id.tv_durationProp);
        TextView tvSize=view.findViewById(R.id.tv_fileSizeProp);
        TextView tvLocation=view.findViewById(R.id.tv_locationProp);

        String name= mInfo.getFileName();
        String duration= mInfo.getDuration();
        String size= mInfo.getFileSize();
        String location=Uri.parse(mInfo.getLocation()).getPath();

        size=MediaUtils.convertToSizeMb(size);
        duration=MediaUtils.convertDuration(duration);

        tvName.setText(name);
        tvDuration.setText(duration);
        tvSize.setText(size);
        tvLocation.setText(location);

        builder.setView(view)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                });
        return builder.create();
    }

}
