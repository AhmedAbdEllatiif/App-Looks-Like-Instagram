package com.example.ahmedd.firabasetest.Fragments.MainActivityFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Adapters.ChatListAdapter;
import com.example.ahmedd.firabasetest.Activities.MessageActivity;
import com.example.ahmedd.firabasetest.Helpers.OnBackListener_ChatFragment;
import com.example.ahmedd.firabasetest.Model.ChatList;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;

    private DatabaseReference referenceOnUsersThatHaveChatWith;

    private List<User> usersList;
    private List<ChatList> myChatsList;

    private TextView txt_empty_chat_users;
    private TextView txt_startChat;

    private ImageButton img_arrow_back;

    private OnBackListener_ChatFragment onBackListener_chatFragment;

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        initViews();

        onViewsClicked();

        setupRecyclerView();


        myChatsList = new ArrayList<>();
        referenceOnUsersThatHaveChatWith = MyFireBase.getReferenceOnChatList()
                .child(MyFireBase.getCurrentUser().getUid());

        referenceOnUsersThatHaveChatWith.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myChatsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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


    private void initViews() {
        txt_empty_chat_users = view.findViewById(R.id.txt_empty_chat_users);
        txt_startChat = view.findViewById(R.id.txt_startChat);
        img_arrow_back = view.findViewById(R.id.img_arrow_back);
    }

    private void onViewsClicked() {
        img_arrow_back.setOnClickListener(view -> onBackListener_chatFragment.onBackPressed_ChatFragment());
    }

    private void setupRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView_usersYouHavechatwith);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void fillTheChatList() {
        usersList = new ArrayList<>();
        MyFireBase.getReferenceOnAllUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (ChatList chatListItem : myChatsList) {
                        if (user.getId().equals(chatListItem.getId())) {
                            usersList.add(user);
                        }
                    }
                }

                if (usersList.isEmpty()) {
                    txt_empty_chat_users.setVisibility(View.VISIBLE);
                    txt_startChat.setVisibility(View.VISIBLE);
                } else {
                    txt_empty_chat_users.setVisibility(View.GONE);
                    txt_startChat.setVisibility(View.GONE);
                }
                adapter = new ChatListAdapter(getContext(), usersList, true);
                recyclerView.setAdapter(adapter);
                adapter.setOnCardClickListener(new ChatListAdapter.MyOnclickListener() {
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

    public void setOnBackListener_chatFragment(OnBackListener_ChatFragment onBackListener_chatFragment) {
        this.onBackListener_chatFragment = onBackListener_chatFragment;
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
