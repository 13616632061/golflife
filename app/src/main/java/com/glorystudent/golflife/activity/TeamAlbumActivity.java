package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.widget.oywidget.MyGridView;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.TeamAlbumGridAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.entity.TeamAlbumEntity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 球队相册页面
 */
public class TeamAlbumActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "TeamAlbumActivity";
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    @Bind(R.id.gv_team_album_grid_view)
    MyGridView gridView;
    @Bind(R.id.ll_no_team_photo)
    LinearLayout noPhotoLayout;
    private int teamId;//球队id
    private boolean isRefresh = true;//是否下拉刷新
    private List<TeamAlbumEntity.EventactivityphotolistBean> datas;//相册信息
    private TeamAlbumGridAdapter teamAlbumGridAdapter;//相册适配器

    @Override
    protected int getContentId() {
        return R.layout.activity_team_album;
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
            }
        });
        Intent intent = getIntent();
        teamId = intent.getIntExtra("teamId", -1);
        teamAlbumGridAdapter = new TeamAlbumGridAdapter(this);
        gridView.setAdapter(teamAlbumGridAdapter);
        gridView.setOnItemClickListener(this);
    }

    /**
     * TODO 获取球队相册数据
     */
    @Override
    protected void loadDatas() {
        String requestJson = RequestAPI.QueryTeamEventActivityPhoto(this, teamId+"");
        Log.i(TAG, "loadDatas: " + requestJson);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {//正常
                        refreshView.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                        TeamAlbumEntity teamAlbumEntity = new Gson().fromJson(jo.toString(), TeamAlbumEntity.class);
                        datas = teamAlbumEntity.getEventactivityphotolist();
                        if (datas != null && datas.size() > 0) {
                            noPhotoLayout.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            teamAlbumGridAdapter.setDatas(datas);
                        } else {
                            noPhotoLayout.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);
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
                Toast.makeText(TeamAlbumActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                refreshView.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
            }
        }).getEntityData(this, ConstantsURL.QueryTeamEventActivityPhoto, requestJson);
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * TODO 相册点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, TeamAlbumDetailActivity.class);
        intent.putExtra("eventactivity_id", datas.get(position).getEventactivity_id());
        intent.putExtra("eventactivity_name", datas.get(position).getEventactivity_name());
        startActivity(intent);
    }
}
