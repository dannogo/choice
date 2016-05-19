package com.elantix.dopeapp;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import com.bumptech.glide.util.Util;

/**
 * Created by oleh on 4/3/16.
 */
public class CommentsActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private ImageButton mSendButton;
    private ImageButton mLeftToolbarButton;
    private EditText mNewCommentField;
    protected RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
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

        Intent intent = getIntent();
        mDopeId = intent.getStringExtra("dopeId");
        String question = intent.getStringExtra("question");
        int votesCnt = intent.getIntExtra("votesCnt", 0);

        TextView questionView = (TextView) findViewById(R.id.comments_question);
        TextView votesView = (TextView) findViewById(R.id.comments_number_of_votes);
        questionView.setText(question);
        votesView.setText(""+votesCnt+" Votes");

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
        mRecyclerView.setHasFixedSize(true);
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        softKeyboard.unRegisterSoftKeyboardCallback();
    }




    private void sendComment(){
        //Send to server. If success call adapters addComment method

        String msg = mNewCommentField.getText().toString();
        if (!msg.isEmpty()) {
            HttpKit http = new HttpKit(CommentsActivity.this);
            http.sendComment(Utilities.sToken, mDopeId, msg, null);
        }

    }

    public void showMyNewComment(String text, String commentId, String dateCreate){
        ((AdapterComments)mAdapter).addComment(text, commentId, dateCreate);
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

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(CommentsActivity.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) CommentsActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                AdapterComments testAdapter = (AdapterComments)recyclerView.getAdapter();
                if (testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                if (Utilities.sUid == null){
                    return 0;
                }
                if (!testAdapter.mCommentsList.get(position).user_id.equals(Utilities.sUid)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                AdapterComments adapter = (AdapterComments)mRecyclerView.getAdapter();
//                if (adapter.mCommentsList.get(swipedPosition).user_id.equals(Utilities.sUid)) {
                    adapter.pendingRemoval(swipedPosition);
//                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

//                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }
}
