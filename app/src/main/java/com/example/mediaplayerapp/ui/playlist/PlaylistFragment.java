package com.example.mediaplayerapp.ui.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.FragmentPlaylistBinding;

import java.util.List;

public class PlaylistFragment extends Fragment implements View.OnClickListener {
    private FragmentPlaylistBinding binding;
    private PlaylistDetailsFragment detailsFragment=new PlaylistDetailsFragment();
    private PlaylistAdapter adapter;
    private PlaylistViewModel playlistViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        playlistViewModel=new ViewModelProvider(this).get(PlaylistViewModel.class);

        playlistViewModel.getAllPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                makeToast("onChanged");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter=new PlaylistAdapter(new PlaylistAdapter.PlaylistDiff());
        binding.rcvPlaylists.setAdapter(adapter);
        playlistViewModel.getAllPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            // Update the cached copy of the playlist in the adapter.
            adapter.submitList(playlists);
        });

        SharedViewModel viewModel=new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        //List<Playlist> mPlaylists =new ArrayList<>();
        adapter.setListener((v, position) -> {
            viewModel.setSelected(adapter.getPlaylistItemAt(position));
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main,detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.btnDeleteALl.setOnClickListener(this);
        binding.layoutItemAddPlaylist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_deleteALl:
                playlistViewModel.deleteAll();
                break;
            case R.id.layoutItem_addPlaylist:
                addPlaylist();
                break;
        }
    }

    private void makeToast(String mess){
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    private void addPlaylist(){
        Playlist playlist = new Playlist(R.drawable.img_for_test,
                "Name 23",100,true,10,11);
        playlistViewModel.insert(playlist);

        makeToast("ADD Playlist");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
