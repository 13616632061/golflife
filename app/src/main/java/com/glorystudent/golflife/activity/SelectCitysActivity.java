package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.CitysListAdapter;
import com.glorystudent.golflife.entity.CitysEntity;
import com.glorystudent.golflife.util.Constants;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 选择市
 */
public class SelectCitysActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final static String TAG = "SelectCitysActivity";
    @Bind(R.id.lv_citys)
    public ListView lv_citys;
    private CitysListAdapter citysListAdapter;
    private List<CitysEntity.ListchinacityBean> citysData;
    private String provinces;
    private int provincesid;

    @Override
    protected int getContentId() {
        return R.layout.activity_select_citys;
    }
    @Override
    protected void init() {
        Intent intent = getIntent();
        provinces = intent.getStringExtra("provincesname");
        provincesid = intent.getIntExtra("provincesid", -1);
        View head = LayoutInflater.from(this).inflate(R.layout.item_city_head2, null);
        lv_citys.addHeaderView(head);
        citysListAdapter = new CitysListAdapter(this);
        lv_citys.setAdapter(citysListAdapter);
        lv_citys.setOnItemClickListener(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getList(List<CitysEntity.ListchinacityBean> datas){
        citysData = datas;
    }

    @Override
    protected void loadDatas() {
        List<CitysEntity.ListchinacityBean> datas = new ArrayList<>();
        for (int i = 0; i < citysData.size(); i++) {
            if (citysData.get(i).getPid() == provincesid) {
                datas.add(citysData.get(i));
            }
        }
        citysListAdapter.setDatas(datas);
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
     * TODO 城市列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CitysEntity.ListchinacityBean adapterDatas = citysListAdapter.getDatas(position - 1);
        Intent intent = new Intent(this, SelectCountysActivity.class);
        intent.putExtra("provincesname", provinces);
        intent.putExtra("cityname", adapterDatas.getName());
        intent.putExtra("cityid", adapterDatas.getCid());
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 90)
    public void closeActivity(String str){
        if(str.equals(Constants.CLOSE)){
            finish();
        }
    }
}
