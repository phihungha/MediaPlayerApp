package com.example.mediaplayerapp.ui.playlist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.FragmentPlaylistDetailsBinding;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class PlaylistDetailsFragment extends Fragment implements View.OnClickListener {

    private Playlist playlist;
    private FragmentPlaylistDetailsBinding binding;
    private ArrayList<Uri> uri=new ArrayList<>();
    private PlaylistDetailsAdapter adapter;


    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentPlaylistDetailsBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel viewModel=new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(),item -> {

            /*binding.tvIdPlaylist.setText("Playlist id: " + item.getId() +"\n" +
                    "Name: " + item.getName() + "\n"+
                    "Number: " + item.getNumbers() + "\n"+
                    "Song: " + item.getSongID() + "\n"+
                    "Video: " + item.getVideoID() + "\n"+
                    "IsVideo: " + item.isVideo() + "\n");*/

            playlist=item;
          /*  binding.tvPlaylistDetailsName.setText(item.getName().toString());
            binding.tvPlaylistDetailsNumbers.setText("Play list " + String.valueOf(item.getNumbers()));*/

            init();
        });

        binding.btnAddMore.setOnClickListener(this);

        adapter=new PlaylistDetailsAdapter(uri);
        binding.rcvPlaylistsDetails.setAdapter(adapter);

    }

    private void init(){
        binding.tvPlaylistDetailsName.setText(playlist.getName().toString());
        binding.tvPlaylistDetailsNumbers.setText("Play list " + String.valueOf(playlist.getNumbers()));
    }

    ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent data=result.getData();
                        if (data.getClipData()!=null){

                            //pick multiple media file
                            int count=data.getClipData().getItemCount();

                            for(int i=0;i<count;i++){
                                Uri imgUri=data.getClipData().getItemAt(i).getUri();
                                uri.add(imgUri);
                                adapter.notifyDataSetChanged();
                            }
                            //set image
                        }
                        else {
                            //pick single media file
                            Uri imgUri=data.getData();
                            uri.add(imgUri);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }
    );

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addMore:
                AddMoreMedia();
                break;
        }

    }

    private void AddMoreMedia() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        pickerLauncher.launch(Intent.createChooser(intent,"Select Video(s)"));
    }
}