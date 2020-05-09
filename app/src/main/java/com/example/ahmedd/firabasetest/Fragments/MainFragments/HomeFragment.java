package com.example.ahmedd.firabasetest.Fragments.MainFragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Activities.MainActivity;
import com.example.ahmedd.firabasetest.Adapters.HomeAdapter;
import com.example.ahmedd.firabasetest.Helpers.GetImagesTask;
import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private MainActivityViewModel viewModel;
    private View view = null;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;



    public HomeFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       Log.e("onAttach","is here");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause", "is here");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);


        initViews();

        observeImagesFromLiveData();

        return view;
    }


    /**
     * To initialize views
     * */
    private void initViews(){
        recyclerView = view.findViewById(R.id.recyclerView_home);
    }


    /**
     * To observe the liveData
     * */
    private void observeImagesFromLiveData(){
        viewModel.getHomeFragmentImages().observe(getViewLifecycleOwner(), new Observer<List<Photos>>() {
            @Override
            public void onChanged(List<Photos> photos) {
                setupRecyclerView(photos);
            }
        });

    }


    /**
     * To setup recyclerView and fill the adapter with the images
     * */
    private void setupRecyclerView(List<Photos> photos){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HomeAdapter(getContext(),photos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



}
