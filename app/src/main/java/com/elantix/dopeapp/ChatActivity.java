package com.elantix.dopeapp;

/**
 * Created by oleh on 5/31/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elantix.dopeapp.entities.ChatMessage;
import com.elantix.dopeapp.entities.ConversationInfo;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    public ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ImageButton mSendButton;
    private ImageButton mLeftToolbarButton;
    private ImageButton mRightToolbarButton;
    private EditText mNewCommentField;
    public AdapterChat mAdapter;
    private ImageButton mPlusButton;
    private String mDialogId;
    private int mInterval = 20000;
    private Handler mHandler;
    TextView mToolbarTitle;

    private Runnable mDialogUpdater = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("ChatActivity", "updateChat()");
                updateChat();
            } finally {
                mHandler.postDelayed(mDialogUpdater, mInterval);
            }
        }
    };


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
        setGroupName();
//        http.getDialogHistory(Utilities.sToken, mDialogId, String.valueOf(mPageNum), String.valueOf(mPageCount));
//        updateChat();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mHandler = new Handler();
    }

    public void setGroupName(){
        if (Utilities.sConversations != null && !Utilities.sConversations.isEmpty() && mToolbarTitle != null){
            for (int i=0; i<Utilities.sConversations.size(); i++){
                if (Utilities.sConversations.get(i).dialogs_id.equals(mDialogId)){
                    ConversationInfo conversation = Utilities.sConversations.get(i);
                    String fullnameToshow = conversation.members.get(0).fullname;
                    if (fullnameToshow.isEmpty() || fullnameToshow == null || fullnameToshow.equals("null")){
                        fullnameToshow = conversation.members.get(0).username;
                    }
                    if (conversation.members.size() > 1){
                        fullnameToshow += " + "+ (conversation.members.size()-1)+" Friend";
                    }
                    if(conversation.members.size() > 2){
                        fullnameToshow += "s";
                    }
                    // set to toolbar
                    mToolbarTitle.setText(fullnameToshow);
                }
            }

        }

    }



    void startRepeatingTask() {
        mDialogUpdater.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mDialogUpdater);
    }

    public void setDataToAdapter(ArrayList<ChatMessage> messages){
        ArrayList<Integer> changedItems = new ArrayList<>();
        if (mAdapter != null){
            if (mAdapter.mMessages != null){
                if (mAdapter.mMessages.size() == messages.size()){
                    for(int i=0; i<mAdapter.mMessages.size(); i++){
                        if (messages.get(i).votes != mAdapter.mMessages.get(i).votes){
//                            mAdapter.mMessages.set(i, mAdapter.sortMessage(messages.get(i)));
//                            mAdapter.mMessages.set(messages.get(i));

                            Log.e("ChatActivity", "VOTES NOT EQUALS");
                            int proposalNum = mAdapter.mMessages.get(i).proposalNum;
                            int viewType = mAdapter.mMessages.get(i).viewType;
                            mAdapter.mMessages.set(i, new ChatMessage(messages.get(i)));
                            mAdapter.mMessages.get(i).proposalNum = proposalNum;
                            mAdapter.mMessages.get(i).viewType = viewType;

                            changedItems.add(i);
                        }
                    }
                    if (!changedItems.isEmpty()){
                        for (int i=0; i<changedItems.size(); i++){
                            mAdapter.notifyItemChanged(i);
                        }
                    }

                    return;
                }
            }
        }
        mAdapter = new AdapterChat(ChatActivity.this, messages);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void findViews(){
        mRecyclerView = (RecyclerView) findViewById(R.id.comments_comment_list);
        mToolbar = (Toolbar) findViewById(R.id.comments_toolbar);

        mSendButton = (ImageButton) findViewById(R.id.comments_send_button);
        mSendButton.setOnClickListener(this);

        mNewCommentField = (EditText) findViewById(R.id.comments_new_comment_field);
        mNewCommentField.setHint("Write Message...");
        mLeftToolbarButton = (ImageButton) mToolbar.findViewById(R.id.left_button);
        mRightToolbarButton = (ImageButton) mToolbar.findViewById(R.id.right_button);
        mRightToolbarButton.setOnClickListener(this);
        mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
        mLeftToolbarButton.setOnClickListener(this);

        mToolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
//        toolbarTitle.setText("Name of friend(s)");

        LinearLayout infoBar = (LinearLayout) findViewById(R.id.comments_info_bar);
        infoBar.setVisibility(View.GONE);

        mPlusButton = (ImageButton) findViewById(R.id.comments_plus_button);
        mPlusButton.setOnClickListener(this);
    }

    public void showNewMessage(ChatMessage messageInfo){
//        mAdapter.sortMessage(messageInfo);
        mAdapter.mMessages.add(mAdapter.sortMessage(messageInfo));
        mAdapter.notifyItemInserted(mAdapter.mMessages.size());
        mRecyclerView.scrollToPosition(mAdapter.mMessages.size() - 1);
    }

    private void sendMessage(){

        String msg = mNewCommentField.getText().toString();
        if (!msg.isEmpty()) {
            mNewCommentField.setText("");
            http.sendDopeToChat(Utilities.sToken, mDialogId, msg, null, null);
        }

    }

    private void updateChat(){
        http.getDialogHistory(Utilities.sToken, mDialogId, String.valueOf(mPageNum), String.valueOf(mPageCount));
    }



    @Override
    protected void onResume() {
        if (mDialogId != null){
            startRepeatingTask();
//            setGroupName();

//            updateChat();
//            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopRepeatingTask();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utilities.LEAVE_GROUP){
            setGroupName();
            if (resultCode == Activity.RESULT_OK) {
//                if leave - finish this activity
                finish();
            }else if (resultCode == Activity.RESULT_CANCELED){

            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mLeftToolbarButton.getId()){
            finish();
        }else if(id == mRightToolbarButton.getId()){
            Intent intent = new Intent(ChatActivity.this, GroupSettingsActivity.class);
            Log.d("ChatActivity", "onClick mDialogId: "+mDialogId);
            intent.putExtra("dialog_id", mDialogId);
            startActivityForResult(intent, Utilities.LEAVE_GROUP);
        }else if (id == mSendButton.getId()){
            sendMessage();
        }else if (id == mPlusButton.getId()){
            Intent intent = new Intent(ChatActivity.this, TabPlusActivity.class);
            intent.putExtra("dialog_id", Integer.parseInt(mDialogId));
            intent.putExtra("chatExists", true);
            startActivity(intent);
        }
    }
}
