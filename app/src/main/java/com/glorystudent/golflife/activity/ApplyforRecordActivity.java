package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.lazylibrary.util.ToastUtils;
import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.ApplyforRecordAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.entity.BaseRequestEntity;
import com.glorystudent.golflife.entity.ItemMenuEntity;
import com.glorystudent.golflife.entity.RequestApplyforRecordQueryEntity;
import com.glorystudent.golflife.entity.ResponseApplyforRecordQuery;
import com.glorystudent.golflife.util.Constants;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 *TODO 提现记录
 */
public class ApplyforRecordActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.actionbar)
    LinearLayout actionbar;
    @Bind(R.id.applyfor_record_list)
    RecyclerView applyforRecordList;


    private ApplyforRecordAdapter adapter;
    private List<ItemMenuEntity<ResponseApplyforRecordQuery.ApplyCashInfosBean>> data=new ArrayList<>();


    @Override
    protected int getContentId() {
        return R.layout.activity_applyfor_record;
    }
    @Override
    protected void init() {
        super.init();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        applyforRecordList.setLayoutManager(linearLayoutManager);

        adapter=new ApplyforRecordAdapter(R.layout.item_applyfor_record,data);
        applyforRecordList.setAdapter(adapter);
        adapter.setOnItemClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back})
    void btnOnClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * TODO 获取提现记录
     */
    private void request(){
        data.clear();
        Map<String,Object> map=new BaseRequestEntity(this).getRequestMap();
        map.put("ApplyCash",new RequestApplyforRecordQueryEntity(0));
        String json=new Gson().toJson(map);

        OkGo.<String>post(ConstantsURL.QueryApplyCash)
                .params("request",json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        System.out.println("提现记录："+s);
                        if(!TextUtils.isEmpty(s)){
                            ResponseApplyforRecordQuery entity=new Gson().fromJson(s,ResponseApplyforRecordQuery.class);
                            if(entity.getStatuscode()==1){
                                for (ResponseApplyforRecordQuery.ApplyCashInfosBean bean:entity.getApplyCashInfos()){
                                    data.add(new ItemMenuEntity<ResponseApplyforRecordQuery.ApplyCashInfosBean>(bean));
                                }
                                adapter.notifyDataSetChanged();
                            }else {
                                ToastUtils.showToast(ApplyforRecordActivity.this,entity.getStatusmessage());
                            }
                        }
                    }
                });
    }

    /**
     * TODO 提现记录点击事件
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ResponseApplyforRecordQuery.ApplyCashInfosBean bean=data.get(position).getData();
        if(bean!=null){
            Intent intent=new Intent(this,ApplyforDetailActivity.class);
            intent.putExtra("data",bean);
            startActivity(intent);
        }
    }
}
