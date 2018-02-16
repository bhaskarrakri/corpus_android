package com.video.corpus.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.video.corpus.media.PlayerMoviesActivity;
import com.video.corpus.pojos.homecontent_model;

import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 1/8/2018.
// */

public class MoviesSynopsisFragment extends BaseFragment {


    private Context context;
    private View view;
    private TextView txtTitle, txtMovieCertificate, txtMovieTime, txtMovieYear, txtMovieCategory
            , txtSynopsisContent, txtCastcrewContent, txtMovieException;
    private ImageView imgSynopsis;
    private ConstraintLayout consSynopsis, conslayMain;
    private ProgressBar progressbar;
    private ArrayList<homecontent_model> models;
    private View txtTimeview, txtYearview;
    private boolean issynopsis=false;
    private boolean issynopsisopen=false,isrelatedmovies=false;
    private RelativeLayout  relException;
    private LinearLayout linContent;
    private  LayoutInflater layoutInflater;
    private commonclass cc;
    private MediaSynopsisInterface mediaSynopsisInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_player_movies, container, false);

        cc=new commonclass(context);
        layoutInflater=inflater;
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
        txtTitle=view.findViewById(R.id.Txt_title);
        txtMovieCertificate=view.findViewById(R.id.Txt_movieCertificate);
        txtMovieTime=view.findViewById(R.id.Txt_movieTime);
        txtMovieYear=view.findViewById(R.id.Txt_movieYear);
        txtMovieCategory=view.findViewById(R.id.Txt_movieCategory);
        txtSynopsisContent=view.findViewById(R.id.Txt_synopsis_content);
        txtCastcrewContent=view.findViewById(R.id.Txt_castcrew_content);
        txtMovieException=view.findViewById(R.id.Txt_movie_exception);
        imgSynopsis=view.findViewById(R.id.Img_synopsis);
        consSynopsis=view.findViewById(R.id.cons_synopsis);
        conslayMain=view.findViewById(R.id.conslay_main);
        progressbar=view.findViewById(R.id.progressbar);
        txtTimeview=view.findViewById(R.id.Txt_timeview);
        txtYearview=view.findViewById(R.id.Txt_yearview);
        relException= view.findViewById(R.id.Rel_exception);
        linContent = view.findViewById(R.id.Lin_content);
        assignmetadata();


        imgSynopsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(issynopsis)
                {
                    if(!issynopsisopen)
                    {
                        issynopsisopen=true;
                        imgSynopsis.setImageResource(R.drawable.up);
                        consSynopsis.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        issynopsisopen=false;
                        imgSynopsis.setImageResource(R.drawable.down);
                        consSynopsis.setVisibility(View.GONE);
                    }

                }
            }
        });


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
            if(isnotempty(cc.getmovieContent())) {
                Gson gson = new Gson();
                models = gson.fromJson(cc.getmovieContent(),
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
            txtTitle.setText(models.get(cc.getContentClickpos()).getName());
            if(models.get(cc.getContentClickpos()).isIsmetadata())
            {
                checkdata(txtMovieTime,models.get(cc.getContentClickpos()).getMovieruntime());
                checkdata(txtMovieCategory,models.get(cc.getContentClickpos()).getMoviecategory());
                checkdata(txtMovieCertificate,"");
                checkdata(txtMovieYear,models.get(cc.getContentClickpos()).getMoviereleaseyear());
                checkmetadata(txtCastcrewContent,models.get(cc.getContentClickpos()).getMoviecastcrew());
                checkmetadata(txtSynopsisContent,models.get(cc.getContentClickpos()).getMoviesynopsis());

                if(!(isnotempty(txtMovieCategory.getText().toString())))
                {
                    txtYearview.setVisibility(View.GONE);
                }
                if(!(isnotempty(txtMovieYear.getText().toString())))
                {
                    txtTimeview.setVisibility(View.GONE);
                }
                if(isnotempty(txtMovieTime.getText().toString()))
                {
                    txtMovieTime.setText(txtMovieTime.getText().toString().concat(" ").concat(getString(R.string.min)));
                }

                issynopsis = !(txtCastcrewContent.getText().toString().equals(getString(R.string.na)) &&
                        txtSynopsisContent.getText().toString().equals(getString(R.string.na)));

                if(issynopsis)
                {
                    imgSynopsis.setVisibility(View.VISIBLE);
                }
              else
                {
                    imgSynopsis.setVisibility(View.GONE);
                }
            }
            else
            {
                consSynopsis.setVisibility(View.GONE);
            }
        }


        if(models.size()>0)
        {
                addContent_views(models,getString(R.string.relatedmovies));
                linContent.setVisibility(View.VISIBLE);

             if(!isrelatedmovies)
            {
                displaynomovies();
            }
        }
        else
        {
            displaynomovies();
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

    //check content
    private void checkdata(TextView textView,String data)
    {
        if(isnotempty(data))
        {
                textView.setText(data);
        }
        else
        {
            textView.setVisibility(View.GONE);
        }
    }

    //check content
    private void checkmetadata(TextView textView,String data)
    {
        if(isnotempty(data))
        {
            textView.setText(data);
        }
        else
        {
            textView.setText(R.string.na);
        }
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
                    && !(models.get(cc.getContentClickpos()).getId()==models.get(i).getId()))
            {
                isrelatedmovies=true;
                showlogs("homecontent_models",homecontent_models.get(i).getName());
                view=layoutInflater.inflate(R.layout.content_layout,nullParent);
                view.setTag(homecontent_models.get(i));
                ImageView imageView= view.findViewById(R.id.imgview);
                Picasso.with(context).load(homecontent_models.get(i).getImage()).placeholder(R.mipmap.placeholder_crousel).error(R.mipmap.ic_error_image).into(imageView);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(getScreenWidth()/width_num,(int)((getScreenWidth()/width_num)*(aspectratio_movies))));
                TextView textView= view.findViewById(R.id.txtview);
                textView.setText(homecontent_models.get(i).getName());
                textView.setWidth((getScreenWidth()/width_num));
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins((int)getResources().getDimension(R.dimen.dp_four),0,0,0);
                view.setLayoutParams(layoutParams);
                view.setId(i);
                view.setTag(String.valueOf(false));
                linearLayoutDyanmic.addView(view);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
                            cc.setHomeContent(converttoGSON(homecontent_models));
                        }
                        else
                        {
                            cc.setmovieContent(converttoGSON(homecontent_models));
                        }
                        mediaSynopsisInterface.onfragmentclick();
                        Intent intent=new Intent(context, PlayerMoviesActivity.class);
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