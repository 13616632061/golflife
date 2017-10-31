package com.glorystudent.golflife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseFragment;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.activity.LookDetailActivity;
import com.glorystudent.golflife.adapter.MyReleasedEventListAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.MyListView;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.entity.CompetityRequestEntity;
import com.glorystudent.golflife.entity.EventCompetityEntity;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * TODO 我发布的赛事活动
 * Created by Gavin.J on 2017/10/30.
 */

public class MyReleasedEventFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MyReleasedEventFragment";
    @Bind(R.id.released_event_lv)
    MyListView releasedEventLv;
    @Bind(R.id.no_event)
    RelativeLayout noEvent;
    @Bind(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    //    @Bind(R.id.released_event_lv)
//    MyListView releasedEventLv;
//    @Bind(R.id.refresh_view)
//    PullToRefreshLayout refreshView;
//    @Bind(R.id.no_event)
//    RelativeLayout noEvent;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private MyReleasedEventListAdapter myReleasedEventListAdapter;
    private int page = 1;
    private List<EventCompetityEntity.ListeventactivityBean> datas;

    @Override
    protected int getContentId() {
        return R.layout.fragment_my_released_event;
    }

    @Override
    protected void init(View view) {
        EventBus.getDefault().register(this);
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
        myReleasedEventListAdapter = new MyReleasedEventListAdapter(getActivity());
        releasedEventLv.setAdapter(myReleasedEventListAdapter);
        releasedEventLv.setOnItemClickListener(this);
    }

    @Override
    protected void loadDatas() {
        CompetityRequestEntity competityRequestEntity = new CompetityRequestEntity();
        CompetityRequestEntity.EventactivityBean eventactivityBean = new CompetityRequestEntity.EventactivityBean();
        eventactivityBean.setEventactivity_publisherid(1);
        competityRequestEntity.setEventactivity(eventactivityBean);
        competityRequestEntity.setPage(page);
        String json = new Gson().toJson(competityRequestEntity);
        String requestJson = RequestAPI.getRequestJson(getActivity(), json);
        OkGo.post(ConstantsURL.QueryEventActivity)
                .tag(this)
                .params("request", requestJson)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jo = new JSONObject(s);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if (statuscode.equals("1")) {
                                refreshView.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                                //有数据则正常显示
                                releasedEventLv.setVisibility(View.VISIBLE);
                                noEvent.setVisibility(View.GONE);
                                EventCompetityEntity eventCompetityEntity = new Gson().fromJson(jo.toString(), EventCompetityEntity.class);
                                datas = eventCompetityEntity.getListeventactivity();
                                if (datas != null) {
                                    if (isRefresh) {
                                        myReleasedEventListAdapter.setDatas(datas);
                                    } else {
                                        myReleasedEventListAdapter.addDatas(datas);
                                    }
                                }
                            } else if (statuscode.equals("2")) {
                                Log.i(TAG, "parseDatas: " + statusmessage);
                                refreshView.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                                if (datas == null) {
                                    //没有数据显示noEvent
                                    releasedEventLv.setVisibility(View.GONE);
                                    noEvent.setVisibility(View.VISIBLE);
                                }
                            } else {
                                refreshView.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissLoading();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dismissLoading();
                        refreshView.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        //访问失败则当前页需要重新加载
                        page--;
                    }
                });
    }

    /**
     * TODO 赛事活动点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //赛事状态 -0 未开赛 1 进行中 2已结束 3 已删除 4 取消活动
        String eventactivity_state = datas.get(position).getEventactivity_state();
        if (eventactivity_state.equals("4")) {
            Toast.makeText(getActivity(), "活动已取消", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), LookDetailActivity.class);
            intent.putExtra("id", datas.get(position).getEventActivity_id());
            startActivity(intent);
        }
    }

    /**
     * TODO Event返回处理
     *
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("MyReleasedEventFragment")) {
            if (map.get("MyReleasedEventFragment").equals("refresh")) {
                Log.i(TAG, "getEvent: 获取到数据刷新");
                isRefresh = true;//刷新
                page = 1;
                loadDatas();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
