package com.example.ahmedd.firabasetest.MyFireBase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFireBase {

    private static FirebaseAuth auth;
    private static DatabaseReference referenceOnAllUsers;
    private static DatabaseReference referenceOnCurrentUser;
    private static DatabaseReference referenceOnDataBase;
    private static DatabaseReference referenceOnChats;
    private static DatabaseReference referenceOnChatList;
    private static FirebaseUser getCurrentUser;
    private static FirebaseDatabase FirebaseDatabase;



    public static FirebaseAuth getAuth() {

        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

    public static FirebaseUser getCurrentUser() {

        return getCurrentUser = auth.getInstance().getCurrentUser();
    }

    private static FirebaseDatabase getGetFirebaseDatabase() {

        if (FirebaseDatabase == null){

            FirebaseDatabase = FirebaseDatabase.getInstance();
        }

        return FirebaseDatabase;
    }

    public static DatabaseReference referenceOnAllUsers() {

            return referenceOnAllUsers = getGetFirebaseDatabase().getReference("Users");
    }

    public static DatabaseReference getReferenceOnCurrentUser() {

        return referenceOnCurrentUser = getGetFirebaseDatabase().getReference("Users").child(getCurrentUser().getUid());
    }

    public static DatabaseReference referenceOnChats() {

        return referenceOnChats = getGetFirebaseDatabase().getReference("Chats");
    }

    public static DatabaseReference getReferenceOnChatList() {

        return referenceOnChatList = getGetFirebaseDatabase().getReference("ChatList");
    }

    public static DatabaseReference getReferenceOnDataBase() {

        return referenceOnDataBase= getGetFirebaseDatabase().getReference();

    }
}
