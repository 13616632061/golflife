package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 球队信息页面
 */
public class TeamInfoActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_activity_name)
    TextView activityName;
    @Bind(R.id.tv_introduction)
    TextView tvIntroduction;
    private String summary;//球队简介
    @Override
    protected int getContentId() {
        return R.layout.activity_team_info;
    }
    @Override
    protected void init() {
        Intent intent = getIntent();
        summary = intent.getStringExtra("summary");
        tvIntroduction.setText(summary);
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            };
    }
}
