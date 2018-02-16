package com.video.corpus.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.video.corpus.pojos.carousel_model;
import com.video.corpus.R;

import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 12/12/2017.
// */

public class custompageradapter  extends PagerAdapter {

    private final Context mContext;
    private final ArrayList<carousel_model> carousel_models;

    public custompageradapter(Context context, ArrayList<carousel_model> carousel_model) {
        mContext = context;
        carousel_models=carousel_model;
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.carousel_layout, collection, false);
        ImageView imageView= layout.findViewById(R.id.carousel_image);
        Picasso.with(mContext).load(carousel_models.get(position).getCarousel_url()).error(R.mipmap.ic_error_image).placeholder(R.mipmap.placeholder_crousel).into(imageView);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return carousel_models.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



}
