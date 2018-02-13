package com.video.corpus.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.video.corpus.R;
import com.video.corpus.controllers.HomeActivity;
import com.video.corpus.global.Utils;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.AMSRequest;

///**
// * Created by Bhaskar.c on 2/2/2018.
// */

public class SettingsFragment extends BaseFragment {
    private View view;
    private commonclass cc;
    private   Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings, container, false);
         context = getActivity();
        cc=new commonclass(context);
        initViews();
        return view;
    }

    //initiating views
    private void initViews() {
        Button btnLogout = view.findViewById(R.id.btn_logout);

        //logout click
     btnLogout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             new AMSRequest(cc.gettrapurl(), Utils.getInstance().setamsobject(context,AMS_INACTIVE));
             cc.resetsharedpref();
             showtaost(context,getResources().getString(R.string.loggedout));
             Intent intent=new Intent(getActivity(),HomeActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(intent);

         }
     });

    }
    }
