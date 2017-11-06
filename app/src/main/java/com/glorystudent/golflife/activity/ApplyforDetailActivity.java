package com.glorystudent.golflife.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lazylibrary.util.ToastUtils;
import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.dialog.iosdialog.AlertDialog;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.entity.BaseRequestEntity;
import com.glorystudent.golflife.entity.BaseResponseEntity;
import com.glorystudent.golflife.entity.RequestApplyforDetailQueryEntity;
import com.glorystudent.golflife.entity.RequestCancelApplyforEntity;
import com.glorystudent.golflife.entity.ResponseApplyforDetailQuery;
import com.glorystudent.golflife.entity.ResponseApplyforRecordQuery;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.TimeUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * TODO 提现详情
 */
public class ApplyforDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.actionbar)
    LinearLayout actionbar;
    @Bind(R.id.apply_topbar_noapply)
    TextView applyTopbarNoapply;
    @Bind(R.id.applyfor_detail_number)
    TextView applyforDetailNumber;
    @Bind(R.id.applyfor_detail_type)
    TextView applyforDetailType;
    @Bind(R.id.applyfor_detail_paymoney)
    TextView applyforDetailPaymoney;
    @Bind(R.id.applyfor_detail_pay_type)
    TextView applyforDetailPayType;
    @Bind(R.id.applyfor_detail_date)
    TextView applyforDetailDate;
    @Bind(R.id.applyfor_detail_balance)
    TextView applyforDetailBalance;
    @Bind(R.id.applyfor_detail_message)
    TextView applyforDetailMessage;

    private ResponseApplyforRecordQuery.ApplyCashInfosBean bean;

    @Override
    protected int getContentId() {
        return R.layout.activity_applyfor_detail;
    }
    @Override
    protected void init() {
        super.init();
        bean= (ResponseApplyforRecordQuery.ApplyCashInfosBean) getIntent().getSerializableExtra("data");

        applyforDetailNumber.setText(bean.getID()+"");
        applyforDetailType.setText("提现");
        applyforDetailPaymoney.setText(bean.getApplyMoney()+"");
        applyforDetailPayType.setText("支付宝");
        applyforDetailDate.setText(TimeUtil.getEventTime(bean.getApplyDate()));
        applyforDetailMessage.setText(bean.getRemark());

        if(bean.getApplyState()==1 || bean.getApplyState()==2){
            applyTopbarNoapply.setVisibility(View.VISIBLE);
        }
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back,R.id.apply_topbar_noapply})
    void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.apply_topbar_noapply:
                showDialog();
                break;
        }
    }


    /**
     * 取消申请
     */
    private void request(){
        Map<String,Object> map=new BaseRequestEntity(this).getRequestMap();
        map.put("ApplyCash",new RequestCancelApplyforEntity(bean.getID(),bean.getApplyState()));
        String json=new Gson().toJson(map);

        OkGo.<String>post(ConstantsURL.UpdateApplyCash)
                .params("request",json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(!TextUtils.isEmpty(s)){
                            BaseResponseEntity entity=new Gson().fromJson(s,BaseResponseEntity.class);
                            ToastUtils.showToast(ApplyforDetailActivity.this,entity.getStatusmessage());
                            if(entity.getStatuscode()==1){
                                request(bean.getID());

                            }
                        }
                    }
                });
    }




    private void showDialog(){
        AlertDialog dialog=new AlertDialog(this).builder()
                .setMsg("是否取消提现")
                .setCancelable(true)
                .setNegativeButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        request();
                    }
                })
                .setPositiveButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog.show();

    }


    /**
     * TODO 获取提现记录详情
     * @param applyforId
     */
    private void request(int applyforId){
        Map<String,Object> map=new BaseRequestEntity(this).getRequestMap();
        map.put("ApplyCash",new RequestApplyforDetailQueryEntity(applyforId));
        String json=new Gson().toJson(map);

        OkGo.<String>post(ConstantsURL.GetApplyCash)
                .params("request",json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(!TextUtils.isEmpty(s)){
                            ResponseApplyforDetailQuery entity=new Gson().fromJson(s,ResponseApplyforDetailQuery.class);
                            if(entity.getStatuscode()==1){
                                applyforDetailNumber.setText(entity.getApplyCash().getID());
                                applyforDetailType.setText("提现");
                                applyforDetailPaymoney.setText(entity.getApplyCash().getApplyMoney()+"");
                                applyforDetailPayType.setText("支付宝");
                                applyforDetailDate.setText(TimeUtil.getEventTime(entity.getApplyCash().getApplyDate()));
                                applyforDetailMessage.setText(entity.getApplyCash().getRemark());
                                if(bean.getApplyState()==1 || bean.getApplyState()==2){
                                    applyTopbarNoapply.setVisibility(View.VISIBLE);
                                }else {
                                    applyTopbarNoapply.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                });
    }
}
