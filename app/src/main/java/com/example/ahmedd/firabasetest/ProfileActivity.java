package com.example.ahmedd.firabasetest;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {



    private ImageView img_profile;
    private StorageTask uploadTask;
    private Uri img_uri;
    private TextView email_cardView_profileActivity;
    private TextView txt_userName_profileActivity;
    private ImageButton img_btn_edit;
    private EditText editText_username_profileActivity;
    private Button btn_update_profileActivity;
    private Button btn_cancel_profileActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        MyOnClickListeners();


    }

    private void MyOnClickListeners() {
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
        img_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_username_profileActivity.setText(MyFireBase.getCurrentUser().getDisplayName());
                txt_userName_profileActivity.setVisibility(View.INVISIBLE);
                email_cardView_profileActivity.setVisibility(View.INVISIBLE);
                editText_username_profileActivity.setVisibility(View.VISIBLE);
                editText_username_profileActivity.requestFocus();
                btn_update_profileActivity.setVisibility(View.VISIBLE);
                btn_cancel_profileActivity.setVisibility(View.VISIBLE);
            }
        });

        btn_cancel_profileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_username_profileActivity.setVisibility(View.INVISIBLE);
                txt_userName_profileActivity.setVisibility(View.VISIBLE);
                email_cardView_profileActivity.setVisibility(View.VISIBLE);
                btn_update_profileActivity.setVisibility(View.GONE);
                btn_cancel_profileActivity.setVisibility(View.GONE);
            }
        });

        btn_update_profileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });


    }

    private void initViews() {
        email_cardView_profileActivity = findViewById(R.id.email_cardView_profileActivity);
        txt_userName_profileActivity = findViewById(R.id.txt_userName_profileActivity);
        editText_username_profileActivity = findViewById(R.id.editText_username_profileActivity);
        img_profile = findViewById(R.id.img_profile_frahmentProfile);
        img_btn_edit = findViewById(R.id.img_btn_edit);
        btn_update_profileActivity = findViewById(R.id.btn_update_profileActivity);
        btn_cancel_profileActivity = findViewById(R.id.btn_cancel_profileActivity);


        email_cardView_profileActivity.setText(MyFireBase.getCurrentUser().getEmail());
        txt_userName_profileActivity.setText(MyFireBase.getCurrentUser().getDisplayName());
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
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


    private String getFileExtension(Uri uri){
        ContentResolver resolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void uploadProfileImage(){
        //showProgressBar("please wait","uploading your profile Image");
        Log.e("UploadImage","begin the method");

        Handler handler = new Handler();
        Runnable runnable=  new Runnable() {
            @Override
            public void run() {
                if (img_uri!=null){
                    final StorageReference storageReference= MyFireBase.getStorageReferenceOnUploads().child(System.currentTimeMillis() +
                            "." + getFileExtension(img_uri));
                    uploadTask = storageReference.putFile(img_uri);


                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if(!task.isSuccessful()){
                                Log.e("onComplete","not Successful");
                                throw task.getException();

                            }
                            return  storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Log.e("onComplete","Successful");
                                Uri downloadUri = task.getResult();
                                String myUri = downloadUri.toString();

                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("ImageURL",myUri);
                                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                        .updateChildren(hashMap);
                                //hideProgressBar();
                            }else {
                                Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                //hideProgressBar();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ProfileActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        };
        handler.post(runnable);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.equals(null)){
            finish();
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                if(!result.getUri().equals(null)){
                    img_uri = result.getUri();
                    Picasso.get().load(img_uri).into(img_profile);
                    uploadProfileImage();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(ProfileActivity.this, "something went wrong ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}




