package com.video.corpus.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.video.corpus.adapters.gridadapter;
import com.video.corpus.global.commonclass;
import com.video.corpus.media.PlayerMoviesActivity;
import com.video.corpus.network.NetworkRequest;
import com.video.corpus.pojos.homecontent_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 1/29/2018.
// */

public class MoviesFragment extends BaseFragment  implements ItemclickListener {
    private Response_string<String> moviesresponse;
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
        cc.setIslivetv(false);
        view = inflater.inflate(R.layout.fragment_home_content, container, false);

        initViews();
        if(cc.getmovieContent().length()==0) {
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
        cc.setmovieContent(converttoGSON(models));
        cc.setIshomecontent(false);
        Intent intent=new Intent(context, PlayerMoviesActivity.class);
        startActivity(intent);
    }


    private void setparams() {
        params = new ArrayList<>();
    }



    private void readresponse() {
        moviesresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("movies _response", res);
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
            JSONArray jsonArray=jsonObject_main.optJSONArray("movie");
            if(isnotempty(jsonArray.toString()))
            {
                models=new ArrayList<>();

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
            cc.setmovieContent(converttoGSON(models));
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
        showlogs("getmovie",""+"loaddynamicdata");
        readresponse();
        setparams();
        if(checkpremium())
        {
            String url=APP_SERVER_COMB+cc.getSessioId()+movies_url_prm;
            showlogs("movies_url",url);
            new NetworkRequest(context, url, params, moviesresponse).execute();
        }
        else
        {
            showlogs("movies_url",movies_url_free);
            new NetworkRequest(context, movies_url_free, params, moviesresponse).execute();
        }
    }

    //load static data
    private  void loadstaticdata()
    {
        showlogs("getmovie",""+"loadstaticdata");
        if(isnotempty(cc.getmovieContent())) {
            showlogs("getmovie","true");
            Gson gson = new Gson();
            models = gson.fromJson(cc.getmovieContent(),
                    new TypeToken<ArrayList<homecontent_model>>() {
                    }.getType());
        }
        Loadgridview();
    }
}