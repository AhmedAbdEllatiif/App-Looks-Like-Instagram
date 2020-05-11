package com.example.ahmedd.firabasetest.Fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.BaseActivities.BaseDialogFragment;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.example.ahmedd.firabasetest.Fragments.MainFragments.AccountFragment.IMAGE_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserNameDialogFragment extends BaseDialogFragment {


    private View view;
    private TextInputEditText editText_userName_dialogFragment;
    private Button btn_update;
    private ImageView img_profile;
    private StorageTask uploadTask;
    private Uri image_uri;
    private ValueEventListener stopUpdateListener;

    public EditUserNameDialogFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_edit_user_name_dialog, container, false);

        img_profile = view.findViewById(R.id.img_profile_editImage);
        editText_userName_dialogFragment = view.findViewById(R.id.editText_userName_dialogFragment);
        //editText_userName_dialogFragment.setText(MyFireBase.getCurrentUser().getDisplayName());
        editText_userName_dialogFragment.requestFocus();
        btn_update = view.findViewById(R.id.btn_update);


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_userName_dialogFragment.getText().toString().trim().isEmpty()){
                    editText_userName_dialogFragment.setError("Enter new user name");
                }else {
                    stopUpdateListener = MyFireBase.getReferenceOnAllUsers().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                User user = snapshot.getValue(User.class);
                                if(user.getId().equals(MyFireBase.getCurrentUser().getUid())){
                                    HashMap<String,Object> hashMap =  new HashMap<>();
                                    String newUserName = editText_userName_dialogFragment.getText().toString().trim();
                                    hashMap.put("userName",newUserName);
                                    snapshot.getRef().updateChildren(hashMap);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }

        });

       // setProfileData();
        return view;
    }

    private void setProfileData() {
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user =  dataSnapshot.getValue(User.class);


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
        showProgressBar("please wait","uploading your profile Image");
        Log.e("UploadImage","begin the method");

        if (image_uri!=null){
            MyFireBase.getStorageReferenceOnUploads().child(System.currentTimeMillis() +
                    "." + getFileExtension(image_uri));

            uploadTask =  MyFireBase.getStorageReferenceOnUploads().putFile(image_uri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        Log.e("onComplete","not Successful");
                        throw task.getException();

                    }
                    return  MyFireBase.getStorageReferenceOnUploads().getDownloadUrl();
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
                        hideProgressBar();
                    }else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        hideProgressBar();
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

 /*   @Override
    public void onPause() {
        super.onPause();
        if (!stopUpdateListener.equals(null)){
        MyFireBase.getReferenceOnAllUsers().removeEventListener(stopUpdateListener);
        }
    }*/
}

