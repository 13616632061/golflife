package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lazylibrary.util.ToastUtils;
import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.widget.oywidget.MyListView;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.TransactionListAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.entity.ResponseQueryBalanceEntity;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * TODO 我的钱包页面
 */
public class MyWalletActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_wallet_take_out)
    TextView takeOut;
    @Bind(R.id.list_view)
    MyListView listView;
    @Bind(R.id.tv_no_event)
    TextView noEvent;
    @Bind(R.id.tv_wallet_amount)
    TextView tvWalletAmount;
    @Bind(R.id.tv_wallet_balance)
    TextView tvWalletBalance;
    @Bind(R.id.tv_wallet_no_balance)
    TextView tvWalletNoBalance;
    private List<Map<String, Object>> list;//测试假数据
    private TransactionListAdapter transactionListAdapter;

    @Override
    protected int getContentId() {
        return R.layout.activity_my_wallet;
    }
    @Override
    protected void init() {
        initValue();
        transactionListAdapter = new TransactionListAdapter(this);
        listView.setAdapter(transactionListAdapter);
        transactionListAdapter.setDatas(list);
    }

    private void initValue() {
        list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "丰顺高尔夫夏季赛");
        map1.put("time", "2017-05-15");
        map1.put("money", 100.00);
        map1.put("state", 1);
        list.add(map1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "提现");
        map2.put("time", "2017-05-12");
        map2.put("money", 100.00);
        map2.put("state", 0);
        list.add(map2);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "丰顺高尔夫夏季赛");
        map3.put("time", "2017-05-16");
        map3.put("money", 2100.00);
        map3.put("state", 1);
        list.add(map3);
    }

    @Override
    protected void loadDatas() {
        requestBalance();
    }

    /**
     * TODO 获取余额
     */
    private void requestBalance() {
        OkGo.<String>post(ConstantsURL.GetUserApplyCashInfo)
                .params("request", RequestAPI.getJson(this,""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        System.out.println("获取余额："+s);
                        if(!TextUtils.isEmpty(s)){
                            ResponseQueryBalanceEntity queryBalance=new Gson().fromJson(s,ResponseQueryBalanceEntity.class);
                            if(queryBalance.getStatuscode()==1){
                                tvWalletAmount.setText(queryBalance.getTotalAmount()+"");
                                tvWalletNoBalance.setText("冻结金额:"+queryBalance.getFreezeTotalMoney()+"");
                                tvWalletBalance.setText(("可用金额:"+(queryBalance.getTotalAmount()-queryBalance.getFreezeTotalMoney()))+"");
                            }else {
                                ToastUtils.showToast(MyWalletActivity.this,queryBalance.getStatusmessage());
                            }
                        }
                    }
                });

    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_wallet_take_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_wallet_take_out:
                Intent intent = new Intent(this, WithdrawalsActivity.class);
                intent.putExtra("money", tvWalletAmount.getText().toString());
                startActivity(intent);
                break;
        }
    }

}
