package com.example.ahmedd.firabasetest;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.CameraFragment;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.MainFragment;
import com.example.ahmedd.firabasetest.Helpers.MyViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Fragments.ChatFragment;
import com.example.ahmedd.firabasetest.Fragments.MyPhotos;
import com.example.ahmedd.firabasetest.Fragments.HomeFragment;
import com.example.ahmedd.firabasetest.Fragments.UsersFragment;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.Receivers.WIFIBroadCastReceiver;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity{

    //Views
    private ImageView profile_img;
    private TextView userName;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private ViewPager mainViewPager;

    //Fragments
    private Fragment fragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);


        //Receivers
        //checkWifiConnectionWithWIFIRECEIVER();

        //initialize views, setToolbar, setCurrentUserInfo
        initViews();
        //setToolBar();
        //setCurrentUserInfo();


        //Set Drawer, FloatingActionButton, ButtonNavigationView
        //setFloatingActionButton();
        //setDrawerLayout();
        //setNavHearderInNavigationDrawer();
        //setBottomNavigationView();


        setUpViewPager();

        //set a tag to everyuser in oneSingle to help sending notification to it by this tag
        OneSignal.sendTag("User ID", MyFireBase.getCurrentUser().getUid());


    }


    /*******************************************************************************************************/
    private void setUpViewPager(){
        MainPageAdapter pageAdapter = new MainPageAdapter(getSupportFragmentManager(), PagerAdapter.POSITION_NONE);
        pageAdapter.addFragment(new CameraFragment());
        pageAdapter.addFragment(new MainFragment());
        //pageAdapter.addFragment(new UsersFragment());
        //pageAdapter.addFragment(new MyPhotos());
        int limit = (pageAdapter.getCount() > 1 ? pageAdapter.getCount() - 1 : 1);
        mainViewPager.setOffscreenPageLimit(limit);
        mainViewPager.setCurrentItem(1);


        mainViewPager.setAdapter(pageAdapter);
    }

    /*******************************************************************************************************/


    /*******************************************************************************************************/
    private void initViews() {
       // profile_img = findViewById(R.id.profile_Image);
        //userName = findViewById(R.id.userName);
        mainViewPager = findViewById(R.id.mainViewPager);

        //collapsingToolbarLayout = findViewById(R.id.collapsing);
        //collapsingToolbarLayout.setTitle("Home");
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

    private void setCurrentUserInfo() {
        MyFireBase.getReferenceOnAllUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(MyFireBase.getCurrentUser().getUid())) {

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(user.getUserName())
                                .setPhotoUri(Uri.parse(user.getImageURL()))
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
    /*******************************************************************************************************/




    /*******************************************************************************************************/
    //FloatingActionButton
    private void setFloatingActionButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, PhotoActivity.class);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }
    /*******************************************************************************************************/




    /*******************************************************************************************************/
    //DrawerLayout
    private void setDrawerLayout() {
    /*    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
    }


    private void setNavHearderInNavigationDrawer() {
     /*   NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        final TextView user_name = (TextView) hView.findViewById(R.id.txt_name_nav_header);
        final TextView Email = (TextView) hView.findViewById(R.id.txt_email_nav_header);
        final ImageView img_profile = (ImageView) hView.findViewById(R.id.img_nav_header);

        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        user_name.setText(user.getUserName());
                        Email.setText(MyFireBase.getCurrentUser().getEmail());
                        if (user.getImageURL().equals("default")) {
                            img_profile.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Picasso.get().load(user.getImageURL()).into(img_profile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/


    }

/*    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    /*******************************************************************************************************/


    /*******************************************************************************************************/
    //BottomNavigationView
    private void setBottomNavigationView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.home);


    }

  /*  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.home:
                    fragment = new HomeFragment();
                    collapsingToolbarLayout.setTitle("Home");
                    fab.setVisibility(View.VISIBLE);
                    mainViewPager.setCurrentItem(1);
                    break;
                case R.id.chats:
                    fragment = new ChatFragment();
                    collapsingToolbarLayout.setTitle("Chats");
                    mainViewPager.setCurrentItem(0);
                    fab.setVisibility(View.GONE);
                    break;
                case R.id.users:
                    fragment = new UsersFragment();
                    collapsingToolbarLayout.setTitle("Users");
                    mainViewPager.setCurrentItem(2);
                    fab.setVisibility(View.GONE);
                    break;
                case R.id.myPhotos:
                    fragment = new MyPhotos();
                    collapsingToolbarLayout.setTitle("My Photos");
                    mainViewPager.setCurrentItem(3);
                    fab.setVisibility(View.VISIBLE);
                    break;

            }

*//*
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.viewPager, fragment)
                    .commit();*//*
            Log.e("MainActivity", "Replacing Fragment");
            return true;
        }
    };*/
    /*******************************************************************************************************/


    /*******************************************************************************************************/
    //WIFI RECEIVER
    private void checkWifiConnectionWithWIFIRECEIVER() {
        WIFIBroadCastReceiver myrecevier = new WIFIBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(myrecevier, intentFilter);
    }

    /*******************************************************************************************************/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                MyFireBase.getAuth().signOut();
                startActivity(new Intent(Main2Activity.this, StartFragment.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    if (AccessToken.getCurrentAccessToken() != null){
                        LoginManager loginManager =LoginManager.getInstance();
                        loginManager.logOut();
                    }
                    finish();
                return true;

            case R.id.profile_menu :   startActivity(new Intent(Main2Activity.this, ProfileActivity.class));
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


    //this method used to get KeyHash to put it in the facebook app developer
    private void printKeyHash() {

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.dentistry.ahmed.dentistryapp",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature : packageInfo.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //To check what data usage you use
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    Log.e("Wifi", "Wifi is connected");
                } else {
                    Toast.makeText(this, "Wifi isn't Connected Trun on your mobile data", Toast.LENGTH_LONG).show();

                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
