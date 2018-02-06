package com.video.corpus.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.video.corpus.Interface.ItemclickListener;
import com.video.corpus.R;
import com.video.corpus.pojos.homecontent_model;
import java.util.ArrayList;
///**
// * Created by Bhaskar.c on 1/31/2018.
// */

public class catchupadapter extends RecyclerView.Adapter<catchupadapter.MyViewHolder>
{
    private Context context;
    private ArrayList<homecontent_model> models;
    private ItemclickListener itemclickListener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView  imageView;
        ConstraintLayout relativeLayout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txtview);
            imageView = view.findViewById(R.id.imgview);
            relativeLayout= view.findViewById(R.id.cons_lay);
        }
    }


    public catchupadapter(Context c, ArrayList<homecontent_model> data, ItemclickListener clickListener) {
        context = c;
        models = data;
        itemclickListener=clickListener;
    }

    @Override
    public catchupadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catchup_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(catchupadapter.MyViewHolder holder, int position) {
        homecontent_model data = models.get(position);
        holder.title.setText(data.getName());
        Picasso.with(context).load(models.get(position).getImage())
                .error(R.mipmap.ic_launcher).placeholder(R.mipmap.placeholder_crousel).into(holder.imageView);
        Log.e("url_catchup",models.get(position).getImage());
        holder.relativeLayout.setTag(position);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemclickListener.onitemclcik((int)v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}