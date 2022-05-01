package com.example.mediaplayerapp.ui.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.FragmentPlaylistBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class PlaylistFragment extends Fragment implements View.OnClickListener {
    private FragmentPlaylistBinding binding;
    private PlaylistDetailsFragment detailsFragment=new PlaylistDetailsFragment();
    private PlaylistAdapter adapter;
    private PlaylistViewModel playlistViewModel;
    BottomSheetDialog bottomSheetDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        playlistViewModel=new ViewModelProvider(this).get(PlaylistViewModel.class);

        playlistViewModel.getAllPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                //makeToast("onChanged");
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

        //set click item listener for recyclerview
        adapter.setListener((v, position) -> {
            viewModel.setSelected(adapter.getPlaylistItemAt(position));
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main,detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
        //click bottom sheet rename item recyclerview
        adapter.setBSRenameListener((position)->{
            Playlist playlist=adapter.getPlaylistItemAt(position);
            PlaylistRenameDialog dialog= PlaylistRenameDialog.newInstance(playlist);
            dialog.show(getParentFragmentManager(),PlaylistConstants.TAG_BS_RENAME_DIALOG);
        });
        //click bottom sheet delete item recyclerview
        adapter.setBSDeleteListener((position -> {
            Playlist playlist=adapter.getPlaylistItemAt(position);
            PlaylistDeleteDialog dialog= PlaylistDeleteDialog.newInstance(playlist);
            dialog.show(getParentFragmentManager(),PlaylistConstants.TAG_BS_DELETE_DIALOG);
        }));

        binding.layoutItemAddPlaylist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutItem_addPlaylist:
                openBottomSheetDialogAddPlaylist();
                break;
        }
    }

    private void makeToast(String mess){
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /** Open BottomSheet Dialog Add playlist
     * */
    private void openBottomSheetDialogAddPlaylist(){
        bottomSheetDialog=new BottomSheetDialog(getContext(),R.style.BottomSheetTheme);
        View bsAddView=LayoutInflater.from(getContext()).inflate(R.layout.playlist_create_bs_layout,
                getActivity().findViewById(R.id.bs_playlist_create));

        //set click event here
        EditText edtName = bsAddView.findViewById(R.id.edt_playlistNameCreate);
        Button btnCreate = bsAddView.findViewById(R.id.btn_createPlaylist);
        RadioButton radioAudio = bsAddView.findViewById(R.id.radio_audio);
        RadioButton radioVideo = bsAddView.findViewById(R.id.radio_video);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtName.getText().toString().trim().isEmpty()){
                    makeToast("Name is empty!");
                }
                else if (!radioAudio.isChecked() && !radioVideo.isChecked()){
                    makeToast("Please check type for playlist!");
                } else {
                    Playlist playlist = new Playlist(R.drawable.img_for_test,
                            edtName.getText().toString().trim(),0,radioVideo.isChecked(),null,null);
                    playlistViewModel.insert(playlist);

                    bottomSheetDialog.dismiss();
                    makeToast("Create Playlist Success!");
                }
            }
        });

        bottomSheetDialog.setContentView(bsAddView);
        bottomSheetDialog.show();
    }
}
