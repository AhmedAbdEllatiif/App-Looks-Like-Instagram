package com.example.ahmedd.firabasetest.Fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.MainActivity;

import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.PhotoActivity;
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
                upload();
            }
        });
    }


    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUSET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUSET && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            img_uri = data.getData();
            Picasso.get().load(img_uri).into(img_upload);
        }//ifCondition

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
                                txt_uploading.setVisibility(View.INVISIBLE);
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
