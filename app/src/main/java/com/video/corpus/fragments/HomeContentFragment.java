package com.video.corpus.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.video.corpus.Interface.ItemclickListener;
import com.video.corpus.R;
import com.video.corpus.adapters.gridadapter;
import com.video.corpus.global.commonclass;
import com.video.corpus.media.PlayerLiveTvActivity;
import com.video.corpus.media.PlayerMoviesActivity;
import com.video.corpus.pojos.homecontent_model;

import java.util.ArrayList;


///**
// * Created by Bhaskar.c on 1/2/2018.
// */

public class HomeContentFragment extends BaseFragment  implements ItemclickListener{

    private Context context;
    private  View view;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView txtexception;
    private commonclass cc;
    private ArrayList<homecontent_model> models;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        cc=new commonclass(context);

        view = inflater.inflate(R.layout.fragment_home_content, container, false);

        initViews();


        return view;
    }

    private void initViews() {

        txtexception= view.findViewById(R.id.Txt_exception);
        recyclerView= view.findViewById(R.id.recycler);
        progressBar= view.findViewById(R.id.progressbar);
        if(isnotempty(cc.getHomeContent()))
        {
            Gson gson=new Gson();
            models = gson.fromJson(cc.getHomeContent(),
                    new TypeToken<ArrayList<homecontent_model>>() {}.getType());
            showlogs("home content models",""+models);

            if(models.size()>0)
            {
                gridviewadapter();
            }
            else
            {
                nodata();
            }
        }
    }


    //loading data in to gridview
    private void gridviewadapter()
    {
       GridLayoutManager layoutManager = new GridLayoutManager (context,
               recyclerview_columns,
                LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new gridadapter(context,models,this));
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
        cc.setHomeContent(converttoGSON(models));
        Intent intent;
        if(cc.islivetv())
        {
             intent=new Intent(context, PlayerLiveTvActivity.class);
        }
        else
        {
            intent=new Intent(context, PlayerMoviesActivity.class);
        }
        startActivity(intent);
    }
}
