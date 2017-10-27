package com.glorystudent.golflife.customView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

/**
 * Created by Gavin.J on 2017/10/27.
 */

public class MyWebChromeClient extends WebChromeClient {
    private WebCall webCall;

    public MyWebChromeClient setWebCall(WebCall webCall) {
        this.webCall = webCall;
        return null;
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (webCall != null)
            webCall.fileChose(uploadMsg);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    // For Android > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        if (webCall != null)
            webCall.fileChose5(filePathCallback);
        return super.onShowFileChooser(webView, filePathCallback,
                fileChooserParams);
    }

    public interface WebCall {
        void fileChose(ValueCallback<Uri> uploadMsg);

        void fileChose5(ValueCallback<Uri[]> uploadMsg);
    }
}
