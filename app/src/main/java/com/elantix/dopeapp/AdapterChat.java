package com.elantix.dopeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elantix.dopeapp.entities.ChatMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by oleh on 4/16/16.
 */
public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mInflater;
    private Context mContext;

    public ArrayList<ChatMessage> mMessages;
    public List<Object> items;
    private final int DATELINE = 0, OTHERS_MSG = 1, MY_MSG = 2, OTHERS_PROPOSAL = 3, MY_PROPOSAL = 4;

    // TODO:
    // Do not forget about dateline

    private String replaceFullnameIfNecessary(ChatMessage messageObj){
        String result = (messageObj.fullname.isEmpty() || messageObj.fullname.equals("null") || messageObj.fullname == null) ? messageObj.username : messageObj.fullname;
        return result;
    }

    public void sortMessage(ChatMessage item){
        String fullname = replaceFullnameIfNecessary(item);
        String time = Utilities.convertDate(item.date_send, true);
        String message = item.message.replaceAll("(\\r|\\n)", "");

        if (item.sender.equals(Utilities.sUid)){
            if (item.photo1.isEmpty()){
                items.add(new MyMsg(item.avatar, fullname, time, message));
            }else{
                items.add(new MyDopeProposal(fullname, item.avatar,
                        item.photoSoc, item.photo1, item.photo2, message, time));
            }
        }else{
            if (item.photo1.isEmpty()){
                items.add(new OthersMsg(fullname, item.avatar, message, time));
            }else{
                items.add(new OthersDopeProposal(fullname, item.avatar, time, message,
                        item.photoSoc, item.photo1, item.photo2));
            }
        }
    }

    public AdapterChat(Context context, ArrayList<ChatMessage> messages) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        mMessages = new ArrayList<>(messages);
        items = new ArrayList<>();

        for (int i=0; i<mMessages.size(); i++){
            ChatMessage item = mMessages.get(i);
            sortMessage(item);
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
        switch (holder.getItemViewType()){
            case OTHERS_MSG:
                OthersMsgVH omvh = (OthersMsgVH) holder;
                OthersMsg om = (OthersMsg) items.get(position);
                Glide.with(mContext).load(om.avatar)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(omvh.avatarView);
                omvh.usernameView.setText(om.fullname);
                omvh.messageView.setText(om.message);
                omvh.timeView.setText(om.time);
                break;
            case MY_MSG:
                MyMsgVH mmvh = (MyMsgVH) holder;
                MyMsg mm = (MyMsg) items.get(position);
                mmvh.messageView.setText(mm.message);
                mmvh.timeView.setText(mm.time);
                break;
            case DATELINE:
                DateLineVH dlvh = (DateLineVH) holder;
                DateLine dl = (DateLine) items.get(position);
                dlvh.dateView.setText(dl.date);
                break;
            case OTHERS_PROPOSAL:
                final OthersDopeProposalVH odpvh = (OthersDopeProposalVH) holder;
                final OthersDopeProposal odp = (OthersDopeProposal) items.get(position);
                Glide.with(mContext).load(odp.avatar)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(odpvh.avatarView);
                odpvh.usernameView.setText(odp.fullname);
                odpvh.timeView.setText(odp.time);
                odpvh.questionView.setText(odp.message);

                Object[] paramsOth = {odp.photo1, odp.photo2, odpvh.leftPicView, odpvh.rightPicView};
                ImageTransformationComputations taskOth = new ImageTransformationComputations();
                taskOth.execute(paramsOth);
                break;
            case MY_PROPOSAL:
                MyDopeProposalVH mdpvh = (MyDopeProposalVH) holder;
                final MyDopeProposal mp = (MyDopeProposal) items.get(position);
                Glide.with(mContext).load(mp.avatar)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(mdpvh.avatarView);
                mdpvh.usernameView.setText(mp.fullname);
                mdpvh.timeView.setText(mp.time);
                mdpvh.questionView.setText(mp.message);

                Object[] paramsMy = {mp.photo1, mp.photo2, mdpvh.leftPicView, mdpvh.rightPicView};
                ImageTransformationComputations taskMy = new ImageTransformationComputations();
                taskMy.execute(paramsMy);
                break;
            default:

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof OthersMsg){
            return OTHERS_MSG;
        }else if(items.get(position) instanceof MyMsg){
            return MY_MSG;
        }else if(items.get(position) instanceof DateLine){
            return DATELINE;
        }else if(items.get(position) instanceof OthersDopeProposal){
            return OTHERS_PROPOSAL;
        }else if (items.get(position) instanceof MyDopeProposal){
            return MY_PROPOSAL;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
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

    class OthersDopeProposalVH extends RecyclerView.ViewHolder{

        private ImageView avatarView;
        private TextView usernameView;
        private TextView timeView;
        private TextView questionView;
        private ImageView leftPicView;
        private ImageView rightPicView;
//        private ImageView photoSoc;

        public OthersDopeProposalVH(View itemView) {
            super(itemView);
            avatarView = (ImageView) itemView.findViewById(R.id.chat_others_dope_proposal_avatar);
            usernameView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_username);
            timeView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_time);
            questionView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_question);
            leftPicView = (ImageView) itemView.findViewById(R.id.option_picture_1);
            rightPicView = (ImageView) itemView.findViewById(R.id.option_picture_2);
//            photoSoc = (ImageView) itemView.findViewById(R.id.chat_photoSoc);
        }
    }

    class MyDopeProposalVH extends RecyclerView.ViewHolder{

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
        }
    }

    class OthersMsg{
        private String fullname;
        private String avatar;
        private String message;
        private String time;

        public OthersMsg(String fullname, String avatar, String message, String time) {
            this.fullname = fullname;
            this.avatar = avatar;
            this.message = message;
            this.time = time;
        }
    }

    class MyMsg{
        private String time;
        private String message;
        private String avatar;
        private String fullname;

        public MyMsg(String avatar, String fullname, String time, String message) {
            this.time = time;
            this.message = message;
            this.avatar = avatar;
            this.fullname = fullname;
        }
    }

    class OthersDopeProposal{
        private String fullname;
        private String avatar;
        private String time;
        private String message;
        private String photo1;
        private String photo2;

        public OthersDopeProposal(String fullname, String avatar, String time, String message, String photoSoc, String photo1, String photo2) {
            this.fullname = fullname;
            this.avatar = avatar;
            this.message = message;
            this.time = time;
            this.photo1 = photo1;
            this.photo2 = photo2;
        }
    }

    class MyDopeProposal{
        private String fullname;
        private String avatar;
        private String photoSoc;
        private String photo1;
        private String photo2;
        private String message;
        private String time;

        public MyDopeProposal(String fullname, String avatar, String photoSoc, String photo1, String photo2, String message, String time) {
            this.fullname = fullname;
            this.avatar = avatar;
            this.message = message;
            this.time = time;
            this.photoSoc = photoSoc;
            this.photo1 = photo1;
            this.photo2 = photo2;
        }
    }

    class DateLine{
        public DateLine(String date) {
            this.date = date;
        }

        private String date;
    }


    private class ImageTransformationComputations extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object... params) {

            Bitmap leftPic = Utilities.getBitmapFromURL((String)params[0]);
            Bitmap resultLeft = Utilities.getOneSideRoundedBitmap(mContext, leftPic, true, 35);

            Bitmap rightPic = Utilities.getBitmapFromURL((String) params[1]);
            Bitmap resultRight = Utilities.getOneSideRoundedBitmap(mContext, rightPic, false, 35);

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

