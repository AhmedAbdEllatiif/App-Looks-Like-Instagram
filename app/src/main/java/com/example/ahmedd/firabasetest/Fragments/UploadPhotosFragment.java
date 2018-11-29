package com.example.ahmedd.firabasetest.Fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.PhotoActivity;
import com.example.ahmedd.firabasetest.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadPhotosFragment extends Fragment {

    private View view;
    private Button btn_pick_photo;
    private TextInputEditText edit_txt_photo_name;
    private ImageView img_upload;
    private Button btn_upload;
    private TextView txt_showUploads;
    private TextView txt_uploading;


    private static final int PICK_IMAGE_REQUSET = 2;
    private Uri img_uri;
    private StorageTask mUploadTask;

    public UploadPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_upload_photos, container, false);

        btn_pick_photo = view.findViewById(R.id.btn_pick_photo);
        edit_txt_photo_name = view.findViewById(R.id.edit_txt_photo_name);
        img_upload = view.findViewById(R.id.img_upload);
        btn_upload = view.findViewById(R.id.btn_upload);
        txt_showUploads = view.findViewById(R.id.txt_showUploads);
        txt_uploading = view.findViewById(R.id.txt_uploading);


        MyClickListeners();




        return view;
    }


    private void MyClickListeners(){
        txt_showUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), PhotoActivity.class);
                startActivity(intent);
            }
        });


        btn_pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_uploading.setVisibility(View.VISIBLE);
                txt_uploading.setText("Uploading...");
                upload();
            }
        });
    }


    private void openFileChooser(){
//using Android-Image-Cropper library
       /* // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getActivity());

// start cropping activity for pre-acquired image saved on the device
        CropImage.activity(img_uri)
                .start(getActivity());*/


// for fragment (DO NOT use `getActivity()`)
        CropImage.activity()
                .start(getContext(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                img_uri = result.getUri();

                Picasso.get().load(img_uri).into(img_upload);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "something went wrong ", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private String getFileExtension(Uri uri){
        ContentResolver resolver = getActivity().getContentResolver();
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
                        Uri downloadUri = task.getResult();
                        String imgURL = "default";
                        if(downloadUri!=null){
                            imgURL = downloadUri.toString();
                        }


                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("name",name);
                        hashMap.put("url",imgURL);
                        MyFireBase.getReferenceOnDataBase().child("Photos").child(MyFireBase.getCurrentUser().getUid()).push().setValue(hashMap);

                        txt_uploading.setText("Upload Complete");
                        Handler handlerToShowUploadComplete = new Handler();
                        handlerToShowUploadComplete.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txt_uploading.setVisibility(View.GONE);
                            }
                        },2000);
                    }else {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    txt_uploading.setVisibility(View.GONE);
                }
            });
        }else {
            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
            txt_uploading.setVisibility(View.GONE);
        }


    }


}
