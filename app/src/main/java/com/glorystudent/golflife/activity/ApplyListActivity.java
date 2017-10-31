package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.ApplyListAdapter;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.customView.PullableListView;
import com.glorystudent.golflife.entity.ApplyListEntity;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 报名名单列表页面
 */
public class ApplyListActivity extends BaseActivity {

    private static final String TAG = "ApplyListActivity";
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_sweep_sign_up)
    TextView sweepSignUp;
    @Bind(R.id.lv_apply_listView)
    PullableListView listView;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private ApplyListAdapter applyListAdapter;//报名名单适配器
    private int eventactivity_id;
    private String eventactivity_name;
    private int page = 1;//页码
    private List<ApplyListEntity.ListsignupBean> datas;
    private View henderView;//头布局

    @Override
    protected int getContentId() {
        return R.layout.activity_apply_list;
    }

    @Override
    protected void init() {
        refreshView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新回调
                isRefresh = true;
                page = 1;
                loadDatas();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载回调
                isRefresh = false;
                page++;
                loadDatas();
            }
        });
        showLoading();
        Intent intent = getIntent();
        eventactivity_id = intent.getIntExtra("id", -1);
        eventactivity_name = intent.getStringExtra("name");
        applyListAdapter = new ApplyListAdapter(this);
        listView.setAdapter(applyListAdapter);
        henderView = View.inflate(this, R.layout.item_apply_list_no_event, null);
    }

    @Override
    protected void loadDatas() {
        String requestJson = RequestAPI.QuerySignUpByEventID(ApplyListActivity.this,eventactivity_id+"",page+"");
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
                        //有数据则正常显示
                        listView.removeHeaderView(henderView);
                        ApplyListEntity applyListEntity = new Gson().fromJson(jo.toString(), ApplyListEntity.class);
                        datas = applyListEntity.getListsignup();
                        if (datas != null) {
                            if (isRefresh) {
                                applyListAdapter.setDatas(datas);
                            } else {
                                applyListAdapter.addDatas(datas);
                            }
                        }
                    } else if (statuscode.equals("2")) {//暂无数据
                        Log.i(TAG, "parseDatas: " + statusmessage);
                        refreshView.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                        if (datas == null) {
                            //没有数据显示noEvent
                            listView.removeHeaderView(henderView);
                            listView.addHeaderView(henderView, null, true);
                            listView.setHeaderDividersEnabled(false);
                        }
                    } else {//失败
                        refreshView.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoading();
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                refreshView.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                Toast.makeText(ApplyListActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                //访问失败则当前页需要重新加载
                page--;
            }
        }).getEntityData(this, ConstantsURL.QuerySignUpByEventID, requestJson);
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_sweep_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_sweep_sign_up:
                startEventSignUp();
                break;
        }
    }
    /**
     * 活动签到
     */

    private void startEventSignUp() {
        Intent intent = new Intent(this, EventSignUpActivity.class);
        intent.putExtra("id", eventactivity_id);
        intent.putExtra("name", eventactivity_name);
        startActivity(intent);
    }
}
