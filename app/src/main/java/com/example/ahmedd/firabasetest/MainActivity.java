package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Adapters.PageAdapter;
import com.example.ahmedd.firabasetest.Fragments.ChatFragment;
import com.example.ahmedd.firabasetest.Fragments.UsersFragment;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBaseAuth;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView profile_img;
    private TextView userName;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setToolBar();

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());

        pageAdapter.AddFragmentPage(new ChatFragment(),getString(R.string.chats));
        pageAdapter.AddFragmentPage(new UsersFragment(),getString(R.string.users));

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initViews() {
        profile_img = findViewById(R.id.profile_Image);
        userName = findViewById(R.id.userName);
        tabLayout = findViewById(R.id.tabLayot);
        viewPager = findViewById(R.id.viewPager);
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.myUserToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //set user data in the toolBar
        MyFireBaseAuth.referenceOnUserChild().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //getValue hatrg3 object jason
                //ha3ml class 2st2bl feh l object
                User user = dataSnapshot.getValue(User.class);

                if (user.getUserName() != null) {
                    userName.setText(user.getUserName());
                }

                if (user.getImageURL().equals("default")) {
                    profile_img.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageURL()).into(profile_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
                return true;
        }

        return false;
    }
}
