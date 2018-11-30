package com.example.ahmedd.firabasetest.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.PhotosAdapter;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PhotosAdapter adapter;
    private List<Photos> photosList;
    private View view;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        fillRecyclerViewWithPhotos();
    return view;
    }
    private void fillRecyclerViewWithPhotos() {
        LinearLayoutManager layoutManager =  new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView_photos);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setStackFromEnd(false);

        photosList = new ArrayList<>();

        MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        photosList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Photos photosItem = snapshot.getValue(Photos.class);
                            photosList.add(photosItem);
                        }


                        List<Photos> updatePhotoList = new ArrayList<>();
                        for(int i = (photosList.size())-1; i>=0 ;i--){
                            Photos updatePhotoItem = photosList.get(i);
                            updatePhotoList.add(updatePhotoItem);
                        }

                        adapter = new PhotosAdapter(getActivity(),updatePhotoList);

                        recyclerView.setAdapter(adapter);

                        onClickListenerInRecyclerView(adapter);
                      /*  adapter.setOnAsProfileImgClickListener(new PhotosAdapter.MyOnClickListener() {
                            @Override
                            public void myOnClickListener(int position, Photos photosItem) {
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("ImageURL",photosItem.getUrl());
                              MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                      .updateChildren(hashMap);

                            }
                        });
*/


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    private void onClickListenerInRecyclerView(PhotosAdapter photosAdapter) {
        photosAdapter.setOnAsProfileImgClickListener(new PhotosAdapter.MyOnClickListener() {
            @Override
            public void myOnClickListener(int position, Photos photosItem) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("ImageURL", photosItem.getUrl());
                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                        .updateChildren(hashMap);

            }
        });

       /* photosAdapter.setOnDescriptionClickListener(new PhotosAdapter.MyOnClickListener() {
            @Override
            public void myOnClickListener(int position, Photos photosItem) {

            }
        });*/
    }

}
