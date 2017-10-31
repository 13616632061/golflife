package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.ChoosePitchListAdapter;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.entity.CourtLocationEntity;
import com.glorystudent.golflife.entity.SingleLocationEntity;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.RequestAPI;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * TODO 选择球场页面
 */
public class ChoosePitchActivity extends BaseActivity implements AdapterView.OnItemClickListener, TextWatcher {


    private static final String TAG = "ChoosePitchActivity";
    @Bind(R.id.lv_pitch)
    public ListView lv_pitch;

    private ChoosePitchListAdapter choosePitchListAdapter;//球场列表适配器
    private String longitude;
    private String latitude;
    private int page = 1;
    private int searchPage = 1;

    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;
    private boolean isRefresh = true;//true 是下拉刷新， false 是上拉加载

    @Bind(R.id.et_court)
    public EditText et_court;

    @Bind(R.id.tv_search)
    public TextView tv_search;

    private boolean isSearch = false;//是否搜索
    private boolean searchFlag = false; //true 为搜索  false 为取消
    private List<CourtLocationEntity.ListCourtBean> firstlistCourt;
    private TextView tv_near;
    @Override
    protected int getContentId() {
        return R.layout.activity_choose_pitch;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");
        Log.d("print", "longitude:" + longitude+"  latitude:"+latitude);
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新回调
                isRefresh = true;
                if (isSearch) {
                    searchPage = 1;
                    String text = et_court.getText().toString();
                    if (text != null && !text.isEmpty()) {
                        searchCourt(text);
                    }
                } else {
                    page = 1;
                    loadDatas();
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载回调
                isRefresh = false;
                if (isSearch) {
                    searchPage++;
                    String text = et_court.getText().toString();
                    if (text != null && !text.isEmpty()) {
                        searchCourt(text);
                    }
                } else {
                    page++;
                    loadDatas();
                }
            }
        });
        et_court.addTextChangedListener(this);
        firstlistCourt = new ArrayList<>();
        Log.d(TAG, "init: ----》" + longitude + " " + latitude);
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_choose_pitch_head, null);
        tv_near = (TextView) inflate.findViewById(R.id.tv_near);
        lv_pitch.addHeaderView(inflate);
        choosePitchListAdapter = new ChoosePitchListAdapter(this);
        lv_pitch.setAdapter(choosePitchListAdapter);
        lv_pitch.setOnItemClickListener(this);
    }

    @Override
    protected void loadDatas() {
        if (longitude != null && latitude != null) {
            showLoading();
            SingleLocationEntity singleLocationEntity = new SingleLocationEntity();
            SingleLocationEntity.CourtBean courtBean = new SingleLocationEntity.CourtBean();
            courtBean.setCourt_longitude(longitude);
            courtBean.setCourt_latitude(latitude);
            singleLocationEntity.setCourt(courtBean);
            singleLocationEntity.setPage(page);
            String request = new Gson().toJson(singleLocationEntity);
            String requestJson = RequestAPI.getRequestJson(this, request);
            Log.d("print", "searchCourt: --->" + requestJson);
            OkGo.post(ConstantsURL.QueryCourt)
                    .tag(this)
                    .params("request",requestJson)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String statuscode = jo.getString("statuscode");
                                String statusmessage = jo.getString("statusmessage");
                                if (statuscode.equals("1")) {
                                    refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                                    CourtLocationEntity courtLocationEntity = new Gson().fromJson(jo.toString(), CourtLocationEntity.class);
                                    firstlistCourt = courtLocationEntity.getListCourt();
                                    if (firstlistCourt != null && firstlistCourt.size() > 0) {
                                        if (isRefresh) {
                                            choosePitchListAdapter.setDatas(firstlistCourt);
                                        } else {
                                            choosePitchListAdapter.addDatas(firstlistCourt);
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
                            refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                            Toast.makeText(ChoosePitchActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * TODO 球场列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        choosePitchListAdapter.setChooseCourt(position - 1);
    }
    @OnClick({R.id.back, R.id.tv_search, R.id.sure})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.tv_search:
                //搜索
                if (searchFlag = !searchFlag) {
                    tv_near.setVisibility(View.GONE);
                    tv_search.setText("取消");
                    isSearch = true;
                    searchPage = 1;
//                lv_pitch.removeViewAt(0);
                    String text = et_court.getText().toString();
                    if (text != null && !text.isEmpty()) {
                        searchCourt(text);
                    } else {
                        Toast.makeText(ChoosePitchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    tv_near.setVisibility(View.VISIBLE);
                    tv_search.setText("搜索");
                    et_court.setText("");
                    choosePitchListAdapter.setChooseCourt(0);
                    choosePitchListAdapter.setDatas(firstlistCourt);
                }

                break;
            case R.id.sure:
                //确定
                CourtLocationEntity.ListCourtBean datas = choosePitchListAdapter.getDatas(choosePitchListAdapter.getChooseCourtPosition());
                Intent intent = new Intent();
                intent.putExtra("courtname", datas.getCourt_name());
                intent.putExtra("courtid", datas.getCourt_id());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
    /**
     * TODO 输入框变化监听
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

    }

    /**
     * TODO 搜索
     * @param courtname
     */
    private void searchCourt(String courtname) {
        SingleLocationEntity singleLocationEntity = new SingleLocationEntity();
        SingleLocationEntity.CourtBean courtBean = new SingleLocationEntity.CourtBean();
        courtBean.setCourt_name(courtname);
        singleLocationEntity.setCourt(courtBean);
        singleLocationEntity.setPage(searchPage);
        String request = new Gson().toJson(singleLocationEntity);
        String requestJson = RequestAPI.getRequestJson(this, request);
        Log.d("print", "searchCourt: --->" + requestJson);
        OkGo.post(ConstantsURL.QueryCourt)
                .tag(this)
                .params("request",requestJson)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            Log.d("print", "parseDatas: " + s);
                            JSONObject jo = new JSONObject(s);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if (statuscode.equals("1")) {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                                CourtLocationEntity courtLocationEntity = new Gson().fromJson(jo.toString(), CourtLocationEntity.class);
                                List<CourtLocationEntity.ListCourtBean> listCourt = courtLocationEntity.getListCourt();
                                if (listCourt != null && listCourt.size() > 0) {
                                    if (isRefresh) {
                                        choosePitchListAdapter.setChooseCourt(0);
                                        choosePitchListAdapter.setDatas(listCourt);
                                    } else {
                                        choosePitchListAdapter.addDatas(listCourt);
                                    }
                                }
                            } else if (statuscode.equals("2")) {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                            } else {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                    }
                });
    }
}
