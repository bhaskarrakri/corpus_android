package com.video.corpus.google;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

///**
// * Created by Bhaskar.c on 12/21/2017.
// */

public class LargeBannerView {

    public static  AdView BannerView(final Context context, int adtype, String addunitid) {
        AdView mAdViewBigTitleBanner = new AdView (context);
        AdSize adSize;
        switch (adtype)
        {
            case  0: adSize=AdSize.LARGE_BANNER;
                break;
            case  1: adSize=AdSize.MEDIUM_RECTANGLE;
                break;
            case  2: adSize=AdSize.FULL_BANNER;
                break;
            case  3: adSize=AdSize.LARGE_BANNER;
                break;
            default:adSize=AdSize.SMART_BANNER;
                break;
        }

        mAdViewBigTitleBanner.setAdSize (adSize);
        mAdViewBigTitleBanner.setAdUnitId (addunitid);
        AdRequest adRequest = new AdRequest.Builder ().build ();
        mAdViewBigTitleBanner.loadAd (adRequest);
        return mAdViewBigTitleBanner;
    }
}
