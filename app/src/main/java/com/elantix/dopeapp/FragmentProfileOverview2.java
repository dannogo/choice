package com.elantix.dopeapp;

        import android.app.Activity;
        import android.app.Fragment;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.mikhaellopez.circularimageview.CircularImageView;

        import java.io.File;

        import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 7/18/16.
 */
public class FragmentProfileOverview2 extends Fragment implements View.OnClickListener, View.OnLongClickListener{

    private View mFragmentView;
    private CircularImageView mAvatar;
    private ImageView mAvatarPlaceHolder;
    private FancyButton mFollowButton;
//    private FancyButton mShareProfileButton;
    private LinearLayout mDopesInfo;
    private LinearLayout mFollowersInfo;
    private LinearLayout mFollowingsInfo;
    private Boolean mIsOwn;
    private String mUid;
    private boolean mIsFollowed = false;
    private TextView mUsername;
    private TextView mDescription;
    private LinearLayout mNoDopesLayout;
    private FancyButton mNoDopesButton;
    private RecyclerView mRecyclerView;
    private TextView mNumberOfDopesView;
    private String uid;
    private AdapterProfileOverview adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_profile_overview2, container, false);

        Utilities.sCurProfile = null;
        Bundle bundle = this.getArguments();
        mIsOwn = bundle.getBoolean("own");
        mUid = bundle.getString("uid");
        Boolean isRecovery = bundle.getBoolean("isRecovery", false);

        if (isRecovery) {
//            Utilities.sFragmentHistory.remove(Utilities.sFragmentHistory.size()-1);
        }


        mUsername = (TextView) mFragmentView.findViewById(R.id.profile_overview_user_name);
        mDescription = (TextView) mFragmentView.findViewById(R.id.profile_overview_user_description);
        mNoDopesLayout = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_there_are_no_dopes);
        mNumberOfDopesView = (TextView) mFragmentView.findViewById(R.id.profile_overview_number_of_dopes);
        mNoDopesButton = (FancyButton) mFragmentView.findViewById(R.id.profile_overview_there_are_no_dopes_button);
        mNoDopesButton.setOnClickListener(this);

        mAvatar = (CircularImageView) mFragmentView.findViewById(R.id.profile_overview_avatar);
        mAvatar.setOnLongClickListener(this);
        mAvatarPlaceHolder = (ImageView) mFragmentView.findViewById(R.id.profile_settings_user_icon);
        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.profile_overview_posted_dopes_list);

        mFollowButton = (FancyButton) mFragmentView.findViewById(R.id.profile_overview_follow_button);
        mFollowButton.setOnClickListener(this);
        ImageView followButtonIconView = mFollowButton.getIconImageObject();
        if (mIsOwn){
            followButtonIconView.setImageResource(R.drawable.profile_edit_pencil_white);
            mFollowButton.getTextViewObject().setText(R.string.profile_settings_edit_profile_left_button_text);
            followButtonIconView.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        }else{
            followButtonIconView.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            mAvatar.setVisibility(View.VISIBLE);
        }

//        mShareProfileButton = (FancyButton) mFragmentView.findViewById(R.id.profile_overview_share_profile_button);
//        mShareProfileButton.getIconImageObject().setVisibility(View.GONE);
//        mShareProfileButton.setOnClickListener(this);

        mDopesInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_dopes_info);
        mFollowersInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_followers_info);
        mFollowingsInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_followings_info);
        mDopesInfo.setOnClickListener(this);
        mFollowersInfo.setOnClickListener(this);
        mFollowingsInfo.setOnClickListener(this);

        uid = (mIsOwn) ? Utilities.sUid : mUid;
//        HttpKit http = new HttpKit(getActivity());
//        http.getProfileInformation(uid, Utilities.sToken);
//        http.getUsersDopes(uid, Utilities.sToken, null, null);

        return mFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        HttpKit http = new HttpKit(getActivity());
        http.getProfileInformation(uid, Utilities.sToken);
        http.getUsersDopes(uid, Utilities.sToken, null, null);
    }

    public void drawUserDopesPanel(DopeInfo[] dopes){
        if (dopes != null && dopes.length > 0){
            mNoDopesLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            configurateRecyclerView(dopes);
        }else{
            if (mIsOwn){
                mNoDopesLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }else {
                mNoDopesLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Configurates recyclerView
     */
    private void configurateRecyclerView(DopeInfo[] dopes){
        mRecyclerView.setFocusable(false);

        adapter = new AdapterProfileOverview(getActivity(), dopes, mIsOwn);
        mRecyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    public void handleDopeDeleting(int dopePosition){
        Log.e("ProfileOverview", "DopeDeleting. Position: " + dopePosition);
        adapter.mDopes.remove(dopePosition);
        adapter.notifyItemRemoved(dopePosition);
        mNumberOfDopesView.setText(String.valueOf(--Utilities.sCurProfile.dopes));
    }

    public void setInfo(ProfileInfo info){

        Utilities.sCurProfile = info;
        if (info.id.equals(Utilities.sUid)){
            Utilities.sMyProfile = info;
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utilities.MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();
            editor.putString("uid", Utilities.sUid);
            editor.putString("token", Utilities.sToken);
            editor.putString("avatar", info.avatar);
            editor.putString("username", info.username);
            editor.putString("fullname", info.fullname);
            editor.putString("email", info.email);
            editor.commit();
        }

        mUsername.setText(info.username);
        mDescription.setText(info.bio);
        mNumberOfDopesView.setText(String.valueOf(info.dopes));
        String toolbarTitle = (!info.username.isEmpty()) ? info.username : info.fullname;
        ((MainActivity)getActivity()).toolbarTitle.setText(toolbarTitle);

        if(!mIsOwn) {
            if (Utilities.sMyFollowings != null) {
                for (int i = 0; i < Utilities.sMyFollowings.length; i++) {
                    if (Utilities.sMyFollowings[i].equals(info.id)) {
                        mIsFollowed = true;
                        mFollowButton.setText("Following");
                        mFollowButton.getIconImageObject().setVisibility(View.GONE);
                        break;
                    }
                }
            }

        }

        if (Utilities.sCurProfile.avatar == null || Utilities.sCurProfile.avatar.isEmpty()){
            mAvatarPlaceHolder.setVisibility(View.VISIBLE);
            mAvatar.setVisibility(View.GONE);
        }else{
            mAvatar.setVisibility(View.VISIBLE);
            if (Utilities.sCurProfile.avatar.startsWith("http")) {
                Glide.with(this).load(Uri.parse(Utilities.sCurProfile.avatar)).into(mAvatar);
            }else {
                Glide.with(this).load(new File(Utilities.sCurProfile.avatar).getPath()).into(mAvatar);
            }
        }

        TextView followersNum = (TextView) mFragmentView.findViewById(R.id.profile_overview_number_of_followERS);
        TextView followingsNum = (TextView) mFragmentView.findViewById(R.id.profile_overview_number_of_followINGS);
        followersNum.setText(String.valueOf(info.followers));
        followingsNum.setText(String.valueOf(info.followings));
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
//        if (id == mShareProfileButton.getId()){
//            Intent intent = new Intent(getActivity(), ShareProfileActivity.class);
//            intent.putExtra("own", mIsOwn);
//            getActivity().startActivity(intent);
//        }else if (id == mDopesInfo.getId()){
        if (id == mDopesInfo.getId()){

        }else if (id == mFollowersInfo.getId()){
            ((MainActivity)getActivity()).switchPageHandler(MainActivity.Page.ProfileFollowers);
        }else if (id == mFollowingsInfo.getId()){
            ((MainActivity)getActivity()).switchPageHandler(MainActivity.Page.ProfileFollowings);
        }else if(id == mFollowButton.getId()){
            if (mIsOwn){
                Intent i = new Intent(getActivity(), ProfileSettingsActivity.class);
                startActivityForResult(i, Utilities.EDIT_PROFILE);
            }else {
                HttpKit http = new HttpKit(getActivity());
                boolean isFollow;
                if (mIsFollowed){
                    mFollowButton.setText("Follow");
                    mFollowButton.getIconImageObject().setVisibility(View.VISIBLE);
                    isFollow = false;
                }else{
                    mFollowButton.setText("Following");
                    mFollowButton.getIconImageObject().setVisibility(View.GONE);
                    isFollow = true;
                }
                mIsFollowed = !mIsFollowed;

                http.followUnfollow(isFollow, Utilities.sCurProfile.id, Utilities.sToken);
            }
        }else if(id == mNoDopesButton.getId()){
            Intent intent = new Intent(getActivity(), TabPlusActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Utilities.EDIT_PROFILE) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                if (Utilities.sCurProfile.avatar != null) {
                    if (Utilities.sCurProfile.avatar.startsWith("http")) {
                        Glide.with(this).load(Uri.parse(Utilities.sCurProfile.avatar)).into(mAvatar);
                    }else {
                        Glide.with(this).load(new File(Utilities.sCurProfile.avatar).getPath()).into(mAvatar);
                    }
                    mAvatarPlaceHolder.setVisibility(View.GONE);
                    mAvatar.setVisibility(View.VISIBLE);
                }
                if (Utilities.sCurProfile.username != null && !Utilities.sCurProfile.username.isEmpty()) {
                    mUsername.setText(Utilities.sCurProfile.username);
                }
//                if (Utilities.sCurProfile.fullname != null && !Utilities.sCurProfile.fullname.isEmpty()) {
//                    mDescription.setText(Utilities.sCurProfile.fullname);
//                }
                    mDescription.setText(Utilities.sCurProfile.bio);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    @Override
    public boolean onLongClick(View v) {

        for (ChainLink chainLink : Utilities.sFragmentHistory){
            Log.e("ProfileOverview2", "fragment: "+chainLink.fragment);
        }

        Utilities.decorStyle = Utilities.DecorStyle.First;
        Toast.makeText(getActivity(), "Switch to OLD scool decor style", Toast.LENGTH_SHORT).show();
        ((MainActivity)getActivity()).switchPageHandler(Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size() - 1).fragment, true);

        return true;
    }
}

