package com.example.ahmedd.firabasetest.Helpers;


import com.example.ahmedd.firabasetest.Activities.MainActivity;
import com.example.ahmedd.firabasetest.Fragments.MainActivityFragments.MainFragment;

/**
 * This interface used to send the position of the viewpager inside {@link MainFragment}
 * to the {@link MainActivity}
 * Initialized in the {@link MainActivity}
 * */
public interface OnMyViewPagerListener {

    void onPageChanged(int position);
}
