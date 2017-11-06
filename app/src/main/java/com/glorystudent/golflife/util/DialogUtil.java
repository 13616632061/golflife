package com.glorystudent.golflife.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.glorystudent.golflibrary.dialog.iosdialog.AlertDialog;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.activity.LoginActivity;

/**
 * TODO Dialog工具类
 * Created by Gavin.J on 2017/10/26.
 */

public class DialogUtil {

    public OnShowDialogListener onShowDialogListener;

    public interface OnShowDialogListener{
        void onSure();
        void onCancel();
    }

    /**
     * 获取Dialog实例对象
     * @return
     */
    public static DialogUtil getInstance() {
        return new DialogUtil();
    }
    /**
     * TODO 登录
     * @param context
     */
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
    /**
     * TODO 设置Dialog的监听
     * @param onShowDialogListener
     * @return
     */
    public DialogUtil setOnShowDialogListener(OnShowDialogListener onShowDialogListener) {
        this.onShowDialogListener = onShowDialogListener;
        return this;
    }
    /**
     * 弹出dialog
     * @param context
     * @param title
     * @param meassage
     */
    public void showDialog(Context context, String title, String meassage, String sureStr) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(meassage);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton(sureStr,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (onShowDialogListener != null) {
                            onShowDialogListener.onSure();
                        }
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (onShowDialogListener != null) {
                            onShowDialogListener.onCancel();
                        }
                    }
                });
        builder.create().show();
    }
    /**
     * 弹出dialog
     * @param context
     * @param title
     * @param meassage
     */
    public void showDialogButton3(Context context, String title, String meassage, String cancel, String sureStr) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(meassage);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton(sureStr,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (onShowDialogListener != null) {
                            onShowDialogListener.onSure();
                        }
                    }
                });
        builder.setNegativeButton(cancel,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (onShowDialogListener != null) {
                            onShowDialogListener.onCancel();
                        }
                    }
                });
        builder.create().show();
    }
}
