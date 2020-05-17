package com.example.ahmedd.firabasetest.Fragments.MainFragments.ProfileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.PostsAdapter;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaggedImagesFragment extends Fragment {

    private static final String TAG = "TaggedImagesFragment";

    private MainActivityViewModel viewModel;

    private View view;
    private RecyclerView recyclerView;

    private PostsAdapter adapter;

    //private Boolean deleteIsCancelled = false;

    public TaggedImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tagged_images, container, false);


        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //To initialize views
        initViews();


        setupRecyclerView();

        //To observe user images the liveData
        observeImagesFromLiveData();

        //To observe current user data from live data
        //observeCurrentUserDataFromLiveData();


        return view;
    }

    /**
     * To initialize views
     */
    private void initViews() {
        recyclerView = view.findViewById(R.id.recyclerView_photos);
    }


    /**
     * To observe user images the liveData
     */
    private void observeImagesFromLiveData() {

        viewModel.getMyPhotosFragmentImages().observe(getViewLifecycleOwner(), photos -> {
            if (photos != null) {
                adapter.setPostModelList(photos);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        });
    }

    /**
     * To observe current user data from live data
     */
    private void observeCurrentUserDataFromLiveData() {
        viewModel.getCurrentUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                adapter.setCurrentUser(user);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * To setup recyclerView and fill the adapter with the images
     */
    private void setupRecyclerView() {
        adapter = new PostsAdapter(getActivity(), PostsAdapter.POST_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    //*** On child of adapter clicked*/
    /*private void onPhotosAdapterClicked(final PostsAdapter photosAdapter) {

        photosAdapter.setOnMenuClickListener(new PostsAdapter.OnPostMenuOptionClickListener() {
            @Override
            public void myUpdateAndCancelClickListener(int position, final PostModel photosItem, final TextView txtOptionMenu, final TextView txt_name, final EditText editTxt_name, final EditText editText_description, final TextView txtDescription, final ImageButton update, final ImageButton cancel) {

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

    }*/


}
