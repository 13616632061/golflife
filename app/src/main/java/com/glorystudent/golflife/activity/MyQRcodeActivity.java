package com.glorystudent.golflife.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.QRCodeUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class MyQRcodeActivity extends BaseActivity {

    @Bind(R.id.tv_username)
    public TextView tv_username;
    @Bind(R.id.tv_address)
    public TextView tv_address;
    @Bind(R.id.qrcode)
    public ImageView qrcode;
    @Bind(R.id.qrcode_headpic)
    public ImageView qrcode_headpic;

    @Override
    protected int getContentId() {
        return R.layout.activity_my_qrcode;
    }
    @Override
    protected void init() {
        String url = SharedUtil.getString(Constants.HEAD_PORTRAIT);
        if ( url != null) {
            GlideUtil.loadCircleImageView(this, url, qrcode_headpic);
        }else{
            qrcode_headpic.setImageResource(R.drawable.pic_default_avatar);
        }
        tv_username.setText(SharedUtil.getString(Constants.NICKNAME));
        tv_address.setText(SharedUtil.getString(Constants.ADDRESS));
        //创建二维码
        QRCodeUtil.createCode(this, qrcode, SharedUtil.getString(Constants.USER_ID));
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
        }
    }
}
