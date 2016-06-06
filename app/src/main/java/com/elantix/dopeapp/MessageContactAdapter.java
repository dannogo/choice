package com.elantix.dopeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elantix.dopeapp.entities.ConversationInfo;

import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by oleh on 3/19/16.
 */
public class MessageContactAdapter extends RecyclerView.Adapter<MessageContactAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    HttpChat http;

    Integer checkedItem = null;
    //    ArrayList<Integer> checkedItems = new ArrayList<>();
    public static ArrayList<Integer> selectedIds = new ArrayList<>();
    ArrayList<Integer> online = new ArrayList<>();
    ArrayList<ProfileInfo> mUsers;
    ArrayList<ConversationInfo> mConvs;
    public FragmentNewMessage.ListType listType;


    public MessageContactAdapter(Context context, ProfileInfo[] users, FragmentNewMessage.ListType type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        selectedIds.clear();

//        online.add(1);
//        online.add(4);
//        online.add(5);
//        online.add(11);
//        online.add(16);
//        online.add(17);

        mUsers = new ArrayList<ProfileInfo>(Arrays.asList(users));
        listType = type;
        http = new HttpChat(this.context);
    }

    public MessageContactAdapter(Context context, ArrayList<ConversationInfo> conversations, FragmentNewMessage.ListType type) {
        Log.e("MessageContactAdapter", "context: " + context);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        online.add(1);
        online.add(4);
        online.add(5);
        online.add(11);
        online.add(16);
        online.add(17);

        mConvs = new ArrayList<>(conversations);
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

        switch (listType) {
            case First:
                if (checkedItem != null && checkedItem == position) {
                    holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border_checked));
                } else {
                    holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border));
                }
                // DEPRECATED
//                if (online.contains(position)){
//                    holder.onlineStatus.setVisibility(View.VISIBLE);
//                }else{
//                    holder.onlineStatus.setVisibility(View.GONE);
//                }

                if (mConvs.get(position).is_read.equals("0")) {
                    holder.onlineStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.onlineStatus.setVisibility(View.GONE);
                }

                holder.description.setText(mConvs.get(position).last_message);
                holder.date.setText(Utilities.convertDate(mConvs.get(position).date_updated));
                holder.date.setVisibility(View.VISIBLE);
                holder.checkmark.setVisibility(View.GONE);

                String avatarFirst;
                int membersCount = mConvs.get(position).members.size();
                if (membersCount > 0) {



                    String fullnameToshow = mConvs.get(position).members.get(0).fullname;
                    if (fullnameToshow.isEmpty() || fullnameToshow == null || fullnameToshow.equals("null")){
                        fullnameToshow = mConvs.get(position).members.get(0).username;
                    }
                    if ( membersCount > 1){
                        fullnameToshow += " + "+ (mConvs.get(position).members.size()-1)+" Friend";
                    }
                    if (membersCount > 2){
                        fullnameToshow += "s";
                    }
                    holder.contactName.setText(fullnameToshow);
                    avatarFirst = mConvs.get(position).members.get(0).avatar;

                } else {
                    holder.contactName.setText(mConvs.get(position).fullname);
                    avatarFirst = mConvs.get(position).avatar;
                }
                if (avatarFirst != null && !avatarFirst.isEmpty() && !avatarFirst.equals("null")) {
                    Glide.with(context).load(Uri.parse(avatarFirst))
                            .bitmapTransform(new CropCircleTransformation(context)).into(holder.avatar);
                }
                break;
            case Second:
                checkedItem = null;
                holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border));
                holder.onlineStatus.setVisibility(View.GONE);
                holder.date.setVisibility(View.GONE);
                holder.checkmark.setVisibility(View.GONE);
                holder.description.setText(mUsers.get(position).bio);
//                holder.contactName.setText(mUsers.get(position).fullname);

                String fullname = mUsers.get(position).fullname;
                if (fullname != null && !fullname.isEmpty()) {
                    holder.contactName.setText(fullname);
                } else {
                    holder.contactName.setText(mUsers.get(position).username);
                }

                String avatar = mUsers.get(position).avatar;
                if (avatar != null && !avatar.isEmpty() && !avatar.equals("null")) {
                    Glide.with(context).load(Uri.parse(avatar))
                            .bitmapTransform(new CropCircleTransformation(context)).into(holder.avatar);
                }

                break;
            case Third:
                holder.onlineStatus.setVisibility(View.GONE);
                holder.date.setVisibility(View.GONE);

                if (selectedIds.contains(Integer.parseInt(mUsers.get(position).id))) {
                    holder.checkmark.setImageResource(R.drawable.list_checkmark_full);
                } else {
                    holder.checkmark.setImageResource(R.drawable.list_checkmark_empty);
                }


                holder.checkmark.setVisibility(View.VISIBLE);
                holder.description.setText(mUsers.get(position).bio);
//                holder.contactName.setText(mUsers.get(position).fullname);
                String fullnameThird = mUsers.get(position).fullname;
                if (fullnameThird != null && !fullnameThird.isEmpty()) {
                    holder.contactName.setText(fullnameThird);
                } else {
                    holder.contactName.setText(mUsers.get(position).username);
                }

                String avatarThird = mUsers.get(position).avatar;
                if (avatarThird != null && !avatarThird.isEmpty() && !avatarThird.equals("null")) {
                    Glide.with(context).load(Uri.parse(mUsers.get(position).avatar))
                            .bitmapTransform(new CropCircleTransformation(context)).into(holder.avatar);
                }
                break;
        }


    }

    @Override
    public int getItemCount() {
        if (listType == FragmentNewMessage.ListType.First) {
            return mConvs.size();
        } else {
            return mUsers.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView onlineStatus;
        TextView contactName;
        TextView description;
        ImageView avatar;
        TextView date;
        ImageView checkmark;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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

//                        http.getDialogHistory(Utilities.sToken, mConvs.get(getAdapterPosition()).dialogs_id, null, null);
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("dialog_id", mConvs.get(getAdapterPosition()).dialogs_id);


                        context.startActivity(intent);
                        ((MessageActivity)context).finish();

                    } else {
                        v.setBackground(ContextCompat.getDrawable(context, R.drawable.bottom_border));
                        checkedItem = null;
                    }

                    break;
                case Second:
                    Log.e("MessageContactAdapter", "Selected user`s id: " + mUsers.get(getAdapterPosition()).id);
                    http.createConversation(Utilities.sToken, mUsers.get(getAdapterPosition()).id);
                    break;
                case Third:

                    if (!selectedIds.contains(Integer.parseInt(mUsers.get(getAdapterPosition()).id))) {
                        selectedIds.add(Integer.parseInt(mUsers.get(getAdapterPosition()).id));
                        checkmark.setImageResource(R.drawable.list_checkmark_full);
                    } else {
                        selectedIds.remove(new Integer(Integer.parseInt(mUsers.get(getAdapterPosition()).id)));
                        checkmark.setImageResource(R.drawable.list_checkmark_empty);
                    }

                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listType == FragmentNewMessage.ListType.First) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String title;
                String message;
                String negativeButtonText;
                Log.e("ContactAdapter", "user_id: "+mConvs.get(getAdapterPosition()).creator_id+"\nmy id: "+Utilities.sUid);
                if (mConvs.get(getAdapterPosition()).creator_id.equals(Utilities.sUid)){
                    title = "Deleting/Leaving Conversation";
                    message = "Are you sure you want to delete/leave this conversation?";
                    negativeButtonText = "Cancel";

                    builder.setNeutralButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            http.leaveConversation(Utilities.sToken, mConvs.get(getAdapterPosition()).dialogs_id, String.valueOf(getAdapterPosition()));
                        }
                    });
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            http.deleteConversation(Utilities.sToken, mConvs.get(getAdapterPosition()).dialogs_id, String.valueOf(getAdapterPosition()));
                        }
                    });
                }else{
                    title = "Leaving Conversation";
                    message = "Are you sure you want to leave this conversation?";
                    negativeButtonText = "No";
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // LeaveConversation
                            http.leaveConversation(Utilities.sToken, mConvs.get(getAdapterPosition()).dialogs_id, String.valueOf(getAdapterPosition()));
//                            Toast.makeText(context, "Leave, brahh", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
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
