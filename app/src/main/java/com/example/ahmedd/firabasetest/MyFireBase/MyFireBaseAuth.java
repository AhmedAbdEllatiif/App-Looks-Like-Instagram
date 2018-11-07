package com.example.ahmedd.firabasetest.MyFireBase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFireBaseAuth {

    private static FirebaseAuth auth;
    private static FirebaseUser firebaseUser;
    private static  DatabaseReference reference;


    public static FirebaseAuth getAuth(){

        if(auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

    public static FirebaseUser getUser(){

        if (firebaseUser == null){
            firebaseUser = auth.getInstance().getCurrentUser();
        }
        return firebaseUser;
    }

    public static DatabaseReference referenceOnUserChild(){
        if (reference == null){
            reference = FirebaseDatabase.getInstance().getReference("Users").child(getUser().getUid());
        }
        return reference;
    }

    public static DatabaseReference referenceOnAllUsers(){
        if (reference == null){
            reference = FirebaseDatabase.getInstance().getReference("Users");

        }
        return reference;
    }
}
