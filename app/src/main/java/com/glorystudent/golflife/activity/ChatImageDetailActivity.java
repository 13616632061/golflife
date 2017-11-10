package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflife.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO 聊天图片详情页
 */
public class ChatImageDetailActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.iv_image_detail)
    ImageView ivImageDetail;

    @Override
    protected int getContentId() {
        return R.layout.activity_chat_image_detail;
    }
    @Override
    protected void init() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (url != null) {
            GlideUtil.loadImageView(this, url, ivImageDetail);
        }
    }
    @OnClick({R.id.back})
    public void onViewClick(View v){
        switch(v.getId()){
            case R.id.back:
                finish();
                break;
        }

    }
}
