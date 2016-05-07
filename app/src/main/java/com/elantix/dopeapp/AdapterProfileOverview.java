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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by oleh on 3/31/16.
 */
public class AdapterProfileOverview extends RecyclerView.Adapter<AdapterProfileOverview.ProfViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private DopeInfo[] mDopeInfo;
    private boolean mIsOwn;

    public AdapterProfileOverview(Context context, DopeInfo[] dopes, boolean isOwn) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        mDopeInfo = dopes;
        mIsOwn = isOwn;
    }

    @Override
    public ProfViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_overview_list_row, parent, false);
        ProfViewHolder holder = new ProfViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProfViewHolder holder, int position) {

        if (mIsOwn){
            holder.mPercentagePanel.setVisibility(View.VISIBLE);
            holder.mQuestion.setVisibility(View.GONE);
        }else{
            holder.mPercentagePanel.setVisibility(View.GONE);
            holder.mQuestion.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(mDopeInfo[position].photo1).thumbnail(0.05f).into(holder.mImage1);
        Glide.with(context).load(mDopeInfo[position].photo2).thumbnail(0.05f).into(holder.mImage2);

        holder.mPercentLeft.setText(mDopeInfo[position].percent1+"%");
        int percent2 = (mDopeInfo[position].percent1 != 0) ? 100 - mDopeInfo[position].percent1 : mDopeInfo[position].percent2;
        holder.mPercentRight.setText(percent2+"%");

    }

    @Override
    public int getItemCount() {
        return mDopeInfo.length;
    }

    class ProfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImage1;
        private ImageView mImage2;
        private TextView mQuestion;
        private LinearLayout mPercentagePanel;
        private TextView mPercentLeft;
        private TextView mPercentRight;

        public ProfViewHolder(View itemView) {
            super(itemView);
            mImage1 = (ImageView) itemView.findViewById(R.id.profile_overview_row_image_1);
            mImage2 = (ImageView) itemView.findViewById(R.id.profile_overview_row_image_2);
            mQuestion = (TextView) itemView.findViewById(R.id.profile_overview_list_row_question);
            mPercentagePanel = (LinearLayout) itemView.findViewById(R.id.profile_overview_list_row_percentage_panel);
            mPercentLeft = (TextView) itemView.findViewById(R.id.profile_overview_row_percent_left);
            mPercentRight = (TextView) itemView.findViewById(R.id.profile_overview_row_percent_right);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == itemView.getId()) {
                Intent intent = new Intent(context, DopeStatisticsActivity.class);
                intent.putExtra("id", mDopeInfo[getAdapterPosition()].id);
                intent.putExtra("uid", mDopeInfo[getAdapterPosition()].userId);
                intent.putExtra("question", mDopeInfo[getAdapterPosition()].question);
                intent.putExtra("votes", mDopeInfo[getAdapterPosition()].votesAll);
                intent.putExtra("percent1", mDopeInfo[getAdapterPosition()].percent1);
                intent.putExtra("percent2", mDopeInfo[getAdapterPosition()].percent2);
                intent.putExtra("comments", mDopeInfo[getAdapterPosition()].comments);
                intent.putExtra("photo1", mDopeInfo[getAdapterPosition()].photo1.toString());
                intent.putExtra("photo2", mDopeInfo[getAdapterPosition()].photo2.toString());
//
                context.startActivity(intent);
            }
        }
    }
}
