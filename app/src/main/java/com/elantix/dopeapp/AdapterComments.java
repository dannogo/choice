package com.elantix.dopeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by oleh on 4/16/16.
 */
public class AdapterComments extends RecyclerView.Adapter<AdapterComments.CommentsVH> {

    private LayoutInflater mInflater;
    private Context mContext;
//    public CommentInfo[] mComments;
    private String mDopeId;

//    private int[] avatars = {R.drawable.fr3, R.drawable.fr4, R.drawable.fr5
//            , R.drawable.fr3, R.drawable.fr4, R.drawable.fr5
//
//    };
//    private int[] userNameResources = {R.string.single_comment_dummy_user_name_1, R.string.single_comment_dummy_user_name_2, R.string.single_comment_dummy_user_name_3
//            , R.string.single_comment_dummy_user_name_1, R.string.single_comment_dummy_user_name_2, R.string.single_comment_dummy_user_name_3
//
//    };
//    private int[] commentsTextResources = {R.string.single_comment_dummy_text_1, R.string.single_comment_dummy_text_2, R.string.single_comment_dummy_text_3
//            , R.string.single_comment_dummy_text_1, R.string.single_comment_dummy_text_2, R.string.single_comment_dummy_text_3
//    };

//    private int mSelfAvatar;
//    private int mDefaultUsernameColor = R.color.lower_bar_active_color;
//    private int mMyUserNameColor = R.color.single_comment_user_name_color;
//    private String mMyUserName = "Nikolay Meltskiy";
//    private ArrayList<Integer> mMyCommentNumbers = new ArrayList<>();

//    public ArrayList<Integer> avatarResources = new ArrayList<>();
//    public ArrayList<String> userNames = new ArrayList<>();
//    public ArrayList<String> commentsText = new ArrayList<>();
//    public ArrayList<String> commentsTime = new ArrayList<>();

    public ArrayList<CommentInfo> mCommentsList = new ArrayList<>();

    public AdapterComments(Context context, String dopedId, CommentInfo[] comments) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDopeId = dopedId;

        for (int i=0; i<comments.length; i++){
            mCommentsList.add(comments[i]);
        }
//        mComments = comments;

//        for (int i=0; i< avatars.length; i++){
//            avatarResources.add(avatars[i]);
//            userNames.add(context.getResources().getString(userNameResources[i]));
//            commentsText.add(context.getResources().getString(commentsTextResources[i]));
//            commentsTime.add("2:46pm");
//        }
    }

    public void addComment(String message){
        if (Utilities.sMyProfile == null){
            Toast.makeText(mContext, "You are not logged in", Toast.LENGTH_SHORT).show();
        }else {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.fullname = Utilities.sMyProfile.fullname;
            commentInfo.username = Utilities.sMyProfile.username;
            commentInfo.date_create = new Date().toString();
            commentInfo.comment = message;
            commentInfo.avatar = Utilities.sMyProfile.avatar;
            commentInfo.email = Utilities.sMyProfile.email;
            commentInfo.user_id = Utilities.sMyProfile.id;


            mCommentsList.add(0, commentInfo);

//        avatarResources.add(R.drawable.maletskiy1);
//        userNames.add(mMyUserName);
//        commentsText.add(message);
//        commentsTime.add("Just now");
//            notifyItemInserted(commentsText.size() - 1);
//            ((CommentsActivity) mContext).mRecyclerView.scrollToPosition(commentsText.size() - 1);

            notifyItemInserted(0);
            ((CommentsActivity) mContext).mRecyclerView.scrollToPosition(0);

//            notifyItemInserted(mCommentsList.size() - 1);
//            ((CommentsActivity) mContext).mRecyclerView.scrollToPosition(mCommentsList.size() - 1);
        }
    }

    @Override
    public CommentsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_comment, parent, false);
        CommentsVH holder = new CommentsVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentsVH holder, int position) {

        Glide.with(mContext).load(mCommentsList.get(position).avatar).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.avatar);
        holder.userName.setText(mCommentsList.get(position).username);
        holder.commentText.setText(mCommentsList.get(position).comment);
        holder.time.setText(mCommentsList.get(position).date_create);


//        Glide.with(mContext).load(mComments[position].avatar).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.avatar);
//        holder.userName.setText(mComments[position].username);
//        holder.commentText.setText(mComments[position].comment);
//        holder.time.setText(mComments[position].date_create);

    }

    @Override
    public int getItemCount() {
//        return mComments.length;
        return mCommentsList.size();
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

