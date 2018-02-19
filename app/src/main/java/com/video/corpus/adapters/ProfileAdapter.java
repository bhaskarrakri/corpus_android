package com.video.corpus.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.corpus.R;

import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 2/19/2018.
// */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder>
{
    private ArrayList<String> values;
    private String[] titles;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,txt_value;
        ConstraintLayout relativeLayout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_title);
            txt_value = view.findViewById(R.id.txt_value);
            relativeLayout= view.findViewById(R.id.cons_lay);
        }
    }


    public ProfileAdapter(ArrayList<String> values,String[] titles) {
        this.titles=titles;
        this.values=values;
    }

    @Override
    public ProfileAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item_layout, parent, false);

        return new ProfileAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.MyViewHolder holder, int position) {
        holder.title.setText(titles[position]);

        holder.relativeLayout.setTag(position);
        holder.txt_value.setText(values.get(position));


    }

    @Override
    public int getItemCount() {
        return titles.length;
    }



}