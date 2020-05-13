package com.example.ahmedd.firabasetest.Fragments.MainFragments.ProfileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.PostsAdapter;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.Helpers.ViewHelpers.SpacesItemDecoration;
import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentUserImagesFragment extends Fragment {
    private static final String TAG = "CurrentUserImagesFragme";


    private MainActivityViewModel viewModel;
    private View view;
    private RecyclerView recyclerView;

    private PostsAdapter adapter;

    public CurrentUserImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_user_images, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //To initialize views
        initViews();

        //To observe images from the liveData
        observeImagesFromLiveData();


        return view;
    }

    /**
     * To initialize views
     * */
    private void initViews() {
        recyclerView = view.findViewById(R.id.recyclerView_photos);
    }

    /**
     * To observe images from the liveData
     */
    private void observeImagesFromLiveData() {

        viewModel.getMyPhotosFragmentImages().observe(getViewLifecycleOwner(), new Observer<List<Photos>>() {
            @Override
            public void onChanged(List<Photos> photos) {
                setupRecyclerView_GridLayout(photos);
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * To setup recyclerView and fill the adapter with the images
     */
    private void setupRecyclerView(List<Photos> photos) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PostsAdapter(getActivity(),photos, PostsAdapter.POST_VIEW);
        recyclerView.setAdapter(adapter);
    }


    /**
     * To setup recyclerView and fill the adapter with the images
     */
    private void setupRecyclerView_GridLayout(List<Photos> photos) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new SpacesItemDecoration(3));
        adapter = new PostsAdapter(getActivity(),photos, PostsAdapter.GRID_VIEW);
        recyclerView.setAdapter(adapter);
    }


}
