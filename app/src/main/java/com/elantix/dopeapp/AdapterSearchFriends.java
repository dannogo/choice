package com.elantix.dopeapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/29/16.
 */
public class AdapterSearchFriends extends RecyclerView.Adapter<AdapterSearchFriends.MyViewHolder>{

    private LayoutInflater inflater;
    private Context context;

    String[] contactNames = {"Yana Sheleykis", "Nikolay Maletskiy", "Voktoria Colt", "Alisa Parker", "Nataliya Trush", "Maria Tkachik",
            "Yana Sheleykis", "Nikolay Maletskiy", "Voktoria Colt", "Alisa Parker", "Nataliya Trush", "Maria Tkachik",
            "Yana Sheleykis", "Nikolay Maletskiy", "Voktoria Colt", "Alisa Parker", "Nataliya Trush", "Maria Tkachik"
    };

    String[] occupation ={
            "Specialist", "Designer", "M.Style", "dance",
            "Management", "schastie", "free", "busy",
            "Specialist", "Designer", "M.Style", "dance",
            "Management", "schastie", "free", "busy",
            "Specialist", "Designer"
    };

    int[] avatars = {
            R.drawable.fr1, R.drawable.fr2, R.drawable.fr3, R.drawable.fr4,
            R.drawable.fr5, R.drawable.fr6, R.drawable.fr7,
            R.drawable.fr1, R.drawable.fr2, R.drawable.fr3, R.drawable.fr4,
            R.drawable.fr5, R.drawable.fr6, R.drawable.fr7,
            R.drawable.fr1, R.drawable.fr2, R.drawable.fr3, R.drawable.fr4,
    };


    ArrayList<Integer> checkedItems = new ArrayList<>();
    private Utilities.FollowingListType mType;

    public AdapterSearchFriends(Context context, Utilities.FollowingListType type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        mType = type;
        switch (type){
            case ProfileFollowing:
                for (int i=0; i<contactNames.length; i++){
                    checkedItems.add(i);
                }
                break;
            default:

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

        if (checkedItems.contains(Integer.valueOf(position))){
            holder.mFollowButton.setVisibility(View.GONE);
            holder.mFollowButtonChecked.setVisibility(View.VISIBLE);
        }else{
            holder.mFollowButton.setVisibility(View.VISIBLE);
            holder.mFollowButtonChecked.setVisibility(View.GONE);
        }

        holder.description.setText(occupation[position]);
        holder.contactName.setText(contactNames[position]);
        holder.avatar.setImageResource(avatars[position]);

    }

    @Override
    public int getItemCount() {
        return contactNames.length;
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
            if (v.getId() == mFollowButton.getId()){
                checkedItems.add(getAdapterPosition());
                mFollowButton.setVisibility(View.GONE);
                mFollowButtonChecked.setVisibility(View.VISIBLE);
            }else if (v.getId() == mFollowButtonChecked.getId()){
                checkedItems.remove(Integer.valueOf(getAdapterPosition()));
                mFollowButton.setVisibility(View.VISIBLE);
                mFollowButtonChecked.setVisibility(View.GONE);
            }


        }

        @Override
        public boolean onLongClick(View v) {
            ((MainActivity)context).switchPageHandler(MainActivity.Page.FriendsDope);
            return false;
        }
    }

}
