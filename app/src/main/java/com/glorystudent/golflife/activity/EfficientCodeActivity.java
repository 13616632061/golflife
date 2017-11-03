package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.PhoneFormatCheckUtils;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 有效验证码页面
 */
public class EfficientCodeActivity extends BaseActivity {

    private static final String TAG = "EfficientCodeActivity";
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.et_efficient_code)
    EditText efficientCode;
    @Bind(R.id.tv_sure)
    TextView sure;
    private int eventactivity_id;

    @Override
    protected int getContentId() {
        return R.layout.activity_efficient_code;
    }
    @Override
    protected void init() {
        Intent intent = getIntent();
        eventactivity_id = intent.getIntExtra("id", -1);
    }

    /**
     * TODO 点击事件处理
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_sure:
                String codeStr = efficientCode.getText().toString().trim();
                if (codeStr.isEmpty()) {
                    Toast.makeText(this, "有效码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (codeStr.length() == 11) {
                        //手机号
                        if (!PhoneFormatCheckUtils.isPhoneLegal(codeStr)) {
                            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                        } else {
                            signUp(codeStr,2);
                        }
                    } else {
                        //凭证号
                        signUp(codeStr,1);
                    }
                }
                break;
        }
    }

    /**
     * TODO 手机号 或凭证号签到
     * @param codeStr
     * @param type 1 凭证号 2手机号
     */
    private void signUp(String codeStr,int type) {
        showLoading();
        String requestJson="";
        if(type==1){
            requestJson = RequestAPI.SweepCodeSignUp(this,eventactivity_id+"",codeStr,1);
        }else if(type==2){
         requestJson = RequestAPI.SweepCodeSignUp(this,eventactivity_id+"",codeStr,2);
        }
        Log.i(TAG, "signUpByPhone: " + requestJson);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    Log.i(TAG, "signUpjo: " + jo);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {
                        dismissLoading();
                        Toast.makeText(EfficientCodeActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(EfficientCodeActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        }).getEntityData(this, ConstantsURL.SweepCodeSignUp, requestJson);
    }
}
