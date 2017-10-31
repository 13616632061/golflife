package com.glorystudent.golflife.activity;


import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.PhoneFormatCheckUtils;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.UserEntity;
import com.glorystudent.golflife.entity.WxLoginRequestEntity;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.util.LoginUtil;
import com.glorystudent.golflife.api.RequestAPI;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * TODO 绑定手机号码
 */
public class BindPhoneNumberActivity extends BaseActivity implements TextWatcher {
    private final static String TAG = "LoginActivity";

    @Bind(R.id.et_phone_number)
    public EditText et_phone_number;

    @Bind(R.id.et_phone_code)
    public EditText et_phone_code;

    @Bind(R.id.tv_get_code)
    public TextView tv_get_code;

    private boolean isPhoneNumber = false;//false不是合格手机号码，true是合格手机号
    private UserEntity userEntity;//用户实体类
    private final static int COUNTDOWN = 0x001; //获取验证码倒计时
    private int time = 60;//验证码过期倒数60秒
    private boolean isCheckAgree = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNTDOWN:
                    if (time <= 0) {
                        time = 60;
                        tv_get_code.setEnabled(true);
                        tv_get_code.setText(R.string.login_text3);
                        GradientDrawable myGrad = (GradientDrawable) tv_get_code.getBackground();
                        myGrad.setStroke(2, getResources().getColor(R.color.colorOrange));
                        tv_get_code.setTextColor(getResources().getColor(R.color.colorOrange));
                    } else {
                        tv_get_code.setEnabled(false);
                        tv_get_code.setText(time + "s重新获取");
                        GradientDrawable myGrad = (GradientDrawable) tv_get_code.getBackground();
                        myGrad.setStroke(2, getResources().getColor(R.color.colorGray11));
                        tv_get_code.setTextColor(getResources().getColor(R.color.colorGray11));
                        time--;
                        handler.sendEmptyMessageDelayed(COUNTDOWN, 1000);
                    }
                    break;
            }
        }
    };
    private String openid;


    @Override
    protected int getContentId() {
        return R.layout.activity_bind_phone_number;
    }

    /**
     * TODO 初始化控件
     */
    @Override
    protected void init() {
        Intent intent = getIntent();
        openid = intent.getStringExtra("openid");

        //已经登录，则跳转主页
        if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        //手机号码输入的监听事件
        et_phone_number.addTextChangedListener(this);
    }

    /**
     * TODO 监听输入手机号码
     *
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
        isPhoneNumber = false;//重置状态
        String strPhone = s.toString();
        if (s.length() >= 8) {
            if (PhoneFormatCheckUtils.isChinaPhoneLegal(strPhone)) {
                isPhoneNumber = true;
            }
        }
    }

    /**
     * TODO 点击事件的监听
     * 点击事件的监听
     *
     * @param v
     */
    @OnClick({R.id.back, R.id.tv_get_code, R.id.btn_login})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.tv_get_code:
                //获取手机验证码
                getPhoneNumberCode();
                break;
            case R.id.btn_login:
                //登陆
                Login();
                break;
        }
    }

    /**
     * TODO 获取手机验证码
     */
    private void getPhoneNumberCode() {
        //获取手机验证码
        if (!isPhoneNumber) {
            Toast.makeText(BindPhoneNumberActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            //获取虚拟短信验证码接口
            handler.sendEmptyMessage(COUNTDOWN);
            String getCode = RequestAPI.getSMSCheck(this, et_phone_number.getText().toString());
            OkGo.post(ConstantsURL.GetSMSCheck)
                    .tag(this)
                    .params("request", getCode)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Toast.makeText(BindPhoneNumberActivity.this, "请检查网络，无法连接服务器", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * TODO 登录
     */
    private void Login(){
        //登陆
        if(et_phone_number.getText().toString().isEmpty()){
            Toast.makeText(BindPhoneNumberActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else if(et_phone_code.getText().toString().isEmpty()){
            Toast.makeText(BindPhoneNumberActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
        }else{
            //进行注册登陆
            WxLoginRequestEntity wxLoginRequestEntity = new WxLoginRequestEntity();
            WxLoginRequestEntity.WxLoginBean wxLoginBean = new WxLoginRequestEntity.WxLoginBean();
            wxLoginBean.setOpenid(openid);
            wxLoginRequestEntity.setWxlogin(wxLoginBean);
            wxLoginRequestEntity.setPhonenum(et_phone_number.getText().toString());
            wxLoginRequestEntity.setPhonencode(et_phone_code.getText().toString());
            String request = new Gson().toJson(wxLoginRequestEntity);
            String requestJson = RequestAPI.getRequestJson(this, request);
            OkGo.post(ConstantsURL.BindingWX)
                    .tag(this)
                    .params("request", requestJson)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                userEntity = new Gson().fromJson(jo.toString(), UserEntity.class);
                                if(userEntity.getStatuscode() == 1){
                                    //保存用户信息参数
                                    LoginUtil.saveSharedPreferences(userEntity.getUserid(),userEntity.getUserinfo().getUserid(),userEntity.getGroupid(),
                                            userEntity.getAccesstoken(),userEntity.getUserinfo().getPhonenumber(),(String) userEntity.getUserinfo().getCustomerphoto(),
                                            (String) userEntity.getUserinfo().getUsername(),userEntity.getUserinfo().getGender(),(int) userEntity.getUserinfo().getGolfage() + "",
                                            (String) userEntity.getUserinfo().getChinacity_name(),userEntity.getUserinfo().getUsertype());
                                    //环信登录
                                    LoginUtil.loginHuanxin(userEntity.getUserinfo().getPhonenumber());
                                    startActivity(new Intent(BindPhoneNumberActivity.this, MainActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(BindPhoneNumberActivity.this, "绑定失败，错误码:" + userEntity.getStatusmessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Toast.makeText(BindPhoneNumberActivity.this, "请检查网络，无法连接服务器", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
