package com.example.ahmedd.firabasetest.Fragments.MainActivityFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmedd.firabasetest.Adapters.MainPageAdapter;
import com.example.ahmedd.firabasetest.Fragments.HomeFragment;
import com.example.ahmedd.firabasetest.Fragments.MyPhotos;
import com.example.ahmedd.firabasetest.Fragments.UsersFragment;
import com.example.ahmedd.firabasetest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private View view;

    private ViewPager viewPager;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);


        initViews();


        return view;
    }

    private void initViews() {
        viewPager =  view.findViewById(R.id.mainFragmentViewPager);
        setUpViewPager();

    }


    /*******************************************************************************************************/
    private void setUpViewPager(){
        MainPageAdapter pageAdapter = new MainPageAdapter(getChildFragmentManager(), PagerAdapter.POSITION_NONE);
        pageAdapter.addFragment(new HomeFragment());
        pageAdapter.addFragment(new UsersFragment());
        pageAdapter.addFragment(new MyPhotos());
        //pageAdapter.addFragment(new UsersFragment());
        //pageAdapter.addFragment(new MyPhotos());
        int limit = (pageAdapter.getCount() > 1 ? pageAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.setAdapter(pageAdapter);
    }

    /*******************************************************************************************************/
}
