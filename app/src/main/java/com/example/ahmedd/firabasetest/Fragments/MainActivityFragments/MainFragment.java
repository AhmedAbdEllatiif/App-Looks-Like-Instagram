package com.example.ahmedd.firabasetest.Fragments.MainActivityFragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.HomeFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.MyPhotos;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.ProfileFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.UsersFragment;
import com.example.ahmedd.firabasetest.Fragments.UploadPhotosFragment;
import com.example.ahmedd.firabasetest.Helpers.OnToolBarIconsListener;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private MainActivityViewModel viewModel;

    private OnToolBarIconsListener  onToolBarIconsListener;

    //Views
    private View view;
    private TextView txt_title;
    private ImageButton img_camera;
    private ImageButton img_Chat;
    private ViewPager viewpager;




    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        initViews();

        setBottomNavigationView();

        onViewsClicked();

        setUpViewPager();

        return view;
    }

    private void initViews() {
        txt_title = view.findViewById(R.id.title);
        img_camera = view.findViewById(R.id.img_camera);
        img_Chat = view.findViewById(R.id.img_Chat);
        viewpager = view.findViewById(R.id.viewpager);
    }

    private void onViewsClicked(){
        img_camera.setOnClickListener(v -> onToolBarIconsListener.onCameraClicked());
        img_Chat.setOnClickListener(v -> onToolBarIconsListener.onChatClicked());

    }


    /**
     * To setup viewPager
     * */
    private void setUpViewPager(){
        MainPageAdapter pageAdapter = new MainPageAdapter(getChildFragmentManager(), PagerAdapter.POSITION_NONE);
        pageAdapter.addFragment(new HomeFragment());
        pageAdapter.addFragment(new UsersFragment());
        pageAdapter.addFragment(new UploadPhotosFragment());
        pageAdapter.addFragment(new MyPhotos());
        pageAdapter.addFragment(new ProfileFragment());
        int limit = (pageAdapter.getCount() > 1 ? pageAdapter.getCount() - 1 : 1);
        viewpager.setOffscreenPageLimit(limit);
        viewpager.setAdapter(pageAdapter);
    }




    /**
     * To setup BottomNavigationView
     * */
    private void setBottomNavigationView() {
        BottomNavigationView navigation = view.findViewById(R.id.mainBottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    viewpager.setCurrentItem(0,false);
                    txt_title.setText(R.string.home);
                    return true;
                case R.id.users:
                    viewpager.setCurrentItem(1,false);
                    txt_title.setText(R.string.users);
                    return true;
                case R.id.add:
                    viewpager.setCurrentItem(2,false);
                    txt_title.setText(R.string.upload);
                    return true;
                case R.id.myPhotos:
                    viewpager.setCurrentItem(3,false);
                    txt_title.setText(R.string.photos);
                    return true;
                case R.id.myProfile:
                    viewpager.setCurrentItem(4,false);
                    txt_title.setText(R.string.profile);
                    return true;
            }
            return false;
        }
    };



    /**
     * To set on camera item clicked listener
     * */
    public void setToolBarListener(OnToolBarIconsListener onToolBarIconsListener) {
        this.onToolBarIconsListener = onToolBarIconsListener;
    }
}