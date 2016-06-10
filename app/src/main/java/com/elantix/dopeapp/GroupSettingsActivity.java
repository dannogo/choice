package com.elantix.dopeapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elantix.dopeapp.entities.ConversationInfo;

import java.util.ArrayList;

/**
 * Created by oleh on 6/4/16.
 */
public class GroupSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar mToolbar;
    ImageButton mLeftToolbarButton;
    ProgressDialog mProgressDialog;
    HttpChat http;
    AdapterGroupSettings mAdapter;
    RecyclerViewWithMaxHeight mRecyclerView;
    public ConversationInfo mConversation;
    String mDialogId;
    private LinearLayout mAddMemberField;
    private LinearLayout mLeaveGroup;
    public TextView mGroupName;

    // Replace these two vars with regular ones
    String page = "1";
    String count = "50";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);

        mDialogId = getIntent().getStringExtra("dialog_id");



        http = new HttpChat(GroupSettingsActivity.this);
        mAddMemberField = (LinearLayout) findViewById(R.id.profile_settings_fragment_contact_us);
        mAddMemberField.setOnClickListener(this);
        mLeaveGroup = (LinearLayout) findViewById(R.id.leave_group_container);
        mLeaveGroup.setOnClickListener(this);
        mGroupName = (TextView) findViewById(R.id.group_settings_group_name);
        mRecyclerView = (RecyclerViewWithMaxHeight) findViewById(R.id.group_settings_members_list);
        float maxHeight = getResources().getDimension(R.dimen.message_list_row_height);
        mRecyclerView.setMaxHeight((int) (maxHeight*3));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(GroupSettingsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

//        if (Utilities.sConversations != null){
//            boolean isFound = false;
//            for (int i=0; i<Utilities.sConversations.length; i++){
//                if (Utilities.sConversations[i].dialogs_id.equals(dialogId)){
//                    setDataToAdapter(Utilities.sConversations[i]);
//                    isFound = true;
//                    break;
//                }
//            }
//            if (!isFound){
//                http.getConversationList(Utilities.sToken, Utilities.sUid, page, count);
//            }
//        }else{
//            http.getConversationList(Utilities.sToken, Utilities.sUid, page, count);
//        }

        mConversation = Utilities.findConversationById(Utilities.sConversations, mDialogId);
        Log.d("Group Settings", "mConversation: "+mConversation);
        if (mConversation != null) {
            setDataToAdapter(mConversation);

        }else{
            http.getConversationList(Utilities.sToken, Utilities.sUid, page, count);
        }

        mToolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mLeftToolbarButton = (ImageButton) findViewById(R.id.left_button);
        mLeftToolbarButton.setOnClickListener(this);
        mLeftToolbarButton.setImageResource(R.drawable.toolbar_cross_icon);

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (23.5*scale + 0.5f);
        mLeftToolbarButton.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.group_settings_toolbar_title);
        ImageView rightToolbarButton = (ImageView) findViewById(R.id.right_button);
        rightToolbarButton.setVisibility(View.GONE);


    }

    public void bringDataToActivity(ArrayList<ConversationInfo> conversations){
        mConversation = Utilities.findConversationById(conversations, mDialogId);
        if (mConversation != null){
            setDataToAdapter(mConversation);
        }
    }

//    public static ConversationInfo findConversationById(ConversationInfo[] conversations, String dialogId){
//        if (dialogId == null){
//            Log.e("findConversationById", "dialogId is NULL");
//            return null;
//        }
//        if (conversations == null){
//            return null;
//        }else{
//            for (int i=0; i<conversations.length; i++){
//                if (conversations[i].dialogs_id.equals(dialogId)){
//                    return conversations[i];
//                }
//            }
//            return null;
//        }
//    }

    public void setDataToAdapter(ConversationInfo conversation){
        mAdapter = new AdapterGroupSettings(GroupSettingsActivity.this, conversation);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void finishActivityWithResult(){
        Intent returnIntent = new Intent();
//        returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void removeMemberFromAdapter(String position){
        Log.e("GroupSettings", "removeMemberFromAdapter position: " + position);
        int adapterPosition = Integer.parseInt(position);
        mAdapter.mMembers.remove(adapterPosition);
        mAdapter.notifyItemRemoved(adapterPosition);
        mConversation.members.remove(adapterPosition);
        mAdapter.setGroupName();
//        Utilities.sConversations
        if (Utilities.sConversations != null) {
            for (int i = 0; i < Utilities.sConversations.size(); i++) {
                if (Utilities.sConversations.get(i).id.equals(mDialogId)) {
                    Utilities.sConversations.get(i).members.remove(adapterPosition);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mLeftToolbarButton.getId()){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }else if (id == mAddMemberField.getId()){

        }else if (id == mLeaveGroup.getId()){
            http.leaveConversation(Utilities.sToken, mDialogId, null);
        }
    }
}
