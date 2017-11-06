package com.glorystudent.golflife.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lazylibrary.util.ToastUtils;
import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.entity.BaseRequestEntity;
import com.glorystudent.golflife.entity.BaseResponseEntity;
import com.glorystudent.golflife.entity.RequestBlankAccountAddEntity;
import com.glorystudent.golflife.util.Constants;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 *TODO  添加账户信息
 */
public class AddAccountActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.et_account_input)
    EditText accountInput;
    @Bind(R.id.et_name_input)
    EditText nameInput;
    @Bind(R.id.tv_complete)
    TextView tvComplete;
    @Bind(R.id.account_add_ischeck)
    CheckBox accountAddIscheck;
    @Bind(R.id.account_add_ischeckTV)
    TextView accountAddIscheckTV;
    @Override
    protected int getContentId() {
        return R.layout.activity_add_account;
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_complete,R.id.account_add_ischeckTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_complete:
                //完成添加
                if (!acquireData()) {
                    return;
                }
                finish();
                break;
            case R.id.account_add_ischeckTV://选中默认地址
                accountAddIscheck.setChecked(!accountAddIscheck.isChecked());
                if(accountAddIscheck.isChecked()){
                    accountAddIscheckTV.setTextColor(ContextCompat.getColor(this,R.color.colorOrange));
                }else {
                    accountAddIscheckTV.setTextColor(ContextCompat.getColor(this,R.color.colorGray14));
                }
                break;
        }
    }

    /**
     * TODO 默认账号的选择变化监听
     * @param compoundButton
     * @param isCheck
     */
    @OnCheckedChanged(R.id.account_add_ischeck)
    void onCheckBank(android.widget.CompoundButton compoundButton,boolean isCheck){
        if(isCheck){
            accountAddIscheckTV.setTextColor(ContextCompat.getColor(this,R.color.colorOrange));
        }else {
            accountAddIscheckTV.setTextColor(ContextCompat.getColor(this,R.color.colorGray14));
        }
    }

    /**
     * 获取输入数据
     */
    private boolean acquireData() {
        String accountStr = accountInput.getText().toString().trim();
        if (accountStr.isEmpty()) {
            Toast.makeText(this, "账户不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        String nameStr = nameInput.getText().toString().trim();
        if (nameStr.isEmpty()) {
            Toast.makeText(this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        requestAddBindAliPay(accountStr, nameStr, accountAddIscheck.isChecked(),1);

        return true;
    }

    /**
     * TODO 绑定支付宝账号
     *
     * @param accountNum  账户
     * @param accountName 姓名
     * @param isCheck     是否默认选择
     * @param bankType    银行类型
     */
    private void requestAddBindAliPay(String accountNum, String accountName, boolean isCheck, int bankType) {
        Map<String, Object> map = new BaseRequestEntity(this).getRequestMap();
        RequestBlankAccountAddEntity requestBlankAccountAddEntity = new RequestBlankAccountAddEntity(isCheck, bankType, accountNum, accountName);
        map.put("UserBank", requestBlankAccountAddEntity);
        String json = new Gson().toJson(map);
        OkGo.<String>post(ConstantsURL.AddUserBank)
                .params("request", json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!TextUtils.isEmpty(s)) {
                            BaseResponseEntity entity=new Gson().fromJson(s,BaseResponseEntity.class);
                            ToastUtils.showToast(AddAccountActivity.this,entity.getStatusmessage());
                            if(entity.getStatuscode()==1){
                                finish();
                            }
                        }
                    }
                });
    }
}
