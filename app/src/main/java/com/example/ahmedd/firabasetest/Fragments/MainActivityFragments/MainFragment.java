package com.example.ahmedd.firabasetest.Fragments.MainActivityFragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Activities.MainActivity;
import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.HomeFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.ProfileFragments.ProfileFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.UserActivityFragment;
import com.example.ahmedd.firabasetest.Fragments.MainFragments.UsersFragment;
import com.example.ahmedd.firabasetest.Fragments.UploadPhotosFragment;
import com.example.ahmedd.firabasetest.Helpers.ViewHelpers.ViewPagerHelperMainFragment;
import com.example.ahmedd.firabasetest.R;
import com.example.ahmedd.firabasetest.ViewModel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private MainActivityViewModel viewModel;


    private static final String TAG = "MainFragment";

    //Views
    private View view;


    private ViewPagerHelperMainFragment viewpager;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //to Initialize views
        initViews();

        //to setup bottomNavigationView
        setBottomNavigationView();

        //to setup viewPager
        setUpViewPager();

        //to send a callback of the viewpager position to the MainActivity
        sendViewPagerPositionTo_MainActivity();

        return view;
    }

    /**
     * To Initialize views
     * */
    private void initViews() {
        viewpager = view.findViewById(R.id.viewpager);
    }


    /**
     * To setup viewPager
     */
    private void setUpViewPager() {
        MainPageAdapter pageAdapter = new MainPageAdapter(getChildFragmentManager(), PagerAdapter.POSITION_NONE);
        pageAdapter.addFragment(new HomeFragment());
        pageAdapter.addFragment(new UsersFragment());
        pageAdapter.addFragment(new UploadPhotosFragment());
        pageAdapter.addFragment(new UserActivityFragment());
        pageAdapter.addFragment(new ProfileFragment());
        int limit = (pageAdapter.getCount() > 1 ? pageAdapter.getCount() - 1 : 1);
        viewpager.setOffscreenPageLimit(limit);
        viewpager.setAdapter(pageAdapter);
        viewpager.setPagingEnabled(true);
    }


    /**
     * This method to send a callback of the viewpager position to the {@link MainActivity}
     */
    private void sendViewPagerPositionTo_MainActivity() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    viewpager.setPagingEnabled(false);
                }
                if (!isOnMyViewPagerListenerNull()) {
                    viewModel.onMyViewPagerListener.onPageChanged(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * To Check if the listener in null
     * The listener must be initialize in the {@link MainActivity}
     * This listener send a callback of the viewpager position to the {@link MainActivity}
     */
    private boolean isOnMyViewPagerListenerNull() {
        if (viewModel.onMyViewPagerListener == null) {
            Log.e(TAG, "isOnMyViewPagerListenerNull ==> null pointer");
            return true;
        }
        return false;
    }


    /**
     * To setup BottomNavigationView
     */
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
                    viewpager.setCurrentItem(0, false);
                    return true;
                case R.id.users:
                    viewpager.setCurrentItem(1, false);
                    return true;
                case R.id.add:
                    viewpager.setCurrentItem(2, false);
                    return true;
                case R.id.myPhotos:
                    viewpager.setCurrentItem(3, false);
                    return true;
                case R.id.myProfile:
                    viewpager.setCurrentItem(4, false);
                    return true;
            }
            return false;
        }
    };


}