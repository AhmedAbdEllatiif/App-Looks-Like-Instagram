package com.example.ahmedd.firabasetest.Fragments.MainFragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ahmedd.firabasetest.Adapters.UsersAdapter;
import com.example.ahmedd.firabasetest.Activities.MessageActivity;
import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private View view = null;
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<User> userList;
    private List<Following> followings;
    private EditText editText_searchUsers;
    private String currentUserID;
    private ValueEventListener readAllUsersListener;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
        view = inflater.inflate(R.layout.fragment_users, container, false);
        }

        editText_searchUsers = view.findViewById(R.id.editText_searchUsers);
        searchByCapitalLetter();

        currentUserID =  MyFireBase.getCurrentUser().getUid();

        setupRecyclerView();
        readAllUsers();
        return view;
    }

    private void searchByCapitalLetter() {
        /*FireBase is case sensitive so all users names must start with capital letter */
        editText_searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                searchUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable st) {
               if (editText_searchUsers.getText().toString().length()>0 && !editText_searchUsers.getText().toString().isEmpty()){

                boolean isUpperCase = Character.isUpperCase(editText_searchUsers.getText().toString().charAt(0));
                   if ( !isUpperCase){
                       editText_searchUsers.setError("First letter must be uppercase");
               }
                }else if(editText_searchUsers.getText().toString().length() == 0) {
                   editText_searchUsers.requestFocus();
                }


            }
        });
    }

    private void searchUsers(String s) {

        Query query = MyFireBase.getReferenceOnAllUsers().orderByChild("userName")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    User userItem = snapshot.getValue(User.class);

                    assert userItem !=null;
                    if (!userItem.getId().equals(MyFireBase.getCurrentUser().getUid())){
                        userList.add(userItem);
                    }
                }
                adapter =  new UsersAdapter(getContext(),userList,null,false);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        userList = new ArrayList<>();
        followings = new ArrayList<>();
    }


    private void readAllUsers() {
        readAllUsersListener = MyFireBase.getReferenceOnAllUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (editText_searchUsers.getText().toString().isEmpty()){

                userList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert MyFireBase.getCurrentUser() != null;

                    if (!user.getId().equals(MyFireBase.getCurrentUser().getUid())) {
                        userList.add(user);
                    }
                }
                    MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                            .child("Following").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            followings.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Following followingItem = snapshot.getValue(Following.class);
                                followings.add(followingItem);
                            }
                            adapter = new UsersAdapter(getContext(), userList,followings,false);
                            recyclerView.setAdapter(adapter);
                            myAdapterClickListeners(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                //onCancelled reference following
                        }
                    });

            }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //onCancelled reference users
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MyFireBase.getReferenceOnAllUsers().removeEventListener(readAllUsersListener);
    }

    private void myAdapterClickListeners(UsersAdapter usersAdapter) {

        usersAdapter.setOnCardClickListener(new UsersAdapter.MyOnclickListener() {
            @Override
            public void onClick(int position, User userItem) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("userID", userItem.getId());
                startActivity(intent);
            }
        });


        usersAdapter.setOnFollowClickListener(new UsersAdapter.MyOnclickListener() {
            @Override
            public void onClick(int position, final User userItem) {
                final DatabaseReference databaseReference =
                        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                .child("Following").child(userItem.getId());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            databaseReference.child("id").setValue(userItem.getId());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


        });

        usersAdapter.setOnUnFollowClickListener(new UsersAdapter.MyOnclickListener() {
            @Override
            public void onClick(int position, User userItem) {
                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                        .child("Following").child(userItem.getId()).removeValue();
            }


        });

    }

    /*                  ---***How it works***----

     * retrieving all users in a recyclerView
     * all users have the current user too
     * so check for the current user and make a list of other users
     * if (!user.getId().equals(MyFireBase.getCurrentUser().getUid()))
     * If you want to search for a user type the username in the search bar
     * when you search we make a Query to search in the branch users
     * must remove the listener on the users when the avtivity onPause() to avoid null pointer when you logout from the account
     * */
}
