package com.example.mediaplayerapp.ui.playlist;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.databinding.BottomSheetPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.ui.playlist.dialogs.PlaylistDetailsPropertiesDialog;
import com.example.mediaplayerapp.ui.special_playlists.MediaQueueUtil;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.MessageUtils;
import com.example.mediaplayerapp.utils.item_touch_helper.ItemTouchHelperAdapter;
import com.example.mediaplayerapp.utils.item_touch_helper.ItemTouchHelperViewHolder;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class PlaylistItemAdapter
        extends ListAdapter<PlaylistItem, PlaylistItemAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String LOG_TAG = PlaylistItemAdapter.class.getSimpleName();
    private final Application application;
    private final Context context;
    private final Fragment fragment;
    private final CompositeDisposable disposables;

    private final PlaylistItemViewModel viewModel;

    private final boolean isVideo;
    private final ArrayList<Integer> itemPositions = new ArrayList<>();

    private DisplayMode displayMode = DisplayMode.LIST;

    public PlaylistItemAdapter(Fragment fragment,
                               PlaylistItemViewModel viewModel,
                               CompositeDisposable disposables,
                               boolean isVideo) {
        super(new PlaylistItemAdapter.PlaylistMediaDiff());
        this.fragment = fragment;
        this.disposables = disposables;
        this.context = fragment.requireContext();
        this.application = fragment.requireActivity().getApplication();
        this.viewModel = viewModel;
        this.isVideo = isVideo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (displayMode == DisplayMode.GRID) {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.item_playlist_item_grid,
                    parent,
                    false);
        } else {
            itemView = LayoutInflater.from(context).inflate(
                    R.layout.item_playlist_item_list,
                    parent,
                    false);
        }
        return new PlaylistItemAdapter.ViewHolder(itemView);
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getItemViewType(int position) {
        return displayMode.getValue();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setPlaylistItem(getItem(position));
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        itemPositions.add(fromPosition);
        itemPositions.add(toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void swapItem() {
        int fromPosition = getFirstPos();
        int toPosition = getSecondPos();

        ArrayList<PlaylistItem> items = new ArrayList<>(getCurrentList());

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else if (fromPosition > toPosition) {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }

        for (int i = 0; i < items.size(); i++) {
            PlaylistItem item = items.get(i);
            item.setOrderIndex(i);
        }

        Disposable disposable = viewModel.updatePlaylistItems(items)
                .subscribe(
                        () -> {},
                        e -> MessageUtils.displayError(
                                context,
                                LOG_TAG,
                                e.getMessage())
                );
        disposables.add(disposable);
    }

    public int getListPosSize() {
        return itemPositions.size();
    }

    public int getFirstPos() {
        return itemPositions.get(0);
    }

    public void clearList() {
        itemPositions.clear();
    }

    public int getSecondPos() {
        return itemPositions.get(itemPositions.size() - 1);
    }

    @Override
    public void onItemDismiss(int position) {
        viewModel.deletePlaylistItem(getItem(position));
    }

    @Override
    public void submitList(@Nullable List<PlaylistItem> list) {
        super.submitList(list);
    }

    public static class PlaylistMediaDiff extends DiffUtil.ItemCallback<PlaylistItem> {
        @Override
        public boolean areItemsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {

        private PlaylistItem playlistItem;
        private String title;
        private int duration;
        private String location;

        private final TextView itemTitle;
        private final TextView itemDuration;
        private final ImageView itemThumbnail;

        private final BottomSheetDialog bottomSheetDialog;
        private final BottomSheetPlaylistDetailsBinding bottomSheetBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.playlist_details_item_title);
            itemDuration = itemView.findViewById(R.id.playlist_details_item_duration);
            itemThumbnail = itemView.findViewById(R.id.playlist_details_item_thumbnail);
            itemView.setOnClickListener(view -> play());

            bottomSheetDialog = new BottomSheetDialog(itemView.getContext(), R.style.BottomSheetTheme);
            bottomSheetBinding = BottomSheetPlaylistDetailsBinding.inflate(
                    LayoutInflater.from(context),
                    (ViewGroup)itemView,
                    false);
            bottomSheetBinding.playlistDetailsBottomSheetPlay
                    .setOnClickListener(view -> {
                        play();
                        bottomSheetDialog.dismiss();
                    });
            bottomSheetBinding.playlistDetailsBottomSheetFavorite
                    .setOnClickListener(view -> {
                        addToFavourite();
                        bottomSheetDialog.dismiss();
                    });
            bottomSheetBinding.playlistDetailsBottomSheetWatchLater
                    .setOnClickListener(view -> {
                        addToWatchLater();
                        bottomSheetDialog.dismiss();
                    });
            bottomSheetBinding.playlistDetailsBottomSheetRemove
                    .setOnClickListener(view -> {
                        deleteItem();
                        bottomSheetDialog.dismiss();
                    });
            bottomSheetBinding.playlistDetailsBottomSheetProperties
                    .setOnClickListener(view -> {
                        showProperties();
                        bottomSheetDialog.dismiss();
                    });
            bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
            itemView.findViewById(R.id.playlist_details_item_more_btn)
                    .setOnClickListener(view -> bottomSheetDialog.show());
        }

        public void setPlaylistItem(PlaylistItem playlistItem) {
            this.playlistItem = playlistItem;

            if (isVideo)
                viewModel.getVideoMetadata(playlistItem.getAndroidMediaUri())
                        .observe(fragment.getViewLifecycleOwner(),
                                 videoMetadata -> {
                                     title = videoMetadata.getTitle();
                                     duration = videoMetadata.getDuration();
                                     location = videoMetadata.getLocation();
                                     setMediaMetadata();
                        });
            else
                viewModel.getSongMetadata(playlistItem.getAndroidMediaUri())
                        .observe(fragment.getViewLifecycleOwner(),
                                 songMetadata -> {
                                    title = songMetadata.getTitle();
                                    duration = songMetadata.getDuration();
                                    location = songMetadata.getLocation();
                                    setMediaMetadata();
                                });

            setItemThumbnail();
        }

        private void setMediaMetadata() {
            bottomSheetBinding.playlistDetailsBottomSheetName.setText(title);
            itemTitle.setText(title);
            itemDuration.setText(MediaTimeUtils.getFormattedTimeFromLong(duration));
        }

        private void setItemThumbnail() {
            Drawable thumbnail = MediaMetadataUtils.getThumbnail(
                    context,
                    playlistItem.getAndroidMediaUri(),
                    R.drawable.ic_playlist_24dp);
            itemThumbnail.setImageDrawable(thumbnail);
        }

        private void addToFavourite() {
            if (isVideo)
                MediaQueueUtil.insertVideoToFavourite(application, playlistItem.getMediaUri());
            else
                MediaQueueUtil.insertSongToFavourite(application, playlistItem.getMediaUri());
            Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
        }

        private void addToWatchLater() {
            if (isVideo)
                MediaQueueUtil.insertVideoToWatchLater(application, playlistItem.getMediaUri());
            else
                MediaQueueUtil.insertSongToWatchLater(application, playlistItem.getMediaUri());
            Toast.makeText(context, R.string.added_to_watch_later, Toast.LENGTH_SHORT).show();
        }

        private void showProperties() {
            PlaylistDetailsPropertiesDialog dialog
                    = PlaylistDetailsPropertiesDialog.newInstance(title, duration, location);
            dialog.show(fragment.getParentFragmentManager(), null);
        }

        private void deleteItem() {
            Disposable disposable = viewModel.deletePlaylistItem(playlistItem)
                    .subscribe(
                            () -> {},
                            e -> MessageUtils.displayError(
                                    context,
                                    LOG_TAG,
                                    e.getMessage())
                    );
            disposables.add(disposable);
        }

        private void play() {
            Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlistItem.getId(), playlistItem.getOrderIndex());
            if (isVideo) {
                VideoPlayerActivity.launchWithUri(context, playbackUri);
            } else {
                MusicPlayerActivity.launchWithUri(context, playbackUri);
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundResource(R.color.bright_grey);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);

            if (getListPosSize() < 2)
                return;
            swapItem();
            clearList();
        }
    }
}
