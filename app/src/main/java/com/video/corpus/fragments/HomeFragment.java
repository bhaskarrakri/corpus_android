package com.video.corpus.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.video.corpus.controllers.BaseActivity;
import com.video.corpus.global.commonclass;
import com.video.corpus.media.PlayerLiveTvActivity;
import com.video.corpus.media.PlayerMoviesActivity;
import com.video.corpus.pojos.carousel_model;
import com.video.corpus.pojos.homecontent_model;
import com.video.corpus.R;
import com.video.corpus.global.CorpusAdViewFlipper;
import com.video.corpus.network.NetworkRequest;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.adapters.custompageradapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 12/19/2017.
// */

public class HomeFragment extends BaseFragment {
    private Context context;
    private View view;
    private Response_string<String> getresponse, homecontent_response;
    private CorpusAdViewFlipper viewFlipper;
    private TabLayout adsIndicatorLayout;
    private ProgressBar progressbar;
    private RelativeLayout relCarousel, relException;
    private LinearLayout linContent;
    private ArrayList<String> params;
    private  LayoutInflater layoutInflater;
    private commonclass cc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        cc=new commonclass(context);
        cc.setIshomecontent(true);
         layoutInflater=inflater;
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews();

        return view;
    }

    //initiating views
    private void initViews() {

        toolbartext(context, getResources().getString(R.string.home_title));
        viewFlipper = view.findViewById(R.id.viewFlipper);
        adsIndicatorLayout = view.findViewById(R.id.view_indicator);
        progressbar = view.findViewById(R.id.progressbar);
        relCarousel = view.findViewById(R.id.Rel_carousel);
        relException= view.findViewById(R.id.Rel_exception);
        linContent = view.findViewById(R.id.Lin_content);
        readresponse();
        setparams();

        //to get carousel ads
        if(checkpremium())
        {
            String url=APP_SERVER_COMB+cc.getSessioId()+carousel_ads_url_prm;
            showlogs("carousel_url",url);
            new NetworkRequest(context, url, params, getresponse).execute();
        }
        else
        {
            showlogs("carousel_url",carousel_ads_url_free);
            new NetworkRequest(context, carousel_ads_url_free, params, getresponse).execute();
        }


    }

    private void readresponse() {
        getresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("carouselads_response", res);
                if (!TextUtils.isEmpty(res)) {
                    PopulateCarouselAds(res);
                }
                else
                {
                    home_content_request();
                }
            }
        };

        homecontent_response = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("homecontent_response", res);
                if (!TextUtils.isEmpty(res)) {
                    Populate_homecontent(res);
                }
                else
                {
                    progressbar.setVisibility(View.GONE);
                    relException.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    // Dummy data to populate Carousel ads
    private void PopulateCarouselAds(String res) {

        ArrayList<carousel_model> carousel_models = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONObject jsonObject_inner = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject_inner.getJSONArray("content");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject_loop = jsonArray.getJSONObject(i);
                carousel_model data = new carousel_model();
                data.setCarousel_url(jsonObject_loop.optString("otherDeviceCarouselImage", ""));
                carousel_models.add(data);
            }

            if (carousel_models.size() > 0) {
                setheight(viewFlipper, getScreenHeight());
                viewFlipper.setAdapter(new custompageradapter(context, carousel_models));
                adsIndicatorLayout.setupWithViewPager(viewFlipper);
            } else {
                viewFlipper.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        home_content_request();
    }


    private void setparams() {
        params = new ArrayList<>();
    }

    // add google ads to layout
    private void Googleads(int adtype, String addunitid) {
        linContent.addView(addADMOB(context, adtype, addunitid));
    }

    //to fetch home page content
    private void Populate_homecontent(String res) {

        try {
            JSONObject jsonObject = new JSONObject(res);
            jsonArray_base = jsonObject.optJSONArray("content");
            JSONArray jsonArray = jsonObject.optJSONArray("content");
            if (jsonArray.length() > 0) {
                showlogs("jsonArray", "" + jsonArray);
                showlogs("default", "" + "ad");
                displayContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //id passing to google ads
    private void getappunitId(JSONObject jsonObject, int addtype) {
        try {
            if (!TextUtils.isEmpty(jsonObject.toString())) {
                JSONArray jsonArray_content = jsonObject.optJSONArray("content");
                if (jsonArray_content.length() > 0) {
                    for (int j = 0; j < jsonArray_content.length(); j++) {
                        JSONObject jsonObject_loop = jsonArray_content.getJSONObject(j);
                        Googleads(addtype, jsonObject_loop.optString("leaderBoardValue", ""));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //display content
    private void displayContent() {
        if (jsonArray_base.length() > 0) {

            try {

                for (int i = 0; i < jsonArray_base.length(); i++) {
                    JSONObject jsonObject_inner = jsonArray_base.optJSONObject(i);
                    if(isnotempty(jsonObject_inner.toString()))
                    {
                        if (jsonObject_inner.optString("command").equals(adsconstant_LEADER)) {
                            getappunitId(jsonObject_inner, LEADERBOARD);
                        } else if (jsonObject_inner.optString("command").equals(adsconstant_BIGBOX)) {
                            showlogs("add bigbox", "" + i);
                            getappunitId(jsonObject_inner, BIGBOX);
                        }
                        else
                        {
                                try {
                                    content(jsonObject_inner);
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        progressbar.setVisibility(View.GONE);
        relCarousel.setVisibility(View.VISIBLE);
        linContent.setVisibility(View.VISIBLE);
    }

//    private void dynamic_constraints()
//    {
//        ConstraintSet constraintSet=new ConstraintSet();
//        constraintSet.connect(relCarousel.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT);
//        constraintSet.connect(relCarousel.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
//        constraintSet.connect(relCarousel.getId(),ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
//        constraintSet.connect(relCarousel.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT);
//        constraintSet.connect(linContent.getId(),ConstraintSet.BOTTOM,relCarousel.getId(),ConstraintSet.BOTTOM);
//    }


    //horizontal scroll view for content
    private void content(JSONObject jsonObject)
    {
        try{
            JSONArray jsonArray=jsonObject.optJSONArray("content");
            if(isnotempty(jsonArray.toString()))
            {
               ArrayList<homecontent_model> items_array=new ArrayList<>();

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
                        items_array.add(data);
                    }
                }
                if(isnotempty(items_array.toString()))
                {
                    if(isnotempty(jsonObject.toString()))
                    {
                        if(jsonObject.optString("command","").contains(LIVETV))
                        {
                            addContent_views(items_array,jsonObject.optString("title",""),true);
                        }
                        else if(jsonObject.optString("command","").contains(VOD))
                        {
                            addContent_views(items_array,jsonObject.optString("title",""),false);
                        }
                        else
                        {
                            addContent_views(items_array,jsonObject.optString("title",""),true);
                        }

                    }

                }

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

        //adding content dynamically (live tv,channels , etc..)
    private void addContent_views(final ArrayList<homecontent_model> homecontent_models,
                                  String Title, boolean livetv)
    {
         double aspectratio_local;
        if(livetv)
        {
            aspectratio_local=aspectratio;
        }
        else
        {
            aspectratio_local=aspectratio_movies;
        }

        final LinearLayout linearLayoutDyanmic;
        View view;
        linearLayoutDyanmic=new LinearLayout(context);
        setparams(linearLayoutDyanmic);
        linearLayoutDyanmic.setOrientation(LinearLayout.HORIZONTAL);

      //  addcontent_title_dynamic(Title);
        addcontent_title(Title,homecontent_models,livetv);

        for(int i=0;i<MAXCONTENTCOUNT;i++)
        {
            showlogs("homecontent_models",homecontent_models.get(i).getName());
            view=layoutInflater.inflate(R.layout.content_layout,nullParent);
            view.setTag(homecontent_models.get(i));
            ImageView imageView= view.findViewById(R.id.imgview);
            Picasso.with(context).load(homecontent_models.get(i).getImage()).placeholder(R.mipmap.placeholder_crousel).error(R.mipmap.ic_launcher).into(imageView);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(getScreenWidth()/width_num,(int)((getScreenWidth()/width_num)*(aspectratio_local))));
            TextView textView= view.findViewById(R.id.txtview);
            textView.setText(homecontent_models.get(i).getName());
            textView.setWidth((getScreenWidth()/width_num));
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int)getResources().getDimension(R.dimen.dp_four),0,0,0);
            view.setLayoutParams(layoutParams);
            view.setId(i);
            view.setTag(String.valueOf(livetv));
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
                    sethomecontent(homecontent_models,cc);
                    Intent intent;
                    if (cc.islivetv())
                    {
                         intent=new Intent(context, PlayerLiveTvActivity.class);
                    }
                    else
                    {
                        intent=new Intent(context, PlayerMoviesActivity.class);
                    }

                    startActivity(intent);
                }
            });

        }


            HorizontalScrollView horizontalScrollView=new HorizontalScrollView(context);
            setparams(horizontalScrollView);
            horizontalScrollView.addView(linearLayoutDyanmic);
            horizontalScrollView.setTag(homecontent_models.toString());
            linContent.addView(horizontalScrollView);
    }


    //http home content request
    private void home_content_request()
    {

        //to get home content
        if(checkpremium())
        {
            String url=APP_SERVER_COMB+cc.getSessioId()+home_content_url_prm;
            showlogs("home_content_url",url);
            new NetworkRequest(context, url, params, homecontent_response).execute();
        }
        else
        {
            showlogs("home_content_url",home_content_url_free);
            new NetworkRequest(context, home_content_url_free, params, homecontent_response).execute();
        }

    }

    //add content title static
    private void addcontent_title(String contentTitle, final ArrayList<homecontent_model> homecontent_models, final boolean livetv)
    {
        View view=layoutInflater.inflate(R.layout.title,nullParent);
        TextView txttitle= view.findViewById(R.id.Txt_title);
        final TextView txtMoreOpts= view.findViewById(R.id.Txt_moreOpts);
        txtMoreOpts.setTag(homecontent_models.toString());

        if(isnotempty(contentTitle))
        {
            txttitle.setText(contentTitle);
        }

        if(homecontent_models.size()>MAXCONTENTCOUNT)
        {
            txtMoreOpts.setVisibility(View.VISIBLE);
        }

        txtMoreOpts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isnotempty((txtMoreOpts.getTag().toString())))
                {
                    moreoptions_homecontent(livetv,homecontent_models);
                }
            }
        });

        linContent.addView(view);
    }


    //options more to load home content
    private void moreoptions_homecontent(boolean islivetv,ArrayList<homecontent_model> data)
    {

        sethomecontent(data,cc);
        cc.setIslivetv(islivetv);
        util.fragmenttransaction((AppCompatActivity) activity,new HomeContentFragment(),
                HomeContentFragment.class.getName(),true);
    }



    //add content title dynamic
//    private void addcontent_title_dynamic(String title)
//    {
//        TextView textView_title=new TextView(context);
//        textView_title.setText(title);
//        textView_title.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
//        textView_title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        textView_title.setTextSize((int)getResources().getDimension(R.dimen.sp_ten));
//        textView_title.setPadding((int)getResources().getDimension(R.dimen.dp_ten),0,0,0);
//        linContent.addView(textView_title);
//    }

}
