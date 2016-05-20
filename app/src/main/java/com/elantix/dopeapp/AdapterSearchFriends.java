package com.elantix.dopeapp;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/29/16.
 */
public class AdapterSearchFriends extends RecyclerView.Adapter<AdapterSearchFriends.MyViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    public ArrayList<ProfileInfo> mUsers;
    private String[] mFollowingsIds;


    ArrayList<Integer> checkedItems = new ArrayList<>();
    private Utilities.FollowingListType mType;

    public AdapterSearchFriends(Context context, Utilities.FollowingListType type, ProfileInfo[] users){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        mUsers = new ArrayList<>();

        if (users != null) {
            for (int i = 0; i < users.length; i++) {
                mUsers.add(users[i]);
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_friends_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (mUsers.get(position).id.equals(Utilities.sUid)){
            holder.mFollowButton.setVisibility(View.GONE);
            holder.mFollowButtonChecked.setVisibility(View.GONE);
        }else if (mUsers.get(position).follow == 1){
            holder.mFollowButton.setVisibility(View.GONE);
            holder.mFollowButtonChecked.setVisibility(View.VISIBLE);
        }else{
            holder.mFollowButton.setVisibility(View.VISIBLE);
            holder.mFollowButtonChecked.setVisibility(View.GONE);
        }

        holder.description.setText(mUsers.get(position).bio);
        String name = (mUsers.get(position).fullname != null && !mUsers.get(position).fullname.isEmpty()) ? mUsers.get(position).fullname : mUsers.get(position).username;
        holder.contactName.setText(name);
        if (!mUsers.get(position).avatar.isEmpty()) {
            Glide.with(context).load(Uri.parse(mUsers.get(position).avatar))
                    .bitmapTransform(new CropCircleTransformation(context)).into(holder.avatar);
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView contactName;
        private TextView description;
        private ImageView avatar;
        private FancyButton mFollowButton;
        private FancyButton mFollowButtonChecked;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            contactName = (TextView) itemView.findViewById(R.id.list_contact_name);
            description = (TextView) itemView.findViewById(R.id.contact_description);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            mFollowButton = (FancyButton) itemView.findViewById(R.id.search_friends_follow_button);
            mFollowButtonChecked = (FancyButton) itemView.findViewById(R.id.search_friends_follow_button_checked);
            mFollowButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            mFollowButton.setOnClickListener(this);
            mFollowButtonChecked.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            HttpKit http = new HttpKit(context);
            boolean isFollow;
            if (v.getId() == mFollowButton.getId()){
                checkedItems.add(getAdapterPosition());
                mFollowButton.setVisibility(View.GONE);
                mFollowButtonChecked.setVisibility(View.VISIBLE);
                isFollow = true;
            }else if (v.getId() == mFollowButtonChecked.getId()){
                checkedItems.remove(Integer.valueOf(getAdapterPosition()));
                mFollowButton.setVisibility(View.VISIBLE);
                mFollowButtonChecked.setVisibility(View.GONE);
                isFollow = false;
            }else{
                isFollow = false;
            }

            String id = mUsers.get(getAdapterPosition()).id;
            Log.e("AdapterSearchFriends", "id: "+id+"\nisFollow: "+isFollow);
            http.followUnfollow(isFollow, id, Utilities.sToken);

        }

        @Override
        public boolean onLongClick(View v) {
//            ((MainActivity)context).switchPageHandler(MainActivity.Page.FriendsDope);
            return false;
        }
    }

}
