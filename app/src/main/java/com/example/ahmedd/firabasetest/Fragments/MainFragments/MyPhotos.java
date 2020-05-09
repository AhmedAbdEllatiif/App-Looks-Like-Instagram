package com.example.ahmedd.firabasetest.Fragments.MainFragments;


import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Adapters.PhotosAdapter;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.Model.User;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;

import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPhotos extends Fragment {

    private static final String TAG = "MyPhotos";

    private MainActivityViewModel viewModel;

    private View view = null;
    private RecyclerView recyclerView;
    private PhotosAdapter adapter;
    Boolean deleteIsCancelled = false;
    private TextView txt_empty_cardView;


    public MyPhotos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_photos, container, false);

        viewModel = new  ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        initViews();

        onViewClicked();



        observeImagesFromLiveData();



        return view;
    }


    /**
     * To initialize views
     * */
    private void initViews(){
        txt_empty_cardView = view.findViewById(R.id.txt_empty_cardView);
        recyclerView = view.findViewById(R.id.recyclerView_photos);
    }


    /**
     * on ViewClicked
     * */
    private void onViewClicked(){
        txt_empty_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick ==> txt_empty_cardView");

            }
        });
    }

    /**
     * To observe the liveData
     * */
    private void observeImagesFromLiveData() {

        viewModel.getMyPhotosFragmentImages().observe(getViewLifecycleOwner(), new Observer<List<Photos>>() {
            @Override
            public void onChanged(List<Photos> photos) {

                ShowTextPickImage(photos.isEmpty());

                setupRecyclerView(photos);

            }
        });
    }


    /**
     * Show text (Pick a new photo) if the imagesList is empty
     * */
    private void ShowTextPickImage(boolean isPhotosEmpty){
        if (isPhotosEmpty) {
            txt_empty_cardView.setVisibility(View.VISIBLE);
        } else {
            txt_empty_cardView.setVisibility(View.GONE);
        }

    }

    /**
     * To setup recyclerView and fill the adapter with the images
     * */
    private void setupRecyclerView(List<Photos> photos){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PhotosAdapter(getActivity(), photos);
        recyclerView.setAdapter(adapter);

        onPhotosAdapterClicked(adapter);
    }


    /**
     * On child of adapter clicked
     * */
    private void onPhotosAdapterClicked(final PhotosAdapter photosAdapter) {
        photosAdapter.setOnUpdateClickListener(new PhotosAdapter.MyUpdateAndCancelClickListener() {
            @Override
            public void myUpdateAndCancelClickListener(int position, Photos photosItem, TextView txtOptionMenu, TextView txt_name, EditText editTxt_name, EditText editText_description, TextView txtDescription, ImageButton update, ImageButton cancel) {

                if (editText_description.getText().toString().trim().isEmpty()
                        && editTxt_name.getText().toString().trim().isEmpty()) {
                    txtDescription.setVisibility(View.VISIBLE);
                    txt_name.setVisibility(View.VISIBLE);
                    editTxt_name.setVisibility(View.GONE);
                    editText_description.setVisibility(View.GONE);
                    update.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                } else {
                    photosItem.setDescription(editText_description.getText().toString().trim());
                    photosItem.setName(editTxt_name.getText().toString().trim());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("description", photosItem.getDescription());
                    hashMap.put("name", photosItem.getName());

                    MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                            .child("Myphotos").child(photosItem.getKey())
                            .updateChildren(hashMap);
                }
            }
        });


        photosAdapter.setOnCaneclClickListener(new PhotosAdapter.MyUpdateAndCancelClickListener() {
            @Override
            public void myUpdateAndCancelClickListener(int position, Photos photosItem, TextView txtOptionMenu, TextView txt_name, EditText editTxt_name, EditText editText_description, TextView txtDescription, ImageButton update, ImageButton cancel) {
                txtDescription.setVisibility(View.VISIBLE);
                txt_name.setVisibility(View.VISIBLE);
                editTxt_name.setVisibility(View.GONE);
                editText_description.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);


            }
        });

        photosAdapter.setOnMenuClickListener(new PhotosAdapter.MyUpdateAndCancelClickListener() {
            @Override
            public void myUpdateAndCancelClickListener(int position, final Photos photosItem, final TextView txtOptionMenu, final TextView txt_name, final EditText editTxt_name, final EditText editText_description, final TextView txtDescription, final ImageButton update, final ImageButton cancel) {

                PopupMenu popupMenu = new PopupMenu(getActivity(), txtOptionMenu);
                popupMenu.inflate(R.menu.cardview_myphotos_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        deleteIsCancelled = false;
                        switch (item.getItemId()) {

                            case R.id.set_as_ProfileImage:
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("ImageURL", photosItem.getUrl());
                                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                        .updateChildren(hashMap);

                                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                User user = dataSnapshot.getValue(User.class);
                                                HashMap<String, Object> hashMap1 = new HashMap<>();
                                                hashMap1.put("userImage", user.getImageURL());
                                                MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                                                        .child("Myphotos").child(photosItem.getKey()).updateChildren(hashMap1);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                Snackbar.make(txtOptionMenu, "Profile Picture Updated Successful  ", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                break;
                            case R.id.edit_photoInfo:
                                editText_description.setText(photosItem.getDescription());
                                editTxt_name.setText(photosItem.getName());
                                txtDescription.setVisibility(View.INVISIBLE);
                                txt_name.setVisibility(View.INVISIBLE);
                                editTxt_name.setVisibility(View.VISIBLE);
                                editText_description.setVisibility(View.VISIBLE);
                                update.setVisibility(View.VISIBLE);
                                cancel.setVisibility(View.VISIBLE);

                                break;
                            case R.id.delete_menu:

                                Snackbar.make(txtOptionMenu, "Deleting Photo", Snackbar.LENGTH_LONG)
                                        .setAction("Cancel", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                deleteIsCancelled = true;
                                            }
                                        }).show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (deleteIsCancelled == false) {
                                            MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                                                    .child("Myphotos").child(photosItem.getKey()).removeValue();
                                        }
                                    }
                                }, 3000);


                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

}