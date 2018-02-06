package com.video.corpus.controllers;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.video.corpus.Interface.Constants;
import com.video.corpus.Interface.actionbarcustom;
import com.video.corpus.R;
import com.video.corpus.global.BottomNavigationViewBehavior;
import java.lang.reflect.Field;

///**
// * Created by Bhaskar.c on 12/6/2017.
// */

public class BaseActivity extends AppCompatActivity implements actionbarcustom,Constants {

    public static final int timeout=10000;
    public static final int exit_timeout=5000;
   // static final int responsecode=200;
    private static final boolean showLogs=false;
    final int splash_milli=2000;
    private final boolean showlogs=true;
    //to show hide action bar
    @Override
    public void actionbarshown(ActionBar actionBar,boolean isshown,String title) {
        if (isshown) {
            actionBar.show();
            if(checkStringUtils(title))
            {
                actionBar.setTitle(title);
            }
        }
        else
        {
            actionBar.hide();
        }
    }


    //check if string is empty or null
    private boolean checkStringUtils(String msg)
    {
        boolean result=false;

        if(!(TextUtils.isEmpty(msg))) {
            result = true;
        }
        return  result;
    }



    //set fixed mode for bottom navigation
     void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }


        //set behaviour for bottom navigation
        void setlayoutparams(BottomNavigationView bottomNavigationView)
        {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
            layoutParams.setBehavior(new BottomNavigationViewBehavior());
        }


    //show logs
//    public void showlogs(String msg,String key)
//    {
//        if(showlogs)
//        {
//            if(!(TextUtils.isEmpty(msg)) &&  !(TextUtils.isEmpty(key)))
//            {
//                Log.e(msg,key);
//            }
//        }
//
//    }


    //print url & result in logs
    public static void logs(String key, String msg)
    {


        if((!(TextUtils.isEmpty(key) && TextUtils.isEmpty(msg))) && showLogs)
        {

            Log.e(key,msg);
        }
    }

    void toolbartext(Context context, String res)
    {

        if(!TextUtils.isEmpty(res) && context!=null)
        {
            View  toolbar=findViewById(R.id.toolbar);
            TextView toolbartext= toolbar.findViewById(R.id.Txt_title);
            toolbartext.setText(res);
        }
    }

    // fragment transaction method
//    public void fragmenttransaction(Fragment fragment, String tag, boolean backstack)
//    {
//        FragmentTransaction transaction=getFragmentManager().beginTransaction();
//        transaction.add(R.id.frame_layout,fragment,tag);
//        if(backstack)
//        {
//            transaction.addToBackStack(tag);
//        }
//        transaction.commit();
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    protected  boolean isnotempty(String data)
    {
        return  !TextUtils.isEmpty(data);
    }


    protected void showlogs(String msg, String key)
    {
        if(showlogs)
        {
            if(!(TextUtils.isEmpty(msg)) &&  !(TextUtils.isEmpty(key)))
            {
                Log.e(msg,key);
            }
        }

    }

    public  void showtaost(Context context,String msg)
    {
        if(isnotempty(msg))
        {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }

    }

    //convert scends to minutes
    public  int convertsectomins(int sec)
    {
        if(sec>0)
        {
            return (sec *1000);
        }
        return sec;

    }

}
