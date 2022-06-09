package com.example.mediaplayerapp.ui.playlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.databinding.BottomSheetPlaylistBinding;
import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.ui.playlist.dialogs.PlaylistDeleteDialog;
import com.example.mediaplayerapp.ui.playlist.dialogs.PlaylistRenameDialog;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PlaylistAdapter extends ListAdapter<Playlist, PlaylistAdapter.ViewHolder> {

    private final FragmentManager fragmentManager;
    private final Context context;
    private final PlaylistItemViewModel playlistItemViewModel;

    protected PlaylistAdapter(Fragment fragment, PlaylistItemViewModel playlistItemViewModel) {
        super(new PlaylistAdapter.PlaylistDiff());
        this.context = fragment.getContext();
        this.fragmentManager = fragment.getParentFragmentManager();
        this.playlistItemViewModel = playlistItemViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPlaylistBinding.inflate(
                                LayoutInflater.from(context),
                                parent,
                    false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist current = getItem(position);
        holder.setPlaylist(current);
    }

    static class PlaylistDiff extends DiffUtil.ItemCallback<Playlist> {
        @Override
        public boolean areItemsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Playlist playlist;
        private final BottomSheetDialog bottomSheetDialog;
        private final ItemPlaylistBinding binding;
        private final BottomSheetPlaylistBinding bottomSheetBinding;

        public ViewHolder(@NonNull ItemPlaylistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            bottomSheetDialog = new BottomSheetDialog(itemView.getContext(), R.style.BottomSheetTheme);
            bottomSheetBinding = BottomSheetPlaylistBinding.inflate(
                                    LayoutInflater.from(context),
                                    (ViewGroup)itemView,
                                    false);
            bottomSheetBinding.playlistBottomSheetPlayAll
                    .setOnClickListener(view -> {
                        playAll();
                        bottomSheetDialog.dismiss();
                    });
            bottomSheetBinding.playlistBottomSheetRename
                    .setOnClickListener(view -> {
                        rename();
                        bottomSheetDialog.dismiss();
                    });
            bottomSheetBinding.playlistBottomSheetDelete
                    .setOnClickListener(view -> {
                        delete();
                        bottomSheetDialog.dismiss();
                    });
            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());

            binding.moreBtn.setOnClickListener(view -> bottomSheetDialog.show());
            binding.getRoot().setOnClickListener(view -> openDetails());
        }

        public void setPlaylist(Playlist playlist) {
            this.playlist = playlist;
            binding.playlistName.setText(playlist.getName());
            bottomSheetBinding.playlistBottomSheetName.setText(playlist.getName());
            setThumbnail();
            setItemCount();
        }

        private void setThumbnail() {
            PlaylistItem playlistItem = playlistItemViewModel.findByItemId(playlist.getId());

            if (playlistItem == null)
                return;

            Bitmap thumbnailBitmap = MediaMetadataUtils.getThumbnail(
                    context,
                    playlistItem.getAndroidMediaUri(),
                    R.drawable.ic_playlist_24dp);
            binding.playlistThumbnail.setImageBitmap(thumbnailBitmap);
        }

        private void setItemCount() {
            String text = playlist.getItemCount() + " ";
            if (playlist.isVideo())
                text += context.getString(R.string.video_count_postfix);
            else
                text += context.getString(R.string.song_count_postfix);
            binding.playlistItemCount.setText(text);
        }

        private void playAll() {
            Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlist.getId(), 0);
            if (playlist.isVideo())
                VideoPlayerActivity.launchWithUri(context, playbackUri);
            else
                MusicPlayerActivity.launchWithUri(context, playbackUri);
        }

        private void rename() {
            PlaylistRenameDialog dialog = PlaylistRenameDialog.newInstance(playlist.getId(), playlist.getName());
            dialog.show(fragmentManager, null);
        }

        private void delete() {
            PlaylistDeleteDialog dialog = PlaylistDeleteDialog.newInstance(playlist.getId());
            dialog.show(fragmentManager, null);
        }

        private void openDetails() {
            Navigation.findNavController(binding.getRoot()).navigate(
                    PlaylistFragmentDirections
                            .actionNavigationPlaylistToNavigationPlaylistDetails(
                                    playlist.getId())
            );
        }
    }
}
