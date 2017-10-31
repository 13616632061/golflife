package com.glorystudent.golflife.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.dialog.iosdialog.AlertDialog;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.customView.SharePopupWindow;
import com.glorystudent.golflife.entity.ShareModel;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import okhttp3.Call;
import okhttp3.Response;

public class EventDetailActivity extends BaseActivity implements PlatformActionListener {

    private final static String TAG = "EventDetailActivity";

    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_share)
    TextView tvShare;

    private int id;//赛事活动id
    private ShareModel model;
    private SharePopupWindow share;
    private String weburl;

    // 支付的url
    private static final String ORDER_URL = "http://www.pgagolf.cn/?OrderID";
    // 荣耀服务的url
    private static final String HONOR_SERVER_URL = "home/license";
    // 我的活动url
    private static final String MY_COMPETITION_URL = "Activityapp/MyActivityList";
    // 拨号url
    private static final String CALL_TEL_URL = "tel:";
    // 保存相册url
    private static final String SAVE_TO_PHOTO_ALBUMS_URL = "isAndroid:";
    // 跳到我的报名页
    private static final String JUMP_TO_SIGN_UP_URL = "Activity/MyActivityList";

    private LinkedList<String> loadHistoryUrls;
    private String orderId;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessage5;
    public static final int FILECHOOSER_RESULTCODE = 5173;
    public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 5174;

    @Override
    protected int getContentId() {
        return R.layout.activity_event_detail;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
            id = intent.getIntExtra("id", -1);
            String name = intent.getStringExtra("name");
            String detail = intent.getStringExtra("detail");
            String imageUrl = intent.getStringExtra("imageUrl");

            loadHistoryUrls = new LinkedList<>();

            model = new ShareModel();
            model.setImageUrl(imageUrl);
            model.setText(detail);
            model.setTitle(name);
            model.setUrl(String.format(ConstantsURL.EVENT_SHARE_URL, id));

        if (id != -1) {
            weburl = String.format(ConstantsURL.EVENT_DETAIL_URL, id, SharedUtil.getString(Constants.USER_ID));
            webView.loadUrl(weburl);
        }
    }
    @Override
    protected void loadDatas() {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        settings.setUseWideViewPort(true);
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
//处理webview URL跳转事件
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadHistoryUrls.add(url);
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //接收所有证书
                handler.proceed();
//                super.onReceivedSslError(view, handler, error);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("url="+url);
                if (url.contains(MY_COMPETITION_URL)) {
                    //我的活动
                    startActivity(new Intent(EventDetailActivity.this, MyEventActivity.class));
                    return true;
                } else if (url.contains(CALL_TEL_URL)) {
                    //电话
                    final String phoneStr = url.substring(4);
                    callPhone(phoneStr);
                    return true;
                } else if (url.contains(HONOR_SERVER_URL)) {
                    //荣耀服务条款
                    startActivity(new Intent(EventDetailActivity.this, ServiceTermsActivity.class));
                    return true;
                } else if (url.contains(SAVE_TO_PHOTO_ALBUMS_URL)) {
                    //保存到相册
                    return true;
                } else if (url.contains(JUMP_TO_SIGN_UP_URL)) {
                    //返回我的参加的活动
                    startActivity(new Intent(EventDetailActivity.this, MyEventActivity.class));
                    finish();
                    return true;
                } else if (url.contains(ORDER_URL)) {
                    //支付
                    orderId = url.substring(url.indexOf("=") + 1);
                    accessOrder();
                    return true;
                }else {
                }
                return super.shouldOverrideUrlLoading(view, url);
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
                    pb.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        setWebChromeClient();//处理android系统webView调用H5文件上传JS无反应操作
        if (id != -1) {
            weburl = String.format(ConstantsURL.EVENT_DETAIL_URL, id, SharedUtil.getString(Constants.USER_ID));
            webView.loadUrl(weburl);
            System.out.println("weburl="+weburl);
        }
    }

    /**
     *  TODO 处理android系统webView调用H5文件上传JS无反应操作
     */
    private void setWebChromeClient(){
        webView.setWebChromeClient(new WebChromeClient() {

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg){
                this.openFileChooser(uploadMsg, "*/*");
            }

            // For Android >= 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType) {
                this.openFileChooser(uploadMsg, acceptType, null);
            }

            // For Android >= 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                if (mUploadMessage5 != null) {
                    mUploadMessage5.onReceiveValue(null);
                    mUploadMessage5 = null;
                }
                mUploadMessage5 = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent,
                            FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
                } catch (ActivityNotFoundException e) {
                    mUploadMessage5 = null;
                    return false;
                }
                return true;
            }
        });
    }
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessage5) {
                return;
            }
            mUploadMessage5.onReceiveValue(WebChromeClient.FileChooserParams
                    .parseResult(resultCode, intent));
            mUploadMessage5 = null;
        }
    }
    /**
     * TODO 拨打电话
     *
     * @param phoneStr
     */
    private void callPhone(final String phoneStr) {
        new AlertDialog(EventDetailActivity.this)
                .builder()
                .setTitle(phoneStr)
                .setCancelable(true)
                .setPositiveButton("拨号", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermission(new String[]{Manifest.permission.CALL_PHONE}, true, new PermissionsResultListener() {
                            @Override
                            public void onPermissionGranted() {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phoneStr));
                                startActivity(intent);
                            }

                            @Override
                            public void onPermissionDenied() {
                                Toast.makeText(EventDetailActivity.this, "没有拨号权限", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null).show();
    }
    /**
     *TODO 获取微信统一下单
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
                                    final IWXAPI msgApi = WXAPIFactory.createWXAPI(EventDetailActivity.this, Constants.WX_APPID);
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
                                    SharedUtil.putString(Constants.ORDER_ID, orderId);
                                }
                            } else if (statuscode.equals("2")) {
                                Log.i(TAG, "parseDatas: " + statusmessage);
                            } else {
                                Log.i(TAG, "parseDatas: " + statusmessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dismissLoading();
                        Toast.makeText(EventDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                close();
                break;
            case R.id.tv_share:
                if (weburl != null) {
                    share = new SharePopupWindow(this);
                    share.setPlatformActionListener(this);
                    share.initShareParams(model);
                    share.showShareWindow();
                    // 显示窗口 (设置layout在PopupWindow中显示的位置)
                    share.showAtLocation(this.findViewById(R.id.back),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    // 设置背景颜色变暗
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                    share.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 1f;
                            getWindow().setAttributes(lp);
                        }
                    });
                }
                break;
        }
    }

    /**
     * TODO 关闭赛事详情页
     */
    private void close() {
        Log.i(TAG, "close: " + webView.canGoBack());
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            EventBus.getDefault().post(EventBusMapUtil.getStringMap("EventActivity", "refresh"));
            finish();
        }
    }

    /**
     * TODO 物理键盘返回监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * TODO 分享活动过程中事件监听
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
