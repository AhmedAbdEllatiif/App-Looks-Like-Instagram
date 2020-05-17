package com.example.ahmedd.firabasetest.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.CameraFragment;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.MainFragment;

import com.example.ahmedd.firabasetest.Helpers.ViewHelpers.MyViewPager;
import com.example.ahmedd.firabasetest.Helpers.OnBackListener_ChatFragment;
import com.example.ahmedd.firabasetest.Helpers.OnMyViewPagerListener;
import com.example.ahmedd.firabasetest.Helpers.OnToolBarIconsListener;


import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.R;

import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;


import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.ChatFragment;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;


import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements OnMyViewPagerListener, OnToolBarIconsListener, OnBackListener_ChatFragment {

    private static final String TAG = "MainActivity";

    private MainActivityViewModel viewModel;
    private MyViewPager mainViewPager;
    private MainPageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //to initialize views
        initViews();

        //to setup viewPager
        setUpViewPager();


        //To request the current user followings
        viewModel.requestFollowingOfCurrentUser();

        //To request the current user images
         viewModel.requestCurrentUserImageFromServer();

        //To request the user chatList with other users
         viewModel.requestChatListFromServer();

        //To request current user data
         viewModel.requestCurrentUserDataFromServer();

        //To set a listener on the home toolbar icons
        viewModel.setOnToolBarIconsListener(MainActivity.this);

        //To set a listener on back in ChatFragment toolbar
        viewModel.setOnBackListener_chatFragment(MainActivity.this);

        //To set a listener on pageChanged inside the viewPager of MainFragment
        viewModel.setOnMyViewPagerListener(MainActivity.this);

        //set a tag to everyUser in oneSingle to help sending notification to it by this tag
        OneSignal.sendTag("User ID", MyFireBase.getCurrentUser().getUid());

        //testFireStore();

    }


    private void testFireStore() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document("bXQoU58ULE1YCxzpRSer").collection("posts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e(TAG, "onComplete: "+ document.getId() + " => " + document.getData() );
                    }
                }else {
                    Log.e(TAG, "Error getting documents.", task.getException());
                }
            }
        });
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //User user = document.toObject(User.class);
                               // Log.e(TAG, "onComplete: "+ document.getId() + " => " + document.getData() );
                                //Log.e(TAG, document.getId() + " => " + document.getData());
                              /*  Log.e(TAG, document.get() + " => " + document.getData());
                                Log.e(TAG, document.getId() + " => " + document.getData());*/
                            }
                        } else {
                            Log.e(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    /**
     * To initialize views
     */
    private void initViews() {
        mainViewPager = findViewById(R.id.mainViewPager);
    }


    /**
     * To get user status we update the child status on tha activity lifecycle
     * when activity onResume() make status online
     * when activity onPause() make status offline
     */
    private void getUserStatus(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid()).updateChildren(hashMap);
    }


    /**
     * To setup viewPager
     */
    private void setUpViewPager() {
        pageAdapter = new MainPageAdapter(getSupportFragmentManager(), PagerAdapter.POSITION_NONE);
        pageAdapter.addFragment(new CameraFragment());
        pageAdapter.addFragment(new MainFragment());
        pageAdapter.addFragment(new ChatFragment());
        int limit = (pageAdapter.getCount() > 1 ? pageAdapter.getCount() - 1 : 1);
        mainViewPager.setOffscreenPageLimit(limit);
        mainViewPager.setAdapter(pageAdapter);
        mainViewPager.setCurrentItem(1);
        mainViewPager.setPagingEnabled(true);//To make is scrollable
    }

    /**
     * Return true if the current page is the {@link MainFragment}
     */
    private boolean isHomePage() {
        return mainViewPager.getCurrentItem() == 1;
    }












    /*/////////////////////////////////////////////////////Callbacks////////////////////////////////////////////////////*/

    @Override
    public void onBackPressed() {
        if (!isHomePage()) {
            mainViewPager.setCurrentItem(1);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed_ChatFragment() {
        mainViewPager.setCurrentItem(1, true);
    }

    @Override
    public void onCameraClicked() {
        mainViewPager.setCurrentItem(0, false);
    }

    @Override
    public void onChatClicked() {
        if (pageAdapter != null) {
            mainViewPager.setCurrentItem(pageAdapter.getCount() - 1, false);
        }
    }

    /**
     * Call back of {@link OnMyViewPagerListener} this interface initialized at {@link MainFragment}
     * To send the position of MainFragment.Viewpager
     * if the position of MainFragment.Viewpager is 0 ==> prevent  this.mainViewPager from scrolling
     * else make this.mainViewPager scrollable
     */
    @Override
    public void onPageChanged(int position) {
        Log.e(TAG, "onPageSelected: ==> position " + position);
        if (position != 0) {
            mainViewPager.setPagingEnabled(false);
        } else {
            mainViewPager.setPagingEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Photo uploaded", Toast.LENGTH_SHORT).show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "something went wrong ", Toast.LENGTH_SHORT).show();
            }
        }*/
    }


    /*/////////////////////////////////////////////////////LifeCycle////////////////////////////////////////////////////*/
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

}
