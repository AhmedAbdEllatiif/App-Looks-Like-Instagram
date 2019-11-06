package com.example.ahmedd.firabasetest.Adapters;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;

    public PageAdapter(FragmentManager fm) {
        super(fm);
        this.fragments= new ArrayList<>();
        this.titles = new ArrayList<>();
    }


    public void AddFragmentPage(Fragment fragment, String title ){
        fragments.add(fragment);
        titles.add(title);
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }
}



