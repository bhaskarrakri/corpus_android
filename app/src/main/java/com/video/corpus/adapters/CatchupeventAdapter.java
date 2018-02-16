package com.video.corpus.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.video.corpus.Interface.ItemclickListener;
import com.video.corpus.R;
import com.video.corpus.global.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

///**
// * Created by Bhaskar.c on 2/16/2018.
// */

public class CatchupeventAdapter extends RecyclerView.Adapter<CatchupeventAdapter.MyViewHolder> {
    private ArrayList<JSONObject> arrayList = new ArrayList<> ();
    private String Title;
    private ItemclickListener itemclickListener;
    private Context context;
    private int curServiceAssetId=0;


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView videoTitle, videoName, videoEpisode;
        private LinearLayout catchUpBackGround;

        private MyViewHolder (View view) {
            super (view);
            videoTitle = view.findViewById (R.id.textview_video_title);
            videoName = view.findViewById (R.id.textview_video_name);
            videoEpisode = view.findViewById (R.id.textview_episode_number);
            catchUpBackGround = view.findViewById (R.id.catchUpBackGround);
        }
    }


    public CatchupeventAdapter (Context context,ArrayList<JSONObject> moviesList, String title, ItemclickListener itemclickListener) {
        this.arrayList = moviesList;
        this.context=context;
        Title=title;
        this.itemclickListener=itemclickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from (parent.getContext ())
                .inflate (R.layout.single_catchup_video_item, parent, false);

        return new MyViewHolder (itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder,  int position) {
        try {
            holder.catchUpBackGround.setId (arrayList.get (position).getInt ("channelprogramid"));

            String title =  Title+ "  " + Utils.getFormattedDDMMYYYY (arrayList.get (position).getLong ("starttime"));

            holder.videoTitle.setText (title, TextView.BufferType.NORMAL);

            holder.videoName.setText (arrayList.get (position).getString ("synopsis"), TextView.BufferType.NORMAL);
            holder.videoName.setMaxLines (3);
            holder.videoName.setEllipsize (TextUtils.TruncateAt.END);
         //   String tag = arrayList.get (position).isNull ("playbackurl") ? "" : arrayList.get (position).getString ("playbackurl2");

            String episodeNo = arrayList.get (position).getString ("episodeNumber");

            if (episodeNo.equals ("")) {
                holder.videoEpisode.setVisibility (View.GONE);
            } else {
                holder.videoEpisode.setVisibility (View.VISIBLE);
                holder.videoEpisode.setText (String.format ("Episode - %s", episodeNo), TextView.BufferType.NORMAL);
            }

            holder.catchUpBackGround.setTag (position);

            holder.catchUpBackGround.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    try {
                    itemclickListener.onitemclcik((int)v.getTag());
                    curServiceAssetId=arrayList.get ((int)v.getTag()).getInt ("channelprogramid");
                     notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace ();
                    }
                }
            });
             try{
            if (curServiceAssetId == arrayList.get (position).getInt ("channelprogramid")) {
                holder.catchUpBackGround.setBackgroundColor (ContextCompat.getColor(context,R.color.toolbar_text));
            } else {
                holder.catchUpBackGround.setBackgroundColor (ContextCompat.getColor(context,R.color.colorPrimary));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
            holder.catchUpBackGround.setBackgroundColor (ContextCompat.getColor(context,R.color.colorPrimary));
        }


        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    @Override
    public int getItemCount () {
        return arrayList.size ();
    }
}