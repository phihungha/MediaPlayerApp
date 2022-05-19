package com.example.mediaplayerapp.ui.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    BottomSheetDialog bottomSheetDialog;
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

    public void setBinding(Playlist playlist, String textCount) {
        binding.imgThumbnail.setImageResource(playlist.getIdResource());
        binding.tvPlaylistName.setText(playlist.getName());
        binding.tvPlaylistNumbers.setText(textCount);

        if (playlist.getId() == 1){
            binding.imgBtnMore.setVisibility(View.GONE);
        }
    }

    static PlaylistViewHolder create(ViewGroup parent, IOnItemClickListener l,
                                     IOnItemClickListener _bsRenameListener,
                                     IOnItemClickListener _bsDeleteListener,
                                     IOnItemClickListener _bsPlayListener
                                        ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(inflater, parent, false);
        listener = l;
        bsRenameListener = _bsRenameListener;
        bsDeleteListener = _bsDeleteListener;
        bsPlayListener=_bsPlayListener;
        return new PlaylistViewHolder(binding);
    }

    private void StartPlaylist() {
        bsPlayListener.onClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void RenamePlaylist() {
        bsRenameListener.onClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void DeletePlaylist() {
        bsDeleteListener.onClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void OnClickItem() {
        if (listener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
            listener.onClick(itemView.getRootView(), getBindingAdapterPosition());
        }
    }

}
