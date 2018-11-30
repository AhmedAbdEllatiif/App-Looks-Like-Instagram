package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Adapters.MessagesAdapter;
import com.example.ahmedd.firabasetest.Model.Chats;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {


    //views
    private CircleImageView profile_pic;
    private TextView userName;
    private TextView status;
    private ImageButton imageButton_send;
    private TextInputEditText editText_messageToSend;

    //recyclerView
    private RecyclerView recyclerView;
    private List<Chats> chatsList;
    private MessagesAdapter adapter;
    private  String currentTime;

    private String userToChatWith;
    private Intent intent;

    //this listener to remove the event listener when we check seen message
    private ValueEventListener seenListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initViews();
        setupToolbar();

        MyFireBase.getReferenceOnAllUsers().child(getTheUserIdYouChatWIth()).addValueEventListener(new ValueEventListener() {
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
                readMessages(myID, userToChatWith,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showTheButtonSendWhenUserStartTyping();
        Button_send_listener();
        SetMessageSeenWhenTheCurrentUserOpenMessageActivity(userToChatWith);
        setUserStatusOnlineOrOffline(getTheUserIdYouChatWIth());

    }

    private void showTheButtonSendWhenUserStartTyping() {
        editText_messageToSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText_messageToSend.getText().toString().trim().length() > 0){
                    imageButton_send.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText_messageToSend.getText().toString().trim().length() > 0){
                    imageButton_send.setVisibility(View.VISIBLE);
                }else if (editText_messageToSend.getText().toString().trim().isEmpty()){
                    imageButton_send.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public String getCurrentTime() {
        DateFormat dateFormat =  new SimpleDateFormat("h:mm a");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentTime = dateFormat.format(Calendar.getInstance().getTime());
        }
        return currentTime;
    }

    private String getTheUserIdYouChatWIth() {
        intent = getIntent();
        Log.e("userId",intent.getStringExtra("userID"));
        userToChatWith = intent.getStringExtra("userID");
        return userToChatWith;
    }

    private void setUserStatusOnlineOrOffline(String userYouChatWith) {
        MyFireBase.getReferenceOnAllUsers().child(userYouChatWith)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user =  dataSnapshot.getValue(User.class);
                        if(user.getStatus().equals("Online")){
                            status.setVisibility(View.VISIBLE);
                            status.setText("online");
                        }else {
                            status.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void initViews() {
        profile_pic = findViewById(R.id.messageActivity_profile_Image);
        userName = findViewById(R.id.messageActivity_userName);
        status = findViewById(R.id.online_offline_message_activity);
        imageButton_send = findViewById(R.id.img_btn_send);
        editText_messageToSend = findViewById(R.id.editText_messageToSend);
        editText_messageToSend.requestFocus();

        recyclerView = findViewById(R.id.recyclerView_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        //setStackFromEnd() default==false
        //setStackFromEnd() when false fill the recyclerView from top
        //setStackFromEnd() when true fill the recyclerView from bottom
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void Button_send_listener() {
        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = editText_messageToSend.getText().toString().trim();

                if (!message.isEmpty()){
                    sendMessage(MyFireBase.getCurrentUser().getUid(), userToChatWith, message, getCurrentTime());
                    sendNotification(message);
                }else {
                    editText_messageToSend.setError(getString(R.string.enter_message));
                    editText_messageToSend.requestFocus();
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
               startActivity(new Intent(MessageActivity.this,Main2Activity.class)
                       .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


    }

    private void sendMessage(String sender,String receiver,String message,String time){

        //To make a new branch jason called Chats
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isSeen",false);
        hashMap.put("time",time);

        //this method getReferenceOnDataBase() returns a reference on the dataBase
        MyFireBase.getReferenceOnDataBase().child("Chats").push().setValue(hashMap);

        final DatabaseReference referenceChatList = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(MyFireBase.getCurrentUser().getUid())
                .child(userToChatWith);

        referenceChatList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    referenceChatList.child("id").setValue(userToChatWith);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(final String myID, final String userID, final String imageURl){

        chatsList = new ArrayList<>();
        DatabaseReference reference = MyFireBase.getReferenceOnChats();

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

    private void getUserStatus(String status){
        /*To get user status we update the child status on tha activity lifecycle
         *when activity onResume() make status online
         * when activity onPause() make status offline
         */
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid()).updateChildren(hashMap);
    }

    private void SetMessageSeenWhenTheCurrentUserOpenMessageActivity(final String userToChatWithID){

        seenListener = MyFireBase.getReferenceOnChats().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chats chatItem = snapshot.getValue(Chats.class);
                    if (chatItem.getReceiver().equals(MyFireBase.getCurrentUser().getUid()) && chatItem.getSender().equals(userToChatWithID)){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserStatus(getString(R.string.online));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyFireBase.getReferenceOnChats().removeEventListener(seenListener);
        getUserStatus(getString(R.string.offline));
    }


    private void sendNotification(final String notificationMessage) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String userToSendNotification =  userToChatWith;

                    //This is a Simple Logic to Send Notification different Device Programmatically....


                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic ODI3ZmFjMDYtYzU1Yy00MDg5LTg1MTMtNzc4Y2JjNTY4ODU4");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"24df44f2-5ccb-40ae-a724-dfa50bb4acfc\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User ID\", \"relation\": \"=\", \"value\": \"" + userToSendNotification + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \""+ notificationMessage+ "\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }


    /*                  ---***How it works***----

     * */

}
