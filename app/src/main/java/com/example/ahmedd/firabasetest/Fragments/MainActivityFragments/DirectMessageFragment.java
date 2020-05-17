package com.example.ahmedd.firabasetest.Fragments.MainActivityFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Activities.MainActivity;
import com.example.ahmedd.firabasetest.Adapters.ChatListAdapter;
import com.example.ahmedd.firabasetest.Activities.MessageActivity;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class DirectMessageFragment extends Fragment {

    private static final String TAG = "DirectMessageFragment";

    private MainActivityViewModel viewModel;

    private View view;
    private RecyclerView recyclerView;


    private DatabaseReference referenceOnUsersThatHaveChatWith;



    private TextView txt_empty_chat_users;
    private TextView txt_startChat;

    private ImageButton img_arrow_back;


    public DirectMessageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_direct_message, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        initViews();

        onViewsClicked();



        observeChatListData_liveData();


        return view;
    }


    /**
     * To initialize views
     * */
    private void initViews() {
        recyclerView = view.findViewById(R.id.recyclerView_usersYouHavechatwith);
        txt_empty_chat_users = view.findViewById(R.id.txt_empty_chat_users);
        txt_startChat = view.findViewById(R.id.txt_startChat);
        img_arrow_back = view.findViewById(R.id.img_arrow_back);
    }

    /**
     * On Views Clicked
     * */
    private void onViewsClicked() {
        img_arrow_back.setOnClickListener(view -> {
            if (!isOnBackListenerNull()) {
                viewModel.onBackListener_chatFragment.onBackPressed_ChatFragment();
            }
        });
    }


    /**
     * To observe data from live data
     * */
    private void observeChatListData_liveData(){
        viewModel.getUserChatList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                setupRecyclerView(userList);
            }
        });
    }


    /**
     * To setup recyclerView with usersChatList
     * */
    private void setupRecyclerView(List<User> usersList) {

        isUsersChatListIsEmpty(usersList);

        ChatListAdapter adapter = new ChatListAdapter(getContext(), usersList, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        onUserClickedToChatWith(adapter);
        //SvwLz9q8ZfcJjzr8rzAvAKMwmIA3
                //l12SZctOPpTUSXRzuZco6G2xaFm2


    }

    /**
     * To Check if the listener in null
     * The listener must be initialize in the {@link MainActivity}
     * This listener move the viewPager inside the mainActivity  from the DirectMessageFragment to HomeFragment
     * */
    private boolean isOnBackListenerNull(){
        if (viewModel.onBackListener_chatFragment == null){
            Log.e(TAG, "isOnBackListenerNull ==> null pointer" );
            return true;
        }
        return false;
    }




    /**
     * To Handle when user press on other user to chat with
     * Go to {@link MessageActivity} with extra {userID}
     * */
    private void onUserClickedToChatWith(ChatListAdapter adapter){
        adapter.setOnCardClickListener(new ChatListAdapter.MyOnclickListener() {
            @Override
            public void onClick(int position, User userItem) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra(getString(R.string.userId), userItem.getId());
                startActivity(intent);

            }
        });
    }

    private void isUsersChatListIsEmpty(List<User> usersList){
        if (usersList.isEmpty()) {
            txt_empty_chat_users.setVisibility(View.VISIBLE);
            txt_startChat.setVisibility(View.VISIBLE);
        } else {
            txt_empty_chat_users.setVisibility(View.GONE);
            txt_startChat.setVisibility(View.GONE);
        }
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
