package com.example.ahmedd.firabasetest;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Fragments.DatePickerDialogFragment;
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

public class ProfileActivity extends AppCompatActivity implements DatePickerDialogFragment.OnCompleteListener {

    //MyViews
    private TextView email_cardView_profileActivity;
    private TextView txt_userName_profileActivity;
    private TextView txt_change_profileImage;
    private TextView txt_birthday;
    private ImageView img_profile;
    private ImageButton img_btn_edit;
    private ImageButton img_btn_calender;
    private EditText editText_username_profileActivity;
    private EditText edit_txt_birthday;
    private Button btn_update_profileActivity;
    private Button btn_cancel_profileActivity;


    //Variables to update profile Info
    private String updatedUserName;
    private String updatedBirthday;
    private String myUri;
    private Uri img_uri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setToolBar();
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
                txt_birthday.setVisibility(View.INVISIBLE);
                editText_username_profileActivity.setVisibility(View.VISIBLE);
                editText_username_profileActivity.requestFocus();
                btn_update_profileActivity.setVisibility(View.VISIBLE);
                btn_cancel_profileActivity.setVisibility(View.VISIBLE);
                txt_change_profileImage.setVisibility(View.VISIBLE);
                img_btn_calender.setVisibility(View.VISIBLE);
                edit_txt_birthday.setVisibility(View.VISIBLE);
            }
        });

        btn_cancel_profileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_userName_profileActivity.setVisibility(View.VISIBLE);
                txt_birthday.setVisibility(View.VISIBLE);
                editText_username_profileActivity.setVisibility(View.INVISIBLE);
                btn_update_profileActivity.setVisibility(View.GONE);
                btn_cancel_profileActivity.setVisibility(View.GONE);
                txt_change_profileImage.setVisibility(View.GONE);
                img_btn_calender.setVisibility(View.GONE);
                edit_txt_birthday.setVisibility(View.GONE);
            }
        });

        btn_update_profileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();


            }
        });

        txt_change_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        img_btn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerDialogFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


    }

    private void initViews() {
        email_cardView_profileActivity = findViewById(R.id.email_cardView_profileActivity);
        txt_userName_profileActivity = findViewById(R.id.txt_userName_profileActivity);
        txt_change_profileImage = findViewById(R.id.txt_change_profileImage);
        editText_username_profileActivity = findViewById(R.id.editText_username_profileActivity);
        edit_txt_birthday = findViewById(R.id.edit_txt_birthday);
        img_profile = findViewById(R.id.img_profile_frahmentProfile);
        img_btn_edit = findViewById(R.id.img_btn_edit);
        btn_update_profileActivity = findViewById(R.id.btn_update_profileActivity);
        btn_cancel_profileActivity = findViewById(R.id.btn_cancel_profileActivity);
        txt_birthday = findViewById(R.id.txt_birthday);
        img_btn_calender = findViewById(R.id.img_btn_calender);




        email_cardView_profileActivity.setText(MyFireBase.getCurrentUser().getEmail());
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        txt_userName_profileActivity.setText(user.getUserName());
                        txt_birthday.setText(user.getBirthday());
                        edit_txt_birthday.setText(user.getBirthday());

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

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.profileActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView userNameToolBar = findViewById(R.id.profileActivity_userName);
        final ImageView imgToolBar= findViewById(R.id.profileActivity_profile_Image);

        //set user data in the toolBar
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (user.getUserName() != null) {
                    userNameToolBar.setText(user.getUserName());
                }

                if (user.getImageURL().equals("default")) {
                    imgToolBar.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageURL()).into(imgToolBar);
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

    private void updateUserProfile() {
        Log.e("UploadImage","begin the method");

        final HashMap<String, Object> hashMap = new HashMap<>();
        updatedBirthday = edit_txt_birthday.getText().toString().trim();
        updatedUserName = editText_username_profileActivity.getText().toString().trim();


        Handler handler = new Handler();
        Runnable runnable=  new Runnable() {
            @Override
            public void run() {
                if (img_uri != null && !editText_username_profileActivity.getText().toString().trim().isEmpty()
                        && !edit_txt_birthday.getText().toString().trim().isEmpty()) {

                    final StorageReference storageReference= MyFireBase.getStorageReferenceOnUploads().child(System.currentTimeMillis() +
                            "." + getFileExtension(img_uri));
                    uploadTask = storageReference.putFile(img_uri);


                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task){

                            if (!task.isSuccessful()) {
                                Log.e("onComplete", "not Successful");

                            }
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.e("onComplete", "Successful");
                                Uri downloadUri = task.getResult();
                                if (downloadUri == null) {
                                    Log.e("Uri", "null");
                                } else {

                                    myUri = downloadUri.toString();
                                }

                                hashMap.put("ImageURL", myUri);
                                hashMap.put("userName", updatedUserName);
                                hashMap.put("birthday", updatedBirthday);
                                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                        .updateChildren(hashMap);

                            } else {
                                Toast.makeText(ProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                if (img_uri == null) {

                    if (img_profile.getResources().equals(R.mipmap.ic_launcher)) {
                        Toast.makeText(ProfileActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "No  new Image Selected", Toast.LENGTH_SHORT).show();

                    }
                }

                if (editText_username_profileActivity.getText().toString().trim().isEmpty() ||
                        edit_txt_birthday.getText().toString().trim().isEmpty()) {

                    if (editText_username_profileActivity.getText().toString().trim().isEmpty()) {
                        editText_username_profileActivity.setError("username can't be empty");
                    }

                    if (edit_txt_birthday.getText().toString().trim().isEmpty()) {
                        edit_txt_birthday.setError("birthday can't be empty");
                    }
                } else {
                    hashMap.put("userName", updatedUserName);
                    hashMap.put("birthday", updatedBirthday);
                        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                .updateChildren(hashMap);
                    }


            }
        };
        handler.post(runnable);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                if(!result.getUri().equals(null)){
                    img_uri = result.getUri();
                    Picasso.get().load(img_uri).into(img_profile);
                    updateUserProfile();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(ProfileActivity.this, "something went wrong ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onComplete(String birthday) {
        edit_txt_birthday.setText(birthday);
    }
}




