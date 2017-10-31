package com.glorystudent.golflife.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 赛事活动详情页面
 */
public class ReleasedEventInfoActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.et_info_content)
    EditText content;
    private InputMethodManager imm;

    @Override
    protected int getContentId() {
        return R.layout.activity_released_event_info;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        content.setText(text);
        content.setFocusable(true);
        content.setFocusableInTouchMode(true);
        content.requestFocus();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(content, 0);
                }
            }
        }, 200);
    }

    /**
     * TODO 点击事件监听
      * @param view
     */
    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                //先关闭输入法，这样返回后才能让上一个页面的活动详情获取焦点
                if (imm != null) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                }
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        close();
                    }
                }, 200);
                break;
        }
    }

    /**
     * 关闭页面
     */
    private void close() {
        Intent intent = new Intent();
        intent.putExtra("text", content.getText().toString().trim());
        this.setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * TODO 物理返回键监听
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
}
