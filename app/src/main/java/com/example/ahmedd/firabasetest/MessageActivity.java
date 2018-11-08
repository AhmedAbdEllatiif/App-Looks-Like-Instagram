package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private String clickedUserID;
    private Intent intent;

    private CircleImageView profile_pic;
    private TextView userName;
    private ImageButton imageButton_send;
    private EditText editText_messageToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profile_pic = findViewById(R.id.messageActivity_profile_Image);
        userName = findViewById(R.id.messageActivity_userName);
        imageButton_send = findViewById(R.id.img_btn_send);
        editText_messageToSend = findViewById(R.id.editText_messageToSend);


        setupToolbar();

        intent = getIntent();
        Log.e("userId",intent.getStringExtra("userID"));
        clickedUserID = intent.getStringExtra("userID");

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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendMessage(String sender,String receiver,String message){

        //To make a new branch jason called Chats
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        //this method getReferenceOnDataBase() returns a reference on the dataBase
        MyFireBase.getReferenceOnDataBase().child("Chats").push().setValue(hashMap);

    }

}
