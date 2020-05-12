package com.example.ahmedd.firabasetest.Fragments.MainActivityFragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.HomeFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.ProfileFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.AccountFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.UsersFragment;
import com.example.ahmedd.firabasetest.Fragments.UploadPhotosFragment;
import com.example.ahmedd.firabasetest.Helpers.MyViewPager;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private MainActivityViewModel viewModel;



    //Views
    private View view;


    private MyViewPager viewpager;




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



        setUpViewPager();

        return view;
    }

    private void initViews() {


        viewpager = view.findViewById(R.id.viewpager);
    }



    /**
     * To setup viewPager
     * */
    private void setUpViewPager(){
        MainPageAdapter pageAdapter = new MainPageAdapter(getChildFragmentManager(), PagerAdapter.POSITION_NONE);
        pageAdapter.addFragment(new HomeFragment());
        pageAdapter.addFragment(new UsersFragment());
        pageAdapter.addFragment(new UploadPhotosFragment());
        pageAdapter.addFragment(new ProfileFragment());
        pageAdapter.addFragment(new AccountFragment());
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
                    //txt_title.setText(R.string.home);
                    return true;
                case R.id.users:
                    viewpager.setCurrentItem(1,false);
                    //txt_title.setText(R.string.users);
                    return true;
                case R.id.add:
                    viewpager.setCurrentItem(2,false);
                    //txt_title.setText(R.string.upload);
                    return true;
                case R.id.myPhotos:
                    viewpager.setCurrentItem(3,false);
                    //txt_title.setText(R.string.photos);
                    return true;
                case R.id.myProfile:
                    viewpager.setCurrentItem(4,false);
                    //txt_title.setText(R.string.profile);
                    return true;
            }
            return false;
        }
    };



}