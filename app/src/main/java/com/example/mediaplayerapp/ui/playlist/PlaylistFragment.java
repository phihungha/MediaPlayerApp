package com.example.mediaplayerapp.ui.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.FragmentPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment {
    private FragmentPlaylistBinding binding;
    private PlaylistDetailsFragment detailsFragment=new PlaylistDetailsFragment();
    private PlaylistAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel viewModel=new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Playlist> mPlaylists =new ArrayList<>();
        mPlaylists.add(new Playlist(1,R.drawable.img_for_test,"Name 1","1 video"));
        mPlaylists.add(new Playlist(2,R.drawable.img_for_test,"Name 2","2 videos"));
        mPlaylists.add(new Playlist(3,R.drawable.img_for_test,"Name 3","3 videos"));
        mPlaylists.add(new Playlist(4,R.drawable.img_for_test,"Name 4","4 videos"));
        mPlaylists.add(new Playlist(5,R.drawable.img_for_test,"Name 5","5 videos"));
        mPlaylists.add(new Playlist(6,R.drawable.img_for_test,"Name 6","6 videos"));
        mPlaylists.add(new Playlist(7,R.drawable.img_for_test,"Name 7","7 videos"));
        mPlaylists.add(new Playlist(8,R.drawable.img_for_test,"Name 8","8 videos"));
        mPlaylists.add(new Playlist(9,R.drawable.img_for_test,"Name 9","9 videos"));
        mPlaylists.add(new Playlist(10,R.drawable.img_for_test,"Name 10","10 videos"));

        adapter=new PlaylistAdapter(mPlaylists);
        binding.rcvPlaylists.setAdapter(adapter);
        adapter.setListener((v, position) -> {
            viewModel.setSelected(adapter.getPlaylistItemAt(position));
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main,detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.layoutItemAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlaylist();
            }
        });
    }

    private void makeToast(String mess){
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    private void addPlaylist(){
        makeToast("ADD Playlist");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
