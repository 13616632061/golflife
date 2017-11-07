package com.glorystudent.golflife.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.customView.SharePopupWindow;
import com.glorystudent.golflife.entity.ShareModel;
import com.glorystudent.golflife.util.onekeyshare.OnekeyShare;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.ImageUtil;

import java.util.HashMap;

import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * TODO 邀请球友页面
 */
public class MyInviteActivity extends BaseActivity implements PlatformActionListener {

    private OnekeyShare oks;
    private SharePopupWindow share;
    private String picPath;


    @Override
    protected int getContentId() {
        return R.layout.activity_my_invite;
    }
    @Override
    protected void init() {
        ShareSDK.initSDK(this);
    }

    @OnClick({R.id.back, R.id.tv_invite})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.tv_invite:
                //邀请
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                picPath = ImageUtil.saveBitmap(bitmap, System.currentTimeMillis() + "");
                share = new SharePopupWindow(this);
                share.setPlatformActionListener(this);
                ShareModel model = new ShareModel();
                model.setImageUrl(picPath);
                model.setText("点击下载高尔夫人生，开启高尔夫人生精彩生活");
                model.setTitle(SharedUtil.getString(Constants.NICKNAME) + "邀请您加入高尔夫人生");
                model.setUrl("http://www.pgagolf.cn/home/share?userid=" + SharedUtil.getString(Constants.USER_ID) + "&fromrode=0");
                share.initShareParams(model);
                share.showShareWindow();
                // 显示窗口 (设置layout在PopupWindow中显示的位置)
                share.showAtLocation(this.findViewById(R.id.back),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f;
                getWindow().setAttributes(lp);
                share.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                    }
                });
                break;
        }
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        Log.d("", "onCancel: ");
    }

    @Override
    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {

    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {

    }

}
