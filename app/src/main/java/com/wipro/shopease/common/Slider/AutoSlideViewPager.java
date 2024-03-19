package com.wipro.shopease.common.Slider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.wipro.shopease.R;

import java.lang.reflect.Field;


public class AutoSlideViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private static final int INTERVAL_TIME = 2000;
    private static final double SCROOL_FACTOR = 1.0d;
    private static final int WHAT = 1;
    private double autoScrollFactor = SCROOL_FACTOR;
    private Context context;
    private boolean isAuto = false;
    private boolean isShowPoint = true;
    private boolean isShowTitle = true;
    private boolean isStopByTouch = false;
    private AutoSlideViewPageAdapter mAdapter;
    private TextView mDescriptionTextView;
    private LayoutParams mDescriptionTextViewParam;
    private Handler mHandler;
    private int mIntervalTime = 2000;
    private int mNormalPointImageResid;
    private LinearLayout.LayoutParams mPointImageLayoutParam;
    private LinearLayout mPointLinearLayout;
    private LayoutParams mPointLinearLayoutParam;
    private Integer mPreviousItem = null;
    private int mSelectPointImageResid;
    private ViewPager mViewPager;
    private OnPageChangeListener pageChangeListener;
    private CustomDurationScroller scroller;
    private boolean stopScrollWhenTouch = true;
    private double swipeScrollFactor = SCROOL_FACTOR;

    @SuppressLint({"HandlerLeak"})
    private class MHandler extends Handler {
        private MHandler() {
        }

        public void handleMessage(Message msg) {
            try {
                if (AutoSlideViewPager.this.isAuto) {
                    AutoSlideViewPager.this.scroller.setScrollDurationFactor(AutoSlideViewPager.this.autoScrollFactor);
                    AutoSlideViewPager.this.mViewPager.setCurrentItem(AutoSlideViewPager.this.mViewPager.getCurrentItem() + 1, true);
                    AutoSlideViewPager.this.scroller.setScrollDurationFactor(AutoSlideViewPager.this.swipeScrollFactor);
                    AutoSlideViewPager.this.sendMessage(1, (long) (AutoSlideViewPager.this.mIntervalTime + AutoSlideViewPager.this.scroller.getDuration()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public AutoSlideViewPager(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public AutoSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(this.context, R.layout.slider_auto_scroll_viewpage, this);
        this.mViewPager = findViewById(R.id.item_viewpage);
        this.mDescriptionTextView = findViewById(R.id.item_tv_desc);
        this.mPointLinearLayout = findViewById(R.id.item_llayout_point);
        this.mNormalPointImageResid = R.drawable.ic_point_normal;
        this.mSelectPointImageResid = R.drawable.ic_point_select;
        this.mViewPager.setOnPageChangeListener(this);


        setViewPagerScroller();
    }

    public void setMarginPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom, int margin) {
        this.mViewPager.setClipToPadding(false);
        this.mViewPager.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        this.mViewPager.setPageMargin(margin);
    }

    public void setAdapter(AutoSlideViewPageAdapter adapter) {
        this.mAdapter = adapter;
        this.mViewPager.setAdapter(adapter);
        if (this.isShowPoint) {
            this.mPointLinearLayout.removeAllViews();
            if (this.mPointImageLayoutParam == null) {
                this.mPointImageLayoutParam = new LinearLayout.LayoutParams(-2, -2);
                this.mPointImageLayoutParam.setMargins(10, 4, 10, 4);
            }
            for (int i = 0; i < adapter.getPageCount(); i++) {
                ImageView pointImage = new ImageView(this.context);
                pointImage.setLayoutParams(this.mPointImageLayoutParam);
                pointImage.setBackgroundResource(this.mNormalPointImageResid);
                this.mPointLinearLayout.addView(pointImage);
            }
        }
        this.mViewPager.setCurrentItem(this.mAdapter.getPageCount() * 100);
    }

    public void setPointImageResId(int normal, int select) {
        this.mNormalPointImageResid = normal;
        this.mSelectPointImageResid = select;
    }

    public void setCurrentPageDescription(CharSequence charSequence) {
    }

    public void setIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
    }

    public void startSlide() {
        this.isAuto = true;
        if (this.mHandler == null) {
            this.mHandler = new MHandler();
        }
        removeMessages(1);
        sendMessage(1, (long) (((double) this.mIntervalTime) + ((((double) this.scroller.getDuration()) / this.autoScrollFactor) * this.swipeScrollFactor)));
    }

    public void stopSlide() {
        this.isAuto = false;
        if (this.mHandler != null) {
            removeMessages(1);
        }
    }

    private void sendMessage(int what, long delayMillis) {
        this.mHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    private void removeMessages(int what) {
        this.mHandler.removeMessages(what);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.stopScrollWhenTouch) {
            if (ev.getAction() == 0 && this.isAuto) {
                this.isStopByTouch = true;
                stopSlide();
            } else if (ev.getAction() == 1 && this.isStopByTouch) {
                startSlide();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mViewPager.setCurrentItem(item + (this.mViewPager.getCurrentItem() - getCurrentItem()), smoothScroll);
        stopSlide();
        startSlide();
    }

    public int getCurrentItem() {
        return this.mViewPager.getCurrentItem() % this.mAdapter.getPageCount();
    }

    public void setShowPoint(boolean isShowPoint) {
        this.isShowPoint = isShowPoint;
    }

    public void setDescriptionTextViewParam(LayoutParams mDescriptionTextViewParam) {
        this.mDescriptionTextViewParam = mDescriptionTextViewParam;
        this.mDescriptionTextView.setLayoutParams(this.mDescriptionTextViewParam);
    }

    public void setPointLinearLayoutParam(LayoutParams mPointLinearLayoutParam) {
        this.mPointLinearLayoutParam = mPointLinearLayoutParam;
        this.mPointLinearLayout.setLayoutParams(this.mPointLinearLayoutParam);
    }

    public void setPointImageLayoutParam(LinearLayout.LayoutParams mPointImageLayoutParam) {
        this.mPointImageLayoutParam = mPointImageLayoutParam;
        for (int i = 0; i < this.mPointLinearLayout.getChildCount(); i++) {
            this.mPointLinearLayout.getChildAt(i).setLayoutParams(this.mPointImageLayoutParam);
        }
    }

    public void setShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);
            this.scroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
            scrollerField.set(this.mViewPager, this.scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSwipeScrollDurationFactor(double scrollFactor) {
        this.scroller.setScrollDurationFactor(scrollFactor);
        this.swipeScrollFactor = scrollFactor;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.pageChangeListener = listener;
    }

    public void setAutoScrollDurationFactor(double scrollFactor) {
        this.autoScrollFactor = scrollFactor;
    }

    public void onPageScrollStateChanged(int state) {
        if (this.pageChangeListener != null) {
            this.pageChangeListener.onPageScrollStateChanged(state);
        }
    }

    public void onPageScrolled(int i, float v, int j) {
        if (this.pageChangeListener != null) {
            this.pageChangeListener.onPageScrolled(i, v, j);
        }
    }

    public void onPageSelected(int position) {
        if (this.isShowPoint) {
            changePointImageState(this.mAdapter.getPosition(position), true);
            if (this.mPreviousItem != null) {
                changePointImageState(this.mAdapter.getPosition(this.mPreviousItem.intValue()), false);
            }
            this.mPreviousItem = Integer.valueOf(position);
        }
        if (this.isShowTitle) {
            setCurrentPageDescription(this.mAdapter.getPageTitle(position));
        }
        if (this.pageChangeListener != null) {
            this.pageChangeListener.onPageSelected(position % this.mAdapter.getPageCount());
        }
    }

    private void changePointImageState(int index, boolean isSelect) {
        try {
            this.mPointLinearLayout.getChildAt(index).setBackgroundResource(isSelect ? this.mSelectPointImageResid : this.mNormalPointImageResid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
