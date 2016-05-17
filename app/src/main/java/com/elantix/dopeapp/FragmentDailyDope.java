package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by oleh on 3/17/16.
 */
public class FragmentDailyDope extends Fragment implements View.OnClickListener{

    protected View fragmentView;
    private LinearLayout mInfoBar;
    ImageView mOptionPicture1;
    ImageView mOptionPicture2;
    ChoiceAnimationHelper mAnimHelper = null;
    int mDopeNum;
    private FragmentViewPager.CommunicatorOne mCommOne;
    private ImageButton moreButton;
    private ImageButton mMessageButton;

    public DopeInfo mCurItem;
    private TextView mQuestion;
    private TextView mVotes;
    private TextView mUserName;
    private ImageView mAvatar;


    // newInstance constructor for creating fragment with arguments
    public static FragmentDailyDope newInstance(int page) {
        FragmentDailyDope fragmentDailyDope = new FragmentDailyDope();
        Bundle args = new Bundle();
        args.putInt("dopeNum", page);
        fragmentDailyDope.setArguments(args);
        return fragmentDailyDope;
    }



    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mDopeNum = bundle.getInt("dopeNum", 0);
        if (Utilities.sDopeListType == Utilities.DopeListType.Ten){
            mCurItem = Utilities.sDopes10[mDopeNum];
        }else{
            if (Utilities.sDopes100 != null) {
                mCurItem = Utilities.sDopes100[mDopeNum];
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_daily_dope, container, false);
        mCommOne = (FragmentViewPager.CommunicatorOne) getActivity();

        mQuestion = (TextView) fragmentView.findViewById(R.id.question);
        mVotes = (TextView) fragmentView.findViewById(R.id.votes);
        mUserName = (TextView) fragmentView.findViewById(R.id.contact_name);
        mAvatar = (ImageView) fragmentView.findViewById(R.id.contact_avatar_icon);

        moreButton = (ImageButton) fragmentView.findViewById(R.id.more_button);
        moreButton.setOnClickListener(this);
        mMessageButton = (ImageButton) fragmentView.findViewById(R.id.message_button);
        mMessageButton.setOnClickListener(this);

        mInfoBar = (LinearLayout) fragmentView.findViewById(R.id.daily_dope_info_bar);
        mInfoBar.setOnClickListener(this);

        if (mCurItem != null && mCurItem.myVote != 0){
            if (Utilities.sRateStateBackups[mDopeNum] == null){
                ChoiceAnimationHelper.ChoiceSide side;
                if (mCurItem.myVote == 1){
                    side = ChoiceAnimationHelper.ChoiceSide.Left;
                }else {
                    side = ChoiceAnimationHelper.ChoiceSide.Right;
                }
                boolean direction = ((mDopeNum + 1) % 2 == 0) ? false : true;

                Utilities.sRateStateBackups[mDopeNum] = new RateStateBackup(side, mCurItem.percent1, direction);
                applyRestoredRateData();
            }
        }

        if (Utilities.sRateStateBackups != null && Utilities.sRateStateBackups[mDopeNum] != null){
            applyRestoredRateData();
        }

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mQuestion.setText(mCurItem.question);
        mVotes.setText(""+mCurItem.votesAll+" Votes");
        if (!mCurItem.fullname.isEmpty()) {
            mUserName.setText(mCurItem.fullname);
        }else{
            mUserName.setText(mCurItem.username);
        }

        Glide.with(this).load(mCurItem.avatar)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(mAvatar);

        ImageView blurredPicture1 = (ImageView) fragmentView.findViewById(R.id.blurred_picture_1);
        ImageView blurredPicture2 = (ImageView) fragmentView.findViewById(R.id.blurred_picture_2);

        mOptionPicture1 = (ImageView) fragmentView.findViewById(R.id.option_picture_1);
        mOptionPicture2 = (ImageView) fragmentView.findViewById(R.id.option_picture_2);
        mOptionPicture1.setOnClickListener(this);
        mOptionPicture2.setOnClickListener(this);

        Uri image1 = mCurItem.photo1;
        Uri image2 = mCurItem.photo2;

        Glide.with(this).load(image1).into(mOptionPicture1);
        Glide.with(this).load(image2).into(mOptionPicture2);

        Glide.with(this).load(image1)
                .bitmapTransform(new BlurTransformation(getActivity()))
                .into(blurredPicture1);

        Glide.with(this).load(image2)
                .bitmapTransform(new BlurTransformation(getActivity()))
                .into(blurredPicture2);

    }

    private void launchRateAnimation(ChoiceAnimationHelper.ChoiceSide side){

        int leftRate = mCurItem.percent1;

        boolean direction = ((mDopeNum + 1) % 2 == 0) ? false : true;
        mAnimHelper = new ChoiceAnimationHelper(getActivity(), fragmentView);
        mAnimHelper.setParameters(side, leftRate, direction);
        mAnimHelper.draw(true);
        Utilities.sRateStateBackups[mDopeNum] = new RateStateBackup(side, leftRate, direction);
//        Utilities.sRateAnimationDirection = !Utilities.sRateAnimationDirection;
    }

    private void applyRestoredRateData(){
        ChoiceAnimationHelper.ChoiceSide side = Utilities.sRateStateBackups[mDopeNum].chosenSide;
        int leftRate = Utilities.sRateStateBackups[mDopeNum].leftRate;
        Boolean directionInside = Utilities.sRateStateBackups[mDopeNum].directionInside;

        mAnimHelper = new ChoiceAnimationHelper(getActivity(), fragmentView);
        mAnimHelper.setParameters(side, leftRate, directionInside);
        mAnimHelper.draw(false);
    }

    private void showNextPage(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCommOne.respond();
            }
        }, 1200);
    }

    private void recalculatePercentage(){
        mCurItem.votesAll += 1;
        mCurItem.percent1 = mCurItem.votes1 * 100 / mCurItem.votesAll;
        mCurItem.percent2 = 100 - mCurItem.percent1;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mOptionPicture1.getId()){
            if (mCurItem.myVote == 0) {
                if (Utilities.sToken != null) {
                    HttpKit http = new HttpKit(getActivity());
                    http.vote(Utilities.sToken, mCurItem.id, "1");
                    mCurItem.votes1 += 1;
                    mCurItem.myVote = 1;
                    recalculatePercentage();
                    mVotes.setText("" + mCurItem.votesAll + " Votes");
                }
                if (mAnimHelper == null) {
                    launchRateAnimation(ChoiceAnimationHelper.ChoiceSide.Left);
                    showNextPage();
                }
            }else{
//                Toast.makeText(getActivity(), "You have already voted this dope", Toast.LENGTH_SHORT).show();
                Utilities.showExtremelyShortToast(getActivity(), "You have already voted this dope", 500);
            }
        }else if (id == mOptionPicture2.getId()){
            if (mCurItem.myVote == 0) {
                if (Utilities.sToken != null) {
                    HttpKit http = new HttpKit(getActivity());
                    http.vote(Utilities.sToken, mCurItem.id, "2");
                    mCurItem.votes2 += 1;
                    mCurItem.myVote = 2;
                    recalculatePercentage();
                    mVotes.setText("" + mCurItem.votesAll + " Votes");
                }

                if (mAnimHelper == null) {
                    launchRateAnimation(ChoiceAnimationHelper.ChoiceSide.Right);
                    showNextPage();
                }
            }else{
                Utilities.showExtremelyShortToast(getActivity(), "You have already voted this dope", 500);
            }
        }else if(id == mInfoBar.getId()){

        }else if (id == moreButton.getId()){
            Bundle bundle = this.getArguments();
            ((MainActivity) getActivity()).showContextOptions(true, mDopeNum);

        }else if(id == mMessageButton.getId()){
            Intent intent = new Intent(getActivity(), CommentsActivity.class);
            intent.putExtra("dopeId", mCurItem.id);
            intent.putExtra("question", mCurItem.question);
            intent.putExtra("votesCnt", mCurItem.votesAll);
            intent.putExtra("type", 0);
            startActivity(intent);
        }
    }
}
