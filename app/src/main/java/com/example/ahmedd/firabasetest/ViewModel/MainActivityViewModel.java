package com.example.ahmedd.firabasetest.ViewModel;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.DirectMessageFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.HomeFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.ProfileFragments.ProfileFragment;
import com.example.ahmedd.firabasetest.Helpers.OnBackListener_ChatFragment;
import com.example.ahmedd.firabasetest.Helpers.OnMyViewPagerListener;
import com.example.ahmedd.firabasetest.Helpers.OnToolBarIconsListener;
import com.example.ahmedd.firabasetest.Model.ChatList;
import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.Model.PostModel;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = "MainActivityViewModel";

    //LiveData
    private MutableLiveData<List<Photos>> homeFragmentImages;
    private MutableLiveData<List<Photos>> myPhotosFragmentImages;
    private MutableLiveData<List<User>> userList_chatWith_liveData;
    private MutableLiveData<User> currentUserData_liveData;
    private MutableLiveData<List<PostModel>> postModel_liveData;
    private MutableLiveData<List<Following>> followingsUsers;



    //ToolBar Listeners
    public OnToolBarIconsListener onToolBarIconsListener;
    public OnBackListener_ChatFragment onBackListener_chatFragment;


    //ViewPager Listener
    public OnMyViewPagerListener onMyViewPagerListener;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        initLiveData();
    }


    /**
     * to initialize live data
     */
    private void initLiveData() {
        homeFragmentImages = new MutableLiveData<>();
        myPhotosFragmentImages = new MutableLiveData<>();
        userList_chatWith_liveData = new MutableLiveData<>();
        currentUserData_liveData = new MutableLiveData<>();
        postModel_liveData = new MutableLiveData<>();
        followingsUsers = new MutableLiveData<>();

    }


    /*////////////////////////////////Request data from FireBase/////////////////////////////////////////////////*/

    /**
     * To request the images of the following users to show in the homePage
     */

    private String oneOfFollowingUsers_ID;
    private String photoID;
    public void requestFollowingUsersImagesFromServer(List<Following> followingList) {
        List<DataSnapshot> photosIDs = new ArrayList<>();
        DatabaseReference referenceOnPhotos = MyFireBase.getReferenceOnPhotos();

        referenceOnPhotos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot photosChild : dataSnapshot.getChildren()) {
                    for (int i = 0; i < followingList.size(); i++) {
                    oneOfFollowingUsers_ID = followingList.get(i).getId();
                    photoID = photosChild.getKey();
                        if (oneOfFollowingUsers_ID .equals(photoID)) {
                            photosIDs.add(photosChild);
                        }
                    }
                }

                requestPhotosOfFollowing_accordingToPhotosID(photosIDs);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void requestPhotosOfFollowing_accordingToPhotosID(List<DataSnapshot> photosOfFollowings) {
        DatabaseReference referenceOnPhotos = MyFireBase.getReferenceOnPhotos();
        for (DataSnapshot image : photosOfFollowings){
            referenceOnPhotos.child(image.getKey())
                    .child("Myphotos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Photos> myList  = new ArrayList<>();
                    myList.clear();
                    for (DataSnapshot myPhotosChildern : dataSnapshot.getChildren()) {
                        Photos photosItem = myPhotosChildern.getValue(Photos.class);
                        myList.add(photosItem);
                    }
                    //createPostsModelList(myList);
                    Log.e(TAG, "onDataChange44: photosSize ==> " + myList.size());
                    homeFragmentImages.setValue(myList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }



    /**
     * To request the current user images from the server
     */
    public void requestCurrentUserImageFromServer() {
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


    /**
     * To request the current user ChatList with other users
     */
    public void requestChatListFromServer() {

        DatabaseReference referenceOnUsersThatHaveChatWith = MyFireBase.getReferenceOnChatList()
                .child(MyFireBase.getCurrentUser().getUid());

        referenceOnUsersThatHaveChatWith.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatList> myChatsList = new ArrayList<>();
                myChatsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    myChatsList.add(chatList);
                }


                MyFireBase.getReferenceOnAllUsers().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<User> usersList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            for (ChatList chatListItem : myChatsList) {
                                if (user.getId().equals(chatListItem.getId())) {
                                    usersList.add(user);
                                }
                                userList_chatWith_liveData.setValue(usersList);
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
     * To get current user data from firebase
     */
    public void requestCurrentUserDataFromServer() {
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        currentUserData_liveData.setValue(user);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public void requestFollowingOfCurrentUser() {
        MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                .child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Following> followingList = new ArrayList<>();
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Following followingItem = snapshot.getValue(Following.class);
                    followingList.add(followingItem);
                }
                followingsUsers.setValue(followingList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "requestFollowingOfCurrentUser ==> onCancelled: " + databaseError);
            }
        });
    }


    public void createPostsModelList(List<Photos> photos) {
        Log.e(TAG, "createPostsModelList: photosSize ==> " + photos.size());
        List<User> users = new ArrayList<>();
        List<PostModel> postModels = new ArrayList<>();
        List<String> usersIDs = new ArrayList<>();


        for (Photos photo : photos) {
            usersIDs.add(photo.getUserID());

        }
        for (String id : usersIDs) {
            MyFireBase.getReferenceOnAllUsers().child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);
                    Log.e(TAG, "onDataChange: userName ==> " + user.getUserName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        Log.e(TAG, "createPostsModelList: usersSize ==> " + users.size());
        if (usersIDs.size() == users.size()) {
            for (int i = 0; i < photos.size(); i++) {
                postModels.add(new PostModel(photos.get(i)));
            }
            postModel_liveData.setValue(postModels);
        }


    }



    /*///////////////////////////////////Getters for liveData/////////////////////////////////////////////////*/

    /**
     * Getter for {@link HomeFragment} fragment images form liveData
     */
    public LiveData<List<Photos>> getHomeFragmentImages() {
        return homeFragmentImages;
    }


    /**
     * Getter for {@link ProfileFragment} fragment images from liveData
     */
    public LiveData<List<Photos>> getMyPhotosFragmentImages() {
        return myPhotosFragmentImages;
    }

    /**
     * Getter for ChatList {@link DirectMessageFragment}
     */
    public LiveData<List<User>> getUserChatList() {
        return userList_chatWith_liveData;
    }


    public LiveData<User> getCurrentUserData() {
        return currentUserData_liveData;
    }

    /**
     * Getter of the current user followings
     */
    public LiveData<List<Following>> getFollowingsUsers() {
        return followingsUsers;
    }




    /*//////////////////////////////////Setters For ToolBar icons listeners///////////////////////////////////////////////*/

    /**
     * Setter for the HomeFragment  toolbar icons (Camera & Chat)
     */
    public void setOnToolBarIconsListener(OnToolBarIconsListener onToolBarIconsListener) {
        this.onToolBarIconsListener = onToolBarIconsListener;
    }

    /**
     * Setter for the DirectMessageFragment toolbar back arrow
     */
    public void setOnBackListener_chatFragment(OnBackListener_ChatFragment onBackListener_chatFragment) {
        this.onBackListener_chatFragment = onBackListener_chatFragment;
    }

    /**
     * Setter for OnMyViewPagerListener interface
     */
    public void setOnMyViewPagerListener(OnMyViewPagerListener onMyViewPagerListener) {
        this.onMyViewPagerListener = onMyViewPagerListener;
    }
}
