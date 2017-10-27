package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.EventListAdapter;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.customView.PullableListView;
import com.glorystudent.golflife.entity.CompetityRequestEntity;
import com.glorystudent.golflife.entity.EventCompetityEntity;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.ConstantsURL;
import com.glorystudent.golflife.util.DialogUtil;
import com.glorystudent.golflife.util.RequestUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class EventActivity extends BaseActivity implements AdapterView.OnItemClickListener, TextWatcher {
    private final static String TAG = "EventActivity";

    @Bind(R.id.competition_lv)
    public PullableListView competition_lv;
    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;

    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private int page = 1;
    private EditText search;//搜索
    private EventListAdapter eventListAdapter;//活动赛事适配器
    private CompetityRequestEntity competityRequestEntity;
    private List<EventCompetityEntity.ListeventactivityBean> datas;//赛事活动数据集合
    @Override
    protected int getContentId() {
        return R.layout.activity_event;
    }

    @Override
    protected void init() {
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
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
        if (competityRequestEntity == null) {
            competityRequestEntity = new CompetityRequestEntity();
            competityRequestEntity.setEventactivity(new CompetityRequestEntity.EventactivityBean());
        }
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_search_list, null);
        search = (EditText) inflate.findViewById(R.id.search_address);
        search.addTextChangedListener(this);
        competition_lv.addHeaderView(inflate);
        eventListAdapter = new EventListAdapter(this);
        competition_lv.setAdapter(eventListAdapter);
        competition_lv.setOnItemClickListener(this);
    }

    @Override
    protected void loadDatas() {
        String text = search.getText().toString().trim();
        requestData(text);
    }

    /**
     * TODO 赛事活动监听
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {//需要登陆
            EventCompetityEntity.ListeventactivityBean datas = eventListAdapter.getData(position - 1);
            int eventActivity_id = datas.getEventActivity_id();
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("id", eventActivity_id);
            intent.putExtra("name", datas.getEventactivity_name());
            intent.putExtra("detail", datas.getEventactivity_detail());
            String imgUrl;
            if (datas.getListeventpic() != null && datas.getListeventpic().size() != 0) {
                imgUrl = datas.getListeventpic().get(0).getEventactivity_picpath();
            } else {
                imgUrl = ConstantsURL.imgUrl;
            }
            intent.putExtra("imageUrl", imgUrl);
            startActivity(intent);
        } else {
            DialogUtil.GotoLoginDialog(EventActivity.this);
        }
    }

    /**
     * TODO 搜索输入框监听事件
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString().trim();
        isRefresh = true;
        page = 1;
        requestData(text);
    }

    /**
     * TODO 点击事件监听
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
     * 根据搜索内容请求数据
     *
     * @param text
     */
    private void requestData(String text) {
        competityRequestEntity.getEventactivity().setEventactivity_name(text);
        competityRequestEntity.setPage(page);
        String json = new Gson().toJson(competityRequestEntity);
        String requestJson = RequestUtil.getRequestJson(this, json);
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
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                                EventCompetityEntity eventCompetityEntity = new Gson().fromJson(jo.toString(), EventCompetityEntity.class);
                                datas = eventCompetityEntity.getListeventactivity();
                                if (datas != null) {
                                    if (isRefresh) {
                                        eventListAdapter.setDatas(datas);
                                    } else {
                                        eventListAdapter.addDatas(datas);
                                    }
                                }
                            } else if (statuscode.equals("2")) {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                            } else {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissLoading();
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dismissLoading();
                        Toast.makeText(EventActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                        //访问失败则当前页需要重新加载
                        page--;
                    }
                });
    }

    /**
     * TODO 接收Event返回事件处理
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("EventActivity")) {
            if (map.get("EventActivity").equals("refresh")) {
                Log.i(TAG, "getEvent: 是否接收到值");
                isRefresh = true;//刷新
                page = 1;
                loadDatas();
            }
        }
    }
}
