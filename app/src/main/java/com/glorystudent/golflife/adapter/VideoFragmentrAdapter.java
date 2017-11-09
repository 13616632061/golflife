package com.glorystudent.golflife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.glorystudent.golflife.entity.TeachingVideoCategoryEntity;
import com.glorystudent.golflife.fragment.TeachVideoFragment;

import java.util.List;

/**
 * TODO 视频模块Fragment设配器
 * Created by Gavin.J on 2017/11/9.
 */

public class VideoFragmentrAdapter extends FragmentPagerAdapter {
    private List<TeachingVideoCategoryEntity.TeachingVideoCategory> datas;
    public VideoFragmentrAdapter(FragmentManager fm, List<TeachingVideoCategoryEntity.TeachingVideoCategory> datas) {
        super(fm);
        this.datas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        return TeachVideoFragment.getInstance(datas.get(position).getId()+"");
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return datas.get(position).getName().toString();//页卡标题
    }

}
