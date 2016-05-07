package com.elantix.dopeapp;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by oleh on 4/3/16.
 */
public class CommentsActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private LinearLayout mCommentsContainer;
    private ScrollView mScrollView;
    private ImageButton mSendButton;
    private ImageButton mLeftToolbarButton;
    private EditText mNewCommentField;
    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SoftKeyboard softKeyboard;
    private LinearLayout mInfoBar;
    private ChatType mType;
    public ProgressDialog mProgressDialog;
    private String mDopeId;


    private enum ChatType{
        Comments, Chat, GroupChat
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        if (Utilities.sMyProfile == null){
            HttpKit http = new HttpKit(CommentsActivity.this);
            http.getMyProfileInfo();
        }

        mDopeId = getIntent().getStringExtra("dopeId");

        mInfoBar = (LinearLayout) findViewById(R.id.comments_info_bar);
        int type = getIntent().getIntExtra("type", 0);
        switch (type){
            case 0: mType = ChatType.Comments; break;
            case 1: mType = ChatType.Chat; break;
            case 2: mType = ChatType.GroupChat;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.comments_comment_list);

        switch (mType){
            case Comments:
                HttpKit http = new HttpKit(CommentsActivity.this);
                http.getComments(mDopeId, Utilities.sToken, null, null);

//                mAdapter = new AdapterComments(this, null);
//                mRecyclerView.setAdapter(mAdapter);
                break;
            case Chat:
                mAdapter = new AdapterChat(this);
                mRecyclerView.setAdapter(mAdapter);
                mInfoBar.setVisibility(View.GONE);
                break;
            case GroupChat:
                mInfoBar.setVisibility(View.GONE);
                break;
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);



        mToolbar = (Toolbar) findViewById(R.id.comments_toolbar);
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.comments_activity_root);
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);


        softKeyboard = new SoftKeyboard(rootLayout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged()
        {

            @Override
            public void onSoftKeyboardHide()
            {
                // Code here
            }

            @Override
            public void onSoftKeyboardShow()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

//        softKeyboard.openSoftKeyboard();
//        softKeyboard.closeSoftKeyboard();



        // DEPRECATED
        mCommentsContainer = (LinearLayout) findViewById(R.id.comments_comments_container);
        mScrollView = (ScrollView) findViewById(R.id.comments_scroll_view);
        // DEPRECATED


        mSendButton = (ImageButton) findViewById(R.id.comments_send_button);
        mSendButton.setOnClickListener(this);

        mNewCommentField = (EditText) findViewById(R.id.comments_new_comment_field);
        mLeftToolbarButton = (ImageButton) mToolbar.findViewById(R.id.left_button);
        ImageButton rightToolbarButton = (ImageButton) mToolbar.findViewById(R.id.right_button);
        rightToolbarButton.setVisibility(View.GONE);
        mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
        mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.comments_toolbar_title);
//        addComments(true);

    }

    public void showComments(CommentInfo[] comments){
        mAdapter = new AdapterComments(this, mDopeId, comments);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        softKeyboard.unRegisterSoftKeyboardCallback();
    }

//    // DEPRECATED
//    private void addComments(Boolean dummy){
//
//        LayoutInflater inflater = getLayoutInflater();
//
//        if (dummy) {
//            int[] avatarsResources = {R.drawable.fr3, R.drawable.fr4, R.drawable.fr5
//                    , R.drawable.fr3, R.drawable.fr4, R.drawable.fr5
//
//            };
//            int[] usersNames = {R.string.single_comment_dummy_user_name_1, R.string.single_comment_dummy_user_name_2, R.string.single_comment_dummy_user_name_3
//                    , R.string.single_comment_dummy_user_name_1, R.string.single_comment_dummy_user_name_2, R.string.single_comment_dummy_user_name_3
//
//            };
//            int[] commentsText = {R.string.single_comment_dummy_text_1, R.string.single_comment_dummy_text_2, R.string.single_comment_dummy_text_3
//                    , R.string.single_comment_dummy_text_1, R.string.single_comment_dummy_text_2, R.string.single_comment_dummy_text_3
//            };
//
//            for (int i=0; i< avatarsResources.length; i++){
//                inflateComment(inflater, true, avatarsResources[i], usersNames[i], commentsText[i]);
//            }
//        }else{
//            if (!mNewCommentField.getText().toString().matches("")) {
//                inflateComment(inflater, false, null, null, null);
//            }
//        }
//
//
//        mScrollView.post(new Runnable() {
//            public void run() {
//                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        });
//
//    }

//    // DEPRECATED
//    private void inflateComment(LayoutInflater inflater, Boolean dummy, Integer avatarRes, Integer userNameRes, Integer textRes) {
//        View comment = inflater.inflate(R.layout.single_comment, mCommentsContainer, false);
//        ImageView avatar = (ImageView) comment.findViewById(R.id.single_comment_avatar);
//        TextView userName = (TextView) comment.findViewById(R.id.single_comment_user_name);
//        TextView commentText = (TextView) comment.findViewById(R.id.single_comment_comment_text);
//        if(dummy){
//            avatar.setImageResource(avatarRes);
//            userName.setText(userNameRes);
//            commentText.setText(textRes);
//        }else{
//            userName.setTextColor(ContextCompat.getColor(CommentsActivity.this, R.color.single_comment_user_name_color));
//            TextView time = (TextView) comment.findViewById(R.id.single_comment_time);
//            time.setText("Just now");
//            avatar.setImageResource(R.drawable.maletskiy1);
//            userName.setText("Nikolay Maletskiy");
//
//            commentText.setText(mNewCommentField.getText());
//
//        }
//
//        mCommentsContainer.addView(comment);
//    }

    private void sendComment(){
        //Send to server. If success call adapters addComment method

        String msg = mNewCommentField.getText().toString();
        if (!msg.isEmpty()) {
            HttpKit http = new HttpKit(CommentsActivity.this);
            http.sendComment(Utilities.sToken, mDopeId, msg, null);
        }

    }

    public void showMyNewComment(String text){
        ((AdapterComments)mAdapter).addComment(text);
        mNewCommentField.setText("");
        DopeStatisticsActivity.sNumOfComments++;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mLeftToolbarButton.getId()){
            finish();
        }else if (id == mSendButton.getId()){
//            addComments(false);
            sendComment();
        }
    }
}
