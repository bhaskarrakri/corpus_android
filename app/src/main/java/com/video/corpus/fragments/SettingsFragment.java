package com.video.corpus.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.video.corpus.Interface.SettingClickListener;
import com.video.corpus.R;
import com.video.corpus.adapters.SettingsAdapter;
import com.video.corpus.controllers.HomeActivity;
import com.video.corpus.global.Utils;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.AMSRequest;

///**
// * Created by Bhaskar.c on 2/2/2018.
// */

public class SettingsFragment extends BaseFragment implements SettingClickListener {
    private View view;
    private commonclass cc;
    private Context context;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings, container, false);
        context = getActivity();
        cc = new commonclass(context);
        initViews();
        return view;
    }

    //initiating views
    private void initViews() {
        recyclerView = view.findViewById(R.id.recylcerview);
        progressBar= view.findViewById(R.id.progressbar);

        recyclerviewadapter();
    }


    //set adapter to recyclerview
    private void recyclerviewadapter()
    {
        int[] icons = new int[context.getResources().getStringArray(R.array.settings_array).length];
        icons[0]= R.mipmap.ic_reminders;
        icons[1]= R.mipmap.ic_recentlywatched;
        icons[2]= R.mipmap.ic_favourites;
        icons[3]= R.mipmap.ic_tv_guide;
        icons[4]= R.mipmap.ic_tv_remote;
        icons[5]= R.mipmap.ic_logout;
         String[] models=context.getResources().getStringArray(R.array.settings_array);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager
                (context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new SettingsAdapter(models, icons,this));

        dataavialble();
    }


    //display data
    private void dataavialble()
    {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void itemclick(String tag, int pos) {
        if(tag.equals(getResources().getString(R.string.Logout)))
        {
            new AMSRequest(cc.gettrapurl(), Utils.getInstance().setamsobject(context, AMS_INACTIVE));
            cc.resetsharedpref();
            showtaost(context, getResources().getString(R.string.loggedout));
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
