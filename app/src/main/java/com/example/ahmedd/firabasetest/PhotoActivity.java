package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Adapters.PhotosAdapter;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private PhotosAdapter adapter;
    private List<Photos> photosList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        setToolBar();
        fillRecyclerViewWithPhotos();


    }

    private void fillRecyclerViewWithPhotos() {
        recyclerView = findViewById(R.id.recyclerView_photos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        photosList = new ArrayList<>();

        MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        photosList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Photos photosItem = snapshot.getValue(Photos.class);
                            photosList.add(photosItem);
                        }
                        adapter = new PhotosAdapter(getApplicationContext(),photosList);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.photoActivity_ToolBar);
        final ImageView photoActivity_profile_Image = findViewById(R.id.photoActivity_profile_Image);
        final TextView photoActivity_userName = findViewById(R.id.photoActivity_userName);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhotoActivity.this,MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });



        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //getValue hatrg3 object jason
                //ha3ml class 2st2bl feh l object
                User user = dataSnapshot.getValue(User.class);

                if (user.getUserName() != null) {
                    photoActivity_userName.setText(user.getUserName());
                }

                if (user.getImageURL().equals("default")) {
                    photoActivity_profile_Image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageURL()).into(photoActivity_profile_Image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
