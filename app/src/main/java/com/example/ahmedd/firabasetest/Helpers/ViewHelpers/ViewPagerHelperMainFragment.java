package com.example.ahmedd.firabasetest.Helpers.ViewHelpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerHelperMainFragment extends ViewPager {


    public ViewPagerHelperMainFragment(@NonNull Context context) {
        super(context);
    }

    public ViewPagerHelperMainFragment(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * To prevent the viewPager from scrolling
     */
    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }


    /**
     * To handle scrolling at the run time
     */
    private boolean isPagingEnabled;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled() && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled() && super.onInterceptTouchEvent(event);
    }


    public boolean isPagingEnabled() {
        return isPagingEnabled;
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

}
