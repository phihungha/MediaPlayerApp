package com.example.mediaplayerapp.utils;

import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;

import java.util.List;

public interface OnPlaylistItemListChangedListener {
    void onNoteListChanged(List<PlaylistItem> customers);
}
