package com.example.mediaplayerapp.ui.playlist;

import com.example.mediaplayerapp.data.playlist.PlaylistItem;

import java.util.List;

public interface OnPlaylistItemListChangedListener {
    void onNoteListChanged(List<PlaylistItem> customers);
}
