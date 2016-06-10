package com.elantix.dopeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elantix.dopeapp.entities.ConversationInfo;
import com.elantix.dopeapp.entities.ConversationMemberInfo;

import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 6/4/16.
 */
public class AdapterGroupSettings extends RecyclerView.Adapter<AdapterGroupSettings.GroupViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private HttpChat http;
    public ArrayList<ConversationMemberInfo> mMembers;

    public AdapterGroupSettings(Context context, ConversationInfo conversation){
        inflater = LayoutInflater.from(context);
        this.context = context;
        http = new HttpChat(context);

        mMembers = new ArrayList<>(conversation.members);
        setGroupName();
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_friends_row, parent, false);
        GroupViewHolder holder = new GroupViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        if (!mMembers.get(position).avatar.isEmpty() && mMembers.get(position).avatar != null && !mMembers.get(position).avatar.equals("null")) {
            Glide.with(context).load(Uri.parse(mMembers.get(position).avatar))
                    .bitmapTransform(new CropCircleTransformation(context)).into(holder.avatar);
        }
        String fullname = (mMembers.get(position).fullname == null || mMembers.get(position).fullname.isEmpty()) ? mMembers.get(position).username : mMembers.get(position).fullname;
        holder.contactName.setText(fullname);
        holder.description.setText(mMembers.get(position).bio);
    }

    public void setGroupName(){
        String fullnameToshow = mMembers.get(0).fullname;
        if (fullnameToshow.isEmpty() || fullnameToshow == null || fullnameToshow.equals("null")){
            fullnameToshow = mMembers.get(0).username;
        }
        if ( mMembers.size() > 1){
            fullnameToshow += " + "+ (mMembers.size()-1)+" Friend";
        }
        if (mMembers.size() > 2){
            fullnameToshow += "s";
        }
        ((GroupSettingsActivity)context).mGroupName.setText(fullnameToshow);
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView contactName;
        private TextView description;
        private ImageView avatar;

        public GroupViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            contactName = (TextView) itemView.findViewById(R.id.list_contact_name);
            description = (TextView) itemView.findViewById(R.id.contact_description);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            FancyButton FollowButton = (FancyButton) itemView.findViewById(R.id.search_friends_follow_button);
            FancyButton FollowButtonChecked = (FancyButton) itemView.findViewById(R.id.search_friends_follow_button_checked);
            FollowButton.setVisibility(View.GONE);
            FollowButtonChecked.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View v) {
            int id  = v.getId();
            if (id == itemView.getId()){
                // Show dialog with proposal to remove user from conversation
                final GroupSettingsActivity activity = ((GroupSettingsActivity)context);
                Log.e("AdapterGroupSettings", "creator_id: "+activity.mConversation.creator_id);
                Log.e("AdapterGroupSettings", "Utilities.sUid: "+Utilities.sUid);
                if (activity.mConversation.creator_id.equals(Utilities.sUid)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Removing member");
                    builder.setMessage("Are you sure you want to remove this member?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            http.removeMember(Utilities.sToken, activity.mDialogId, mMembers.get(getAdapterPosition()).id, String.valueOf(getAdapterPosition()));
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
            }
        }
    }
}
