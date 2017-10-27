package com.glorystudent.golflife.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.fragment.MyJoinEventFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MyEventActivity extends BaseActivity {

    @Bind(R.id.event_tab)
    TabLayout eventTab;
    @Bind(R.id.event_vp)
    ViewPager eventVp;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_release_event)
    TextView tvReleaseEvent;
    private List<String> tabList;
    private List<Fragment> fragmentList;
    @Override
    protected int getContentId() {
        return R.layout.activity_my_event;
    }

    @Override
    protected void init() {
        tabList = new ArrayList<>();
        tabList.add("我参加的");
        tabList.add("我发布的");
        eventTab.addTab(eventTab.newTab().setText(tabList.get(0)));
        eventTab.addTab(eventTab.newTab().setText(tabList.get(1)));
        fragmentList = new ArrayList<>();
        fragmentList.add(new MyJoinEventFragment());
//        fragmentList.add(new MyReleasedEventFragment());
//        EventFragmentAdapter eventFragmentAdapter = new EventFragmentAdapter(this.getSupportFragmentManager(), tabList, fragmentList);
//        eventVp.setAdapter(eventFragmentAdapter);
//        eventVp.setOffscreenPageLimit(1);
//        eventTab.setupWithViewPager(eventVp);
//        eventTab.setTabsFromPagerAdapter(eventFragmentAdapter);
    }
}
