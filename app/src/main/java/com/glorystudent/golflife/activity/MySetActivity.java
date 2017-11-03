package com.glorystudent.golflife.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.DataCleanManager;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflibrary.widget.switchbutton.SwitchView;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.DialogUtil;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 我的设置页面
 */
public class MySetActivity extends BaseActivity implements SwitchView.OnStateChangedListener {

    private final static String TAG = "MySetActivity";
    @Bind(R.id.cache_size)
    public TextView cache_size;
    @Bind(R.id.current_version)
    public TextView current_version;
    @Bind(R.id.switch_view)
    public SwitchView switch_view;
    @Override
    protected int getContentId() {
        return R.layout.activity_my_set;
    }

    @Override
    protected void init() {
        try {
            cache_size.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch_view.setOpened(SharedUtil.getBoolean(Constants.NOTIFICATION));
        switch_view.setOnStateChangedListener(this);
        current_version.setText("V" + RequestAPI.getVersion(this));
    }

    /**
     * TODO 新消息通知开关switch
     * @param view
     */
    @Override
    public void toggleToOn(SwitchView view) {
        switch_view.setOpened(true);
        SharedUtil.putBoolean(Constants.NOTIFICATION, true);
    }

    @Override
    public void toggleToOff(SwitchView view) {
        switch_view.setOpened(false);
        SharedUtil.putBoolean(Constants.NOTIFICATION, false);
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.set_edit_profile, R.id.clear_cache, R.id.opinion_back, R.id.about_us, R.id.exit})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.set_edit_profile:
                //编辑个人资料
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case R.id.clear_cache:
                //清除缓存
                clear_cache();
                break;
            case R.id.opinion_back:
                //意见反馈
                startActivity(new Intent(this, OpinionBackActivity.class));
                break;
            case R.id.about_us:
                //关于我们
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.exit:
                //退出账号
                dialog_Exit(this);
                break;

        }
    }

    /**
     * TODO 退出dialog
     * @param context
     */
    private  void dialog_Exit(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要退出登陆吗?");
        builder.setTitle("提示");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedUtil.putBoolean(Constants.LOGIN_STATE, false);
                        SharedUtil.putString(Constants.GROUP_ID, null);
                        EMClient.getInstance().logout(true);
                        EventBus.getDefault().post(EventBusMapUtil.getStringMap("MainActivity","finish"));
                        startActivity(new Intent(MySetActivity.this,LoginActivity.class));
                        finish();
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * TODO 清除缓存
     */
    private void clear_cache(){
        DialogUtil.getInstance().setOnShowDialogListener(new DialogUtil.OnShowDialogListener() {
            @Override
            public void onSure() {
                DataCleanManager.clearAllCache(MySetActivity.this);
                try {
                    cache_size.setText(DataCleanManager.getTotalCacheSize(MySetActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {

            }
        }).showDialog(this, "清除缓存", "确定清除缓存吗?", "确定");
    }
}
