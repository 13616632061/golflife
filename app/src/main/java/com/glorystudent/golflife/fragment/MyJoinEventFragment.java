package com.glorystudent.golflife.fragment;

import android.widget.RelativeLayout;

import com.glorystudent.golflibrary.base.BaseFragment;
import com.glorystudent.golflibrary.widget.oywidget.MyListView;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.MyJoinEventListAdapter;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.entity.MyJoinEventEntity;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Gavin.J on 2017/10/27.
 */

public class MyJoinEventFragment extends BaseFragment {
    private static final String TAG = "MyJoinEventFragment";
    @Bind(R.id.join_event_lv)
    MyListView joinEventLv;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    @Bind(R.id.no_event)
    RelativeLayout noEvent;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private MyJoinEventListAdapter myJoinEventListAdapter;
    private int page = 1;
    private List<MyJoinEventEntity.ListsignupBean> datas;
    @Override
    protected int getContentId() {
        return R.layout.fragment_my_join_event;
    }
}
