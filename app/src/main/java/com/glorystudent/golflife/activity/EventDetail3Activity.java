package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.dialog.iosdialog.AlertDialog;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.WxPayOrderEntity;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.glorystudent.golflife.api.RequestAPI;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * TODO 活动详情，从我参加进入
 */

public class EventDetail3Activity extends BaseActivity {


    private static final String TAG = "EventDetail3Activity";
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.rl_bottom_state)
    RelativeLayout bottomState;
    @Bind(R.id.tv_bottom_state1)
    TextView tvState1;
    @Bind(R.id.tv_bottom_state2)
    TextView tvState2;
    private int id;
    private String weburl;
    private String state;//报名状态
    // 荣耀服务的url
    private static final String HONOR_SERVER_URL = "home/license";
    // 拨号url
    private static final String CALL_TEL_URL = "tel:";
    // 保存相册url
    private static final String SAVE_TO_PHOTO_ALBUMS_URL = "isAndroid:";
    private int orderId;
    @Override
    protected int getContentId() {
        return R.layout.activity_event_detail3;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        state = intent.getStringExtra("state");
        orderId = intent.getIntExtra("orderId", -1);
        updateUI();
    }

    @Override
    protected void loadDatas() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        //设置缓存，否则后台重定向返回后导致缓存丢失
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //android5.0以上不显示图片的解决办法
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(CALL_TEL_URL)) {
                    //拨打电话
                    CallTel(url);
                    return true;
                } else if (url.contains(HONOR_SERVER_URL)) {
                    //荣耀服务条款
                    startActivity(new Intent(EventDetail3Activity.this, ServiceTermsActivity.class));
                    return true;
                } else if (url.contains(SAVE_TO_PHOTO_ALBUMS_URL)) {
                    //保存到相册
                    Log.i(TAG, "shouldOverrideUrlLoading: " + url);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    pb.setVisibility(View.GONE);
                } else {
                    // 加载中
                    pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        if (id != -1) {
            weburl = String.format(ConstantsURL.EVENT_PREVIEW_URL, id);
            webView.loadUrl(weburl);
        }
    }

    /**
     * TODO 点击监听事件
     * @param view
     */
    @OnClick({R.id.back, R.id.rl_bottom_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                close();
                break;
            case R.id.rl_bottom_state:
                if (state.equals("1")) {
                    //需要支付
                    accessOrder();
                }
                break;
        }
    }

    /**
     * 更新底部按钮的显示F
     */
    private void updateUI() {
        switch (state) {
            case "1"://未支付
                tvState1.setVisibility(View.VISIBLE);
                tvState2.setVisibility(View.GONE);
                break;
            case "2"://报名成功
                tvState1.setVisibility(View.GONE);
                tvState2.setVisibility(View.VISIBLE);
                tvState2.setText("已报名");
                break;
            case "3"://取消报名
                tvState1.setVisibility(View.GONE);
                tvState2.setVisibility(View.VISIBLE);
                tvState2.setText("已取消");
                break;
            case "4"://拒绝报名
                tvState1.setVisibility(View.GONE);
                tvState2.setVisibility(View.VISIBLE);
                tvState2.setText("已拒绝");
                break;
        }
    }

    /**
     *  TODO 拨打电话
     * @param url
     */
    private void CallTel(String url){
        //电话
        final String phoneStr = url.substring(4);
        new AlertDialog(EventDetail3Activity.this)
                .builder()
                .setTitle(phoneStr)
                .setCancelable(true)
                .setPositiveButton("拨号", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermission(new String[]{android.Manifest.permission.CALL_PHONE}, true, new PermissionsResultListener() {
                            @Override
                            public void onPermissionGranted() {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phoneStr));
                                startActivity(intent);
                            }

                            @Override
                            public void onPermissionDenied() {
                                Toast.makeText(EventDetail3Activity.this, "没有拨号权限", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null).show();
    }
    /**
     * TODO 获取微信统一下单
     */
    private void accessOrder() {
        showLoading();
        String requestJson = RequestAPI.WXPayAPP(this,orderId+"");
        OkGo.post(ConstantsURL.WXPayAPP)
                .tag(this)
                .params("request", requestJson)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dismissLoading();
                        try {
                            JSONObject jo = new JSONObject(s);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if (statuscode.equals("1")) {
                                WxPayOrderEntity wxPayOrderEntity = new Gson().fromJson(jo.toString(), WxPayOrderEntity.class);
                                if (wxPayOrderEntity != null) {
                                    final IWXAPI msgApi = WXAPIFactory.createWXAPI(EventDetail3Activity.this, Constants.WX_APPID);
                                    // 将该app注册到微信
                                    msgApi.registerApp(Constants.WX_APPID);
                                    PayReq request = new PayReq();
                                    request.appId = wxPayOrderEntity.getAppId();
                                    request.partnerId = wxPayOrderEntity.getPartnerid();
                                    request.prepayId = wxPayOrderEntity.getPrepayid();
                                    request.packageValue = wxPayOrderEntity.getPackageX();
                                    request.nonceStr = wxPayOrderEntity.getNonceStr();
                                    request.timeStamp = wxPayOrderEntity.getTimeStamp();
                                    request.sign = wxPayOrderEntity.getPaySign();
                                    msgApi.sendReq(request);
                                    SharedUtil.putString(Constants.ORDER_ID, orderId + "");
                                }
                            } else if (statuscode.equals("-121")) {//订单已支付，无需统一下单
                                Toast.makeText(EventDetail3Activity.this, "订单已支付", Toast.LENGTH_SHORT).show();
                                state = "2";
                                updateUI();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dismissLoading();
                        Toast.makeText(EventDetail3Activity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * TODO 关闭页面前上上页面刷新
     */
    private void close() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            EventBus.getDefault().post(EventBusMapUtil.getStringMap("MyJoinEventFragment", "refresh"));
            finish();
        }
    }

    /**
     * TODO 物理返回键监听事件
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * TODO EventBus事件处理
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("EventDetail3Activity")) {
            if (map.get("EventDetail3Activity").equals("success")) {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                state = "2";
                updateUI();
            } else if (map.get("EventDetail3Activity").equals("failure")) {
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
