package com.glorystudent.golflife.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.dialog.iosdialog.AlertDialog;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.fragment.ChatFragment;
import com.glorystudent.golflife.fragment.StudentHomeFragment;
import com.glorystudent.golflife.fragment.StudentMyFragment;
import com.glorystudent.golflife.fragment.StudentVideoFragment;
import com.glorystudent.golflife.util.Constants;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.rb_home)
    RadioButton rbHome;
    @Bind(R.id.rb_video)
    RadioButton rbVideo;
    @Bind(R.id.rb_chat)
    RadioButton rbChat;
    @Bind(R.id.rb_my)
    RadioButton rbMy;
    @Bind(R.id.rg)
    RadioGroup rg;
    @Bind(R.id.tv_message_num)
    TextView tvMessageNum;
    @Bind(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @Bind(R.id.iv_rec)
    ImageView ivRec;
    @Bind(R.id.fragment)
    FrameLayout fragment;

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {


        rg.setOnCheckedChangeListener(this);
        rg.getChildAt(0).performClick();//模拟点击首页
    }
    /**
     * TODO 监听底部导航栏
     * 监听底部导航栏
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //设置底部导航栏字体颜色
        RadioButton rb = (RadioButton) group.findViewWithTag("checked");
        if (rb != null) {
            rb.setTextColor(rb.getResources().getColor(R.color.colorWhite));
            rb.setTag(null);
        }
        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
        radioButton.setTag("checked");
        radioButton.setTextColor(radioButton.getResources().getColor(R.color.colorOrange));
        switch (checkedId) {
            case R.id.rb_home:
                //首页模块
                showFragment(R.id.fragment, new StudentHomeFragment());
                break;
            case R.id.rb_video:
                //视频模块
                if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {
                    showFragment(R.id.fragment, new StudentVideoFragment());
                } else {
                    showLogin();
                    rg.getChildAt(0).performClick();
                }
                break;
            case R.id.rb_chat:
                //消息模块
                if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {
                    showFragment(R.id.fragment, new ChatFragment());
                } else {
                    showLogin();
                    rg.getChildAt(0).performClick();

                }
                break;
            case R.id.rb_my:
                //我的模块
                if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {
                    showFragment(R.id.fragment, new StudentMyFragment());
                } else {
                    showLogin();
                    rg.getChildAt(0).performClick();
                }
                break;
        }
    }

    /**
     * TODO 是否去登录提示
     */
    private void showLogin() {
        new AlertDialog(this).builder()
                .setTitle("此功能需要登录")
                .setMsg("是否去登录")
                .setCancelable(true)
                .setPositiveButton("去登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("取消", null).show();
    }
}
