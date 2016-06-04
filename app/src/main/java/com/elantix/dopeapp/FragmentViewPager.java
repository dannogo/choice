package com.elantix.dopeapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

/**
 * Created by oleh on 4/9/16.
 */
public class FragmentViewPager extends Fragment {

    private View mFragmentView;
    ViewPager mViewPager;
    private static int numItems;

    interface CommunicatorOne{
        public void respond();
    }

    public void showNextPage(){
        int desiredItemIndex = mViewPager.getCurrentItem()+1;
        if(mViewPager.getAdapter().getCount() > desiredItemIndex){
            mViewPager.setCurrentItem(desiredItemIndex);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_view_pager, container, false);

//        Arrays.fill(Utilities.sRateStateBackups, null);
        Bundle bundle = this.getArguments();
        numItems = bundle.getInt("num", 10);
        int itemPosition = bundle.getInt("position", 0);
        if (itemPosition == 0){
            if (Utilities.sFragmentHistory != null) {
                Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size() - 1).bundleData.put("position", 0);
            }
        }
        Log.w("FragmentViewPager", "itemPosition: "+itemPosition);
        Utilities.sRateStateBackups = new RateStateBackup[numItems];

        FragmentStatePagerAdapter adapter = new MyPagerAdapter(getActivity().getFragmentManager());
        mViewPager = (ViewPager) mFragmentView.findViewById(R.id.view_pager);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(itemPosition);



        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                ((MainActivity) getActivity()).toolbarTitle.setText((position + 1) + "/"+numItems);
                Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size() - 1).bundleData.put("position", position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return mFragmentView;
    }

    public static class MyPagerAdapter extends FragmentStatePagerAdapter {
//        private static int NUM_ITEMS = 10;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return numItems;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return FragmentDailyDope.newInstance(position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }



}
