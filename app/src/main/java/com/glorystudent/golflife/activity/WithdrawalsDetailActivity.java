package com.glorystudent.golflife.activity;

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
 *TODO 提现详情页面
 */
public class WithdrawalsDetailActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_complete)
    TextView tvComplete;



    @Override
    protected int getContentId() {
        return R.layout.activity_withdrawals_detail;
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_complete:
                finish();
                break;
        }
    }
}
