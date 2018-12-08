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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Fragments.ChatFragment;
import com.example.ahmedd.firabasetest.Fragments.HomeFragment;
import com.example.ahmedd.firabasetest.Fragments.UsersFragment;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.Receivers.WIFIBroadCastReceiver;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
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

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView profile_img;
    private TextView userName;


    private String currentUserID;
    private FirebaseUser firebaseCurrentUser;
    private Fragment fragment;
    private Uri img_uri;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    collapsingToolbarLayout.setTitle("Home");
                    fab.setVisibility(View.VISIBLE);
                    break;
                case R.id.chats:
                    fragment = new ChatFragment();
                    collapsingToolbarLayout.setTitle("Chats");
                    fab.setVisibility(View.GONE);
                    break;
                case R.id.users:
                    fragment = new UsersFragment();
                    collapsingToolbarLayout.setTitle("Users");
                    fab.setVisibility(View.GONE);
                    break;
            }

            Log.e("Fragment", "Replacing Fragment");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.viewPager,fragment)
                    .commit();
            return true;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        initViews();
        setToolBar();
        // setCurrentUserInfo();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            Intent intent =  new Intent(Main2Activity.this,PhotoActivity.class);
            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setNavHearderInNavigationDrawer();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.home);






        currentUserID = MyFireBase.getCurrentUserID();
        firebaseCurrentUser = MyFireBase.getCurrentUser();

        OneSignal.sendTag("User ID",MyFireBase.getCurrentUser().getUid());
//        checkWifiConnection();

       // printKeyHash();

    }



    private void setNavHearderInNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        final TextView user_name = (TextView)hView.findViewById(R.id.txt_name_nav_header);
        final TextView Email = (TextView)hView.findViewById(R.id.txt_email_nav_header);
        final ImageView img_profile = (ImageView) hView.findViewById(R.id.img_nav_header);

        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);

                        user_name.setText(user.getUserName());
                        Email.setText(MyFireBase.getCurrentUser().getEmail());
                        if (user.getImageURL().equals("default")){
                            img_profile.setImageResource(R.mipmap.ic_launcher);
                        }else {
                            Picasso.get().load(user.getImageURL()).into(img_profile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }


    //will try it later
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private void checkWifiConnection() {
        WIFIBroadCastReceiver myrecevier = new WIFIBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(myrecevier, intentFilter);
    }


 /*   private void setupViewPageAdapter() {
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());

        pageAdapter.AddFragmentPage(new ChatFragment(),getString(R.string.chats));
        pageAdapter.AddFragmentPage(new UsersFragment(),getString(R.string.users));
        pageAdapter.AddFragmentPage(new ProfileFragment(),getString(R.string.profile));
        pageAdapter.AddFragmentPage(new UploadPhotosFragment(),getString(R.string.photos));

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }*/

    private void initViews() {
        profile_img = findViewById(R.id.profile_Image);
        userName = findViewById(R.id.userName);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setTitle("Home");
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
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
    }




    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.myUserToolBar);
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
                }else {
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
                MyFireBase.getAuth().signOut();
                startActivity(new Intent(Main2Activity.this, StartActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    if (AccessToken.getCurrentAccessToken() != null){
                        LoginManager loginManager =LoginManager.getInstance();
                        loginManager.logOut();
                    }

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

    private void setCurrentUserInfo(){
        MyFireBase.getReferenceOnAllUsers().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(currentUserID)){

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


    //this method to keyHash
    //to put in fb sdk
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


}
