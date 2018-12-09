package com.example.ahmedd.firabasetest.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<Following> followings;
    private List<User> userList = new ArrayList<>();
    private List<Photos> photosList = new ArrayList<>();
    private List<String> followersID = new ArrayList<>();

    private List<Following> followingList;
    private List<Photos> photosListOFTheFollowing;

    private HomeAdapter adapter;

    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        followings = new ArrayList<>();
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Following followingItem = snapshot.getValue(Following.class);
                    followings.add(followingItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        photosListOFTheFollowing = new ArrayList<>();
        final DatabaseReference databaseReference = MyFireBase.getReferenceOnPhotos();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (int i = 0; i < followings.size(); i++) {
                        if (followings.get(i).getId().equals(snapshot.getKey())) {
                            DatabaseReference photosReference = databaseReference.child(snapshot.getKey()).child("Myphotos");

                            photosReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapPhotos : dataSnapshot.getChildren()) {
                                        Photos photosItem = snapPhotos.getValue(Photos.class);
                                        Log.e("HomeFragmentphotoName", photosItem.getName());
                                        photosListOFTheFollowing.add(photosItem);
                                    }
                                    adapter = new HomeAdapter(getActivity(), photosListOFTheFollowing);
                                    recyclerView.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                           /* for (DataSnapshot snapPhotos : snapshot.child("Myphotos").getChildren()) {
                                Photos photosItem = snapPhotos.getValue(Photos.class);
                                Log.e("photoName", photosItem.getName());
                                photosListOFTheFollowing.add(photosItem);
                            }*/
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
