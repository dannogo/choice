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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by oleh on 4/16/16.
 */
public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//        implements AdapterChat.AsyncResponse

    private LayoutInflater mInflater;
    private Context mContext;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    private List<Object> items;
    private final int DATELINE = 0, OTHERS_MSG = 1, MY_MSG = 2, OTHERS_PROPOSAL = 3, MY_PROPOSAL = 4;

    private int[] avatars = {R.drawable.fr3, R.drawable.fr4, R.drawable.fr5
            , R.drawable.fr3, R.drawable.fr4, R.drawable.fr5

    };



    public AdapterChat(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        items = new ArrayList<>();
        items.add(new DateLine("Yesterday"));
        items.add(new OthersMsg("Yana Sheleykis", "1.15pm", "Hello!", R.drawable.fr3));
        items.add(new MyMsg("1.17pm", "Hi :)"));
        items.add(new OthersMsg("Yana Sheleykis", "1.18pm", "How are you doing?", R.drawable.fr3));
        items.add(new MyMsg("1.18pm", "I am fine!"));
        items.add(new OthersMsg("Barbra Streisand", "1.18pm", "Sorry. I don`t want to interrupt you, but could you help me with one choice? I can not decide!", R.drawable.fr4));
        items.add(new OthersMsg("Yana Sheleykis", "1.19pm", "Ok, honey, what is it?", R.drawable.fr3));
        items.add(new MyMsg("1.19pm", "No no... I don`t want to make any decisions.. Not today!"));
        items.add(new OthersMsg("Barbra Streisand", "1.20pm", "Why? What happened, sweety?", R.drawable.fr4));
        items.add(new MyMsg("1.20pm", "I just want to relax.."));
        items.add(new OthersMsg("Yana Sheleykis", "1.20pm", "Never mind, I`ll help you. Where is your dope?", R.drawable.fr3));
        items.add(new OthersDopeProposal("Barbra Streisand", "1:21pm", R.drawable.fr4, "Some of the girls more beautiful?", R.drawable.girl3, R.drawable.girl4));
        items.add(new MyMsg("1.21pm", "What the hell?!"));
        items.add(new MyMsg("1.21pm", "You want us to help you compare two girls??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
        items.add(new MyMsg("1.22pm", "Why do you need this??"));
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
                Glide.with(mContext).load(om.avatar).into(omvh.avatarView);
                omvh.usernameView.setText(om.username);
                omvh.messageView.setText(om.message);
                omvh.timeView.setText(om.time);
                break;
            case MY_MSG:
                MyMsgVH mmvh = (MyMsgVH) holder;
                MyMsg mm = (MyMsg) items.get(position);
                mmvh.messageView.setText(mm.msg);
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
                Glide.with(mContext).load(odp.avatar).into(odpvh.avatarView);
                odpvh.usernameView.setText(odp.username);
                odpvh.timeView.setText(odp.time);
                odpvh.questionView.setText(odp.question);

                Object[] params = {odp.leftPic, odp.rightPic, odpvh.leftPicView, odpvh.rightPicView};
                ImageTransformationComputations task = new ImageTransformationComputations();
                task.execute(params);
                break;
            case MY_PROPOSAL:
                MyDopeProposalVH mdpvh = (MyDopeProposalVH) holder;
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

        public OthersDopeProposalVH(View itemView) {
            super(itemView);
            avatarView = (ImageView) itemView.findViewById(R.id.chat_others_dope_proposal_avatar);
            usernameView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_username);
            timeView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_time);
            questionView = (TextView) itemView.findViewById(R.id.chat_others_dope_proposal_question);
            leftPicView = (ImageView) itemView.findViewById(R.id.option_picture_1);
            rightPicView = (ImageView) itemView.findViewById(R.id.option_picture_2);
        }
    }

    class MyDopeProposalVH extends RecyclerView.ViewHolder{

        public MyDopeProposalVH(View itemView) {
            super(itemView);
        }
    }

    class OthersMsg{
        private String username;
        private String time;
        private String message;
        private int avatar;

        public OthersMsg(String username, String time, String message, int avatar) {
            this.username = username;
            this.time = time;
            this.message = message;
            this.avatar = avatar;
        }
    }

    class MyMsg{
        private String time;
        private String msg;

        public MyMsg(String time, String msg) {
            this.time = time;
            this.msg = msg;
        }
    }

    class OthersDopeProposal{
        private String username;
        private String time;
//        private int dopeNumber;
        private int avatar;
        private String question;
        private int leftPic;
        private int rightPic;

        public OthersDopeProposal(String username, String time, int avatar, String question, int leftPic, int rightPic) {
            this.username = username;
            this.time = time;
            this.avatar = avatar;
            this.question = question;
            this.leftPic = leftPic;
            this.rightPic = rightPic;
        }
    }

    class MyDopeProposal{
        private String time;
        private int dopeNumber;
    }

    class DateLine{
        public DateLine(String date) {
            this.date = date;
        }

        private String date;
    }


    private class ImageTransformationComputations extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object[] doInBackground(Object... params) {

            Bitmap leftPic = BitmapFactory.decodeResource(mContext.getResources(),
                    (Integer)params[0]);
            Bitmap resultLeft = Utilities.getOneSideRoundedBitmap(mContext, leftPic, true, 35);
//            ((ImageView)params[2]).setImageBitmap(resultLeft);

            Bitmap rightPic = BitmapFactory.decodeResource(mContext.getResources(),
                    (Integer)params[1]);
            Bitmap resultRight = Utilities.getOneSideRoundedBitmap(mContext, rightPic, false, 35);
//            ((ImageView)params[3]).setImageBitmap(resultRight);

//            Bitmap[] bitmaps = {resultLeft, resultRight};
            Object[] objects = {params[2], params[3], resultLeft, resultRight};

            return objects;
        }

        @Override
        protected void onPostExecute(Object[] res) {
            // Runs on the UI thread
            ((ImageView)res[0]).setImageBitmap((Bitmap)res[2]);
            ((ImageView)res[1]).setImageBitmap((Bitmap)res[3]);

        }

    }
}

