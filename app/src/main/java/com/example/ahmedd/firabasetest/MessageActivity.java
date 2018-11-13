package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Adapters.MessagesAdapter;
import com.example.ahmedd.firabasetest.Model.Chats;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private String clickedUserID;
    private Intent intent;

    private CircleImageView profile_pic;
    private TextView userName;
    private ImageButton imageButton_send;
    private EditText editText_messageToSend;

    private RecyclerView recyclerView;
    private List<Chats> chatsList;
    private MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profile_pic = findViewById(R.id.messageActivity_profile_Image);
        userName = findViewById(R.id.messageActivity_userName);
        imageButton_send = findViewById(R.id.img_btn_send);
        editText_messageToSend = findViewById(R.id.editText_messageToSend);

        recyclerView = findViewById(R.id.recyclerView_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        //setStackFromEnd() default==false
        //setStackFromEnd() when false fill the recyclerView from top
        //setStackFromEnd() when true fill the recyclerView from bottom
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        setupToolbar();

        intent = getIntent();
        Log.e("userId",intent.getStringExtra("userID"));
        clickedUserID = intent.getStringExtra("userID");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(clickedUserID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());
                if(user.getImageURL().equals("default")){
                    profile_pic.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Picasso.get().load(user.getImageURL()).into(profile_pic);
                }
                String myID = MyFireBase.getCurrentUser().getUid();
                readMessages(myID,clickedUserID,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button_send_listener();


    }

    private void Button_send_listener() {
        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = editText_messageToSend.getText().toString();

                if (!message.equals("")){
                    sendMessage(MyFireBase.getCurrentUser().getUid(),clickedUserID,message);
                }else {
                    Toast.makeText(MessageActivity.this, R.string.enter_message, Toast.LENGTH_SHORT).show();
                }
                editText_messageToSend.setText("");
                editText_messageToSend.requestFocus();


            }

        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.messageActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





    }

    private void sendMessage(String sender,String receiver,String message){

        //To make a new branch jason called Chats
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isSeen",false);

        //this method getReferenceOnDataBase() returns a reference on the dataBase
        MyFireBase.getReferenceOnDataBase().child("Chats").push().setValue(hashMap);

        final DatabaseReference referenceChatList = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(MyFireBase.getCurrentUser().getUid())
                .child(clickedUserID);


        referenceChatList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    referenceChatList.child("id").setValue(clickedUserID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //the message will be called when we setup the toolbar
    //When we setup the toolbar we retrieve the userID
    //so we call this method in the reference on the user that we chat with
    private void readMessages(final String myID, final String userID, final String imageURl){

        chatsList = new ArrayList<>();
        DatabaseReference reference = MyFireBase.referenceOnChats();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats chats = snapshot.getValue(Chats.class);

                    //search for the between me and the other user
                    //check if am i sender or receiver and the same for the other user
                    if (chats.getSender().equals(myID)&& chats.getReceiver().equals(userID) ||
                            chats.getReceiver().equals(myID)&&chats.getSender().equals(userID)){
                        chatsList.add(chats);
                    }
                }
                adapter = new MessagesAdapter(MessageActivity.this,chatsList,imageURl);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
