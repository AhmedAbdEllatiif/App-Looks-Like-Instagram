package com.example.ahmedd.firabasetest.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.CameraFragment;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.MainFragment;
import com.example.ahmedd.firabasetest.Helpers.GetImagesTask;
import com.example.ahmedd.firabasetest.Helpers.OnCameraToolBarListener;
import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.Photos;

import com.example.ahmedd.firabasetest.R;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.ChatFragment;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnCameraToolBarListener {

    //Views
    private ImageView profile_img;
    private TextView userName;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private ViewPager mainViewPager;

    //Fragments
    private Fragment fragment;

    private List<Following> followingList;
    final DatabaseReference referenceOnPhotos = MyFireBase.getReferenceOnPhotos();
    List<Photos> myList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);



        initViews();

        setUpViewPager();


       followingList = new ArrayList<>();
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Following followingItem = snapshot.getValue(Following.class);
                    followingList.add(followingItem);
                }
                Log.e("folo",followingList.size()+"");

                referenceOnPhotos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot photosChild : dataSnapshot.getChildren()) {
                            for (int i = 0; i < followingList.size(); i++) {
                                if (followingList.get(i).getId().equals(photosChild.getKey())) {
                                    referenceOnPhotos.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            referenceOnPhotos.child(photosChild.getKey())
                                                    .child("Myphotos").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    //photosListOFTheFollowing.clear();
                                                    for (DataSnapshot myPhotosChildern : dataSnapshot.getChildren()) {
                                                        Photos photosItem = myPhotosChildern.getValue(Photos.class);

                                                        //publishProgress(photosItem);


                                                        myList.add(photosItem);
                                                    }

                                                    getImagesTask.onGettingImagesCompleted(myList);


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //set a tag to everyuser in oneSingle to help sending notification to it by this tag
        OneSignal.sendTag("User ID", MyFireBase.getCurrentUser().getUid());


    }









    /*******************************************************************************************************/
    private void setUpViewPager(){
        MainPageAdapter pageAdapter = new MainPageAdapter(getSupportFragmentManager(), PagerAdapter.POSITION_NONE);
        MainFragment mainFragment  = new MainFragment();
        mainFragment.setOnCameraToolBarListener(MainActivity.this);
        pageAdapter.addFragment(new CameraFragment());
        pageAdapter.addFragment(mainFragment);
        pageAdapter.addFragment(new ChatFragment());
        //pageAdapter.addFragment(new UsersFragment());
        //pageAdapter.addFragment(new MyPhotos());
        int limit = (pageAdapter.getCount() > 1 ? pageAdapter.getCount() - 1 : 1);
        mainViewPager.setOffscreenPageLimit(limit);
        mainViewPager.setAdapter(pageAdapter);


        mainViewPager.setCurrentItem(1);

    }


    private void initViews() {

        mainViewPager = findViewById(R.id.mainViewPager);

    }

    private void setToolBar() {
        //toolbar = findViewById(R.id.myUserToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        //set user data in the toolBar
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //getValue hatrg3 object jason
                //ha3ml class 2st2bl feh l object


                User user = dataSnapshot.getValue(User.class);

                if (user.getUserName() != null) {
                    userName.setText(user.getUserName());
                } else {
                    userName.setText("user name");
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

    private void getUserStatus(String status) {

        /*To get user status we update the child status on tha activity lifecycle
         *when activity onResume() make status online
         * when activity onPause() make status offline
         */
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid()).updateChildren(hashMap);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Photo uploaded", Toast.LENGTH_SHORT).show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "something went wrong ", Toast.LENGTH_SHORT).show();
            }
        }
    }






    private GetImagesTask getImagesTask;

    public void setGetImagesTask(GetImagesTask getImagesTask) {
        this.getImagesTask = getImagesTask;
    }


    @Override
    public void onCameraClicked() {
      mainViewPager.setCurrentItem(0,false);
    }
}
