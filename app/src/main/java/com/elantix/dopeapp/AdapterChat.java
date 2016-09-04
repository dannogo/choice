package com.elantix.dopeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elantix.dopeapp.entities.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by oleh on 4/16/16.
 */
public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mInflater;
    private Context mContext;

    public ArrayList<ChatMessage> mMessages;
    public List<Object> items;
    private final int DATELINE = 0, OTHERS_MSG = 1, MY_MSG = 2, OTHERS_PROPOSAL = 3, MY_PROPOSAL = 4;
    private HttpChat http;
    private ChoiceAnimationHelper mAnimHelper = null;
    private int mProposalNum = 1;

    // TODO:
    // Do not forget about dateline

    private String replaceFullnameIfNecessary(ChatMessage messageObj){
        String result = (messageObj.fullname.isEmpty() || messageObj.fullname.equals("null") || messageObj.fullname == null) ? messageObj.username : messageObj.fullname;
        return result;
    }

    public ChatMessage sortMessage(ChatMessage item){
        Log.e("AdapterChat", "sortMessage");

        item.fullname = replaceFullnameIfNecessary(item);
        item.date_send = Utilities.convertDate(item.date_send, true);
        item.message = item.message.replaceAll("(\\r|\\n)", "");

        if (item.sender.equals(Utilities.sUid)){
            if (item.photo1.isEmpty()){
                item.viewType = MY_MSG;
            }else{
                item.proposalNum = mProposalNum++;
                item.viewType = MY_PROPOSAL;
            }
        }else{
            if (item.photo1.isEmpty()){
                item.viewType = OTHERS_MSG;
            }else{
                item.proposalNum = mProposalNum++;
                item.viewType = OTHERS_PROPOSAL;
            }
        }

        return item;
    }

    public AdapterChat(Context context, ArrayList<ChatMessage> messages) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        mMessages = new ArrayList<>(messages);
        items = new ArrayList<>();
        http = new HttpChat(mContext);

        for (int i=0; i<mMessages.size(); i++){
            ChatMessage item = mMessages.get(i);
            mMessages.set(i, sortMessage(item));
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case OTHERS_MSG:
                View om = inflater.inflate(R.layout.chat_others_message, parent, false);
                holder = new OthersMsgVH(om);
                break;
            case MY_MSG:
                View mm = inflater.inflate(R.layout.chat_my_message, parent, false);
                holder = new MyMsgVH(mm);
                break;
            case DATELINE:
                View dl = inflater.inflate(R.layout.chat_date_divider, parent, false);
                holder = new DateLineVH(dl);
                break;
            case OTHERS_PROPOSAL:
                View op = inflater.inflate(R.layout.chat_others_dope_proposal, parent, false);
                holder = new OthersDopeProposalVH(op);
                break;
            case MY_PROPOSAL:
                View mp = inflater.inflate(R.layout.chat_my_dope_proposal, parent, false);
                holder = new MyDopeProposalVH(mp);
                break;
            default:
                View def = inflater.inflate(R.layout.chat_my_message, parent, false);
                holder = new MyMsgVH(def);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage item = mMessages.get(position);
        switch (holder.getItemViewType()){
            case OTHERS_MSG:
                OthersMsgVH omvh = (OthersMsgVH) holder;
                Glide.with(mContext).load(item.avatar)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(omvh.avatarView);
                omvh.usernameView.setText(item.fullname);
                omvh.messageView.setText(item.message);
                omvh.timeView.setText(item.date_send);
                break;
            case MY_MSG:
                MyMsgVH mmvh = (MyMsgVH) holder;
                mmvh.messageView.setText(item.message);
                mmvh.timeView.setText(item.date_send);
                break;
            case DATELINE:
                DateLineVH dlvh = (DateLineVH) holder;
                DateLine dl = (DateLine) items.get(position);
                dlvh.dateView.setText(dl.date);
                break;
            case OTHERS_PROPOSAL:
                final OthersDopeProposalVH odpvh = (OthersDopeProposalVH) holder;
                Glide.with(mContext).load(item.avatar)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(odpvh.avatarView);
                odpvh.usernameView.setText(item.fullname);
                odpvh.timeView.setText(item.date_send);
                odpvh.questionView.setText(item.message);

                odpvh.leftPicView.setImageDrawable(null);
                odpvh.rightPicView.setImageDrawable(null);
                Object[] paramsOth = {item.photo1, item.photo2, odpvh.leftPicView, odpvh.rightPicView};
                ImageTransformationComputations taskOth = new ImageTransformationComputations();
                taskOth.execute(paramsOth);

                if (item.myVote > 0) {
//                    launchRateAnimation(odpvh.itemView, item, false);
                    showVotersAvatars(odpvh.itemView, item);
                }
                break;
            case MY_PROPOSAL:
                MyDopeProposalVH mdpvh = (MyDopeProposalVH) holder;
                Glide.with(mContext).load(item.avatar)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(mdpvh.avatarView);
                mdpvh.usernameView.setText(item.fullname);
                mdpvh.timeView.setText(item.date_send);
                mdpvh.questionView.setText(item.message);

                mdpvh.leftPicView.setImageDrawable(null);
                mdpvh.rightPicView.setImageDrawable(null);
                Object[] paramsMy = {item.photo1, item.photo2, mdpvh.leftPicView, mdpvh.rightPicView};
                ImageTransformationComputations taskMy = new ImageTransformationComputations();
                taskMy.execute(paramsMy);

                if (item.myVote > 0) {
//                    launchRateAnimation(mdpvh.itemView, item, false);
                    showVotersAvatars(mdpvh.itemView, item);
                }
                break;
            default:

        }
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).viewType;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    class OthersMsgVH extends RecyclerView.ViewHolder{

        private ImageView avatarView;
        private TextView usernameView;
        private TextView messageView;
        private TextView timeView;

        public OthersMsgVH(View itemView) {
            super(itemView);
            avatarView = (ImageView) itemView.findViewById(R.id.chat_others_message_avatar);
            usernameView = (TextView) itemView.findViewById(R.id.chat_others_message_username);
            messageView = (TextView) itemView.findViewById(R.id.chat_others_message_text_field);
            timeView = (TextView) itemView.findViewById(R.id.chat_others_message_time);
        }

    }

    class MyMsgVH extends RecyclerView.ViewHolder{

        private TextView messageView;
        private TextView timeView;

        public MyMsgVH(View itemView) {
            super(itemView);
            messageView = (TextView) itemView.findViewById(R.id.chat_my_message_text);
            timeView = (TextView) itemView.findViewById(R.id.chat_message_time);
        }

    }

    class DateLineVH extends RecyclerView.ViewHolder{

        private TextView dateView;

        public DateLineVH(View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.chat_date_divider_field);
        }
    }

    class OthersDopeProposalVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView avatarView;
        private TextView usernameView;
        private TextView timeView;
        private TextView questionView;
        private ImageView leftPicView;
        private ImageView rightPicView;

        public OthersDopeProposalVH(View itemView) {
            super(itemView);
            avatarView = (ImageView) itemView.findViewById(R.id.chat_others_dope_proposal_avatar);
            usernameView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_username);
            timeView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_time);
            questionView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_question);
            leftPicView = (ImageView) itemView.findViewById(R.id.option_picture_1);
            rightPicView = (ImageView) itemView.findViewById(R.id.option_picture_2);
            leftPicView.setOnClickListener(this);
            rightPicView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            ChatMessage chatMessage = mMessages.get(getAdapterPosition());

            if (chatMessage.myVote > 0) {
                Utilities.showExtremelyShortToast(mContext, "You have already voted this dope", 500);
                return;
            }else if (id == leftPicView.getId()){
                chatMessage.myVote = 1;
                chatMessage.leftVote++;
            }else if (id == rightPicView.getId()){
                chatMessage.myVote = 2;
                chatMessage.rightVote++;
            }
            recalculatePercentage(chatMessage);

            http.chatVote(Utilities.sToken, chatMessage.id, String.valueOf(chatMessage.myVote), itemView, chatMessage);
//            launchRateAnimation(itemView, chatMessage, true);
            showVotersAvatars(itemView, chatMessage);
        }
    }

    public void showVotersAvatars(View itemView, final ChatMessage messageObj){
        final RelativeLayout leftRL = (RelativeLayout) itemView.findViewById(R.id.chat_left_avatar_container);
        final RelativeLayout rightRL = (RelativeLayout) itemView.findViewById(R.id.chat_right_avatar_container);

        final ViewTreeObserver vtoLeft = leftRL.getViewTreeObserver();
        vtoLeft.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ArrayList<String> avatarsPics = new ArrayList<>();
                for (HashMap.Entry<String, String> entry : messageObj.usersVotedLeft.entrySet()) {
                    avatarsPics.add(entry.getValue());
                }
                leftRL.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ChatAvatarRelocationHandler avatarHandler = new ChatAvatarRelocationHandler(mContext, leftRL, avatarsPics);
                avatarHandler.showAvatars();
            }
        });

        ViewTreeObserver vtoRight = rightRL.getViewTreeObserver();
        vtoRight.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ArrayList<String> avatarsPics = new ArrayList<>();
                for (HashMap.Entry<String, String> entry : messageObj.usersVotedRight.entrySet()) {
                    avatarsPics.add(entry.getValue());
                }
                rightRL.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ChatAvatarRelocationHandler avatarHandler = new ChatAvatarRelocationHandler(mContext, rightRL, avatarsPics);
                avatarHandler.showAvatars();
            }
        });

    }

//    public void launchRateAnimation(View itemView, ChatMessage messageObj, boolean animatedDraw){
//
//        int leftRate = messageObj.leftPercent;
//        if (leftRate == 0 && leftRate == messageObj.rightPercent){
//            return;
//        }else {
//            ChoiceAnimationHelper.ChoiceSide side = (messageObj.myVote == 1) ? ChoiceAnimationHelper.ChoiceSide.Left : ChoiceAnimationHelper.ChoiceSide.Right;
//
//            boolean direction = (messageObj.proposalNum % 2 == 0) ? false : true;
//            mAnimHelper = new ChoiceAnimationHelper(((ChatActivity) mContext), itemView);
//            mAnimHelper.setParameters(side, leftRate, direction, true);
//            mAnimHelper.draw(animatedDraw);
//        }
//    }

    private void recalculatePercentage(ChatMessage info){
        info.votes++;
        info.leftPercent = info.leftVote * 100 / info.votes;
        info.rightPercent = 100 - info.leftPercent;

    }

    class MyDopeProposalVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView avatarView;
        private TextView usernameView;
        private TextView timeView;
        private TextView questionView;
        private ImageView leftPicView;
        private ImageView rightPicView;

        public MyDopeProposalVH(View itemView) {
            super(itemView);
            avatarView = (ImageView) itemView.findViewById(R.id.chat_others_dope_proposal_avatar);
            usernameView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_username);
            timeView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_time);
            questionView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_question);
            leftPicView = (ImageView) itemView.findViewById(R.id.option_picture_1);
            rightPicView = (ImageView) itemView.findViewById(R.id.option_picture_2);
            leftPicView.setOnClickListener(this);
            rightPicView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            ChatMessage myDopeInfo = mMessages.get(getAdapterPosition());
            if (myDopeInfo.myVote > 0) {
                Utilities.showExtremelyShortToast(mContext, "You have already voted this dope", 500);
                return;
            }else if (id == leftPicView.getId()){
                myDopeInfo.myVote = 1;
                myDopeInfo.leftVote++;
            }else if (id == rightPicView.getId()){
                myDopeInfo.myVote = 2;
                myDopeInfo.rightVote++;
            }

            recalculatePercentage(myDopeInfo);

            http.chatVote(Utilities.sToken, myDopeInfo.id, String.valueOf(myDopeInfo.myVote), itemView, myDopeInfo);
//            launchRateAnimation(itemView, myDopeInfo, true);
            showVotersAvatars(itemView, myDopeInfo);
        }
    }



//    class OthersMsg{
//        private ChatMessage messageObj;
//
//        public OthersMsg(ChatMessage messageObj){
//            this.messageObj = messageObj;
//        }
//    }
//
//    class MyMsg{
//        private ChatMessage messageObj;
//
//        public MyMsg(ChatMessage messageObj){
//            this.messageObj = messageObj;
//        }
//    }
//
//    class OthersDopeProposal{
//        private ChatMessage messageObj;
//
//        public OthersDopeProposal(ChatMessage messageObj){
//            this.messageObj = messageObj;
//        }
//    }
//
//    class MyDopeProposal{
//        private ChatMessage messageObj;
//
//        public MyDopeProposal(ChatMessage messageObj){
//            this.messageObj = messageObj;
//        }
//    }

    class DateLine{
        public DateLine(String date) {
            this.date = date;
        }

        private String date;
    }


    private class ImageTransformationComputations extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object... params) {

            int radius = mContext.getResources().getDimensionPixelSize(R.dimen.chat_radius);
            Bitmap leftPic = Utilities.getBitmapFromURL((String)params[0]);
            Bitmap resultLeft = Utilities.getOneSideRoundedBitmap(mContext, leftPic, true, radius);
            Bitmap rightPic = Utilities.getBitmapFromURL((String) params[1]);
            Bitmap resultRight = Utilities.getOneSideRoundedBitmap(mContext, rightPic, false, radius);

            Object[] objects = {params[2], params[3], resultLeft, resultRight};

            return objects;
        }

        @Override
        protected void onPostExecute(Object[] res) {
            ((ImageView)res[0]).setImageBitmap((Bitmap)res[2]);
            ((ImageView)res[1]).setImageBitmap((Bitmap) res[3]);

        }

    }
}

