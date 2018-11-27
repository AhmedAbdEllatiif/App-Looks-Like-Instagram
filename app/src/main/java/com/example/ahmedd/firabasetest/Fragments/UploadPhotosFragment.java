package com.example.ahmedd.firabasetest.Fragments;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.PhotoActivity;
import com.example.ahmedd.firabasetest.R;

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
    }

}
