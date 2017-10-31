package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflife.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 照片墙详情页面
 */
public class PhotoWallDetailActivity extends BaseActivity {


    @Bind(R.id.iv_photo_wall_detail)
    ImageView photoWallDetail;
    private String url;
    @Override
    protected int getContentId() {
        return R.layout.activity_photo_wall_detail;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        GlideUtil.loadImageView(this, url, photoWallDetail);
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick(R.id.iv_photo_wall_detail)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_photo_wall_detail:
                finish();
                break;
        }
    }
}
