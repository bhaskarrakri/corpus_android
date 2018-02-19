package com.video.corpus.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.corpus.R;
import com.video.corpus.pojos.homecontent_model;

import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 2/19/2018.
// */

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.MyViewHolder>
{
    private ArrayList<homecontent_model> models;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,expiry,status;
        ConstraintLayout relativeLayout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_packagename);
            expiry = view.findViewById(R.id.txt_expiry);
            status = view.findViewById(R.id.txt_status);
            relativeLayout= view.findViewById(R.id.cons_lay);
        }
    }


    public PackagesAdapter(Context context,ArrayList<homecontent_model>  models ) {
        this.models=models;
        this.context=context;
    }

    @Override
    public PackagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_item_layout, parent, false);

        return new PackagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PackagesAdapter.MyViewHolder holder, int position) {
        holder.title.setText(models.get(position).getName());
        if(!(TextUtils.isEmpty(models.get(position).getpackageexpiryDate())))
        {
            holder.expiry.setText(context.getResources().getString(R.string.activetill)
                    .concat(" ").concat(models.get(position).getpackageexpiryDate()));
        }

        if(!(TextUtils.isEmpty(models.get(position).getpackagestatus())))
        {

            if(models.get(position).getpackagestatus().toLowerCase()
                    .equals(context.getResources().getString(R.string.active))) {

                holder.status.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
            }
            else if(models.get(position).getpackagestatus().toLowerCase()
                    .equals(context.getResources().getString(R.string.inactive))) {

                holder.status.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            }

            holder.status.setText(context.getResources().getString(R.string.activetill)
                    .concat(" ").concat(models.get(position).getpackagestatus()));

            }
        holder.relativeLayout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }



}