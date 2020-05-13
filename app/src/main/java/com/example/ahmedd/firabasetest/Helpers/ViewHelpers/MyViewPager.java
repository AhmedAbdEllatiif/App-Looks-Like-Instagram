package com.example.ahmedd.firabasetest.Helpers.ViewHelpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {


    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    private boolean isScrollable;

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }



    /**
     * To handle scrolling at the run time
     * */
    private boolean isPagingEnabled;


    @SuppressLint("ClickableViewAccessibility")
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
