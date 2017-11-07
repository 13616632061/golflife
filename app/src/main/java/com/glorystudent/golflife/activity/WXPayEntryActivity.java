package com.glorystudent.golflife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * TODO 微信支付
 * Created by Gavin.J on 2017/11/7.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String orderId = SharedUtil.getString(Constants.ORDER_ID);
            String requestJson = RequestAPI.QueryWXPay(this,orderId);
            Log.i(TAG, "onResp: " + requestJson);
            OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
                @Override
                public void parseDatas(String json) {
                    try {
                        JSONObject jo = new JSONObject(json);
                        String statuscode = jo.getString("statuscode");
                        String statusmessage = jo.getString("statusmessage");
                        if (statuscode.equals("1")) {
                            if (statusmessage.equals("支付成功")) {
                                EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventDetailActivity", "success"));
                                EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventDetail3Activity", "success"));
                            } else if (statusmessage.equals("订单待支付")) {
                                EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventDetailActivity", "failure"));
                                EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventDetail3Activity", "failure"));
                            } else if (statusmessage.equals("订单信息不存在")) {
                                EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventDetailActivity", "failure"));
                                EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventDetail3Activity", "failure"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void requestFailed() {
                    Toast.makeText(WXPayEntryActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventDetailActivity", "failure"));
                    EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventDetail3Activity", "failure"));
                }
            }).getEntityData(this, ConstantsURL.QueryWXPay, requestJson);
            finish();
        }
    }
}
