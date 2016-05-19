package com.elantix.dopeapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by oleh on 4/16/16.
 */
public class AdapterComments extends RecyclerView.Adapter<AdapterComments.CommentsVH> {

    private LayoutInflater mInflater;
    private Context mContext;
    private String mDopeId;
    private static final int PENDING_REMOVAL_TIMEOUT = 2500;

    public ArrayList<CommentInfo> mCommentsList;
    List<CommentInfo> itemsPendingRemoval;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<CommentInfo, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterComments(Context context, String dopeId, CommentInfo[] comments) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDopeId = dopeId;
        mCommentsList = new ArrayList<>();
        itemsPendingRemoval = new ArrayList<>();

        for (int i=0; i<comments.length; i++){
            mCommentsList.add(comments[i]);
        }
    }

    public void addComment(String message, String commentId, String dateCreate){
        if (Utilities.sMyProfile == null){
            Toast.makeText(mContext, "You are not logged in", Toast.LENGTH_SHORT).show();
        }else {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.fullname = Utilities.sMyProfile.fullname;
            commentInfo.username = Utilities.sMyProfile.username;
//            commentInfo.date_create = new Date().toString();
            commentInfo.comment = message;
            commentInfo.id = commentId;
            commentInfo.date_create = dateCreate;
            commentInfo.avatar = Utilities.sMyProfile.avatar;
            commentInfo.email = Utilities.sMyProfile.email;
            commentInfo.user_id = Utilities.sMyProfile.id;

            mCommentsList.add(0, commentInfo);
            notifyItemInserted(0);
            ((CommentsActivity) mContext).mRecyclerView.scrollToPosition(0);

        }
    }

    @Override
    public CommentsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_comment, parent, false);
        CommentsVH holder = new CommentsVH(view);
        return holder;
    }

    public void undoOnClickAction(){

    }

    @Override
    public void onBindViewHolder(CommentsVH holder, int position) {
        final CommentInfo item = mCommentsList.get(position);

        if (itemsPendingRemoval.contains(item)){
            holder.itemView.setBackgroundColor(Color.RED);
            holder.mainContent.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(item);
                    notifyItemChanged(mCommentsList.indexOf(item));
                }
            });
        }else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.mainContent.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mCommentsList.get(position).avatar).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.avatar);
            holder.userName.setText(mCommentsList.get(position).username);
            holder.commentText.setText(mCommentsList.get(position).comment);
            holder.time.setText(mCommentsList.get(position).date_create);
            holder.undoButton.setVisibility(View.GONE);
            holder.undoButton.setOnClickListener(null);
        }
    }

    public void pendingRemoval(final int position) {
        final CommentInfo item = mCommentsList.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(mCommentsList.indexOf(item));
                    // Http here
                    HttpKit http = new HttpKit(mContext);
                    http.deleteComment(Utilities.sToken, item.id, null, String.valueOf(position));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        CommentInfo item = mCommentsList.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (mCommentsList.contains(item)) {
            mCommentsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        CommentInfo item = mCommentsList.get(position);
        return itemsPendingRemoval.contains(item);
    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    class CommentsVH extends RecyclerView.ViewHolder{

        LinearLayout mainContent;
        Button undoButton;
        private ImageView avatar;
        private TextView userName, commentText, time;

        public CommentsVH(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.single_comment_avatar);
            userName = (TextView) itemView.findViewById(R.id.single_comment_user_name);
            commentText = (TextView) itemView.findViewById(R.id.single_comment_comment_text);
            time = (TextView) itemView.findViewById(R.id.single_comment_time);
            mainContent = (LinearLayout) itemView.findViewById(R.id.single_comment_content);
            undoButton = (Button) itemView.findViewById(R.id.single_comment_undo_button);
        }
    }
}

