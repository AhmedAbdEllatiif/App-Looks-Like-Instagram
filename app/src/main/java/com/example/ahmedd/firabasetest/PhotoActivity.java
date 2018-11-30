package com.example.ahmedd.firabasetest;

import android.content.ContentResolver;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private String currentTime;

    private Uri img_uri;
    private StorageTask mUploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        edit_txt_photo_name = findViewById(R.id.edit_txt_photo_name);
        edit_txt_photo_decription = findViewById(R.id.edit_txt_photo_decription);
        img_upload = findViewById(R.id.img_upload);
        btn_upload = findViewById(R.id.btn_upload);
        txt_uploading = findViewById(R.id.txt_uploading);
        inputLayout_imgName = findViewById(R.id.inputLayout_imgName);
        inputLayout_description = findViewById(R.id.inputLayout_description);


        setToolBar();
        MyClickListeners();
        openFileChooser();

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
            public void onClick(View v) {
                txt_uploading.setVisibility(View.VISIBLE);
                txt_uploading.setText("Uploading...");
                upload();

            }
        });

       ;
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


        Log.e("URI", img_uri.toString());
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


                        String name = edit_txt_photo_name.getText().toString().trim();

                        String description = edit_txt_photo_decription.getText().toString().trim();
                        String date = getCurrentTime();

                        Uri downloadUri = task.getResult();
                        String imgURL = "default";
                        if(downloadUri!=null){
                            imgURL = downloadUri.toString();
                        }


                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("name",name);
                        hashMap.put("url",imgURL);
                        hashMap.put("description",description);
                        hashMap.put("date",date);

                        MyFireBase.getReferenceOnDataBase().child("Photos").child(MyFireBase.getCurrentUser().getUid()).push().setValue(hashMap);

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
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PhotoActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    txt_uploading.setVisibility(View.GONE);
                }
            });
        }else {
            Toast.makeText(PhotoActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
            txt_uploading.setVisibility(View.GONE);
        }
finish();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                img_uri = result.getUri();
                Picasso.get().load(img_uri).into(img_upload);

                inputLayout_description.setVisibility(View.VISIBLE);
                inputLayout_imgName.setVisibility(View.VISIBLE);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(PhotoActivity.this, "something went wrong ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
