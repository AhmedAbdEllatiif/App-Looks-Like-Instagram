package com.example.ahmedd.firabasetest.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.HomeAdapter;
import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;

    private List<Photos> photosListOFTheFollowing;

    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        Handler getThePhotosFormBranchPhotosOfFollowing = new Handler();
        getThePhotosFormBranchPhotosOfFollowing.post(new Runnable() {
            @Override
            public void run() {
                photosListOFTheFollowing = new ArrayList<>();
                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                        .child("PhotosOfFollowing").limitToLast(5).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photosListOFTheFollowing.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Photos photosItem = snapshot.getValue(Photos.class);
                    photosListOFTheFollowing.add(photosItem);
                }
                adapter = new HomeAdapter(getActivity(), photosListOFTheFollowing);
                recyclerView.setAdapter(adapter);
            }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }

}
