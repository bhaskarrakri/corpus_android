package com.video.corpus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.video.corpus.Interface.ItemclickListener;
import com.video.corpus.R;
import com.video.corpus.global.Utils;
import com.video.corpus.global.commonclass;
import com.video.corpus.pojos.homecontent_model;
import java.util.ArrayList;

import static com.video.corpus.Interface.Constants.aspectratio_movies;

///**
// * Created by Bhaskar.c on 1/2/2018.
// */

public class gridadapter extends RecyclerView.Adapter<gridadapter.MyViewHolder>
{

    private Context context;
    private  int margin;
    private ArrayList<homecontent_model> models;
    private boolean islivetv=false;
    private ItemclickListener itemclickListener;



     class MyViewHolder extends RecyclerView.ViewHolder {
         TextView title;
        ImageView  imageView;
         RelativeLayout relativeLayout;

         MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txtview);
            imageView = view.findViewById(R.id.imgview);
             relativeLayout= view.findViewById(R.id.RelLay);
        }
    }


    public gridadapter(Context c, ArrayList<homecontent_model> data, ItemclickListener clickListener) {
        context = c;
        commonclass cc = new commonclass(c);
        margin= (int)context.getResources().getDimension(R.dimen.dp_four);
        islivetv= cc.islivetv();
        models = data;
        itemclickListener=clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridcontent, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        homecontent_model data = models.get(position);
        holder.title.setText(data.getName());
        Picasso.with(context).load(models.get(position).getImage())
                .error(R.mipmap.ic_error_image).placeholder(R.mipmap.placeholder_crousel).into(holder.imageView);

        RelativeLayout.LayoutParams layoutParams;
        if(islivetv)
        {
            layoutParams=new RelativeLayout.LayoutParams((Utils.getInstance().getScreenWidth()/2)-margin,
                    (Utils.getInstance().getScreenWidth()/2)-margin);

        }
        else
        {
            layoutParams=new RelativeLayout.LayoutParams((Utils.getInstance().getScreenWidth()/2)-margin,
                    (int)(((Utils.getInstance().getScreenWidth()/2)*aspectratio_movies))-margin);
        }


        layoutParams.setMargins(margin,margin,0,0);
        holder.relativeLayout.setLayoutParams(layoutParams);
        holder.relativeLayout.setTag(position);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cc.setContentClickpos((int)v.getTag());
//                Gson gson = new Gson();
//                cc.setHomeContent(gson.toJson(models));
//                Intent intent=new Intent(context, PlayerActivity.class);
//                context.startActivity(intent);

                itemclickListener.onitemclcik((int)v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}