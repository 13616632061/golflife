package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.CollectListAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.customView.PullableNoUpListView;
import com.glorystudent.golflife.entity.CollectRequestEntity;
import com.glorystudent.golflife.entity.CollectVideoEntity;
import com.glorystudent.golflife.entity.VideoEntity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 我的收藏页面
 */
public class MyCollectActivity extends BaseActivity implements AdapterView.OnItemClickListener, OkGoRequest.OnOkGoUtilListener {

    private final static String TAG = "MyCollectActivity";
    @Bind(R.id.ll_empty)
    public LinearLayout ll_empty;
    @Bind(R.id.teach_video_lv)
    public PullableNoUpListView teach_video_lv;
    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;

    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private CollectListAdapter collectListAdapter;


    @Override
    protected int getContentId() {
        return R.layout.activity_my_collect;
    }
    @Override
    protected void init() {
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新回调
                isRefresh = true;
                loadDatas();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载回调
                isRefresh = false;
            }
        });
        showLoading();
        collectListAdapter = new CollectListAdapter(this);
        teach_video_lv.setAdapter(collectListAdapter);
        teach_video_lv.setOnItemClickListener(this);
    }

    @Override
    protected void loadDatas() {
        ll_empty.setVisibility(View.GONE);
        CollectRequestEntity collectRequestEntity = new CollectRequestEntity();
        CollectRequestEntity.CollectBean collectBean = new CollectRequestEntity.CollectBean();
        collectBean.setCollecttype(5);
        collectRequestEntity.setCollect(collectBean);
        ;
        String request = new Gson().toJson(collectRequestEntity);
        String requestJson = RequestAPI.getRequestJson(this, request);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(this).getEntityData(this, ConstantsURL.QueryCollect, requestJson);
    }

    /**
     * todo 点击事件监听
     * @param view
     */
    @OnClick({R.id.back})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
        }
    }

    /**
     * todo 获取收藏数据返回
     * @param json
     */
    @Override
    public void parseDatas(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            String statuscode = jo.getString("statuscode");
            String statusmessage = jo.getString("statusmessage");
            if (statuscode.equals("1")) {
                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                CollectVideoEntity collectVideoEntity = new Gson().fromJson(jo.toString(), CollectVideoEntity.class);
                if (collectVideoEntity != null) {
                    List<CollectVideoEntity.ListCollectBean> listCollect = collectVideoEntity.getListCollect();
                    collectListAdapter.setDatas(listCollect);
                }
            } else if (statuscode.equals("2")) {
                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                ll_empty.setVisibility(View.VISIBLE);
            } else {
                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dismissLoading();
    }

    @Override
    public void requestFailed() {
        dismissLoading();
        Toast.makeText(MyCollectActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
    }

    /**
     *TODO 收藏列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CollectVideoEntity.ListCollectBean adapterDatas = collectListAdapter.getDatas(position);
        VideoEntity.ListTeachingVideoBean datas = new VideoEntity.ListTeachingVideoBean();
        datas.setTeachingvideo_id(adapterDatas.get_CollectObjectID());
        datas.setTeachingvideo_tittle(adapterDatas.getCollecttitle());
        datas.setTeachingvideo_picture(adapterDatas.getCollectpicurl());
        datas.setTeachingvideo_context(adapterDatas.getCollecttag());
        datas.setTeachingvideo_path(adapterDatas.getCollecturl());
        Intent intent = new Intent(this, VideoDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("video", datas);
        bundle.putInt("collectid", adapterDatas.get_CollectObjectID());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
