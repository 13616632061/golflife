package com.glorystudent.golflife.activity;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.util.ConstantsURL;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 服务条款页面
 */
public class ServiceTermsActivity extends BaseActivity {


    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.webview)
    WebView webview;

    @Override
    protected int getContentId() {
        return R.layout.activity_service_terms;
    }

    @Override
    protected void loadDatas() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("print", "网页加载结束:--->" + url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("print", "开始加载连接:---->" + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("print", "请求链接的url---->");
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webview.setWebChromeClient(new WebChromeClient(){
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

        webview.loadUrl(ConstantsURL.SERVICE_TERMS);
    }
    @OnClick({R.id.back})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
