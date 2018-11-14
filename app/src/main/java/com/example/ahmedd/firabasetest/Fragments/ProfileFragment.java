package com.example.ahmedd.firabasetest.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private View view;
    private TextView userName;
    private ImageView img_profile;
    public ProfileFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.txt_userName_fragmentProfile);
        img_profile = view.findViewById(R.id.img_profile_frahmentProfile);

        setProfileData();


        return view;
    }

    private void setProfileData() {
        MyFireBase.referenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
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
    }


}
