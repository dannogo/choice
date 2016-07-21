package com.elantix.dopeapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by oleh on 4/9/16.
 */
public class ChoiceAnimationHelper {

    private Activity mActivity;
    private int mLeftRateValue;
    private int mRightRateValue;
    private Boolean mDirectionInside;
    private ChoiceSide mChosenSide;
    private WiningSide mWiningSide;
    private Boolean mAnimate;
    private View mFragmentView;

    private LinearLayout mRateBar;
    private RelativeLayout mLeftColoredCoverView;
    private RelativeLayout mRightColoredCoverView;
    private ImageView mLeftDiamondView;
    private ImageView mRightDiamondView;
    private ImageView mDiamond;
    private ImageView mLeftPercentageCircle;
    private ImageView mRightPercentageCircle;
    private TextView mLeftText;
    private TextView mRightText;

    private int mChosenLineWidth;
    private int mNotChosenLineWidth;
    private int mWiningLineColor;
    private int mLosingLineColor;
    private int mEqualLineColor;
    private int mWayRingColor;
    private int mAnimationDuration = 700;
    private int mSmallCircleSize;
    private float mSmallTextSize;

    private boolean mIsChat;

//    private int m

    public enum ChoiceSide{
        Left, Right, None
    }

    private enum WiningSide{
        Left, Right, Equal
    }

    public ChoiceAnimationHelper(Activity activity, View fragmentView) {
        mActivity = activity;
        mFragmentView = fragmentView;

        mRateBar = (LinearLayout) mFragmentView.findViewById(R.id.rate_bar);
        mLeftColoredCoverView = (RelativeLayout) mFragmentView.findViewById(R.id.dope_left_colored_cover);
        mRightColoredCoverView = (RelativeLayout) mFragmentView.findViewById(R.id.dope_right_colored_cover);
        mLeftDiamondView = (ImageView) mFragmentView.findViewById(R.id.dope_left_diamond);
        mRightDiamondView = (ImageView) mFragmentView.findViewById(R.id.dope_right_diamond);
        mLeftPercentageCircle = (ImageView) mFragmentView.findViewById(R.id.dope_left_percentage_circle);
        mRightPercentageCircle = (ImageView) mFragmentView.findViewById(R.id.dope_right_percentage_circle);
        mLeftText = (TextView) mFragmentView.findViewById(R.id.left_rate_text_view);
        mRightText = (TextView) mFragmentView.findViewById(R.id.right_rate_text_view);

        mChosenLineWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.dope_rate_chosen_progress_line_width);
        mNotChosenLineWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.dope_rate_not_chosen_progress_line_width);
        mWiningLineColor = ContextCompat.getColor(mActivity, R.color.dope_statistics_success_color);
        mLosingLineColor = ContextCompat.getColor(mActivity, R.color.dope_statistics_fail_color);
        mEqualLineColor = ContextCompat.getColor(mActivity, R.color.dope_rate_equal_progress_line_color);
        mWayRingColor = ContextCompat.getColor(mActivity, android.R.color.darker_gray);
        mSmallCircleSize = mActivity.getResources().getDimensionPixelSize(R.dimen.dope_rate_small_circle_size);
        mSmallTextSize = mActivity.getResources().getDimension(R.dimen.dope_rate_small_percentage_size);

    }

    public void setParameters(ChoiceSide chosenSide, int leftRate, Boolean directionInside){
        setParameters(chosenSide, leftRate, directionInside, false);
    }

    public void setParameters(ChoiceSide chosenSide, int leftRate, Boolean directionInside, boolean isChat){
        mChosenSide = chosenSide;
        mDirectionInside= directionInside;
        mLeftRateValue = leftRate;
        mRightRateValue = 100-leftRate;
        mIsChat = isChat;

        mLeftText.setText(""+mLeftRateValue+"%");
        mRightText.setText("" + mRightRateValue + "%");

        if(mLeftRateValue > mRightRateValue){
            mWiningSide = WiningSide.Left;
        }else if (mLeftRateValue < mRightRateValue){
            mWiningSide = WiningSide.Right;
        }else{
            mWiningSide = WiningSide.Equal;
        }
    }

    private ShapeDrawable createRoundedDrawable(boolean isLeft, int colorRes){

        ShapeDrawable background = new ShapeDrawable();
        int radius = mActivity.getResources().getDimensionPixelSize(R.dimen.chat_radius);
        float[] radii = new float[8];
        if (isLeft) {
            radii[0] = radius;
            radii[1] = radius;
            radii[6] = radius;
            radii[7] = radius;
        }else{
            radii[2] = radius;
            radii[3] = radius;
            radii[4] = radius;
            radii[5] = radius;
        }
        background.setShape(new RoundRectShape(radii, null, null));
        int color = ContextCompat.getColor(mActivity, colorRes);
        background.getPaint().setColor(color);

        return background;
    }

    public void draw(Boolean animate){
        mAnimate = animate;

        mRateBar.setVisibility(View.VISIBLE);

        int leftRingColor;
        int rightRingColor;
        float leftConvertedToFloatValue = (float) mLeftRateValue / 100;
        float rightConvertedToFloatValue = (float) mRightRateValue / 100;
        int leftLineWidth;
        int rightLineWidth;

        if (mDirectionInside){
            rightConvertedToFloatValue *= -1;
        }else{
            leftConvertedToFloatValue *= -1;
        }

        if (mChosenSide == ChoiceSide.Left){
            mLeftDiamondView.setVisibility(View.VISIBLE);
            mDiamond = mLeftDiamondView;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRightPercentageCircle.getLayoutParams();
            params.width = mSmallCircleSize;
            params.height = mSmallCircleSize;
            mRightPercentageCircle.setLayoutParams(params);
            mRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallTextSize);
            leftLineWidth = mChosenLineWidth;
            rightLineWidth = mNotChosenLineWidth;
        }else if (mChosenSide == ChoiceSide.Right){
            mRightDiamondView.setVisibility(View.VISIBLE);
            mDiamond = mRightDiamondView;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLeftPercentageCircle.getLayoutParams();
            params.width = mSmallCircleSize;
            params.height = mSmallCircleSize;
            mLeftPercentageCircle.setLayoutParams(params);
            mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallTextSize);
            leftLineWidth = mNotChosenLineWidth;
            rightLineWidth = mChosenLineWidth;
        }else{

            RelativeLayout.LayoutParams paramsL = (RelativeLayout.LayoutParams) mLeftPercentageCircle.getLayoutParams();
            paramsL.width = mSmallCircleSize;
            paramsL.height = mSmallCircleSize;
            mLeftPercentageCircle.setLayoutParams(paramsL);
            mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallTextSize);

            RelativeLayout.LayoutParams paramsR = (RelativeLayout.LayoutParams) mRightPercentageCircle.getLayoutParams();
            paramsR.width = mSmallCircleSize;
            paramsR.height = mSmallCircleSize;
            mRightPercentageCircle.setLayoutParams(paramsR);
            mRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallTextSize);

            leftLineWidth = mNotChosenLineWidth;
            rightLineWidth = mNotChosenLineWidth;
        }


        if (mWiningSide == WiningSide.Left){
            leftRingColor = mWiningLineColor;
            rightRingColor = mLosingLineColor;
            if (mIsChat){
                mLeftColoredCoverView.setBackground(createRoundedDrawable(true, R.color.dope_rate_wining_semi_transparent_background));
                mRightColoredCoverView.setBackground(createRoundedDrawable(false, R.color.dope_rate_losing_semi_transparent_background));
            }else {
                mLeftColoredCoverView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.dope_rate_wining_semi_transparent_background));
                mRightColoredCoverView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.dope_rate_losing_semi_transparent_background));
            }
        }else if (mWiningSide == WiningSide.Right){
            leftRingColor = mLosingLineColor;
            rightRingColor = mWiningLineColor;
            if (mIsChat) {
                mLeftColoredCoverView.setBackground(createRoundedDrawable(true, R.color.dope_rate_losing_semi_transparent_background));
                mRightColoredCoverView.setBackground(createRoundedDrawable(false, R.color.dope_rate_wining_semi_transparent_background));
            }else{
                mLeftColoredCoverView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.dope_rate_losing_semi_transparent_background));
                mRightColoredCoverView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.dope_rate_wining_semi_transparent_background));
            }
        }else{
            leftRingColor = mEqualLineColor;
            rightRingColor = mEqualLineColor;
            if (mIsChat) {
                mLeftColoredCoverView.setBackground(createRoundedDrawable(true, R.color.dope_rate_equal_semi_transparent_background));
                mRightColoredCoverView.setBackground(createRoundedDrawable(false, R.color.dope_rate_equal_semi_transparent_background));
            }else{
                mLeftColoredCoverView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.dope_rate_equal_semi_transparent_background));
                mRightColoredCoverView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.dope_rate_equal_semi_transparent_background));
            }

        }

        CircularProgressDrawable leftRingDrawable =  new CircularProgressDrawable.Builder()
                .setRingWidth(leftLineWidth)
                .setOutlineColor(mWayRingColor)
                .setRingColor(leftRingColor)
                .create();

//        leftRingDrawable.set
//        leftRingDrawable.setProgress();
        mLeftPercentageCircle.setImageDrawable(leftRingDrawable);

        CircularProgressDrawable rightRingDrawable =  new CircularProgressDrawable.Builder()
                .setRingWidth(rightLineWidth)
                .setOutlineColor(mWayRingColor)
                .setRingColor(rightRingColor)
                .create();

        mRightPercentageCircle.setImageDrawable(rightRingDrawable);

        if (animate) {
            AnimatorSet animation = new AnimatorSet();

            ObjectAnimator leftProgressAnimation = ObjectAnimator.ofFloat(leftRingDrawable, CircularProgressDrawable.PROGRESS_PROPERTY,
                    0f, leftConvertedToFloatValue);
            leftProgressAnimation.setDuration(mAnimationDuration);
            leftProgressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

            ObjectAnimator rightProgressAnimation = ObjectAnimator.ofFloat(rightRingDrawable, CircularProgressDrawable.PROGRESS_PROPERTY,
                    0f, rightConvertedToFloatValue);
            rightProgressAnimation.setDuration(mAnimationDuration);
            leftProgressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

            animation.playTogether(leftProgressAnimation, rightProgressAnimation);

            if (mChosenSide != ChoiceSide.None) {
                Animation diamondAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.bounce);
                mDiamond.startAnimation(diamondAnimation);
            }
            Animation textAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.bounce_text);
            mLeftText.startAnimation(textAnimation);
            mRightText.startAnimation(textAnimation);
            animation.start();
        }else{
            leftRingDrawable.setProgress(leftConvertedToFloatValue);
            rightRingDrawable.setProgress(rightConvertedToFloatValue);
        }

    }

}
