package com.example.ahmedd.firabasetest.Fragments.MainFragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.BaseActivities.BaseFragment;
import com.example.ahmedd.firabasetest.Fragments.EditUserNameDialogFragment;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
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

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    public static final int IMAGE_REQUEST = 1;

    private View view;
    private TextView userName;
    private ImageView img_profile;
    private ImageButton img_btn_edit;

    private StorageTask uploadTask;
    private Uri image_uri;

    public ProfileFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.txt_userName_profileActivity);
        img_profile = view.findViewById(R.id.img_profile_frahmentProfile);
        img_btn_edit = view.findViewById(R.id.img_btn_edit);


        img_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserNameDialogFragment dialogFragment = new EditUserNameDialogFragment();
                dialogFragment.show(getChildFragmentManager(),"Dialog Fragment");
            }
        });


        setProfileData();

        changeUserName();
        return view;
    }

    private void changeUserName(){
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserNameDialogFragment dialogFragment = new EditUserNameDialogFragment();
                dialogFragment.show(getChildFragmentManager(),"Dialog Fragment");
            }
        });
    }

    private void setProfileData() {
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user =  dataSnapshot.getValue(User.class);

               userName.setText(user.getUserName());
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

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }

    private void openImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver resolver = getContext().getContentResolver();
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
                if (image_uri!=null){
                    final StorageReference storageReference= MyFireBase.getStorageReferenceOnUploads().child(System.currentTimeMillis() +
                            "." + getFileExtension(image_uri));
                    uploadTask = storageReference.putFile(image_uri);


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
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                //hideProgressBar();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        };
        handler.post(runnable);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Log.e("onActivityResult","it is here");
      Log.e("requestCode",requestCode+"");

        if (requestCode == IMAGE_REQUEST && data != null && data.getData() != null){
                image_uri = data.getData();
                Log.e("image_uri",image_uri+"");
                if (uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(getContext(), "Uploading in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uploadProfileImage();
                }
        }
    }
}
