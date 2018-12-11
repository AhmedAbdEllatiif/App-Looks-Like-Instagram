package com.example.ahmedd.firabasetest.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Adapters.HomeAdapter;
import com.example.ahmedd.firabasetest.Model.Following;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;

    private List<Photos> photosListOFTheFollowing;
    private List<Following> followingList;


    public HomeFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       Log.e("onAttach","is here");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause", "is here");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.e("onCreateView","is here");
        recyclerView = view.findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        followingList = new ArrayList<>();
        photosListOFTheFollowing = new ArrayList<>();



                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                        .child("Following").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Following followingItem = snapshot.getValue(Following.class);
                            followingList.add(followingItem);
                        }
                        new GetAllPhotos(getActivity()).execute();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });









        return view;
    }


    private class GetAllPhotos extends AsyncTask<Void,Photos,Void>{

        final DatabaseReference referenceOnPhotos = MyFireBase.getReferenceOnPhotos();
        Context context;

        public GetAllPhotos(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("onPreExecute","begin");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("doInBackground","begin");

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
                                                //photosListOFTheFollowing.clear();
                                                for (DataSnapshot myPhotosChildern : dataSnapshot.getChildren()) {
                                                    Photos photosItem = myPhotosChildern.getValue(Photos.class);

                                                     publishProgress(photosItem);
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

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return null;
        }




        @Override
        protected void onProgressUpdate(Photos... values) {
            super.onProgressUpdate(values);
            Log.e("onProgressUpdate","begin");
           photosListOFTheFollowing.add(values[0]);
            adapter = new HomeAdapter(context,photosListOFTheFollowing);
            recyclerView.setAdapter(adapter);

        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("onPostExecute","begin");

        }

    }




    private class GetAllFollowing extends AsyncTask<Void,Void,List<Following>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Following> followings) {
            super.onPostExecute(followings);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Following> doInBackground(Void... voids) {
            return null;
        }
    }
}
