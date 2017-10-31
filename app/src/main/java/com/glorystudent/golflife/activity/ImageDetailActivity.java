package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflife.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 图片详情页面
 */
public class ImageDetailActivity extends BaseActivity {


    @Bind(R.id.iv_image_detail)
    ImageView imageDetail;
    @Bind(R.id.iv_image_delete)
    ImageView imageDelete;
    private int position;
    @Override
    protected int getContentId() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        position = intent.getIntExtra("position", -1);
        Log.i("tag", "init: position:" + position);
        if (url != null) {
            GlideUtil.loadImageView(this, url, imageDetail);
        }
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.iv_image_detail, R.id.iv_image_delete})
    public void onViewClicked(View view) {
        Intent data = null;
        switch (view.getId()) {
            case R.id.iv_image_detail:
                data = new Intent();
                data.putExtra("isDelete", false);
                this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.iv_image_delete:
                if (position != -1) {
                    data = new Intent();
                    data.putExtra("isDelete", true);
                    data.putExtra("position", position);
                    this.setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(this, "赛事图片不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
