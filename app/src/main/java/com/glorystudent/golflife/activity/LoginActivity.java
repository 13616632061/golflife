package com.glorystudent.golflife.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.PhoneFormatCheckUtils;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.ResponseSMSCodeEntity;
import com.glorystudent.golflife.entity.UserEntity;
import com.glorystudent.golflife.entity.UserRequestEntity;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.ConstantsURL;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.glorystudent.golflife.util.RequestUtil;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;
import okhttp3.Response;

import static com.glorystudent.golflife.R.id.cb_agree;
import static com.glorystudent.golflife.R.id.et_phone_number;

/**
 * TODO 登录页面
 */
public class LoginActivity extends BaseActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    @Bind(et_phone_number)
    EditText etPhoneNumber;
    @Bind(R.id.et_phone_code)
    EditText etPhoneCode;
    @Bind(R.id.tv_get_code)
    TextView tvGetCode;
    @Bind(cb_agree)
    CheckBox cbAgree;
    @Bind(R.id.service_terms)
    TextView serviceTerms;
    @Bind(R.id.btn_login)
    TextView btnLogin;
    @Bind(R.id.tv_chat_login)
    TextView tvChatLogin;

    private boolean isPhoneNumber = false;//false不是合格手机号码，true是合格手机号
    private final static int COUNTDOWN = 0x001; //获取验证码倒计时
    private final static int LOGIN_SUCCEED = 0x002;//微信登陆成功
    private final static int LOGIN_FAILURE = 0x003;//微信登陆失败
    private int time = 60;//验证码过期倒数60秒
    private boolean countDownFlag = true;
    private boolean isCheckAgree = false;//是否同意服务条款
    private UserEntity userEntity;//用户实体类
    private HashMap<String, Object> map;
    private String userid;//用户id
    private String openid;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNTDOWN:
                    //定时器
                    if (time <= 0) {
                        time = 60;
                        tvGetCode.setEnabled(true);
                        tvGetCode.setText(R.string.login_text3);
                        tvGetCode.setTextColor(getResources().getColor(R.color.colorOrange));
                    } else {
                        if (countDownFlag) {
                            tvGetCode.setEnabled(false);
                            tvGetCode.setText(time + "s重新获取");
                            tvGetCode.setTextColor(getResources().getColor(R.color.colorGray11));
                            time--;
                            handler.sendEmptyMessageDelayed(COUNTDOWN, 1000);
                        }
                    }
                    break;
                case LOGIN_SUCCEED:
                    //登陆成功
                    openid = (String) map.get("openid");
                    UserRequestEntity userRequestEntity = new UserRequestEntity();
                    UserRequestEntity.UserBean userBean = new UserRequestEntity.UserBean();
                    userBean.setOpenid(openid);
                    userRequestEntity.setUser(userBean);
                    String request = new Gson().toJson(userRequestEntity);
                    String requestJson = RequestUtil.getRequestJson(LoginActivity.this, request);
                    String url = "/Public/APIPublicUser/QueryUser";
                    OkGo.post(url)
                            .tag(this)
                            .params("request", requestJson)
                            .execute(new StringCallback() {
                                @Override
                                public void onBefore(BaseRequest request) {
                                    System.out.println("Params"+request.getParams());
                                    System.out.println("Url"+request.getUrl());
                                }

                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    try {
                                        JSONObject jo = new JSONObject(s);
                                        String statuscode = jo.getString("statuscode");
                                        String statusmessage = jo.getString("statusmessage");
                                        if (statuscode.equals("1")) {
//                                            UserInformationEntity userInformationEntity = new Gson().fromJson(jo.toString(), UserInformationEntity.class);
//                                            if (userInformationEntity != null) {
//                                                List<UserInformationEntity.ListUsersBean> listUsers = userInformationEntity.getListUsers();
//                                                if (listUsers != null) {
//                                                    saveUserListSharedPreferences(userInformationEntity);//保存参数
//                                                    loginUserListHuanxin(userInformationEntity);
//                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                                    finish();
//                                                } else {
//                                                    Intent intent = new Intent(LoginActivity.this, BindPhoneNumberActivity.class);
//                                                    intent.putExtra("openid", openid);
//                                                    startActivity(intent);
//                                                }
//                                            }
                                        } else if (statuscode.equals("2")) {
//                                            Intent intent = new Intent(LoginActivity.this, BindPhoneNumberActivity.class);
//                                            intent.putExtra("openid", openid);
//                                            startActivity(intent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                }
                            });
                    break;
                case LOGIN_FAILURE:
                    //登陆失败
                    Toast.makeText(LoginActivity.this, "微信登陆失败，请重新登陆", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        if(SharedUtil.getBoolean(Constants.LOGIN_STATE)){
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
        }
        etPhoneNumber.addTextChangedListener(this);
        cbAgree.setOnCheckedChangeListener(this);
        cbAgree.setChecked(true);
    }

    /**
     * TODO 点击事件的监听
     * @param view
     */
    @OnClick({R.id.tv_chat_login,R.id.tv_get_code, R.id.btn_login, R.id.service_terms})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.tv_chat_login:
                //微信登陆
                WechatLogin();
                break;
            case R.id.tv_get_code:
                //获取验证码
                get_code();
                break;
            case R.id.btn_login:
                //登录
                Login();
                break;
            case R.id.service_terms:
                //服务条款
                startActivity(new Intent(LoginActivity.this, ServiceTermsActivity.class));
                break;

        }
    }
    /**
     * TODO 监听输入手机号码
     * 监听输入手机号码
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
     * TODO 是否同意服务条款监听
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isCheckAgree = isChecked;
    }
    /**
     * TODO 获取手机验证码
     */
    private void get_code(){
        //获取手机验证码
        if (!isPhoneNumber) {
            Toast.makeText(LoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            //获取虚拟短信验证码接口
            handler.sendEmptyMessage(COUNTDOWN);
            String getCode = RequestUtil.getSMSCheck(LoginActivity.this,etPhoneNumber.getText().toString());
            OkGo.post(ConstantsURL.GetSMSCheck)
                    .tag(this)
                    .params("request", getCode)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            System.out.println("短信验证码:"+s);
                            if(!TextUtils.isEmpty(s)){
                                ResponseSMSCodeEntity responseSMSCodeEntity=new Gson().fromJson(s,ResponseSMSCodeEntity.class);
                                if(responseSMSCodeEntity.getStatuscode()==1){
//                                            et_phone_code.setText(responseSMSCodeEntity.getStatusmessage());
                                }else {
                                    Toast.makeText(LoginActivity.this, responseSMSCodeEntity.getStatusmessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Toast.makeText(LoginActivity.this, "请检查网络，无法连接服务器", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    /**
     * TODO 登录
     */
    private void Login(){
        //登陆
        if (etPhoneNumber.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if (etPhoneCode.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!isCheckAgree) {
            Toast.makeText(LoginActivity.this, "请同意服务条款", Toast.LENGTH_SHORT).show();
        } else {
            //进行注册登陆
            userEntity = new UserEntity();
            String json = RequestUtil.getLogin(LoginActivity.this, etPhoneNumber.getText().toString(),etPhoneCode.getText().toString());
            OkGo.post(ConstantsURL.SMSLogin)
                    .tag(this)//
                    .params("request", json)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                System.out.println("登录："+jo);
                                userEntity = new Gson().fromJson(jo.toString(), UserEntity.class);
                                if (userEntity.getStatuscode() == 1) {
                                    saveSharedPreferences(userEntity);//保存参数
                                    loginHuanxin();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "错误码:" + userEntity.getStatusmessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * TODO 保存用户信息
     * @param userEntity
     */
    private void saveSharedPreferences(UserEntity userEntity) {
        //保存参数
        SharedUtil.putBoolean(Constants.LOGIN_STATE, true);
        SharedUtil.putString(Constants.USER_ID, userEntity.getUserid());
        SharedUtil.putInt(Constants.NUMBER_ID, userEntity.getUserinfo().getUserid());
        SharedUtil.putString(Constants.GROUP_ID, userEntity.getGroupid());
        Log.d("login", "saveSharedPreferences: 1234--->" + userEntity.getGroupid());
        SharedUtil.putString(Constants.ACCESS_TOKEN, userEntity.getAccesstoken());
        SharedUtil.putString(Constants.PHONE_NUMBER, userEntity.getUserinfo().getPhonenumber());
        SharedUtil.putString(Constants.HEAD_PORTRAIT, (String) userEntity.getUserinfo().getCustomerphoto());
        SharedUtil.putString(Constants.NICKNAME, (String) userEntity.getUserinfo().getUsername());
        SharedUtil.putString(Constants.SEX, userEntity.getUserinfo().getGender());
        SharedUtil.putString(Constants.VETERAN, (int) userEntity.getUserinfo().getGolfage() + "");
        SharedUtil.putString(Constants.ADDRESS, (String) userEntity.getUserinfo().getChinacity_name());
        String usertype = userEntity.getUserinfo().getUsertype();
        SharedUtil.putString(Constants.USER_TYPE, usertype);
    }

    /**
     * TODO 环信登录
     */
    private void loginHuanxin() {
        String phonenumber = userEntity.getUserinfo().getPhonenumber();
        String password = Constants.EMClient_password;
        EMClient.getInstance().login(phonenumber, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }

    /**
     * TODo 微信登录
     */
    private void WechatLogin(){
        Log.d("print", "getEventBus: 点击了微信登陆");
        //微信登陆
        if (!isCheckAgree) {
            Toast.makeText(LoginActivity.this, "请同意服务条款", Toast.LENGTH_SHORT).show();
            return;
        }
        Platform wechat = ShareSDK.getPlatform(LoginActivity.this, Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                userid = platform.getDb().getUserId();
                map = hashMap;
                handler.sendEmptyMessage(LOGIN_SUCCEED);
            }
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                handler.sendEmptyMessage(LOGIN_FAILURE);
            }
            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        } else {
            //authorize与showUser单独调用一个即可
            wechat.authorize();//单独授权，OnComplete返回的hashmap是空的
//                    wechat.SSOSetting(true);//此处设置为false，则在优先采用客户端授权的方法，设置true会采用网页方式
            wechat.showUser(null);//授权并获取用户信息
            //isValid和removeAccount不开启线程，会直接返回。
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownFlag = false;
    }


}
