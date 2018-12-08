package com.example.ahmedd.firabasetest.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;


    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       MyFireBase.getReferenceOnFollowers().child(MyFireBase.getCurrentUser().getUid())
               .addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });




    return view;
    }

}
