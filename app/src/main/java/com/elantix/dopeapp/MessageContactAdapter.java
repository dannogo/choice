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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by oleh on 3/19/16.
 */
public class MessageContactAdapter extends RecyclerView.Adapter<MessageContactAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    HttpChat http;

    String[] dates = {
            "13:23", "14:22", "Yesterday", "00:12", "11:23",
            "13:23", "14:22", "Yesterday", "00:12", "11:23",
            "13:23", "14:22", "Yesterday", "00:12", "11:23",
            "13:23", "14:22", "Yesterday"

    };

    Integer checkedItem = null;
//    ArrayList<Integer> checkedItems = new ArrayList<>();
    public static ArrayList<Integer> selectedIds = new ArrayList<>();
    ArrayList<Integer> online = new ArrayList<>();
    ArrayList<ProfileInfo> mUsers;
    public FragmentNewMessage.ListType listType;


    public MessageContactAdapter(Context context, ProfileInfo[] users, FragmentNewMessage.ListType type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        online.add(1);
        online.add(4);
        online.add(5);
        online.add(11);
        online.add(16);
        online.add(17);

        mUsers = new ArrayList<ProfileInfo>(Arrays.asList(users));
        listType = type;
        http = new HttpChat(this.context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_contacts_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        switch (listType){
            case First:
                if (checkedItem != null && checkedItem == position){
                    holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border_checked));
                }else{
                    holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border));
                }
                if (online.contains(position)){
                    holder.onlineStatus.setVisibility(View.VISIBLE);
                }else{
                    holder.onlineStatus.setVisibility(View.GONE);
                }

                holder.description.setText(mUsers.get(position).bio);

//                holder.description.setText(descriptions[position])
                holder.date.setText(dates[position]);
                holder.date.setVisibility(View.VISIBLE);
                holder.checkmark.setVisibility(View.GONE);
                break;
            case Second:
                checkedItem = null;
                holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border));
                holder.onlineStatus.setVisibility(View.GONE);
                holder.date.setVisibility(View.GONE);
                holder.checkmark.setVisibility(View.GONE);
                holder.description.setText(mUsers.get(position).bio);
                break;
            case Third:
                holder.onlineStatus.setVisibility(View.GONE);
                holder.date.setVisibility(View.GONE);

                if (selectedIds.contains(Integer.parseInt(mUsers.get(position).id))){
                    holder.checkmark.setImageResource(R.drawable.list_checkmark_full);
                }else{
                    holder.checkmark.setImageResource(R.drawable.list_checkmark_empty);
                }

                // Deprecated approach
//                if (checkedItems.contains(position)){
//                    holder.checkmark.setImageResource(R.drawable.list_checkmark_full);
//                }else{
//                    holder.checkmark.setImageResource(R.drawable.list_checkmark_empty);
//                }

                holder.checkmark.setVisibility(View.VISIBLE);
                holder.description.setText(mUsers.get(position).bio);
                break;
        }

        holder.contactName.setText(mUsers.get(position).fullname);
        Glide.with(context).load(Uri.parse(mUsers.get(position).avatar))
                .bitmapTransform(new CropCircleTransformation(context)).into(holder.avatar);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView onlineStatus;
        TextView contactName;
        TextView description;
        ImageView avatar;
        TextView date;
        ImageView checkmark;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            onlineStatus = (ImageView) itemView.findViewById(R.id.list_online_status);
            contactName = (TextView) itemView.findViewById(R.id.list_contact_name);
            description = (TextView) itemView.findViewById(R.id.contact_description);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            date = (TextView) itemView.findViewById(R.id.date);
            checkmark = (ImageView) itemView.findViewById(R.id.checkmark);
        }

        @Override
        public void onClick(View v) {
            switch (listType) {
                case First:
                    if (checkedItem == null || !(getAdapterPosition() == checkedItem)) {
                        Integer previousChecked = checkedItem;
                        v.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border_checked));
                        checkedItem = getAdapterPosition();
                        if (previousChecked != null) {
                            notifyItemChanged(previousChecked);
                        }
                        checkedItem = getAdapterPosition();
                    } else {
                        v.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border));
                        checkedItem = null;
                    }
                    break;
                case Second:
                    Log.e("MessageContactAdapter","Selected user`s id: "+mUsers.get(getAdapterPosition()).id);
                    http.createConversation(Utilities.sToken, mUsers.get(getAdapterPosition()).id);
                    break;
                case Third:

                    if (!selectedIds.contains(Integer.parseInt(mUsers.get(getAdapterPosition()).id))){
                        selectedIds.add(Integer.parseInt(mUsers.get(getAdapterPosition()).id));
                        checkmark.setImageResource(R.drawable.list_checkmark_full);
                    }else{
                        selectedIds.remove(new Integer(Integer.parseInt(mUsers.get(getAdapterPosition()).id)));
                        checkmark.setImageResource(R.drawable.list_checkmark_empty);
                    }

                    // Deprecated approach
//                    if (!checkedItems.contains(getAdapterPosition())){
//                        checkedItems.add(getAdapterPosition());
//                        checkmark.setImageResource(R.drawable.list_checkmark_full);
//                    }else{
//                        checkedItems.remove(Integer.valueOf(getAdapterPosition()));
//                        checkmark.setImageResource(R.drawable.list_checkmark_empty);
//                    }
                    break;

            }
        }
    }

}
