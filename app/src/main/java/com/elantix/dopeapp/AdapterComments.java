package com.elantix.dopeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by oleh on 4/16/16.
 */
public class AdapterComments extends RecyclerView.Adapter<AdapterComments.CommentsVH> {

    private LayoutInflater mInflater;
    private Context mContext;

    private int[] avatars = {R.drawable.fr3, R.drawable.fr4, R.drawable.fr5
            , R.drawable.fr3, R.drawable.fr4, R.drawable.fr5

    };
    private int[] userNameResources = {R.string.single_comment_dummy_user_name_1, R.string.single_comment_dummy_user_name_2, R.string.single_comment_dummy_user_name_3
            , R.string.single_comment_dummy_user_name_1, R.string.single_comment_dummy_user_name_2, R.string.single_comment_dummy_user_name_3

    };
    private int[] commentsTextResources = {R.string.single_comment_dummy_text_1, R.string.single_comment_dummy_text_2, R.string.single_comment_dummy_text_3
            , R.string.single_comment_dummy_text_1, R.string.single_comment_dummy_text_2, R.string.single_comment_dummy_text_3
    };

    private int mSelfAvatar;
    private int mDefaultUsernameColor = R.color.lower_bar_active_color;
    private int mMyUserNameColor = R.color.single_comment_user_name_color;
    private String mMyUserName = "Nikolay Meltskiy";
    private ArrayList<Integer> mMyCommentNumbers = new ArrayList<>();

    public ArrayList<Integer> avatarResources = new ArrayList<>();
    public ArrayList<String> userNames = new ArrayList<>();
    public ArrayList<String> commentsText = new ArrayList<>();
    public ArrayList<String> commentsTime = new ArrayList<>();

    public AdapterComments(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        for (int i=0; i< avatars.length; i++){
            avatarResources.add(avatars[i]);
            userNames.add(context.getResources().getString(userNameResources[i]));
            commentsText.add(context.getResources().getString(commentsTextResources[i]));
            commentsTime.add("2:46pm");
        }
    }

    public void addComment(String message){
        avatarResources.add(R.drawable.maletskiy1);
        userNames.add(mMyUserName);
        commentsText.add(message);
        commentsTime.add("Just now");
        notifyItemInserted(commentsText.size()-1);
        ((CommentsActivity)mContext).mRecyclerView.scrollToPosition(commentsText.size()-1);
    }

    @Override
    public CommentsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_comment, parent, false);
        CommentsVH holder = new CommentsVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentsVH holder, int position) {
        Glide.with(mContext).load(avatarResources.get(position)).into(holder.avatar);
        holder.userName.setText(userNames.get(position));
        holder.commentText.setText(commentsText.get(position));
        holder.time.setText(commentsTime.get(position));
    }

    @Override
    public int getItemCount() {
        return avatarResources.size();
    }

    class CommentsVH extends RecyclerView.ViewHolder{

        private ImageView avatar;
        private TextView userName, commentText, time;

        public CommentsVH(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.single_comment_avatar);
            userName = (TextView) itemView.findViewById(R.id.single_comment_user_name);
            commentText = (TextView) itemView.findViewById(R.id.single_comment_comment_text);
            time = (TextView) itemView.findViewById(R.id.single_comment_time);
        }
    }
}

