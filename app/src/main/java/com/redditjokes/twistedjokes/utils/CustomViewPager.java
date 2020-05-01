package com.redditjokes.twistedjokes.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v instanceof ViewPager) {
//            int currentItem = ((ViewPager) v).getCurrentItem();
//            if((currentItem==0)){
//                return true;
//            }
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

}