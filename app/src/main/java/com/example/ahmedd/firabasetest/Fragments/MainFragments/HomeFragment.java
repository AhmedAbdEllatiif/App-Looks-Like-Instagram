package com.example.ahmedd.firabasetest.Fragments.MainFragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Activities.MainActivity;
import com.example.ahmedd.firabasetest.Adapters.HomeAdapter;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private MainActivityViewModel viewModel;
    private View view = null;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;

    private TextView txt_title;
    private ImageButton img_camera;
    private ImageButton img_Chat;

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("onAttach", "is here");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause", "is here");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);


        initViews();

        observeImagesFromLiveData();

        onViewsClicked();


        return view;
    }


    /**
     * To initialize views
     */
    private void initViews() {
        txt_title = view.findViewById(R.id.title);
        img_camera = view.findViewById(R.id.img_camera);
        img_Chat = view.findViewById(R.id.img_Chat);
        recyclerView = view.findViewById(R.id.recyclerView_home);
    }


    /**
     * To observe the liveData
     */
    private void observeImagesFromLiveData() {
        viewModel.getHomeFragmentImages().observe(getViewLifecycleOwner(), new Observer<List<Photos>>() {
            @Override
            public void onChanged(List<Photos> photos) {
                setupRecyclerView(photos);
            }
        });

    }


    /**
     * To setup recyclerView and fill the adapter with the images
     */
    private void setupRecyclerView(List<Photos> photos) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HomeAdapter(getContext(), photos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void onViewsClicked() {

        //on img camera clicked
        img_camera.setOnClickListener(v -> {
            if (isListenerOnToolBarIcons_Not_Null()) {
                viewModel.onToolBarIconsListener.onCameraClicked();
            }
        });

        //on img chat clicked
        img_Chat.setOnClickListener(v -> {
            if (isListenerOnToolBarIcons_Not_Null()) {
                viewModel.onToolBarIconsListener.onChatClicked();
            }
        });

    }


    /**
     * To Check if the listener in null
     * The listener must be initialize in the {@link MainActivity}
     * This listener move the viewPager inside the mainActivity
     * */
    private boolean isListenerOnToolBarIcons_Not_Null() {
        if (viewModel.onToolBarIconsListener == null) {
            Log.e(TAG, "isListenerOnToolBarIconsNull ==> null pointer");
            return false;
        }
        return true;
    }


}
