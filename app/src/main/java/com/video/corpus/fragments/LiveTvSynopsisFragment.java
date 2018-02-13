package com.video.corpus.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.video.corpus.Interface.MediaSynopsisInterface;
import com.video.corpus.R;
import com.video.corpus.global.commonclass;
import com.video.corpus.media.PlayerLiveTvActivity;
import com.video.corpus.pojos.homecontent_model;

import java.util.ArrayList;


///**
// * Created by Bhaskar.c on 1/8/2018.
// */

public class LiveTvSynopsisFragment extends BaseFragment {


    private Context context;
    private LayoutInflater layoutInflater;
    private View view;
    private LinearLayout linContent;
    private commonclass cc;
    private ConstraintLayout  conslayMain;
    private ArrayList<homecontent_model> models;
    private TextView txtTitle,txtException,txtMovieException;
    private ProgressBar progressbar;
    private boolean isrelatedmovies=false;
    private RelativeLayout relException;
    private MediaSynopsisInterface mediaSynopsisInterface;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        cc=new commonclass(context);
        layoutInflater=inflater;
        view = inflater.inflate(R.layout.fragment_player_livetv, container, false);
        gethomecontent();
        initViews();



        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mediaSynopsisInterface=(MediaSynopsisInterface) getActivity();
    }

    //initiating views
    private void initViews() {

        toolbartext(context, getResources().getString(R.string.livetv));
        conslayMain=view.findViewById(R.id.conslay_main);
        txtTitle=view.findViewById(R.id.txt_name);
        progressbar=view.findViewById(R.id.progressbar);
        txtException= view.findViewById(R.id.Txt_exception);
        linContent = view.findViewById(R.id.Lin_content);
        txtMovieException=view.findViewById(R.id.Txt_movie_exception);
        relException=view.findViewById(R.id.Rel_exception);
        assignmetadata();

    }


    //get home content
    private void gethomecontent()
    {
        if(cc.ishomecontent())
        {
            if(isnotempty(cc.getHomeContent())) {
                Gson gson = new Gson();
                models = gson.fromJson(cc.getHomeContent(),
                        new TypeToken<ArrayList<homecontent_model>>() {
                        }.getType());
            }
        }
        else
        {
            if(isnotempty(cc.getlivetvContent())) {
                Gson gson = new Gson();
                models = gson.fromJson(cc.getlivetvContent(),
                        new TypeToken<ArrayList<homecontent_model>>() {
                        }.getType());
            }
        }

    }

    //assign meta data
    private void assignmetadata()
    {
        if(isnotempty(models.toString()) && models.size()>0)
        {
            if(isnotempty(models.get(cc.getContentClickpos()).getName()))
            {
                txtTitle.setText(models.get(cc.getContentClickpos()).getName());
            }
        }

        if(models.size()>0)
        {
            addContent_views(models,getString(R.string.relatedchannels));
            linContent.setVisibility(View.VISIBLE);

            if(!isrelatedmovies)
            {
                displaynomovies();
            }
        }
        else
        {
            displaynodata();
        }
        displaycontent();

    }

    //display content
    private void displaycontent()
    {
        progressbar.setVisibility(View.GONE);
        conslayMain.setVisibility(View.VISIBLE);
    }

    //display exception no movies
    private void displaynomovies()
    {
        relException.setVisibility(View.VISIBLE);
        txtMovieException.setVisibility(View.VISIBLE);
    }

    //display exception no data
    private void displaynodata()
    {
        txtException.setVisibility(View.VISIBLE);
    }

    //adding content dynamically (live tv,channels , etc..)
    private void addContent_views(final ArrayList<homecontent_model> homecontent_models,
                                  String Title)
    {


        final LinearLayout linearLayoutDyanmic;
        View view;
        linearLayoutDyanmic=new LinearLayout(context);
        setparams(linearLayoutDyanmic);
        linearLayoutDyanmic.setOrientation(LinearLayout.HORIZONTAL);

        //  addcontent_title_dynamic(Title);
        addcontent_title(Title,homecontent_models);

        for(int i=0;i<models.size();i++)
        {
            if((models.get(cc.getContentClickpos()).getCategoryId()==models.get(i).getCategoryId())
                    && !(models.get(cc.getContentClickpos()).getServiceassetid()==models.get(i).getServiceassetid()))
            {
                isrelatedmovies=true;
                showlogs("homecontent_models",homecontent_models.get(i).getName());
                view=layoutInflater.inflate(R.layout.content_layout,nullParent);
                view.setTag(homecontent_models.get(i));
                ImageView imageView= view.findViewById(R.id.imgview);
                Picasso.with(context).load(homecontent_models.get(i).getImage()).placeholder(R.mipmap.placeholder_crousel).error(R.mipmap.ic_launcher).into(imageView);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(getScreenWidth()/width_num,(int)((getScreenWidth()/width_num)*(aspectratio))));
                TextView textView= view.findViewById(R.id.txtview);
                textView.setText(homecontent_models.get(i).getName());
                textView.setWidth((getScreenWidth()/width_num));
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins((int)getResources().getDimension(R.dimen.dp_four),0,0,0);
                view.setLayoutParams(layoutParams);
                view.setId(i);
                view.setTag(String.valueOf(true));
                linearLayoutDyanmic.addView(view);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaSynopsisInterface.onfragmentclick();
                        if(isnotempty(String.valueOf(v.getId())))
                        {
                            cc.setContentClickpos((v.getId()));
                        }
                        if(isnotempty(String.valueOf(v.getTag())))
                        {
                            showlogs("islivetv",""+v.getTag());
                            cc.setIslivetv(Boolean.parseBoolean((String)v.getTag()));
                        }
                        if(cc.ishomecontent())
                        {
                            sethomecontent(homecontent_models,cc);
                        }
                        else
                        {
                            setlivecontent(homecontent_models,cc);
                        }

                                Intent intent=new Intent(context, PlayerLiveTvActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                    }
                });
            }
        }

        if(isrelatedmovies)
        {
            HorizontalScrollView horizontalScrollView=new HorizontalScrollView(context);
            setparams(horizontalScrollView);
            horizontalScrollView.addView(linearLayoutDyanmic);
            horizontalScrollView.setTag(homecontent_models.toString());
            linContent.addView(horizontalScrollView);
        }
    }




    //add content title static
    private void addcontent_title(String contentTitle, final ArrayList<homecontent_model> homecontent_models)
    {
        View view=layoutInflater.inflate(R.layout.title,nullParent);
        TextView txttitle= view.findViewById(R.id.Txt_title);
        final TextView txtMoreOpts= view.findViewById(R.id.Txt_moreOpts);
        txtMoreOpts.setTag(homecontent_models.toString());
        if(isnotempty(contentTitle))
        {
            txttitle.setText(contentTitle);
        }

        linContent.addView(view);
    }

}
