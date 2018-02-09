package com.video.corpus.controllers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.gms.ads.MobileAds;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.R;
import com.video.corpus.fragments.CatchUpFragment;
import com.video.corpus.fragments.HomeFragment;
import com.video.corpus.fragments.LiveTvFragment;
import com.video.corpus.fragments.MoviesFragment;
import com.video.corpus.fragments.SettingsFragment;
import com.video.corpus.global.Util;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.AMSRequest;
import com.video.corpus.network.NetworkRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.video.corpus.global.Util.getInstance;

public class HomeActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private int count =0;
    private Response_string<String> deviceconfigresponse;
    private Context context;
    private commonclass cc;
    private  Handler  handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = HomeActivity.this;
        cc=new commonclass(context);
        toolbartext(context,getString(R.string.home_title));
         bottomNavigationView = findViewById(R.id.bottom_nav_view);



        showlogs("Test Name",cc.getcustomerName());
         if(isnotempty(cc.getSessioId()))
         {
             showlogs("session Id",cc.getSessioId());
             readresponse();
             deviceconfig();
         }


        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getResources().getString(R.string.google_app_testid));

        getInstance().fragmenttransaction(this,new HomeFragment(),
                HomeFragment.class.getName(),false);

        removeShiftMode(bottomNavigationView);
        setlayoutparams(bottomNavigationView);

        if(cc.isLoggedIn())
        {
          bottomNavigationView.getMenu().findItem(R.id.action_settings).setTitle(getResources().getString(R.string.more));
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                toolbartext(context,item.getTitle().toString());
                item.setChecked(true);
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        getInstance().fragmenttransaction(HomeActivity.this,new HomeFragment(),
                                HomeFragment.class.getName(),false);
                        break;

                    case R.id.action_livetv:
                         if(!(getcurrentfragment() instanceof  LiveTvFragment))
                         {
                             getInstance().fragmenttransaction(HomeActivity.this,new LiveTvFragment(),
                                     LiveTvFragment.class.getName(),false);
                         }
                        break;

                    case R.id.action_movies:
                        if(!(getcurrentfragment() instanceof  MoviesFragment)) {
                            getInstance().fragmenttransaction(HomeActivity.this, new MoviesFragment(),
                                    MoviesFragment.class.getName(), false);
                        }
                        break;

                    case R.id.action_catchup:
                        if(!(getcurrentfragment() instanceof  CatchUpFragment)) {
                            getInstance().fragmenttransaction(HomeActivity.this, new CatchUpFragment(),
                                    CatchUpFragment.class.getName(), false);
                        }
                        break;

                    case R.id.action_settings:

                        if(!cc.isLoggedIn())
                        {
                            Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            if(!(getcurrentfragment() instanceof  SettingsFragment)) {
                                getInstance().fragmenttransaction(HomeActivity.this, new SettingsFragment(),
                                        SettingsFragment.class.getName(), false);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

   @Override
    public void onBackPressed()
    {
        FragmentManager fragmentManager = HomeActivity.this.getFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);

        if(currentFragment!=null && currentFragment instanceof  HomeFragment)
        {
           // exit_alert();
            exit_toast();
        }
        else if(currentFragment!=null && (currentFragment instanceof  LiveTvFragment ||
                currentFragment instanceof  MoviesFragment || currentFragment instanceof  CatchUpFragment
                || currentFragment instanceof  SettingsFragment))
        {
            getInstance().fragmenttransaction(HomeActivity.this,new HomeFragment(),
                    HomeFragment.class.getName(),false);

            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
        else
        {
            super.onBackPressed();
        }
    }



    //exit
    private void exit_toast()
    {
        count++;

        if(count==2)
        {
            new AMSRequest(cc.gettrapurl(),  Util.getInstance().setamsobject(context,AMS_INACTIVE));
            finish();
        }
        else if(count<2)
        {
            showtaost(context,getString(R.string.pressexit));
        }
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                count=0;
            }
        },exit_timeout);
    }


//    private void exit_alert()
//    {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
//        dialog.setCancelable(false);
//        dialog.setTitle(getResources().getString(R.string.alert_title));
//        dialog.setMessage(getResources().getString(R.string.alert_title) );
//        dialog.setPositiveButton(getResources().getString(R.string.alert_btnyes), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                //Action for "Delete".
//                finish();
//            }
//        })
//                .setNegativeButton(getResources().getString(R.string.alert_btnno), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Action for "Cancel".
//                        dialog.cancel();
//                    }
//                });
//
//        final AlertDialog alert = dialog.create();
//        alert.show();
//    }


    //get current fragment
    private Fragment getcurrentfragment()
    {
        return HomeActivity.this.getFragmentManager().findFragmentById(R.id.frame_layout);
    }

    //AMS
    private void AMS_START()
    {
         handler=new Handler();
         runnable=new Runnable() {
             @Override
             public void run() {

                 new AMSRequest(cc.gettrapurl(), Util.getInstance().setamsobject(context,AMS_ACTIVE));

                 handler.postDelayed(runnable,cc.getamstimeinterval());

             }
         };
        handler.postDelayed(runnable,cc.getamstimeinterval());
    }


    //device config
    private void deviceconfig()
    {
         ArrayList<String>   params = new ArrayList<>();
        new NetworkRequest(context, APP_SERVER_COMB+cc.getSessioId()+device_config_url,params, deviceconfigresponse).execute();
    }


    private void readresponse() {
        deviceconfigresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("device config _response", res);
                if (!TextUtils.isEmpty(res)) {
                    loadAMS_url(res);
                }
            }
        };
    }

    private void loadAMS_url(String res) {
        try
        {
            if(isnotempty(res)) {
                JSONObject jsonObject = new JSONObject(res);
                if (isnotempty(jsonObject.toString()))
                {
                    JSONArray jsonArray_device=jsonObject.optJSONArray("deviceConfig");
                    if(jsonArray_device.length()>0)
                    {

                        for(int i=0;i<jsonArray_device.length();i++)
                        {
                            JSONObject jsonObject_loop=jsonArray_device.optJSONObject(i);
                            if(isnotempty(jsonObject_loop.toString()))
                            {

                                if(!isnotempty(cc.gettrapurl()))
                                {
                                    if(jsonObject_loop.optString("configName","").equals("TRAP_SERVER_URL"))
                                    {

                                        cc.settrapurl(jsonObject_loop.optString("configValue",""));
                                    }
                                }

                                if(cc.getamstimeinterval()==0)
                                {
                                    if(jsonObject_loop.optString("configName","").equals("ALIVE_TIME_IN_SECONDS"))
                                    {

                                        cc.setamstimeinterval(convertsectomins(Integer.valueOf(jsonObject_loop.optString("configValue",""))));

                                    }
                                }

                                if(isnotempty(cc.gettrapurl()) && cc.getamstimeinterval()!=0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if(cc.getamstimeinterval()==0)
            {
                cc.setamstimeinterval(convertsectomins(amsdeftimeinterval));
            }
            if(isnotempty(cc.gettrapurl()) && cc.getamstimeinterval()!=0)
            {
                AMS_START();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(handler!=null)
        {
            handler.removeCallbacks(runnable);
        }

    }


}
