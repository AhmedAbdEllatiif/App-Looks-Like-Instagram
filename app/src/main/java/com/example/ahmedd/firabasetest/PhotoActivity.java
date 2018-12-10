package com.example.ahmedd.firabasetest;

import android.content.ContentResolver;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {




    private TextInputEditText edit_txt_photo_name;
    private TextInputEditText edit_txt_photo_decription;
    private TextInputLayout inputLayout_imgName;
    private TextInputLayout inputLayout_description;
    private ImageView img_upload;
    private Button btn_upload;
    private TextView txt_uploading;
    private TextView txt_choose_image;
    private String currentTime;

    private Uri img_uri;
    private StorageTask mUploadTask;

    private String photoName;
    private String photoDescription = "";
    private String currentUserName;
    private String currentUserImageURl;

    private List<Following> followingList;


    String currentDate;
    Date date1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        edit_txt_photo_name = findViewById(R.id.edit_txt_photo_name);
        edit_txt_photo_decription = findViewById(R.id.edit_txt_photo_decription);
        img_upload = findViewById(R.id.img_upload);
        btn_upload = findViewById(R.id.btn_share);
        txt_uploading = findViewById(R.id.txt_uploading);
        txt_choose_image = findViewById(R.id.txt_choose_image);
        inputLayout_imgName = findViewById(R.id.inputLayout_imgName);
        inputLayout_description = findViewById(R.id.inputLayout_description);


        setToolBar();
        MyClickListeners();
        openFileChooser();

        editTextWatchers();

        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        currentUserName = user.getUserName();
                        currentUserImageURl = user.getImageURL();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        currentDate = getCurrentTime();
        date1 = null;


    }

    private void editTextWatchers() {
        edit_txt_photo_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 15) {
                    edit_txt_photo_name.setError("invalid must be less than 15 letters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edit_txt_photo_decription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 100) {
                    edit_txt_photo_decription.setError("invalid must be less than 100 letters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openFileChooser(){
//using Android-Image-Cropper library
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

/*// start cropping activity for pre-acquired image saved on the device
        CropImage.activity(img_uri)
                .start(getActivity());*/


/*// for fragment (DO NOT use `getActivity()`)
        CropImage.activity()
                .start(getContext(), this);*/
    }

    public String getCurrentTime() {
        DateFormat dateFormat =  new SimpleDateFormat("EEE,d MMM yyyy HH:mm");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentTime = dateFormat.format(Calendar.getInstance().getTime());
        }
        return currentTime;
    }
    private void MyClickListeners(){

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Snackbar.make(v, "uploading please wait..", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {

                            if (edit_txt_photo_name.getText().toString().trim().isEmpty()) {
                                edit_txt_photo_name.setError("Enter photo name");

                            } else if (!edit_txt_photo_name.getText().toString().trim().isEmpty()
                                    && edit_txt_photo_decription.getText().toString().trim().isEmpty()) {

                                if (edit_txt_photo_name.getText().toString().length() > 15) {
                                    edit_txt_photo_name.setError("invalid must be less than 15 letters");
                                }
                                photoName = edit_txt_photo_name.getText().toString().trim();
                                txt_uploading.setVisibility(View.VISIBLE);
                                txt_choose_image.setVisibility(View.INVISIBLE);
                                txt_uploading.setText("Uploading...");
                                upload();

                            } else if (!edit_txt_photo_name.getText().toString().trim().isEmpty()
                                    && !edit_txt_photo_decription.getText().toString().trim().isEmpty()) {

                                if (edit_txt_photo_name.getText().toString().length() > 15) {
                                    edit_txt_photo_name.setError("invalid must be less than 15 letters");
                                } else if (edit_txt_photo_decription.getText().toString().length() > 100) {
                                    edit_txt_photo_decription.setError("invalid must be less than 100 letters");
                                } else {
                                    photoName = edit_txt_photo_name.getText().toString().trim();
                                    photoDescription = edit_txt_photo_decription.getText().toString().trim();

                                    txt_uploading.setVisibility(View.VISIBLE);
                                    txt_choose_image.setVisibility(View.INVISIBLE);
                                    txt_uploading.setText("Uploading...");
                                    upload();
                                }
                            }
                        }

                    }
                });

            }
        });

        txt_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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
                startActivity(new Intent(PhotoActivity.this,Main2Activity.class)
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

    private String getFileExtension(Uri uri){
        ContentResolver resolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void upload() {

        if (img_uri!=null){
            final StorageReference fileReference = MyFireBase.getStorageReferenceOnPhotos().child(System.currentTimeMillis() +
                    "." + getFileExtension(img_uri));
            mUploadTask = fileReference.putFile(img_uri);


            mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        Log.e("onComplete","not Successful");
                        throw task.getException();

                    }
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Log.e("onComplete","Successful");

                        String mydate = null;
                        String imgURL = "";

                        Uri downloadUri = task.getResult();
                        if(downloadUri!=null){
                            imgURL = downloadUri.toString();
                        }

                        if (photoDescription.equals("")){
                            photoDescription = "write a description";
                        }

                        DateFormat dateFormat =  new SimpleDateFormat("yyyyMMddHHmmss");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                           mydate = dateFormat.format(Calendar.getInstance().getTime());
                        }


                        final HashMap<String, Object> hashMap = getHashMap(photoName, photoDescription, getCurrentTime(),
                                mydate, imgURL, currentUserName, currentUserImageURl);


                        Handler setThePhotoDataInTheUserBranch = new Handler();
                        setThePhotoDataInTheUserBranch.post(new Runnable() {
                            @Override
                            public void run() {
                                MyFireBase.getReferenceOnDataBase().child("Photos").child(MyFireBase.getCurrentUser().getUid())
                                        .child("Myphotos").push().setValue(hashMap);
                                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                        .child("PhotosOfFollowing").push().setValue(hashMap);
                            }
                        });


                        //To the following & To make a branch PhotosOfFollowing
                        Handler handler =  new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //To the following
                                followingList = new ArrayList<>();
                                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                        .child("Following").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            Following followingItem = snapshot.getValue(Following.class);
                                            Log.e("PhotoActivityFollowItem",followingItem.getId());
                                            followingList.add(followingItem);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //To make a branch PhotosOfFollowing
                                final DatabaseReference databaseReference = MyFireBase.getReferenceOnAllUsers();
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            for (int i=0;i<followingList.size();i++){
                                                    Log.e("SnapShotKey",snapshot.getKey());
                                                if (followingList.get(i).getId().equals(snapshot.getKey())){
                                                    databaseReference.child(snapshot.getKey()).child("PhotosOfFollowing").push().setValue(hashMap);
                                                       break;
                                                }
                                            }
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        });





                        txt_uploading.setText("Upload Complete");
                        txt_uploading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_, 0);
                        txt_uploading.setCompoundDrawablePadding(3);
                        Handler handlerToShowUploadComplete = new Handler();
                        handlerToShowUploadComplete.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txt_uploading.setVisibility(View.GONE);
                            }
                        },2000);
                    }else {
                        Toast.makeText(PhotoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        txt_uploading.setText("Failed..");
                        Handler handlerToShowUploadComplete = new Handler();
                        handlerToShowUploadComplete.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txt_uploading.setVisibility(View.GONE);

                            }
                        },2000);
                    }
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PhotoActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    txt_uploading.setVisibility(View.GONE);
                }
            });
        }else {
            txt_uploading.setVisibility(View.GONE);
            Toast.makeText(PhotoActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @NonNull
    private HashMap<String, Object> getHashMap(String photoName, String photoDescription, String currentTime
            , String mydate, String imgURL, String currentUserName, String currentUserImageURl) {
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", photoName);
        hashMap.put("url", imgURL);
        hashMap.put("description", photoDescription);
        hashMap.put("date", currentTime);
        hashMap.put("userName", currentUserName);
        hashMap.put("userImage", currentUserImageURl);
        hashMap.put("uploadedTime", Long.parseLong(mydate));
        return hashMap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
          /*  if(data.equals(null)){
                finish();
            }
*/
            if (resultCode == RESULT_OK) {
                if(!result.getUri().equals(null)){
                img_uri = result.getUri();
                Log.e("uri",img_uri.toString());
                Picasso.get().load(img_uri).into(img_upload);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(PhotoActivity.this, "something went wrong ", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
