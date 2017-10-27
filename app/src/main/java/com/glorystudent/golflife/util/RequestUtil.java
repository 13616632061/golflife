package com.glorystudent.golflife.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.entity.RequestDataHeader;
import com.glorystudent.golflife.entity.SMSCheck;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 请求工具类
 * Created by Gavin.J on 2017/10/24.
 */

public class RequestUtil {

    /**
     * ToDo 获取当前系统时间
     * @return
     */

    public static String getCurrentTime(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String CurrentTime=sdf.format(date);
        return CurrentTime;
    }

    /**
     * TODO 获取应用版本号
     * 获取当前应用的版本号
     *
     * @return
     */
    public static String getVersion(Context context) {
        try {
            if (context != null) {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                String version = info.versionName;
                return version;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1 + "";
    }

    /**
     * TODO 获取虚拟验证码JSON
     * 获取虚拟验证码
     *
     * @param context
     * @return
     */
    public static String getSMSCheck(Context context, String phonenum) {

        SMSCheck smsCheck=new SMSCheck("","","",getVersion(context),getCurrentTime(),"",phonenum,Constants.DEVICE_TYPE,"");
        String request=new Gson().toJson(smsCheck);
        return request;
    }
    /**
     * TODO 请求数据包头封装
     * 请求数据包头封装
     *
     * @param context
     * @param query
     * @return
     */
    public static String getJson(Context context, String query) {
        RequestDataHeader requestDataHeader=new RequestDataHeader(SharedUtil.getString(Constants.USER_ID),SharedUtil.getString(Constants.GROUP_ID),
                SharedUtil.getString(Constants.ACCESS_TOKEN),getVersion(context),getCurrentTime(),"",Constants.DEVICE_TYPE);
        String json=new GsonBuilder().serializeNulls().create().toJson(requestDataHeader).replace("}","")+","+query+"}";
        return json;
    }

    /**
     * TODO 登录
     * @param context
     * @param phonenum
     * @param code
     * @return
     */
    public static String getLogin(Context context, String phonenum, String code) {
        String jgpushid=SharedUtil.getString(Constants.REGISTRATION_ID);
        Map<String,Object> map=new HashMap<>();
        map.put("phonenum",phonenum);
        map.put("smsCheckCode",code);
        map.put("jgpushid",jgpushid);

        String json=new GsonBuilder().serializeNulls().create().toJson(map);
        String login=json.replace("{","").replace("}","");

        return getJson(context, login);
    }
    /**
     * TODO 万能请求url
     */
    public static String getRequestJson(Context context, String json) {
        String requestJson = json.substring(1, json.length() - 1);
        return getJson(context, requestJson);
    }
}
