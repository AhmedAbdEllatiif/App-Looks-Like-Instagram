package com.example.ahmedd.firabasetest.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserNameDialogFragment extends DialogFragment {


    private View view;
    private TextInputEditText editText_userName_dialogFragment;
    private Button btn_update;

    public EditUserNameDialogFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_edit_user_name_dialog, container, false);

        editText_userName_dialogFragment = view.findViewById(R.id.editText_userName_dialogFragment);
        editText_userName_dialogFragment.setText(MyFireBase.getCurrentUser().getDisplayName());
        editText_userName_dialogFragment.requestFocus();
        btn_update = view.findViewById(R.id.btn_update);


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_userName_dialogFragment.getText().toString().trim().isEmpty()){
                    editText_userName_dialogFragment.setError("Enter new user name");
                }else {
                    MyFireBase.getReferenceOnAllUsers().addValueEventListener(new ValueEventListener() {
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


        return view;
    }

}
