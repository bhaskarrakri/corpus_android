package com.video.corpus.google;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.video.corpus.R;

import java.util.HashMap;
import java.util.Map;



///**
// * Created by Bhaskar.c on 12/6/2017.
// */

public  class AnalyticsTrackers {

    private enum Target {
        APP,
        // Add more trackers here if you need, and update the code in #get(Target) below
    }

       private static AnalyticsTrackers sInstance;

    public static synchronized void initialize(Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("Extra call to initialize analytics trackers");
        }

        sInstance = new AnalyticsTrackers(context);
    }

    public static synchronized AnalyticsTrackers getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Call initialize() before getInstance()");
        }

        return sInstance;
    }

    private final Map<Target, Tracker> mTrackers = new HashMap<>();
    private final Context mContext;

    /**
     * Don't instantiate directly - use {@link #getInstance()} instead.
     */
    private AnalyticsTrackers(Context context) {
        mContext = context.getApplicationContext();
    }

    public synchronized Tracker get() {
        if (!mTrackers.containsKey(Target.APP)) {
            Tracker tracker;
            switch (Target.APP) {
                case APP:
                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + Target.APP);
            }
            mTrackers.put(Target.APP, tracker);
        }

        return mTrackers.get(Target.APP);
    }
}
