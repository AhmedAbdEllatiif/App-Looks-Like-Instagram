package com.example.ahmedd.firabasetest.Fragments.MainFragments.ProfileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.PhotosAdapter;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.SpacesItemDecoration;
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

     private PhotosAdapter adapter;
    public CurrentUserImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG, "onCreateView: ");
        view =  inflater.inflate(R.layout.fragment_current_user_images, container, false);
          viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

          initViews();

          observeImagesFromLiveData();
        return view;
    }

     private void initViews(){
        recyclerView = view.findViewById(R.id.recyclerView_photos);


    }

     /**
     * To observe the liveData
     * */
    private void observeImagesFromLiveData() {

        viewModel.getMyPhotosFragmentImages().observe(getViewLifecycleOwner(), new Observer<List<Photos>>() {
            @Override
            public void onChanged(List<Photos> photos) {

                //ShowTextPickImage(photos.isEmpty());

                //setupRecyclerView(photos);
                setupRecyclerView_GridLayout(photos);

            }
        });
    }


    /**
     * To setup recyclerView and fill the adapter with the images
     * */
    private void setupRecyclerView(List<Photos> photos){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PhotosAdapter(getActivity(), photos,PhotosAdapter.ORDINARY_VIEW);
        recyclerView.setAdapter(adapter);
    }



    /**
     * To setup recyclerView and fill the adapter with the images
     * */
    private void setupRecyclerView_GridLayout(List<Photos> photos){
        Log.e(TAG, "setupRecyclerView_GridLayout: " );
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.addItemDecoration(new SpacesItemDecoration(3));
        adapter = new PhotosAdapter(getActivity(), photos,PhotosAdapter.GRID_VIEW);
        recyclerView.setAdapter(adapter);


    }


}
