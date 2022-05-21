package com.example.mediaplayerapp.ui.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    BottomSheetDialog bottomSheetDialog;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private final ItemPlaylistBinding binding;
    private static IOnItemClickListener bsRenameListener;
    private static IOnItemClickListener bsDeleteListener;
    private static IOnItemClickListener bsPlayListener;
    private static IOnItemClickListener listener;

    public PlaylistViewHolder(@NonNull ItemPlaylistBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.imgBtnMore.setOnClickListener(this);
        this.binding.layoutItemPlaylist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgBtn_more) {
            openBottomSheetDialog();
        } else if (id == R.id.bs_startPlaylist) {
            StartPlaylist();
        } else if (id == R.id.bs_renamePlaylist) {
            RenamePlaylist();
        } else if (id == R.id.bs_deletePlaylist) {
            DeletePlaylist();
        } else if (id == R.id.layoutItem_playlist) {
            OnClickItem();
        }
    }

    /**
     * Open BottomSheet Dialog when click to edit the playlist
     */
    private void openBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(itemView.getContext(), R.style.BottomSheetTheme);
        View bsView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.bottom_sheet_playlist,
                itemView.findViewById(R.id.bs_playlist));

        TextView tv_name = bsView.findViewById(R.id.tv_playlist_name);
        tv_name.setText(binding.tvPlaylistName.getText().toString());

        bsView.findViewById(R.id.bs_startPlaylist).setOnClickListener(this);
        bsView.findViewById(R.id.bs_renamePlaylist).setOnClickListener(this);
        bsView.findViewById(R.id.bs_deletePlaylist).setOnClickListener(this);

        bottomSheetDialog.setContentView(bsView);
        bottomSheetDialog.show();
    }

    private void refreshThumb(Playlist playlist) {
        if (playlist.isVideo()) {
            Glide.with(mContext)
                    .load(playlist.getFirstMediaUri())
                    .skipMemoryCache(false)
                    .error(playlist.getIdResource())
                    .centerCrop()
                    .into(binding.imgThumbnail);
        } else {
            if (playlist.getFirstMediaUri()!=null){
                Bitmap thumb = MediaUtils.loadThumbnail(mContext, Uri.parse(playlist.getFirstMediaUri()));
                if (thumb!=null){
                    binding.imgThumbnail.setImageBitmap(thumb);
                }
                else {
                    binding.imgThumbnail.setImageDrawable(
                            ContextCompat.getDrawable(mContext,
                                    playlist.getIdResource()));
                }
            } else {
                binding.imgThumbnail.setImageDrawable(
                        ContextCompat.getDrawable(mContext,
                                playlist.getIdResource()));
            }
        }
    }

    private String getStringCountText(Playlist playlist){
        int count = playlist.getCount();
        String textNumber = count + " ";

        if (playlist.getId() == 1) {
            if (count <= 1) {
                textNumber += "media";
            } else
                textNumber += "medias";
        } else {
            if (playlist.isVideo()) {
                if (count <= 1) {
                    textNumber += "video";
                } else
                    textNumber += "videos";
            } else {
                if (count <= 1) {
                    textNumber += "song";
                } else
                    textNumber += "songs";
            }
        }
        return textNumber;
    }

    public void setBinding(Playlist playlist) {
        if (playlist.getFirstMediaUri()==null) {
            binding.imgThumbnail.setImageResource(playlist.getIdResource());
        } else {
            refreshThumb(playlist);
        }

        binding.tvPlaylistName.setText(playlist.getName());
        binding.tvPlaylistNumbers.setText(getStringCountText(playlist));
    }

    static PlaylistViewHolder create(ViewGroup parent, IOnItemClickListener l,
                                     IOnItemClickListener _bsRenameListener,
                                     IOnItemClickListener _bsDeleteListener,
                                     IOnItemClickListener _bsPlayListener,
                                     Context _context
    ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(inflater, parent, false);
        listener = l;
        bsRenameListener = _bsRenameListener;
        bsDeleteListener = _bsDeleteListener;
        bsPlayListener = _bsPlayListener;
        mContext = _context;
        return new PlaylistViewHolder(binding);
    }

    private void StartPlaylist() {
        bsPlayListener.onClick(itemView, getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void RenamePlaylist() {
        bsRenameListener.onClick(itemView, getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void DeletePlaylist() {
        bsDeleteListener.onClick(itemView, getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void OnClickItem() {
        if (listener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
            listener.onClick(itemView.getRootView(), getBindingAdapterPosition());
        }
    }

}
