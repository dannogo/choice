package com.elantix.dopeapp;

/**
 * Created by oleh on 5/31/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elantix.dopeapp.entities.ChatMessage;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    public ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ImageButton mSendButton;
    private ImageButton mLeftToolbarButton;
    private EditText mNewCommentField;
    public AdapterChat mAdapter;
    private ImageButton mPlusButton;
    private String mDialogId;

    // endless list support stuff
    private HttpChat http;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int mTotalItemCount = 0;
    final private int mPageCount = 50;
    private int mPageNum = 1;
    public boolean isNewFetch = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        mDialogId = intent.getStringExtra("dialog_id");

        findViews();
        http = new HttpChat(ChatActivity.this);
        http.getDialogHistory(Utilities.sToken, mDialogId, String.valueOf(mPageNum), String.valueOf(mPageCount));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setDataToAdapter(ArrayList<ChatMessage> messages){
        mAdapter = new AdapterChat(ChatActivity.this, messages);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void findViews(){
        mRecyclerView = (RecyclerView) findViewById(R.id.comments_comment_list);
        mToolbar = (Toolbar) findViewById(R.id.comments_toolbar);

        mSendButton = (ImageButton) findViewById(R.id.comments_send_button);
        mSendButton.setOnClickListener(this);

        mNewCommentField = (EditText) findViewById(R.id.comments_new_comment_field);
        mNewCommentField.setHint("Write Message...");
        mLeftToolbarButton = (ImageButton) mToolbar.findViewById(R.id.left_button);
        ImageButton rightToolbarButton = (ImageButton) mToolbar.findViewById(R.id.right_button);
        rightToolbarButton.setVisibility(View.GONE);
        mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
        mLeftToolbarButton.setOnClickListener(this);

        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Name of friend(s)");

        LinearLayout infoBar = (LinearLayout) findViewById(R.id.comments_info_bar);
        infoBar.setVisibility(View.GONE);

        mPlusButton = (ImageButton) findViewById(R.id.comments_plus_button);
        mPlusButton.setOnClickListener(this);
    }

    public void showNewMessage(ChatMessage messageInfo){
        mAdapter.sortMessage(messageInfo);
        mAdapter.notifyItemInserted(mAdapter.items.size());
        mRecyclerView.scrollToPosition(mAdapter.items.size()-1);
    }

    private void sendMessage(){

        String msg = mNewCommentField.getText().toString();
        if (!msg.isEmpty()) {
            mNewCommentField.setText("");
            http.sendDopeToChat(Utilities.sToken, mDialogId, msg, null, null);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mLeftToolbarButton.getId()){
            finish();
        }else if (id == mSendButton.getId()){
            sendMessage();
        }else if (id == mPlusButton.getId()){

        }
    }
}
