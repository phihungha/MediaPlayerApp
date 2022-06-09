package com.example.mediaplayerapp.ui.playlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.utils.MediaTimeUtils;

public class PlaylistDetailsPropertiesDialog extends AppCompatDialogFragment {

    private static final String MEDIA_TITLE_KEY = "MediaTitle";
    private static final String MEDIA_DURATION_KEY = "MediaDuration";
    private static final String MEDIA_LOCATION_KEY = "MediaLocation";

    public static PlaylistDetailsPropertiesDialog newInstance(String title,
                                                              int duration,
                                                              String location) {
        PlaylistDetailsPropertiesDialog dialog = new PlaylistDetailsPropertiesDialog();

        Bundle args = new Bundle();
        args.putString(MEDIA_TITLE_KEY, title);
        args.putInt(MEDIA_DURATION_KEY, duration);
        args.putString(MEDIA_LOCATION_KEY, location);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = requireArguments().getString(MEDIA_TITLE_KEY);
        long duration = requireArguments().getInt(MEDIA_DURATION_KEY);
        String location = requireArguments().getString(MEDIA_LOCATION_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View dialogContent = requireActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_playlist_item_properties, null);

        TextView tvName = dialogContent.findViewById(R.id.playlist_item_properties_dialog_title);
        TextView tvDuration = dialogContent.findViewById(R.id.playlist_item_properties_dialog_duration);
        TextView tvLocation = dialogContent.findViewById(R.id.playlist_item_properties_dialog_location);

        tvName.setText(title);
        tvDuration.setText(MediaTimeUtils.getFormattedTimeFromLong(duration));
        tvLocation.setText(location);

        builder.setView(dialogContent)
                .setPositiveButton("OK", (dialogInterface, i) -> {});
        return builder.create();
    }
}
