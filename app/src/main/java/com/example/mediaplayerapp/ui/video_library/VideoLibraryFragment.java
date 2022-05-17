package com.example.mediaplayerapp.ui.video_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.video.Video;
import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;

import java.util.List;


public class VideoLibraryFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "recycler_column_count";
    public static int recyclerViewColumnCount = 2;
    private VideoLibraryViewModel videoLibraryViewModel;
    private FragmentVideoLibraryBinding binding;
    private RecyclerView videoLibraryRecyclerView;
    private VideoLibraryItemAdapter videoLibraryItemAdapter;

    private SortOrder sortOrder = SortOrder.ASC;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_video_library, menu);

        MenuItem searchOption = menu.findItem(R.id.search_menu_item);
        SearchView searchView = (SearchView) searchOption.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                videoLibraryItemAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.grid_list_change_menu_item) {
            if (recyclerViewColumnCount <= 1) {
                recyclerViewColumnCount = 2;
                videoLibraryRecyclerView.setLayoutManager(new GridLayoutManager
                        (binding.getRoot().getContext(), recyclerViewColumnCount));

            } else {
                recyclerViewColumnCount = 1;
                videoLibraryRecyclerView.setLayoutManager(new LinearLayoutManager
                        ((binding.getRoot().getContext())));
            }
            videoLibraryRecyclerView.setAdapter(videoLibraryItemAdapter);
            return true;

        } else if (itemId == R.id.sort_by_name_menu_item) {

            if (sortOrder == SortOrder.ASC) {

                videoLibraryViewModel.getVideosSortByNameDESC().observe(
                        getViewLifecycleOwner(),
                        videos -> videoLibraryItemAdapter.submitList(videos));

                sortOrder = SortOrder.DESC;
            } else {

                videoLibraryViewModel.getVideosSortByNameDESC().observe(
                        getViewLifecycleOwner(),
                        videos -> videoLibraryItemAdapter.submitList(videos));

                sortOrder = SortOrder.ASC;
            }
            return true;

        } else if (itemId == R.id.sort_by_length_menu_item) {
            if (sortOrder == SortOrder.ASC) {

                videoLibraryViewModel.getVideosSortByDurationDESC().observe(
                        getViewLifecycleOwner(),
                        videos -> videoLibraryItemAdapter.submitList(videos));

                sortOrder = SortOrder.DESC;
            } else {

                videoLibraryViewModel.getVideosSortByDurationASC().observe(
                        getViewLifecycleOwner(),
                        videos -> videoLibraryItemAdapter.submitList(videos));

                sortOrder = SortOrder.ASC;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            recyclerViewColumnCount = savedInstanceState.getInt(ARG_COLUMN_COUNT);
        }
        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);
        videoLibraryRecyclerView = binding.videoLibraryRecyclerview;

        if (recyclerViewColumnCount <= 1) {
            videoLibraryRecyclerView.setLayoutManager(new LinearLayoutManager
                    (binding.getRoot().getContext()));
        } else {
            videoLibraryRecyclerView.setLayoutManager(new GridLayoutManager
                    (binding.getRoot().getContext(), recyclerViewColumnCount));
        }

        videoLibraryItemAdapter = new VideoLibraryItemAdapter(
                new VideoLibraryItemAdapter.VideoDiff());
        videoLibraryItemAdapter.setContext(requireActivity());
        videoLibraryRecyclerView.setAdapter(videoLibraryItemAdapter);
        videoLibraryRecyclerView.setHasFixedSize(true);

        videoLibraryViewModel = new ViewModelProvider
                (requireActivity()).get(VideoLibraryViewModel.class);

        videoLibraryViewModel.getAllVideos().observe(
                requireActivity(),
                (Observer<List<Video>>) videos -> videoLibraryItemAdapter.submitList(videos));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_COLUMN_COUNT, recyclerViewColumnCount);
    }

    enum SortOrder {
        ASC,
        DESC
    }
}