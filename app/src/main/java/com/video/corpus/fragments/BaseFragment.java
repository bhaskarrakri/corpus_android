package com.video.corpus.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import com.google.gson.Gson;
import com.video.corpus.Interface.Constants;
import com.video.corpus.global.Util;
import com.video.corpus.global.commonclass;
import com.video.corpus.google.LargeBannerView;
import com.video.corpus.pojos.homecontent_model;

import org.json.JSONArray;

import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 12/19/2017.
// */

public class BaseFragment extends Fragment implements Constants {

    private final boolean showlogs=true;
    JSONArray jsonArray_base;
    public  final ViewGroup nullParent = null;
    Activity activity;
    final Util util = Util.getInstance();
    private commonclass cc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        cc=new commonclass(activity);
    }

    //show logs
    void showlogs(String msg, String key)
    {
        if(showlogs)
        {
            if(!(TextUtils.isEmpty(msg)) &&  !(TextUtils.isEmpty(key)))
            {
                Log.e(msg,key);
            }
        }

    }

    //set height programatically
    protected void setheight(View view, int height)
    {

        double viewpagerdynamicheight = 0.35;
        showlogs("height",""+(int)(height* viewpagerdynamicheight));
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(height* viewpagerdynamicheight));
        view.setLayoutParams(layoutParams);

    }

   // get device width programatically
    public  int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    //get device height programatically
    protected int getScreenHeight() {
        int height=Resources.getSystem().getDisplayMetrics().heightPixels;
        showlogs("heightPixels",""+height);
        return height;
    }



    protected void toolbartext(Context context, String res)
    {

//        if(!TextUtils.isEmpty(res) && context!=null)
//        {
//            View  toolbar=getActivity().findViewById(R.id.toolbar);
//            TextView toolbartext=(TextView)toolbar.findViewById(R.id.Txt_title);
//            toolbartext.setText(res);
//        }
    }

    protected AdView addADMOB(Context context, int addtype, String addid)
    {

        return LargeBannerView.BannerView(context, addtype,addid);
    }


    protected  boolean isnotempty(String data)
    {
        return  !TextUtils.isEmpty(data);
    }


    protected  void setparams(View view)
    {
      //  double contentdynamicheight = 0.25;
    //    ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(getScreenHeight()* contentdynamicheight));
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
    }

    protected void showtoast(Context context,String msg,int type)
    {
        if(isnotempty(msg))
        {
            if(type==toast_short)
            {
                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
            }

        }
    }

    //set content to sharedpref
    public void sethomecontent(ArrayList<homecontent_model> data,commonclass cc)
    {
        Gson gson = new Gson();
        cc.setHomeContent(gson.toJson(data));
    }


    //set content to sharedpref
    public void setlivecontent(ArrayList<homecontent_model> data,commonclass cc)
    {
        Gson gson = new Gson();
        cc.setlivetvContent(gson.toJson(data));
    }


    //set content to sharedpref
    public void setmoviecontent(ArrayList<homecontent_model> data,commonclass cc)
    {
        Gson gson = new Gson();
        cc.setmovieContent(gson.toJson(data));
    }

    //set content to sharedpref
    public void setcatchupcontent(ArrayList<homecontent_model> data,commonclass cc)
    {
        Gson gson = new Gson();
        cc.setcatchupContent(gson.toJson(data));
    }


    protected boolean checkpremium()
    {
        boolean checkresult=false;

        if(cc.isLoggedIn() && cc.getSessioId().length()>0)
        {
            checkresult=true;
        }
        return  checkresult;
    }


    public  void showtaost(Context context,String msg)
    {
        if(isnotempty(msg))
        {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }

    }

}
