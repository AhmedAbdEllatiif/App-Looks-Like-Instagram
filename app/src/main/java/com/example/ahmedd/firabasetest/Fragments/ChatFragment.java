package com.example.ahmedd.firabasetest.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.UsersAdapter;
import com.example.ahmedd.firabasetest.MessageActivity;
import com.example.ahmedd.firabasetest.Model.ChatList;
import com.example.ahmedd.firabasetest.Model.Chats;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private UsersAdapter adapter;

    private DatabaseReference referenceOnUsersThatHaveChatWith;

    private List<User> usersList;
    private List<ChatList> myChatsList;

    public ChatFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_chat, container, false);

         setupRecyclerView();


        myChatsList = new ArrayList<>();
        referenceOnUsersThatHaveChatWith = MyFireBase.getReferenceOnChatList()
                                                    .child(MyFireBase.getCurrentUser().getUid());

        referenceOnUsersThatHaveChatWith.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myChatsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    myChatsList.add(chatList);
                }
                fillTheChatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void setupRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView_usersYouHavechatwith);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void fillTheChatList() {
        usersList = new ArrayList<>();
        MyFireBase.referenceOnAllUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for(ChatList chatListItem : myChatsList){
                        if (user.getId().equals(chatListItem.getId())){
                            usersList.add(user);
                        }
                    }
                }

                adapter =  new UsersAdapter(getContext(),usersList);
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

    /*                  ---***How it works***----

    * To Read the users you have Chat with
    * we make a branch ChatList which have child when you send any message named by the cuurrentUser Id
    * this child has a child with the named by userId you send the message to
    * then we make a reference on the the child which is called by the currentUserID
    * and get a listener on it
    * then compare between the ids from the chatList that we get form the listener and the usersID
    * when the id from the chatlist equals the any UserId
    * that means you have a chat with this Users and we add this user to userList to show in the chats fragments
    * */


}
