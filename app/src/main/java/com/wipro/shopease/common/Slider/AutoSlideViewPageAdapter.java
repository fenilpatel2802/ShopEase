package com.wipro.shopease.common.Slider;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

public abstract class AutoSlideViewPageAdapter extends PagerAdapter implements IAutoSlideViewPagerAdapter {
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public CharSequence getPageTitle(int position) {
        return getCurrentPageTitle(position % getPageCount());
    }

    public int getPosition(int position) {
        return position % getPageCount();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        try {
            position %= getPageCount();
        } catch (Exception e) {
            e.printStackTrace();
            position = 0;
        }
        View view = instantiatePageItem(position);
        container.addView(view);
        return view;
    }
}
