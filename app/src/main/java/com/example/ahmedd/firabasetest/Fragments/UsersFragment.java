package com.example.ahmedd.firabasetest.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.UsersAdapter;
import com.example.ahmedd.firabasetest.MessageActivity;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<User> userList;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_users, container, false);

        setupRecyclerView();
        readAllUsers();
        myClickListeners();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        userList = new ArrayList<>();
    }


    private void readAllUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert MyFireBase.getCurrentUser() != null;


                    if (!user.getId().equals(firebaseUser.getUid())) {
                        userList.add(user);
                    }
                }

                adapter = new UsersAdapter(getContext(), userList,false);
                recyclerView.setAdapter(adapter);
                adapter.setOnCardClickListener(new UsersAdapter.MyOnclickListener() {
                    @Override
                    public void onClick(int position, User userItem) {
                        Intent intent = new Intent(getActivity(), MessageActivity.class);
                        intent.putExtra("userID", userItem.getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myClickListeners() {


    }

}
