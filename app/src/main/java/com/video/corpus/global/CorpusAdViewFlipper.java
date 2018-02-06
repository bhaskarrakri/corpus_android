package com.video.corpus.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

///**
// * Created by Bhaskar.c on 12/13/2017.
// */

public class CorpusAdViewFlipper extends ViewPager {

    private PagerAdapter adapter;
    private final long mFlipInterval = 5000;
    private final Context context;
    private boolean mVisible;



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mVisible = false;
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                mVisible = true;
            }
        }
    };

    public CorpusAdViewFlipper(Context context) {
        super(context);
        this.context = context;
        postDelayed(mFlipRunnable, mFlipInterval);
    }

    public CorpusAdViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        postDelayed(mFlipRunnable, mFlipInterval);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = (visibility == VISIBLE);
        startFlipping();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mVisible = true;

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);

        // OK, this is gross but needed. This class is supported by the
        // remote views mechanism and as a part of that the remote views
        // can be inflated by a context for another user without the app
        // having interact users permission - just for loading resources.
        // For example, when adding widgets from a user profile to the
        // home screen. Therefore, we register the receiver as the current
        // user not the one the context is for.
        context.registerReceiver(mReceiver ,filter, null, getHandler());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        context.unregisterReceiver(mReceiver);
    }




    private void startFlipping() {
        removeCallbacks(mFlipRunnable);
        postDelayed(mFlipRunnable, mFlipInterval);
    }

    private final Runnable mFlipRunnable = new Runnable() {
        @Override
        public void run() {
            if(mVisible) {
                showNext();
            }

        }
    };



    private void showNext() {
        if(adapter==null){
            return;
        }

        int mNextIndex = getCurrentItem();
        if(mNextIndex == adapter.getCount()-1) {
            mNextIndex = 0;
        }else{
            mNextIndex++;
        }

        setCurrentItem(mNextIndex);
        startFlipping();
    }


}

