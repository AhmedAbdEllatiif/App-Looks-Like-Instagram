package com.example.ahmedd.firabasetest.MyFireBase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyFireBase {

    private static FirebaseAuth auth;
    private static DatabaseReference referenceOnAllUsers;
    private static DatabaseReference referenceOnCurrentUser;
    private static DatabaseReference referenceOnDataBase;
    private static DatabaseReference referenceOnChats;
    private static DatabaseReference referenceOnPhotos;
    private static DatabaseReference referenceOnChatList;
    private static DatabaseReference referenceOnFollowers;
    private static FirebaseUser currentUser;
    private static StorageReference storageReferenceOnUploads;
    private static FirebaseDatabase FirebaseDatabase;
    private static FirebaseStorage firebaseStorage;
    private static StorageReference storageReferenceOnPhotos;



    public static FirebaseAuth getAuth() {

        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }
    public static FirebaseStorage getFirebaseStorage() {

        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance();
        }

        return firebaseStorage;
    }
    public static StorageReference getStorageReferenceOnUploads() {

        if (storageReferenceOnUploads == null) {
            storageReferenceOnUploads = getFirebaseStorage().getReference("Uploads");
        }

        return storageReferenceOnUploads;
    }
    public static StorageReference getStorageReferenceOnPhotos() {

        if (storageReferenceOnPhotos == null) {
            storageReferenceOnPhotos = getFirebaseStorage().getReference("photos");
        }

        return storageReferenceOnPhotos;
    }
    public static FirebaseUser getCurrentUser() {

        if (currentUser == null){
            currentUser = getAuth().getInstance().getCurrentUser();
        }
        return currentUser;
    }

    private static FirebaseDatabase getGetFirebaseDatabase() {

        if (FirebaseDatabase == null){

            FirebaseDatabase = FirebaseDatabase.getInstance();
        }

        return FirebaseDatabase;
    }

    public static DatabaseReference getReferenceOnAllUsers() {

            return referenceOnAllUsers = getGetFirebaseDatabase().getReference("Users");
    }

    public static DatabaseReference getReferenceOnCurrentUser() {

        return referenceOnCurrentUser = getGetFirebaseDatabase().getReference("Users").child(getCurrentUser().getUid());
    }

    public static DatabaseReference getReferenceOnChats() {

        return referenceOnChats = getGetFirebaseDatabase().getReference("Chats");
    }

    public static DatabaseReference getReferenceOnChatList() {

        return referenceOnChatList = getGetFirebaseDatabase().getReference("ChatList");
    }

    public static DatabaseReference getReferenceOnPhotos() {

        return referenceOnPhotos = getGetFirebaseDatabase().getReference("Photos");
    }

    public static DatabaseReference getReferenceOnFollowers() {

        return referenceOnFollowers = getGetFirebaseDatabase().getReference("Following");
    }

    public static DatabaseReference getReferenceOnDataBase() {

        return referenceOnDataBase= getGetFirebaseDatabase().getReference();

    }

    public static String getCurrentUserID(){
       return getCurrentUser().getUid();
    }
}
