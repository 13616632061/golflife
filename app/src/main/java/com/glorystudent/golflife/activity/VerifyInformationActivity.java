package com.glorystudent.golflife.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.entity.ApplyFriendsEntity;
import com.glorystudent.golflife.util.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 验证信息页面
 */
public class VerifyInformationActivity extends BaseActivity {


    private final static String TAG = "VerifyInfoActivity";
    @Bind(R.id.et_hello)
    public EditText etHello;

    private int userid;//用户id
    private String applyType;//申请类型
    @Override
    protected int getContentId() {
        return R.layout.activity_verify_information;
    }
    @Override
    protected void init() {
        Intent intent = getIntent();
        userid = intent.getIntExtra("userid", -1);
        applyType = intent.getStringExtra("applyType");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(etHello, 0);
                }
            }
        }, 200);
    }

    /**
     * TODO 点击事件处理
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_send})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.tv_send:
                //发送
                sendApplyFriend();
                break;
        }
    }

    /**
     * TODO 发送添加好友
     */
    public void sendApplyFriend() {
        showLoading();
        String text = etHello.getText().toString().trim();
        ApplyFriendsEntity applyFriendsEntity = new ApplyFriendsEntity();
        ApplyFriendsEntity.ApplyFriendsBean applyFriendsBean = new ApplyFriendsEntity.ApplyFriendsBean();
        applyFriendsBean.setApplyfriends_type(applyType);
        applyFriendsBean.setAnsweruserid(userid);
        applyFriendsBean.setApplyremark(text);
        applyFriendsEntity.setApplyfriends(applyFriendsBean);
        String json = new Gson().toJson(applyFriendsEntity);
        String requestJson = RequestAPI.getRequestJson(this, json);
        Log.i(TAG, "sendApplyFriend: " + requestJson);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {
                        Toast.makeText(VerifyInformationActivity.this, "添加好友成功，请等待对方同意", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(VerifyInformationActivity.this, "添加失败，错误码:" + statusmessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoading();
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(VerifyInformationActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        }).getEntityData(this, ConstantsURL.AddApplyFriends, requestJson);
    }
}
