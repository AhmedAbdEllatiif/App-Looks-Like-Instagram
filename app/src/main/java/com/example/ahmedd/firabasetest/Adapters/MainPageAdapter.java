package com.example.ahmedd.firabasetest.Adapters;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPageAdapter extends FragmentStateAdapter {

    public final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> titles = new ArrayList<>();

    public MainPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MainPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /*public MainPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }*/

    public void addFragment(Fragment fragment, String title){

        fragments.add(fragment);
        titles.add(title);
    }


    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }


    /*
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }*/
}
