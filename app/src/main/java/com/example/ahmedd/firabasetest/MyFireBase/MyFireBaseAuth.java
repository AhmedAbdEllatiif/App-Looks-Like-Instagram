package com.example.ahmedd.firabasetest.MyFireBase;

import com.google.firebase.auth.FirebaseAuth;

public class MyFireBaseAuth {

    private static FirebaseAuth auth;


    public static FirebaseAuth getAuth(){

        if(auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }
}
