package com.video.corpus.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.video.corpus.Interface.ItemclickListener;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.R;
import com.video.corpus.adapters.catchupadapter;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.NetworkRequest;
import com.video.corpus.pojos.homecontent_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 1/31/2018.
// */

public class CatchUpFragment extends BaseFragment implements ItemclickListener {
    private Response_string<String> catchupresponse;
    private Context context;
    private View view;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView txtexception;
    private commonclass cc;
    private ArrayList<homecontent_model> models;
    private ArrayList<String> params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        cc=new commonclass(context);
        cc.setIslivetv(true);
        view = inflater.inflate(R.layout.fragment_home_content, container, false);


        initViews();


        if(cc.getcatchupContent().length()==0) {
            loaddynamicdata();
        }
        else
        {
            loadstaticdata();
        }

        return view;
    }

    private void initViews() {
        txtexception= view.findViewById(R.id.Txt_exception);
        recyclerView= view.findViewById(R.id.recycler);
        progressBar= view.findViewById(R.id.progressbar);

    }


    //loading data in to gridview
    private void gridviewadapter()
    {

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager
                (context,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new catchupadapter(context,models,this));
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
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


    }

    @Override
    public void onitemclcik(int pos) {
        cc.setContentClickpos(pos);
//        setcatchupcontent(models,cc);
//        cc.setIshomecontent(false);
//        Intent intent=new Intent(context, PlayerLiveTvActivity.class);
//        startActivity(intent);
    }


    private void setparams() {
        params = new ArrayList<>();
    }



    private void readresponse() {
        catchupresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("live tv _response", res);
                if (!TextUtils.isEmpty(res)) {
                    loadcontent(res);
                }
                else
                {
                    nodata();
                }
            }
        };
    }

    //horizontal scroll view for content
    private void loadcontent(String res)
    {
        try{
            JSONObject jsonObject_main=new JSONObject(res);
            JSONArray jsonArray=jsonObject_main.optJSONArray("catchupEvents");
            if(isnotempty(jsonArray.toString()))
            {
                models=new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object_loop=(jsonArray.optJSONObject(i));
                    if(isnotempty(object_loop.toString()))
                    {
                        homecontent_model data=new homecontent_model();

                        data.setName(object_loop.getString("programName"));
                        data.setImage(object_loop.optString("image",""));
                        data.setProgramDescription(object_loop.optString("programDescription",""));
                        data.setProgramId(object_loop.optString("programId",""));
                        models.add(data);
                    }
                }
                Loadgridview();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void Loadgridview() {
        if(models.size()>0) {
            setcatchupcontent(models, cc);
            gridviewadapter();
        }
        else
        {
            nodata();
        }
    }


    //load dynamic data
    private  void loaddynamicdata()
    {
        showlogs("getcatchup",""+"loaddynamicdata");
        readresponse();
        setparams();
        if(checkpremium())
        {
            String url=APP_SERVER_COMB+cc.getSessioId()+catchup_url_prm;
            showlogs("catchup_url",url);
            new NetworkRequest(context, url, params, catchupresponse).execute();
        }
        else
        {
            showlogs("catchup_url",catchup_url_free);
            new NetworkRequest(context, catchup_url_free, params, catchupresponse).execute();
        }
    }

    //load static data
    private  void loadstaticdata()
    {
        showlogs("getcatchup",""+"loadstaticdata");
        if(isnotempty(cc.getcatchupContent())) {
            showlogs("getcatchup","true");
            Gson gson = new Gson();
            models = gson.fromJson(cc.getcatchupContent(),
                    new TypeToken<ArrayList<homecontent_model>>() {
                    }.getType());
        }
        Loadgridview();
    }
}