package com.example.mediaplayerapp.ui.playlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

public class PlaylistDetailsFragment extends Fragment implements View.OnClickListener {

    private Playlist playlist;
    private FragmentPlaylistDetailsBinding binding;
    private PlaylistDetailsAdapter adapter;
    private ArrayList<PlaylistVideoModel> arrayListVideos;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(), item -> {

            /*binding.tvIdPlaylist.setText("Playlist id: " + item.getId() +"\n" +
                    "Name: " + item.getName() + "\n"+
                    "Number: " + item.getNumbers() + "\n"+
                    "Song: " + item.getSongID() + "\n"+
                    "Video: " + item.getVideoID() + "\n"+
                    "IsVideo: " + item.isVideo() + "\n");*/

            playlist = item;
          /*  binding.tvPlaylistDetailsName.setText(item.getName().toString());
            binding.tvPlaylistDetailsNumbers.setText("Play list " + String.valueOf(item.getNumbers()));*/

            init();
        });

       // binding.btnAddMore.setOnClickListener(this);

        arrayListVideos = new ArrayList<>();
        arrayListVideos.add(new PlaylistVideoModel("path","thumb","NAMEEEEE"));

        adapter = new PlaylistDetailsAdapter();
        adapter.setArrayListVideos(arrayListVideos);

        binding.rcvPlaylistsDetails.setAdapter(adapter);
    }

    private void init() {
        binding.tvPlaylistDetailsName.setText(playlist.getName());
        binding.tvPlaylistDetailsNumbers.setText("Play list " + String.valueOf(playlist.getNumbers()));
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            final String orderBy=MediaStore.Video.Media.DATE_TAKEN;

            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, orderBy + " DESC");
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getNameFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            final String orderBy=MediaStore.Video.Media.DATE_TAKEN;

            String[] proj = { MediaStore.Images.Media.DISPLAY_NAME };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, orderBy + " DESC");
            int name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            cursor.moveToFirst();
            return cursor.getString(name);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getThumbFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            final String orderBy=MediaStore.Video.Media.DATE_TAKEN;

            String[] proj = { MediaStore.Images.Thumbnails.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, orderBy + " DESC");
            int thumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
            cursor.moveToFirst();
            return cursor.getString(thumb);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        try {
                            if (data.getClipData() != null) {
                                //pick multiple media file
                                Toast.makeText(getContext(),"Multiple",Toast.LENGTH_SHORT).show();
                               /* int count = data.getClipData().getItemCount();

                                for (int i = 0; i < count; i++) {
                                    Uri uri = data.getClipData().getItemAt(i).getUri();
                                    Cursor cursor=null;
                                    int column_index_data, thumb, name;
                                    String absolutePath = null;

                                    String[] projection = {MediaStore.MediaColumns.DATA,
                                            MediaStore.Video.Media.DISPLAY_NAME,
                                            MediaStore.Video.Media._ID,
                                            MediaStore.Video.Thumbnails.DATA
                                    };
                                    cursor = getActivity().getContentResolver()
                                            .query(uri, projection, null, null, null);
                                    cursor.moveToFirst();

                                    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                                    thumb = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
                                    name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                                    absolutePath = cursor.getString(column_index_data);

                                    PlaylistVideoModel video = new PlaylistVideoModel(absolutePath,
                                            cursor.getString(thumb),
                                            cursor.getString(name));
                                    arrayListVideos.add(video);

                                    cursor.close();
                                }*/
                                //set image

                            }
                            else {
                                //pick single media file
                                //Uri uri=data.getData();
                                //String path=getRealPathFromURI(getContext(),uri);
                                //String thumb=getThumbFromURI(getContext(),uri);

                              /*  String name=getNameFromURI(getContext(),uri);

                                Log.d("TAG name: ", name);

                                PlaylistVideoModel video=new PlaylistVideoModel("path","thumb",name);
                                arrayListVideos.add(video);
                                adapter.setArrayListVideos(arrayListVideos);*/




                                /*Uri uri = data.getData();
                                Cursor cursor;
                                int column_index_data, thumb, name;
                                String absolutePath = null;

                                String[] projection = {MediaStore.MediaColumns.DATA,
                                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                                        MediaStore.Video.Media._ID,
                                        MediaStore.Video.Thumbnails.DATA,
                                        MediaStore.Video.Media.DISPLAY_NAME
                                };
                                cursor = getActivity().getApplicationContext().getContentResolver()
                                        .query(uri, projection, null, null, null);
                                cursor.moveToFirst();

                                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                                thumb = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
                                name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                                absolutePath = cursor.getString(column_index_data);

                                PlaylistVideoModel video = new PlaylistVideoModel(absolutePath,
                                        cursor.getString(thumb),
                                        cursor.getString(name));
                                arrayListVideos.add(video);

                                cursor.close();*/
                            }

                        }
                        finally {
                            //adapter.notifyDataSetChanged();
                        }

                    }
                }
            }
    );

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addMore:
                AddMoreMedia();
                break;
        }

    }

    private void AddMoreMedia() {

    }


   /* private void AddMoreMedia() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickerLauncher.launch(Intent.createChooser(intent, "Select Video(s)"));
        //pickerLauncher.launch(intent);

    }*/

   /* private void fetchVideo() {
        Uri uri;
        Cursor cursor;
        int column_index_data, thumb;
        String absolutePath = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA
        };
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        cursor = getActivity().getApplicationContext().getContentResolver()
                .query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        thumb = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);


        while (cursor.moveToNext()) {
            absolutePath = cursor.getString(column_index_data);

            PlaylistVideoModel video = new PlaylistVideoModel(absolutePath,
                    cursor.getString(thumb),
                    String.valueOf(System.currentTimeMillis()));
            arrayListVideos.add(video);
        }
        adapter.notifyDataSetChanged();

    }*/
}