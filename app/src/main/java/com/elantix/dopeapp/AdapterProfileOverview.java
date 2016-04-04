package com.elantix.dopeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by oleh on 3/31/16.
 */
public class AdapterProfileOverview extends RecyclerView.Adapter<AdapterProfileOverview.ProfViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private int mCnt=0;

    public AdapterProfileOverview(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ProfViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_overview_list_row, parent, false);
        ProfViewHolder holder = new ProfViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProfViewHolder holder, int position) {
        int recourseForImage1;
        int recourseForImage2;
        if (mCnt == 0){
            recourseForImage1 = R.drawable.donald;
            recourseForImage2 = R.drawable.ted;
            mCnt++;
        }else if (mCnt == 1){
            recourseForImage1 = R.drawable.girl3;
            recourseForImage2 = R.drawable.girl4;
            mCnt++;
        }else{
            recourseForImage1 = R.drawable.bernie;
            recourseForImage2 = R.drawable.hillary;
            mCnt = 0;
        }

        Glide.with(context).load(recourseForImage1).thumbnail(0.05f).into(holder.mImage1);
        Glide.with(context).load(recourseForImage2).thumbnail(0.05f).into(holder.mImage2);


    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class ProfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImage1;
        private ImageView mImage2;

        public ProfViewHolder(View itemView) {
            super(itemView);
            mImage1 = (ImageView) itemView.findViewById(R.id.profile_overview_row_image_1);
            mImage2 = (ImageView) itemView.findViewById(R.id.profile_overview_row_image_2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                Log.w("Log: OnCLick", "Occur");
            if (v.getId() == itemView.getId()){
                Log.w("Log: ItemView", "Occur");
                Intent intent = new Intent(((MainActivity)context), DopeStatisticsActivity.class);
                context.startActivity(intent);
            }
        }
    }
}
