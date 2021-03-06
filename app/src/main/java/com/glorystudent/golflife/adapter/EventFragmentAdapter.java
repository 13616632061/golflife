package com.glorystudent.golflife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * TODO 赛事活动
 * Created by Gavin.J on 2017/10/31.
 */

public class EventFragmentAdapter extends FragmentPagerAdapter {
    private List<String> tabList;
    private List<Fragment> fragmentList;

    public EventFragmentAdapter(FragmentManager fm, List<String> tabList, List<Fragment> fragmentList) {
        super(fm);
        this.tabList = tabList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return tabList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }
}