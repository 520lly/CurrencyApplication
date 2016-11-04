package com.bank.bgfi.bgfibank.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bank.bgfi.bgfibank.Fragments.ChartFragment;
import com.bank.bgfi.bgfibank.Fragments.HistoryFragment;
import com.bank.bgfi.bgfibank.Fragments.List_change_fragment;
import com.bank.bgfi.bgfibank.Fragments.TradeFragment;

/**
 * Created by ayach on 7/13/16.
 */
public class Tabs_Viewpager_Adapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public Tabs_Viewpager_Adapter (FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                List_change_fragment change = new List_change_fragment();
                return change;
            case 1:
                ChartFragment chart = new ChartFragment();
                return chart;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}
