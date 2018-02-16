package com.video.corpus.controllers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import com.google.android.gms.ads.MobileAds;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.R;
import com.video.corpus.fragments.CatchUpFragment;
import com.video.corpus.fragments.HomeFragment;
import com.video.corpus.fragments.LiveTvFragment;
import com.video.corpus.fragments.MoviesFragment;
import com.video.corpus.fragments.SettingsFragment;
import com.video.corpus.global.Utils;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.AMSRequest;
import com.video.corpus.network.NetworkRequest;
import com.video.corpus.pojos.homecontent_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.video.corpus.global.Utils.getInstance;

public class HomeActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private int count =0;
    private Response_string<String> deviceconfigresponse;
    private Context context;
    private commonclass cc;
    private  Handler  handler,handlerupdate;
    private Runnable runnable,runnableupdate;
    private Response_string<String> subscriberesponse,cmdmanageresponse,livetvresponse,moviesresponse;
    private ArrayList<String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = HomeActivity.this;
        cc=new commonclass(context);
        toolbartext(context,getString(R.string.home_title));
         bottomNavigationView = findViewById(R.id.bottom_nav_view);

        readresponse();
         if(cc.isLoggedIn() && !(isnotempty(cc.getusername())))
         {
             setparams();
             String url=APP_SERVER_COMB+cc.getSessioId()+subscriber_info_url;
             new NetworkRequest(context, url, params, subscriberesponse).execute();
         }

         if(isnotempty(cc.getSessioId()))
         {
             showlogs("session Id",cc.getSessioId());
          //   readresponse();
             deviceconfig();
             Content_Update_START();
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
            new AMSRequest(cc.gettrapurl(),  Utils.getInstance().setamsobject(context,AMS_INACTIVE));
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

                 new AMSRequest(cc.gettrapurl(), Utils.getInstance().setamsobject(context,AMS_ACTIVE));

                 handler.postDelayed(runnable,cc.getamstimeinterval());

             }
         };
        handler.postDelayed(runnable,cc.getamstimeinterval());
    }


    //update content check
    private void Content_Update_START()
    {
        handlerupdate=new Handler();
        runnableupdate=new Runnable() {
            @Override
            public void run() {

                setparams();
                String url=APP_SERVER_COMB+cc.getSessioId()+cmdmaanger_url;
                new NetworkRequest(context, url, params, cmdmanageresponse).execute();
                handlerupdate.postDelayed(runnableupdate,convertsectomins(cmdupdateinterval));
            }
        };
        handlerupdate.postDelayed(runnableupdate,convertsectomins(cmdupdateinterval));
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

        subscriberesponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("subscriberesponse", res);
                if (!TextUtils.isEmpty(res)) {
                    loadsubscriberinfo(res);
                }
            }
        };

        cmdmanageresponse= new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("cmdmangagerresponse", res);
                if (!TextUtils.isEmpty(res)) {
                    loadcmdmanageinfo(res);
                }
            }
        };


        livetvresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("live tv _response", res);
                if (!TextUtils.isEmpty(res)) {
                    loadlivetvcontent(res);
                }
            }
        };


        moviesresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("movies _response", res);
                if (!TextUtils.isEmpty(res)) {
                    loadMoviecontent(res);
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


                                if(isnotempty(cc.gettrapurl()) && cc.getamstimeinterval()!=0 )
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
        if(handlerupdate!=null)
        {
            handlerupdate.removeCallbacks(runnableupdate);
        }

    }

    private void setparams() {
        params = new ArrayList<>();
    }

    //load subscriber info
    private void loadsubscriberinfo(String  res)
    {
        if(readstatuscode(res,context))
        {
            try {
                JSONObject jsonObject=new JSONObject(res);
                cc.setusername(jsonObject.optString("emailId",""));
                showlogs("username",cc.getusername());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //load subscriber info
    private void loadcmdmanageinfo(String  res)
    {
        if(readstatuscode(res,context))
        {
            try {
               JSONObject jsonObject=new JSONObject(res);
               JSONArray jsonArray_cmd=jsonObject.optJSONArray("command");
               if(jsonArray_cmd.length()>0)
               {
                   for(int i=0;i<jsonArray_cmd.length();i++)
                   {
                       JSONObject  jsonObjectLoop=jsonArray_cmd.optJSONObject(i);
                       if(isnotempty(jsonObjectLoop.toString()))
                       {
                           if(jsonObjectLoop.optString("command","").equals(DATA_UPDATE)) {
                               JSONObject jsonObject_Loop_arg = jsonObjectLoop.optJSONObject("argument");
                               if (isnotempty(jsonObject_Loop_arg.toString()))
                               {
                                   if(jsonObject_Loop_arg.optString("updateType","").equals(CHANNEL_UPDATE))
                                   {
                                       showlogs("apiliveTV","apiliveTV");
                                       livetvapireq();
                                   }
                                   if(jsonObject_Loop_arg.optString("updateType","").equals(MOVIE_UPDATE))
                                   {
                                       showlogs("apivod","apivod");
                                       movieapireq();
                                   }
                               }
                           }
                       }

               }
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //livetv asyncall
    private void livetvapireq()
    {
        setparams();
        String url = APP_SERVER_COMB + cc.getSessioId() + livetv_url_prm;
        showlogs("livetv_url", url);
        new NetworkRequest(context, url, params, livetvresponse).execute();
    }

    //Movies asyncall
    private void movieapireq()
    {
        setparams();
        String url=APP_SERVER_COMB+cc.getSessioId()+movies_url_prm;
        showlogs("movies_url",url);
        new NetworkRequest(context, url, params, moviesresponse).execute();
    }



    //horizontal scroll view for content
    private void loadlivetvcontent(String res)
    {
        try{
            ArrayList<homecontent_model> livetvmodels = new ArrayList<>();
            JSONObject jsonObject_main=new JSONObject(res);
            JSONArray jsonArray=jsonObject_main.optJSONArray("Channel");
            if(isnotempty(jsonArray.toString()))
            {
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object_loop=(jsonArray.optJSONObject(i));
                    if(isnotempty(object_loop.toString()))
                    {
                        homecontent_model data=new homecontent_model();
                        data.setServiceassetid(object_loop.optInt("serviceassetid",0));
                        data.setName(object_loop.getString("name"));
                        //setting image content
                        JSONArray jsonArray_image=object_loop.getJSONArray("image");
                        if(isnotempty(jsonArray_image.toString()))
                        {
                            for(int j=0;j<jsonArray_image.length();j++)
                            {
                                String image_url;
                                JSONObject jsonObject_loop_image=jsonArray_image.optJSONObject(j);
                                if(isnotempty(jsonObject_loop_image.toString()))
                                {
                                    String name_img=jsonObject_loop_image.optString("name");
                                    if(name_img.length()>0 && name_img.equals("Icon"))
                                    {
                                        image_url=jsonObject_loop_image.optString("url");
                                        if(isnotempty(image_url))
                                        {
                                            data.setImage(image_url);
                                        }
                                    }
                                    else
                                    {
                                        image_url=jsonObject_loop_image.optString("url");
                                        if(isnotempty(image_url))
                                        {
                                            data.setImage(image_url);
                                        }
                                    }

                                }
                            }
                        }

                        JSONArray jsonArray_media = null;
                        //setting media content
                        if(object_loop.has("streamProfile"))
                        {
                            jsonArray_media=object_loop.optJSONArray("streamProfile");
                        }
                        else if(object_loop.has("playbackStreamProfile"))
                        {
                            jsonArray_media=object_loop.optJSONArray("playbackStreamProfile");
                        }

                        if(jsonArray_media!=null)
                        {
                            if((isnotempty(jsonArray_media.toString())))
                            {
                                for(int j=0;j<jsonArray_media.length();j++)
                                {
                                    JSONObject jsonObject_media=jsonArray_media.optJSONObject(j);
                                    if(isnotempty(jsonObject_media.toString()))
                                    {
                                        JSONArray jsonArray_urltype=jsonObject_media.optJSONArray("urltype");
                                        if(isnotempty(jsonArray_urltype.toString()))
                                        {
                                            for(int k=0;k<jsonArray_urltype.length();k++)
                                            {
                                                JSONObject jsonObject_urltype=jsonArray_urltype.optJSONObject(k);
                                                if(isnotempty(jsonObject_urltype.toString()))
                                                {
                                                    data.setMediacontent(jsonObject_urltype.optString("value",""));
                                                    showlogs("url_data",data.getMediacontent());
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }


                        //set category id
                        JSONArray jsonArray_catid=object_loop.optJSONArray("categoryId");
                        for(int l=0;l<jsonArray_catid.length();l++)
                        {
                            JSONObject jsonObject_catloop=jsonArray_catid.optJSONObject(l);
                            data.setCategoryId(jsonObject_catloop.optInt("id",0));
                            if(isnotempty(String.valueOf(jsonObject_catloop.optInt("id"))))
                            {
                                break;
                            }
                        }

                        //set id
                        data.setId(object_loop.optInt("id",0));

                        //set serviceassetid
                        data.setServiceassetid(object_loop.optInt("serviceassetid",0));


                        //set like , fav data
                        data.setFavourite(object_loop.optBoolean("isFavourite",false));
                        data.setLikeCount(object_loop.optInt("likeCount",0));
                        livetvmodels.add(data);
                    }
                }
            }
            if(livetvmodels.size()>0) {
                cc.setlivetvContent(converttoGSON(livetvmodels));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //horizontal scroll view for content
    private void loadMoviecontent(String res)
    {
        try{
            ArrayList<homecontent_model> vodmodels = new ArrayList<>();
            JSONObject jsonObject_main=new JSONObject(res);
            JSONArray jsonArray=jsonObject_main.optJSONArray("movie");

            if(isnotempty(jsonArray.toString()))
            {
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object_loop=(jsonArray.optJSONObject(i));
                    if(isnotempty(object_loop.toString()))
                    {
                        homecontent_model data=new homecontent_model();
                        data.setServiceassetid(object_loop.optInt("serviceassetid",0));
                        data.setName(object_loop.getString("name"));
                        //setting image content
                        JSONArray jsonArray_image=object_loop.getJSONArray("image");
                        if(isnotempty(jsonArray_image.toString()))
                        {
                            for(int j=0;j<jsonArray_image.length();j++)
                            {
                                String image_url;
                                JSONObject jsonObject_loop_image=jsonArray_image.optJSONObject(j);
                                if(isnotempty(jsonObject_loop_image.toString()))
                                {
                                    String name_img=jsonObject_loop_image.optString("name");
                                    if(name_img.length()>0 && name_img.equals("Icon"))
                                    {
                                        image_url=jsonObject_loop_image.optString("url");
                                        if(isnotempty(image_url))
                                        {
                                            data.setImage(image_url);
                                        }
                                    }
                                    else
                                    {
                                        image_url=jsonObject_loop_image.optString("url");
                                        if(isnotempty(image_url))
                                        {
                                            data.setImage(image_url);
                                        }
                                    }

                                }
                            }
                        }

                        JSONArray jsonArray_media = null;
                        //setting media content
                        if(object_loop.has("streamProfile"))
                        {
                            jsonArray_media=object_loop.optJSONArray("streamProfile");
                        }
                        else if(object_loop.has("playbackStreamProfile"))
                        {
                            jsonArray_media=object_loop.optJSONArray("playbackStreamProfile");
                        }

                        if(jsonArray_media!=null)
                        {
                            if((isnotempty(jsonArray_media.toString())))
                            {
                                for(int j=0;j<jsonArray_media.length();j++)
                                {
                                    JSONObject jsonObject_media=jsonArray_media.optJSONObject(j);
                                    if(isnotempty(jsonObject_media.toString()))
                                    {
                                        JSONArray jsonArray_urltype=jsonObject_media.optJSONArray("urltype");
                                        if(isnotempty(jsonArray_urltype.toString()))
                                        {
                                            for(int k=0;k<jsonArray_urltype.length();k++)
                                            {
                                                JSONObject jsonObject_urltype=jsonArray_urltype.optJSONObject(k);
                                                if(isnotempty(jsonObject_urltype.toString()))
                                                {
                                                    data.setMediacontent(jsonObject_urltype.optString("value",""));
                                                    showlogs("url_data",data.getMediacontent());
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                        //meta data content
                        if(object_loop.has("metaData"))
                        {
                            JSONObject jsonObject_metadata=object_loop.optJSONObject("metaData");
                            if(isnotempty(jsonObject_metadata.toString()))
                            {
                                data.setIsmetadata(true);
                                data.setMoviereleaseyear(jsonObject_metadata.optString("YEAROFRELEASE", ""));
                                data.setMoviesynopsis(jsonObject_metadata.optString("SYNOPSIS", ""));
                                data.setMovieruntime(jsonObject_metadata.optString("RUNTIME", ""));
                                data.setMoviecastcrew(jsonObject_metadata.optString("CASTCREW",""));
                                data.setMoviecategory(jsonObject_metadata.optString("GENRE", ""));
                            }
                            else
                            {
                                data.setIsmetadata(false);
                            }
                        }
                        else
                        {
                            data.setIsmetadata(false);
                        }

                        //set category id
                        JSONArray jsonArray_catid=object_loop.optJSONArray("categoryId");
                        for(int l=0;l<jsonArray_catid.length();l++)
                        {
                            JSONObject jsonObject_catloop=jsonArray_catid.optJSONObject(l);
                            data.setCategoryId(jsonObject_catloop.optInt("id",0));
                            if(isnotempty(String.valueOf(jsonObject_catloop.optInt("id"))))
                            {
                                break;
                            }
                        }

                        //set id
                        data.setId(object_loop.optInt("id",0));

                        //set serviceassetid
                        data.setServiceassetid(object_loop.optInt("serviceassetid",0));


                        //set like , fav data
                        data.setFavourite(object_loop.optBoolean("isFavourite",false));
                        data.setLikeCount(object_loop.optInt("likeCount",0));
                        vodmodels.add(data);
                    }
                }
            }

            if(vodmodels.size()>0) {
                cc.setmovieContent(converttoGSON(vodmodels));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
