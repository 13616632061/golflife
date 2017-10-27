package com.glorystudent.golflife.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.glorystudent.golflibrary.dialog.iosdialog.AlertDialog;
import com.glorystudent.golflife.activity.LoginActivity;

/**
 * TODO Dialog工具类
 * Created by Gavin.J on 2017/10/26.
 */

public class DialogUtil {
    public static void GotoLoginDialog(final Context context){
        new AlertDialog(context).builder()
                .setTitle("此功能需要登录")
                .setMsg("是否去登陆")
                .setCancelable(true)
                .setPositiveButton("去登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                })
                .setNegativeButton("取消", null).show();
    }
}
