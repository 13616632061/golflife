package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 选择性别页面
 */
public class SelectSexActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.iv_flag_m)
    ImageView ivFlagM;
    @Bind(R.id.ll_man)
    LinearLayout llMan;
    @Bind(R.id.iv_flag_f)
    ImageView ivFlagF;
    @Bind(R.id.ll_woman)
    LinearLayout llWoman;
    @Override
    protected int getContentId() {
        return R.layout.activity_select_sex;
    }
    @Override
    protected void init() {
        Intent intent = getIntent();
        String str = intent.getStringExtra("sex");
        if ("男".equals(str)) {
            ivFlagM.setVisibility(View.VISIBLE);
            ivFlagF.setVisibility(View.GONE);
        } else if ("女".equals(str)) {
            ivFlagM.setVisibility(View.GONE);
            ivFlagF.setVisibility(View.VISIBLE);
        }
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.ll_man, R.id.ll_woman})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.ll_man:
                intent.putExtra("sex", "男");
                this.setResult(RESULT_OK, intent);
                break;
            case R.id.ll_woman:
                intent.putExtra("sex", "女");
                this.setResult(RESULT_OK, intent);
                break;
        }
        finish();
    }
}
