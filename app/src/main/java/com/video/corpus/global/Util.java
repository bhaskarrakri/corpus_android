package com.video.corpus.global;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.video.corpus.BuildConfig;
import com.video.corpus.R;

import org.json.JSONObject;

import java.util.logging.Handler;

///**
// * Created by Bhaskar.c on 1/2/2018.
// */

public class Util {

    private static Util util;
    private int seqno=1;

    public static Util getInstance() {
        if(util==null)
        {
            util=new Util();
        }

        return  util;
    }

    public void fragmenttransaction(AppCompatActivity activity,Fragment fragment, String tag, boolean backstack)
    {

        FragmentTransaction transaction=activity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        Log.e("backstack",""+backstack);
        if(backstack)
        {
            transaction.addToBackStack(tag);
        }

        transaction.commit();
        activity.getFragmentManager().executePendingTransactions();
        Log.e("count",""+tag+"      "+activity.getFragmentManager().getBackStackEntryCount());
    }



    // get device width programatically
    public  int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }



    //set params
    public String setamsobject(Context context, String status)
    {
        JSONObject amsJsonObject = null;
        try {
            commonclass cc=new commonclass(context);
            amsJsonObject = new JSONObject();
            try {
                amsJsonObject.putOpt("seqNo", seqno++);
                amsJsonObject.putOpt("command", "SYSTEM");
                amsJsonObject.putOpt("subject", "");
                amsJsonObject.putOpt("action", status);
                amsJsonObject.putOpt("sessionId", cc.getSessioId());
                amsJsonObject.putOpt("initiatiatingApp", 1);
                amsJsonObject.putOpt("dateTime", System.currentTimeMillis());
                amsJsonObject.putOpt("stb", cc.getusername());
                amsJsonObject.putOpt("subDeviceType", "Mobile");
                amsJsonObject.putOpt("subDeviceOS", "Android v" + Build.VERSION.RELEASE);
                amsJsonObject.put("info", "Product Release v" + BuildConfig.VERSION_CODE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (amsJsonObject != null) {
            return amsJsonObject.toString();
        }
        return "";
    }


}
