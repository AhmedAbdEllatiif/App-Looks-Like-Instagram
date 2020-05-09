package com.example.ahmedd.firabasetest.ViewModel;


import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ahmedd.firabasetest.Adapters.PhotosAdapter;
import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Photos>> homeFragmentImages;
    private MutableLiveData<List<Photos>> myPhotosFragmentImages;



    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        homeFragmentImages = new MutableLiveData<>();
        myPhotosFragmentImages = new MutableLiveData<>();
    }

    public LiveData<List<Photos>> getHomeFragmentImages(){
        return homeFragmentImages;
    }
    public LiveData<List<Photos>> getMyPhotosFragmentImages(){
        return myPhotosFragmentImages;
    }

    /**
     * To get the images of the following users to show in the homePage
     * */
    public void getFollowingUsersImagesFromServer(){
        List<Following> followingList = new ArrayList<>();
        DatabaseReference referenceOnPhotos = MyFireBase.getReferenceOnPhotos();
        List<Photos> myList  = new ArrayList<>();

        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Following followingItem = snapshot.getValue(Following.class);
                    followingList.add(followingItem);
                }

                referenceOnPhotos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot photosChild : dataSnapshot.getChildren()) {
                            for (int i = 0; i < followingList.size(); i++) {
                                if (followingList.get(i).getId().equals(photosChild.getKey())) {
                                    referenceOnPhotos.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            referenceOnPhotos.child(photosChild.getKey())
                                                    .child("Myphotos").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot myPhotosChildern : dataSnapshot.getChildren()) {
                                                        Photos photosItem = myPhotosChildern.getValue(Photos.class);

                                                        myList.add(photosItem);
                                                    }
                                                    homeFragmentImages.setValue(myList);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    /**
     * To get the current user images from the server
     * */
    public void getCurrentUserImageFromServer(){
        List<Photos> photosList = new ArrayList<>();
        MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                .child("Myphotos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        photosList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Photos photosItem = snapshot.getValue(Photos.class);
                            photosItem.setKey(snapshot.getKey());
                            photosList.add(photosItem);
                        }
                        List<Photos> updatePhotoList = new ArrayList<>();
                        for (int i = (photosList.size()) - 1; i >= 0; i--) {
                            Photos updatePhotoItem = photosList.get(i);
                            updatePhotoList.add(updatePhotoItem);
                        }

                        myPhotosFragmentImages.setValue(updatePhotoList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


}
