package com.example.ahmedd.firabasetest.Adapters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ProfileFragmentViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "ProfileFragmentViewPage";
    public ArrayList<Fragment> fragments = new ArrayList<>();

    public ProfileFragmentViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
      //  Log.e(TAG, "getItem: " + position );
        return fragments.get(position);
    }

    @Override
    public int getCount() {
       // Log.e(TAG, "getCount: " + fragments.size() );
            return fragments.size();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }
}
