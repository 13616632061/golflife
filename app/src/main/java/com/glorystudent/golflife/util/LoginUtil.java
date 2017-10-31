package com.glorystudent.golflife.util;

import android.util.Log;

import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.entity.UserEntity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by Gavin.J on 2017/10/30.
 */

public class LoginUtil {


    /**
     * TODO 保存用户信息
     *
     */
    public static void saveSharedPreferences(String Userid,int number_id,String Groupid,String Accesstoken,String Phonenumber,String Customerphoto,
                                       String Username,String Gender, String golfage,String Chinacity_name,String Usertype) {
        //保存参数
        SharedUtil.putBoolean(Constants.LOGIN_STATE, true);
        SharedUtil.putString(Constants.USER_ID, Userid);
        SharedUtil.putInt(Constants.NUMBER_ID, number_id);
        SharedUtil.putString(Constants.GROUP_ID, Groupid);
        Log.d("login", "saveSharedPreferences: 1234--->" + Groupid);
        SharedUtil.putString(Constants.ACCESS_TOKEN, Accesstoken);
        SharedUtil.putString(Constants.PHONE_NUMBER, Phonenumber);
        SharedUtil.putString(Constants.HEAD_PORTRAIT, Customerphoto);
        SharedUtil.putString(Constants.NICKNAME, Username);
        SharedUtil.putString(Constants.SEX, Gender);
        SharedUtil.putString(Constants.VETERAN, golfage);
        SharedUtil.putString(Constants.ADDRESS, Chinacity_name);
        String usertype = Usertype;
        SharedUtil.putString(Constants.USER_TYPE, usertype);
    }
    /**
     * TODO 环信登录
     */
   public static void loginHuanxin(String phonenumber) {
        String password = Constants.EMClient_password;
        EMClient.getInstance().login(phonenumber, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }
}
