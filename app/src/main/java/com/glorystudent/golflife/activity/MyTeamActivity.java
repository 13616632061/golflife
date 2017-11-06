package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.widget.oywidget.MyListView;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.MyTeamListAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.entity.TeamManagementEntity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 我的球队页面
 */
public class MyTeamActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MyTeamActivity";
    @Bind(R.id.list_view)
    MyListView listView;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    @Bind(R.id.ll_no_team)
    LinearLayout noTeamLayout;
    private boolean isRefresh = true;
    private MyTeamListAdapter myTeamListAdapter;

    @Override
    protected int getContentId() {
        return R.layout.activity_my_team;
    }

    @Override
    protected void init() {
        refreshView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新回调
                isRefresh = true;
                loadDatas();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载回调
            }
        });
        showLoading();
        myTeamListAdapter = new MyTeamListAdapter(this);
        listView.setAdapter(myTeamListAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void loadDatas() {
        String requestJson = RequestAPI.QueryTeam(this,1+"");
        System.out.println("我的球队requestJson："+requestJson);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                System.out.println("我的球队："+json);
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {//正常
                        refreshView.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                        TeamManagementEntity teamManagementEntity = new Gson().fromJson(jo.toString(), TeamManagementEntity.class);
                        if (teamManagementEntity != null) {
                            List<TeamManagementEntity.MyTeamListBean> myTeamList = teamManagementEntity.getMyTeamList();
                            if (myTeamList != null && myTeamList.size() > 0) {
                                noTeamLayout.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                myTeamListAdapter.setDatas(myTeamList);
                            } else {
                                noTeamLayout.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            }
                        }
                    } else if (statuscode.equals("2")) {//暂无数据
                        refreshView.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                        Log.i(TAG, "parseDatas: " + statusmessage);
                    } else {
                        refreshView.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                        Log.i(TAG, "parseDatas: " + statusmessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoading();
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(MyTeamActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                refreshView.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
            }
        }).getEntityData(this,ConstantsURL.QueryTeam, requestJson);
    }

    @OnClick(R.id.back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * TODO 球队列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Adapter adapter = parent.getAdapter();
        TeamManagementEntity.MyTeamListBean myTeamListBean = ((MyTeamListAdapter) adapter).getDatas(position);
        int teamId = myTeamListBean.getId();
        Intent intent = new Intent(this, TeamDetailActivity.class);
        intent.putExtra("id", teamId);
        startActivity(intent);
    }
}
