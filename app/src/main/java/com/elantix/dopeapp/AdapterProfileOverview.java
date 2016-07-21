package com.elantix.dopeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jirbo.adcolony.AdColonyVideoAd;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by oleh on 3/31/16.
 */
public class AdapterProfileOverview extends RecyclerView.Adapter<AdapterProfileOverview.ProfViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    public ArrayList<DopeInfo> mDopes;
    private boolean mIsOwn;
    private static int dopeCounterForAd = 1;

    public AdapterProfileOverview(Context context, DopeInfo[] dopes, boolean isOwn) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        mDopes = new ArrayList<>(Arrays.asList(dopes));
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

        if (mDopes.get(position).top10.equals("1") || mDopes.get(position).top100.equals("1")){
            holder.mOverlay.setVisibility(View.VISIBLE);
        }else{
            holder.mOverlay.setVisibility(View.GONE);
        }

        Glide.with(context).load(mDopes.get(position).photo1).thumbnail(0.05f).into(holder.mImage1);
        Glide.with(context).load(mDopes.get(position).photo2).thumbnail(0.05f).into(holder.mImage2);

        holder.mPercentLeft.setText(mDopes.get(position).percent1+"%");
        int percent2 = (mDopes.get(position).percent1 != 0) ? 100 - mDopes.get(position).percent1 : mDopes.get(position).percent2;
        holder.mPercentRight.setText(percent2+"%");
        holder.mQuestion.setText(mDopes.get(position).question);
    }

    @Override
    public int getItemCount() {
        return mDopes.size();
    }

    class ProfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private ImageView mImage1;
        private ImageView mImage2;
        private TextView mQuestion;
        private LinearLayout mPercentagePanel;
        private TextView mPercentLeft;
        private TextView mPercentRight;
        private RelativeLayout mOverlay;

        public ProfViewHolder(View itemView) {
            super(itemView);
            mImage1 = (ImageView) itemView.findViewById(R.id.profile_overview_row_image_1);
            mImage2 = (ImageView) itemView.findViewById(R.id.profile_overview_row_image_2);
            mQuestion = (TextView) itemView.findViewById(R.id.profile_overview_list_row_question);
            mPercentagePanel = (LinearLayout) itemView.findViewById(R.id.profile_overview_list_row_percentage_panel);
            mPercentLeft = (TextView) itemView.findViewById(R.id.profile_overview_row_percent_left);
            mPercentRight = (TextView) itemView.findViewById(R.id.profile_overview_row_percent_right);
            mOverlay = (RelativeLayout) itemView.findViewById(R.id.profile_overview_featured_overlay);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == itemView.getId()) {
                dopeCounterForAd++;
                Log.e("AdapterProfileOverview", "dopeCounterForAd: "+dopeCounterForAd);
                if (dopeCounterForAd >= 9){
                    AdColonyVideoAd ad = new AdColonyVideoAd(Utilities.ADCOLONY_ZONE_ID).withListener((MainActivity)context);
                    ad.show();
                    dopeCounterForAd = 1;
                }
                Intent intent = new Intent(context, DopeStatisticsActivity.class);

                int position = getAdapterPosition();
                intent.putExtra("id", mDopes.get(position).id);
                intent.putExtra("uid", mDopes.get(position).userId);
                intent.putExtra("question", mDopes.get(position).question);
                intent.putExtra("votes", mDopes.get(position).votesAll);
                intent.putExtra("percent1", mDopes.get(position).percent1);
                intent.putExtra("percent2", mDopes.get(position).percent2);
                intent.putExtra("comments", mDopes.get(position).comments);
                intent.putExtra("photo1", mDopes.get(position).photo1.toString());
                intent.putExtra("photo2", mDopes.get(position).photo2.toString());
//                intent.putExtra("photoSoc", mDopes.get(position).photoSoc.toString());
                intent.putExtra("percent1", mDopes.get(position).percent1);
                intent.putExtra("myVote", mDopes.get(position).myVote);
                intent.putExtra("userId", mDopes.get(position).userId.toString());

                context.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (Utilities.sCurProfile.id.equals(Utilities.sUid)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Deleting Dope");
                builder.setMessage("Are you sure you want to delete this dope?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = getAdapterPosition();
                        HttpKit http = new HttpKit(context);
                        http.deleteDope(Utilities.sToken, mDopes.get(position).id, position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            return true;
        }
    }
}
