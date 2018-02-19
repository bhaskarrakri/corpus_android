package com.video.corpus.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.video.corpus.Interface.SettingClickListener;
import com.video.corpus.R;

///**
// * Created by Bhaskar.c on 2/19/2018.
// */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder>
{
    private String[] models;
    private int[] Icons;
    private SettingClickListener itemclickListener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        ConstraintLayout relativeLayout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_title);
            imageView = view.findViewById(R.id.img_icon);
            relativeLayout= view.findViewById(R.id.cons_lay);
        }
    }


    public SettingsAdapter(String [] models, int[] icons, SettingClickListener itemclickListener) {
        this.models=models;
        Icons=icons;
        this.itemclickListener=itemclickListener;
    }

    @Override
    public SettingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SettingsAdapter.MyViewHolder holder, int position) {
        holder.title.setText(models[position]);


        holder.relativeLayout.setTag(position);
        holder.imageView.setImageResource(Icons[position]);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    itemclickListener.itemclick(models[(int)v.getTag()],(int)v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.length;
    }



}