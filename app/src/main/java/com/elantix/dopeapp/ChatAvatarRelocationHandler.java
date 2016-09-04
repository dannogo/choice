package com.elantix.dopeapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ChatAvatarRelocationHandler {

    private static String TAG;

    private Context context;
    private RelativeLayout root;
    ArrayList<String> users = new ArrayList<>();

    public ChatAvatarRelocationHandler(Context context, RelativeLayout root, ArrayList<String> users){
        this.context = context;
        this.root = root;
        this.root.removeAllViews();
        this.users = new ArrayList<>(users);
        TAG = getClass().getSimpleName();
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private ArrayList<int[]> calculateMargins(int quantity, int avatarSize){
        ArrayList<int[]> pairs = new ArrayList<>();
        int padding = context.getResources().getDimensionPixelSize(R.dimen.chat_avatars_container_padding);
//        int clearRootWidth = root.getWidth() - padding*2;
//        int clearRootHeight = root.getHeight() - padding*2;
        int distance = context.getResources().getDimensionPixelSize(R.dimen.chat_distance_between_avatar_rows);
        int leftMarginForLeftIcon = root.getWidth()/2/2 + padding - avatarSize/2;
        int leftMarginForRightIcon = root.getWidth()/2 + root.getWidth()/2/2 - padding - avatarSize/2;
        switch (quantity){
            case 1:
            {
                int leftMargin = root.getWidth()/2 - avatarSize/2;
                int topMargin = root.getHeight()/2 - avatarSize/2;
                int[] margins = {leftMargin, topMargin};
                pairs.add(margins);
            }
                break;
            case 2:
            {
                int topMargin = root.getHeight()/2 - avatarSize/2;
                int[] margins = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins);
                topMargin = root.getHeight()/2 - avatarSize/2;
                int[] margins2 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins2);
            }
                break;
            case 3:
            {
                int topMargin = root.getHeight()/2 - avatarSize - distance/2;
                int[] margins = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins);
                int[] margins2 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins2);
                topMargin = root.getHeight()/2 + distance/2;
                int[] margins3 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins3);
            }
                break;
            case 4:
            {
                int topMargin = root.getHeight()/2 - avatarSize - distance/2;
                int[] margins = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins);
                int[] margins2 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins2);
                topMargin = root.getHeight()/2 + distance/2;
                int[] margins3 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins3);
                int[] margins4 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins4);
            }
                break;
            case 5: {
                int topMargin = root.getHeight() / 2 - avatarSize / 2 - avatarSize - distance;
                int[] margins = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins);
                int[] margins2 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins2);
                topMargin = root.getHeight() / 2 - avatarSize / 2;
                int[] margins3 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins3);
                int[] margins4 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins4);
                topMargin = root.getHeight() / 2 + avatarSize / 2 + distance;
                int[] margins5 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins5);
            }
                break;
            case 6:
            {
                int topMargin = root.getHeight()/2 - avatarSize/2 - avatarSize - distance;
                int[] margins = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins);
                int[] margins2 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins2);
                topMargin = root.getHeight()/2 - avatarSize/2;
                int[] margins3 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins3);
                int[] margins4 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins4);
                topMargin = root.getHeight()/2 +avatarSize/2 + distance;
                int[] margins5 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins5);
                int[] margins6 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins6);
            }
                break;
            case 7:
            {
                distance /= 2;
                int topMargin = root.getHeight()/2 - avatarSize - distance/2 - distance - avatarSize;
                int[] margins = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins);
                int[] margins2 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins2);
                topMargin = root.getHeight()/2 - avatarSize - distance/2;
                int[] margins3 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins3);
                int[] margins4 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins4);
                topMargin = root.getHeight()/2 + distance/2;
                int[] margins5 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins5);
                int[] margins6 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins6);
                topMargin = root.getHeight()/2 + distance/2 + avatarSize + distance;
                int[] margins7 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins7);
            }
                break;
            default:
                distance /= 2;
                int topMargin = root.getHeight()/2 - avatarSize - distance/2 - distance - avatarSize;
                int[] margins = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins);
                int[] margins2 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins2);
                topMargin = root.getHeight()/2 - avatarSize - distance/2;
                int[] margins3 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins3);
                int[] margins4 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins4);
                topMargin = root.getHeight()/2 + distance/2;
                int[] margins5 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins5);
                int[] margins6 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins6);
                topMargin = root.getHeight()/2 + distance/2 + avatarSize + distance;
                int[] margins7 = {leftMarginForLeftIcon, topMargin};
                pairs.add(margins7);
                int[] margins8 = {leftMarginForRightIcon, topMargin};
                pairs.add(margins8);
        }
        return pairs;
    }

    public void showAvatars(){

        int size = context.getResources().getDimensionPixelSize(R.dimen.chat_vote_avatar_size);
        if (users.size() == 0) return;
        ArrayList<int[]> pairs = calculateMargins(users.size(), size);

        for (int i=0; i<pairs.size(); i++){
//            ImageView image = new ImageView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
//            Glide.with(context)
//                    .load(users.get(i))
//                    .bitmapTransform(new CropCircleTransformation(context))
//                    .into(image);
//            image.setLayoutParams(params);
//            root.addView(image);
//            setMargins(image, pairs.get(i)[0], pairs.get(i)[1], 0, 0);


//            RelativeLayout testCont = (RelativeLayout)findViewById(R.id.test_cont);
            View avatar = ((ChatActivity)context).getLayoutInflater().inflate(R.layout.chat_vote_avatar, null, false);
            CircularImageView avatarPicture = (CircularImageView) avatar.findViewById(R.id.chat_avatar_picture);

            Object avatarPathToInsert = (users.get(i) != null) ? users.get(i) : R.drawable.user_photo_placeholder;
            Glide.with(context)
                    .load(avatarPathToInsert)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(avatarPicture);
            root.addView(avatar);
            setMargins(avatar, pairs.get(i)[0], pairs.get(i)[1], 0, 0);
        }

    }
}
