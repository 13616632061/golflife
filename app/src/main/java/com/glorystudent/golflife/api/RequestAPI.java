package com.glorystudent.golflife.api;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.entity.AliyunRequestEntity;
import com.glorystudent.golflife.entity.FileUpRequestEntity;
import com.glorystudent.golflife.entity.RequestDataHeader;
import com.glorystudent.golflife.entity.SMSCheck;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.PhoneIpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO 请求工具类
 * Created by Gavin.J on 2017/10/24.
 */

public class RequestAPI {

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

        SMSCheck smsCheck=new SMSCheck("","","",getVersion(context),getCurrentTime(),"",phonenum, Constants.DEVICE_TYPE,"");
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
     * TODO 仅含包头
     *
     * @param context
     * @return
     */
    public static String getEmptyParameter(Context context) {
        Map<String,Object> map=new HashMap<>();
        map.put("userid",SharedUtil.getString(Constants.USER_ID));
        map.put("groupid",SharedUtil.getString(Constants.GROUP_ID));
        map.put("accesstoken",SharedUtil.getString(Constants.ACCESS_TOKEN));
        map.put("version",getVersion(context));
        map.put("datetime",getCurrentTime());
        map.put("DeviceType",Constants.DEVICE_TYPE);
        String json=new GsonBuilder().serializeNulls().create().toJson(map);
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

    /**
     * TODO 微信统一下单
     * @param context
     * @param orderId
     * @return
     */
    public static String WXPayAPP(Context context, String orderId){
        String hostIP = PhoneIpUtil.getHostIP();
        Map<String,Object> map=new HashMap<>();
        map.put("order_id",orderId);
        map.put("clientip",hostIP);
        String json_str=new GsonBuilder().serializeNulls().create().toJson(map);
        String json = "\"wxpay\":" + json_str;
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 新增赛事活动信息
     * @param context
     * @return
     */
    public static String getSignDefinition(Context context) {
        String query = "\"signdefinition\":" + "{}";
        return getJson(context, query);
    }

    /**
     * TODO 获取Aliyun数据
     * @return
     */
    public static AliyunRequestEntity getAliyunOSS() {
        AliyunRequestEntity aliyunRequestEntity = new AliyunRequestEntity();
        List<AliyunRequestEntity.ListsettingvalueBean> listset = new ArrayList<>();
        for (int i = 0; i < Constants.SETTING.length; i++) {
            AliyunRequestEntity.ListsettingvalueBean set = new AliyunRequestEntity.ListsettingvalueBean();
            String setting = SharedUtil.getString(Constants.SETTING[i]);
            if (setting != null) {
                set.setSettingvalue(setting);
                listset.add(set);
            } else {
                return null;
            }
        }
        aliyunRequestEntity.setListsettingvalue(listset);
        return aliyunRequestEntity;
    }
    /**
     * TODO 上传头像
     *
     * @param context
     * @param filemd5
     * @param filename
     * @param partmd5
     * @param filesize
     * @param partsize
     * @param partdata
     * @return
     */
    public static String getFileUp(Context context, String filemd5, String filename, String partmd5, int filesize, int partsize, String partdata) {
        FileUpRequestEntity fileUpRequestEntity = new FileUpRequestEntity();
        fileUpRequestEntity.setFilemd5(filemd5);
        fileUpRequestEntity.setFilename(filename);
        fileUpRequestEntity.setPartid(1);
        fileUpRequestEntity.setFilepartcount(1);
        fileUpRequestEntity.setPartmd5(partmd5);
        fileUpRequestEntity.setFilesize(filesize);
        fileUpRequestEntity.setPartsize(partsize);
        fileUpRequestEntity.setPartdata(partdata);
        fileUpRequestEntity.setPictype(1);
        String json = new Gson().toJson(fileUpRequestEntity);
        return getRequestJson(context, json);
    }

    /**
     * TODO 取消赛事活动
     * @param context
     * @param text
     * @param eventActivity_id
     * @return
     */
    public static String canclellEventActivity(Context context,String text,String eventActivity_id){
        Map<String,Object> map=new HashMap<>();
        map.put("cancelrefuse",text);
        map.put("eventActivity_id",eventActivity_id);
        String json_map=new GsonBuilder().serializeNulls().create().toJson(map);
        String json = "\"eventactivity\":" + "\"" + json_map+ "\"" ;
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 获取活动照片墙数据
     * @return
     */
    public static String QueryEventActivityPicWall(Context context,String eventActivity_id,String photoWallPage){
        Map<String,Object> map=new HashMap<>();
        map.put("eventactivity_id",eventActivity_id);
        String json_map=new GsonBuilder().serializeNulls().create().toJson(map);
        Map<String,Object> map1=new HashMap<>();
        map1.put("eventpic",json_map);
        map1.put("page",photoWallPage);
        String json=new GsonBuilder().serializeNulls().create().toJson(map1);
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 是否允许报名
     * @param context
     * @param Signup_id
     * @param flag
     * @return
     */
    public static String AgreeSignUp(Context context,String Signup_id,boolean flag){
        Map<String,Object> map=new HashMap<>();
        map.put("signup_id",Signup_id);
        map.put("sign_ifallow",flag);
        String json_map=new GsonBuilder().serializeNulls().create().toJson(map);
        Map<String,Object> map1=new HashMap<>();
        map1.put("signup",json_map);
        String json=new GsonBuilder().serializeNulls().create().toJson(map1);
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 获取赛事活动报名名单
     * @param context
     * @param eventactivity_id
     * @param page
     * @return
     */
    public static String QuerySignUpByEventID(Context context,String eventactivity_id,String page){
        Map<String,Object> map=new HashMap<>();
        map.put("sign_activitiesid",eventactivity_id );
        String json_map=new GsonBuilder().serializeNulls().create().toJson(map);
        Map<String,Object> map1=new HashMap<>();
        map1.put("signup",json_map);
        map1.put("page",page);
        String json=new GsonBuilder().serializeNulls().create().toJson(map1);
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }
}
