package com.example.mediaplayerapp.ui.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;

import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    BottomSheetDialog bottomSheetDialog;
    private ItemPlaylistBinding binding;
    private static IOnBottomSheetSelectionClick bsRenameListener;
    private static IOnBottomSheetSelectionClick bsDeleteListener;
    private static IOnBottomSheetSelectionClick bsPlayListener;
    private static IOnPlaylistItemClickListener listener;


    public PlaylistViewHolder(@NonNull ItemPlaylistBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.imgBtnMore.setOnClickListener(this);
        this.binding.layoutItemPlaylist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_more:
                openBottomSheetDialog();
                break;
            case R.id.bs_startPlaylist:
                StartPlaylist();
                break;
            case R.id.bs_renamePlaylist:
                RenamePlaylist();
                break;
            case R.id.bs_deletePlaylist:
                DeletePlaylist();
                break;
            case R.id.layoutItem_playlist:
                OnClickItem();
                break;

        }
    }

    /**
     * Open BottomSheet Dialog when click to edit the playlist
     */
    private void openBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(itemView.getContext(), R.style.BottomSheetTheme);
        View bsView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.playlist_bs_layout,
                itemView.findViewById(R.id.bs_playlist));

        TextView tv_name = bsView.findViewById(R.id.tv_playlist_name);
        tv_name.setText(binding.tvPlaylistName.getText().toString());

        bsView.findViewById(R.id.bs_startPlaylist).setOnClickListener(this);
        bsView.findViewById(R.id.bs_renamePlaylist).setOnClickListener(this);
        bsView.findViewById(R.id.bs_deletePlaylist).setOnClickListener(this);

        bottomSheetDialog.setContentView(bsView);
        bottomSheetDialog.show();
    }

    public void setBinding(int idResource, String name) {
        binding.imgThumbnail.setImageResource(idResource);
        binding.tvPlaylistName.setText(name);
    }

    static PlaylistViewHolder create(ViewGroup parent, IOnPlaylistItemClickListener l,
                                     IOnBottomSheetSelectionClick _bsRenameListener,
                                     IOnBottomSheetSelectionClick _bsDeleteListener,
                                     IOnBottomSheetSelectionClick _bsPlayListener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(inflater, parent, false);
        listener = l;
        bsRenameListener = _bsRenameListener;
        bsDeleteListener = _bsDeleteListener;
        bsPlayListener=_bsPlayListener;
        return new PlaylistViewHolder(binding);
    }

    private void StartPlaylist() {
        bsPlayListener.onItemBSClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void RenamePlaylist() {
        bsRenameListener.onItemBSClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void DeletePlaylist() {
        bsDeleteListener.onItemBSClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void OnClickItem() {
        if (listener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
            listener.onClick(itemView.getRootView(), getBindingAdapterPosition());
        }
    }

}
