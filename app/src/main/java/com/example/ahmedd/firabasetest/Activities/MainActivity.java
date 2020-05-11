package com.example.ahmedd.firabasetest.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.CameraFragment;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.MainFragment;
import com.example.ahmedd.firabasetest.Helpers.OnBackListener_ChatFragment;
import com.example.ahmedd.firabasetest.Helpers.OnToolBarIconsListener;
import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.Photos;

import com.example.ahmedd.firabasetest.R;

import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.ChatFragment;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
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

public class MainActivity extends AppCompatActivity implements OnToolBarIconsListener, OnBackListener_ChatFragment {

    private MainActivityViewModel viewModel;

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
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        initViews();

        setUpViewPager();


        //To request the the user followings images
        viewModel.requestFollowingUsersImagesFromServer();

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



        //set a tag to everyUser in oneSingle to help sending notification to it by this tag
        OneSignal.sendTag("User ID", MyFireBase.getCurrentUser().getUid());



    }








    /*******************************************************************************************************/
    private void setUpViewPager(){
        MainPageAdapter pageAdapter = new MainPageAdapter(getSupportFragmentManager(), PagerAdapter.POSITION_NONE);
        MainFragment mainFragment  = new MainFragment();
        ChatFragment chatFragment  = new ChatFragment();
        /*HomeFragment homeFragment = new HomeFragment();
        homeFragment.setToolBarListener(MainActivity.this);*/
        //chatFragment.setOnBackListener_chatFragment(MainActivity.this);
        pageAdapter.addFragment(new CameraFragment());
        pageAdapter.addFragment(mainFragment);
        pageAdapter.addFragment(chatFragment);
        //pageAdapter.addFragment(new UsersFragment());
        //pageAdapter.addFragment(new ProfileFragment());
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
        if (!isHomePage()){
            mainViewPager.setCurrentItem(1);
        }else {
        super.onBackPressed();
        }
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




    private boolean isHomePage(){
        return mainViewPager.getCurrentItem() == 1;
    }


    @Override
    public void onBackPressed_ChatFragment() {
        mainViewPager.setCurrentItem(1,true);
    }

    @Override
    public void onCameraClicked() {
      mainViewPager.setCurrentItem(0,false);
    }

    @Override
    public void onChatClicked() {
        mainViewPager.setCurrentItem(mainViewPager.getChildCount()-1,false);
    }
}
