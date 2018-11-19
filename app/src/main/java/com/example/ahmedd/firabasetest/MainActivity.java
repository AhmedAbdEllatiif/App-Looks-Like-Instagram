package com.example.ahmedd.firabasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Adapters.PageAdapter;
import com.example.ahmedd.firabasetest.BaseActivities.BaseActivity;
import com.example.ahmedd.firabasetest.Fragments.ChatFragment;
import com.example.ahmedd.firabasetest.Fragments.ProfileFragment;
import com.example.ahmedd.firabasetest.Fragments.UsersFragment;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    private CircleImageView profile_img;
    private TextView userName;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String currentUserID;


    @Override
    protected void onStart() {
        super.onStart();

        setToolBar();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setToolBar();
        setCurrentUserInfo();
        setupViewPageAdapter();

        currentUserID = MyFireBase.getCurrentUser().getUid();
    }





    private void setupViewPageAdapter() {
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());

        pageAdapter.AddFragmentPage(new ChatFragment(),getString(R.string.chats));
        pageAdapter.AddFragmentPage(new UsersFragment(),getString(R.string.users));
        pageAdapter.AddFragmentPage(new ProfileFragment(),getString(R.string.profile));

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        //set user data in the toolBar
        MyFireBase.getReferenceOnCurrentUser().addValueEventListener(new ValueEventListener() {
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

    private void getUserStatus(String status){

        /*To get user status we update the child status on tha activity lifecycle
        *when activity onResume() make status online
        * when activity onPause() make status offline
        */
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        MyFireBase.getReferenceOnAllUsers().child(currentUserID).updateChildren(hashMap);

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
                startActivity(new Intent(MainActivity.this, StartActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }

        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getUserStatus(getString(R.string.online));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getUserStatus(getString(R.string.offline));
    }

    private void setCurrentUserInfo(){
        MyFireBase.getReferenceOnAllUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(MyFireBase.getCurrentUser().getUid())){

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(user.getUserName())
                                .build();

                        MyFireBase.getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.e("CurrentUserInfoUpdate", "done");
                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /*                  ---***How it works***----

     * MainActivity consists of ToolBar & PageAdapter on fragments(Chat,users,profile)
     * ToolBar have userName , profile img & a menu with one item Logout to signout of the Firebase.
     * we get the user status online/offline in this activity in its lifecycle
     * setup the viewpager with the fragments
     * setCurrentUserInfo() this method get the info of the user for database and put it into FireBaseAuth.getInstance().getCurrentUser;
     *  */

}
