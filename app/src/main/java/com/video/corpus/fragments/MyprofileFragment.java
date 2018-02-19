package com.video.corpus.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.R;
import com.video.corpus.adapters.PackagesAdapter;
import com.video.corpus.adapters.ProfileAdapter;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.NetworkRequest;
import com.video.corpus.pojos.homecontent_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 2/19/2018.
// */

public class MyprofileFragment extends BaseFragment  {
    private Response_string<String> myprofileresponse;
    private Context context;
    private View view;
    private ProgressBar progressBar;
    private RecyclerView recyclerView,recyclerViewpackages;
    private TextView txtexception;
    private commonclass cc;
    private  String[] titles;
    private ArrayList<String> params,values;
    private LinearLayout linlayPackages;
    private RelativeLayout customview;
    private LayoutInflater layoutInflater;
    private  boolean ispackshown=false;
    private ImageView imageView_icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        cc = new commonclass(context);
        cc.setIslivetv(true);
        layoutInflater=inflater;
        view = inflater.inflate(R.layout.myprofile_layout, container, false);


        initViews();



        return view;
    }

    private void initViews() {
        txtexception = view.findViewById(R.id.Txt_exception);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerViewpackages= view.findViewById(R.id.recycler_packages);
        progressBar = view.findViewById(R.id.progressbar);
        linlayPackages = view.findViewById(R.id.linlay_packages);
        customview= view.findViewById(R.id.customview);
         imageView_icon=view.findViewById(R.id.img_arrow);
        if(!isnotempty(cc.getprofileinfo()))
        {

            loaddynamicdata();
        }
        else
        {
            loadstaticdata();
        }


    }

    private void loadstaticdata() {

        if (!TextUtils.isEmpty(cc.getprofileinfo())) {

            loadsubscriberinfo(cc.getprofileinfo());
        }
        else
        {
            loaddynamicdata();
        }

    }

    private void loaddynamicdata() {

        readresponse();
        setparams();
        String url=APP_SERVER_COMB+cc.getSessioId()+subscriber_info_url;
        showlogs("myprofile url", url);
        new NetworkRequest(context, url, params, myprofileresponse).execute();
    }


    private void setparams() {
        params = new ArrayList<>();
    }



    private void readresponse() {
        myprofileresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("myprofile _response", res);
                if (!TextUtils.isEmpty(res)) {

                    loadsubscriberinfo(res);
                }
                else
                {
                    nodata();
                }
            }
        };
    }

    //load subscriber info
    private void loadsubscriberinfo(String  res)
    {
        if(readstatuscode(res,context))
        {
            try {
                values=new ArrayList<>();
                titles=new String[getResources().getStringArray(R.array.profile_array).length];
                titles=getResources().getStringArray(R.array.profile_array);
                cc.setprofileinfo(res);
                JSONObject jsonObject=new JSONObject(res);
                cc.setusername(jsonObject.optString("emailId",""));

                values.add(jsonObject.optString("title","")
                .concat(" ")
                .concat(jsonObject.optString("firstName",""))
                .concat( " ")
                .concat(jsonObject.optString("middleName","")
                .concat(" ")
                .concat(jsonObject.optString("lastName","")).concat(" ")));
                values.add(jsonObject.optString("contactNo",""));
                values.add(jsonObject.optString("emailId",""));

                if(jsonObject.optJSONArray("packages").length()>0)
                {
                    Loadpackages(jsonObject.optJSONArray("packages").toString());

                }
                else
                {
                    linlayPackages.setVisibility(View.GONE);
                    Loadrecyclerview();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void Loadrecyclerview() {
        if(values.size()>0) {
            recycleradapter();
        }
        else
        {
            nodata();
        }
    }

        //loading packages
    private void Loadpackages(String res) {
        if(isnotempty(res)) {
            fetchpackageslist(res);

        }
        else
        {
            Loadrecyclerview();
        }
    }

    //fetching packages list
    private void fetchpackageslist(String res) {

        try{
            JSONArray jsonArray_pack=new JSONArray(res);
             ArrayList<homecontent_model> modelspacakages=new ArrayList<>();
            for(int i=0;i<jsonArray_pack.length();i++)
            {
                homecontent_model data=new homecontent_model();
                JSONObject jsonObject_loop=jsonArray_pack.getJSONObject(i);
                data.setName(jsonObject_loop.optString("packagename",""));
                data.setpackagestatus(jsonObject_loop.optString("status",""));
                data.setpackageexpiryDate(jsonObject_loop.optString("expiryDate",""));
                modelspacakages.add(data);
            }

            if(modelspacakages.size()>0)
            {
                recycleradapterPackages(modelspacakages);
            }
            else
            {
                Loadrecyclerview();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //loading data in to gridview
    private void recycleradapter()
    {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager
                (context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ProfileAdapter(values,titles));
        dataprofileavialble();
    }


    //loading data in to gridview
    private void recycleradapterPackages(ArrayList<homecontent_model> models)
    {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager
                (context,LinearLayoutManager.VERTICAL,false);
        recyclerViewpackages.setLayoutManager(linearLayoutManager);
        recyclerViewpackages.setAdapter(new PackagesAdapter(context,models));
        dataavialble();
    }



    //error display nodata
    private void nodata()
    {
        progressBar.setVisibility(View.GONE);
        txtexception.setVisibility(View.VISIBLE);
    }

    //display data
    private void dataavialble()
    {

        addview();

    }

    //display only profile data
    private void dataprofileavialble()
    {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    //add view
    private void addview()
    {

//        View custom=layoutInflater.inflate(R.layout.mypackages_layout, nullParent);
//        final ImageView imageView_icon=custom.findViewById(R.id.img_arrow);
//        customview.addView(custom);

        customview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ispackshown) {
                    imageView_icon.setImageResource(R.mipmap.down);
                    ispackshown=false;
                    recyclerViewpackages.setVisibility(View.GONE);
                }
                else
                {
                    imageView_icon.setImageResource(R.mipmap.up);
                    ispackshown=true;
                    recyclerViewpackages.setVisibility(View.VISIBLE);
                }

            }
        });

        linlayPackages.setVisibility(View.VISIBLE);
        Loadrecyclerview();
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}