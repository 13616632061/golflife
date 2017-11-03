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
        Map<String,Object> map1=new HashMap<>();
        map1.put("wxpay",map);
        String json_str=new GsonBuilder().serializeNulls().create().toJson(map1);
        String json = json_str.substring(1,json_str.length()-1);
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
        Map<String,Object> map1=new HashMap<>();
        map1.put("eventactivity",map);
        String json1=new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);
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
        Map<String,Object> map1=new HashMap<>();
        map1.put("eventpic",map);
        map1.put("page",photoWallPage);
        String json1=new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);
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
        Map<String,Object> map1=new HashMap<>();
        map1.put("signup",map);
        String json1=new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);
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
        Map<String,Object> map1=new HashMap<>();
        map1.put("signup",map);
        map1.put("page",page);
        String json1=new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 获取球队信息
     * @param context
     * @param type
     * @param page
     * @return
     */
    public static String QueryTeam(Context context,String type,String page){
        Map<String,Object> map1=new HashMap<>();
        Map<String,Object> map=new HashMap<>();
        map.put("team",map1);
        map.put("type",type);
        map.put("page",page);
        String json1=new GsonBuilder().serializeNulls().create().toJson(map);
        String json=json1.substring(1,json1.length()-1);
       String requestJson = RequestAPI.getJson(context, json);
        return  requestJson;
    }

    /**
     * TODO 通过球队名获取球队信息
     * @param context
     * @param text
     * @param type
     * @param page
     * @return
     */
    public static String QueryTeamByTitile(Context context,String text,String type,String page  ){
        Map<String,Object> map1=new HashMap<>();
        map1.put("title",text);
        Map<String,Object> map=new HashMap<>();
        map.put("team",map1);
        map.put("type",type);
        map.put("page",page);
        String json1=new GsonBuilder().serializeNulls().create().toJson(map);
        String json=json1.substring(1,json1.length()-1);
        String requestJson = RequestAPI.getJson(context, json);
        return  requestJson;
    }
    /**
     * TODO 通过球队名获取球队信息
     * @param context
     * @param cid
     * @param type
     * @param page
     * @return
     */
    public static String QueryTeamByCityId(Context context,String cid,String type,String page  ){
        Map<String,Object> map1=new HashMap<>();
        map1.put("regionid",cid);
        Map<String,Object> map=new HashMap<>();
        map.put("team",map1);
        map.put("type",type);
        map.put("page",page);
        String json1=new GsonBuilder().serializeNulls().create().toJson(map);
        String json=json1.substring(1,json1.length()-1);
        String requestJson = RequestAPI.getJson(context, json);
        return  requestJson;
    }
    /**
     * TODO 根据经纬度定位URL
     * @param latitude
     * @param longitude
     * @return
     */
    public static String getLocation( String latitude, String longitude){
        String url = "http://maps.google.cn/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true,language=zh-CN";
        return url;
    }

    /**
     * TODO 根据报名id获取凭证信息
     * @param context
     * @param signUpId
     * @return
     */
    public static String QueryEventActivityVoucher(Context context,String signUpId){
        Map<String,Object> map=new HashMap<>();
        map.put("signUpID",signUpId);
        String json=new GsonBuilder().serializeNulls().create().toJson(map).replace("{","").replace("}","");
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 取消赛事报名
     * @param context
     * @param Signup_id
     * @param text
     * @param type
     * @return
     */
    public static String WXPayReFundAPP(Context context,String Signup_id,String text,String type){
        Map<String,Object> map=new HashMap<>();
        map.put("sign_id",Signup_id);
        Map<String,Object> map1=new HashMap<>();
        map1.put("wxpay",map);
        map1.put("signrefse",text);
        map1.put("type",type);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 获取球队详情
     * @param context
     * @param teamId
     * @return
     */
    public static String QueryTeamDetail(Context context,String teamId){
        Map<String,Object> map=new HashMap<>();
        map.put("id",teamId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("team",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 退队或解散
     * @param context
     * @param teamId
     * @return
     */
    public static String SignOutTeam(Context context,String teamId){
        Map<String,Object> map=new HashMap<>();
        map.put("teamId",teamId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("teamUser",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 更改球队队长
     * @param context
     * @param teamId
     * @param captainid
     * @return
     */
    public static String EditTeamInfo(Context context,String teamId,String captainid){
        Map<String,Object> map=new HashMap<>();
        map.put("id",teamId);
        map.put("captainid",captainid);
        Map<String,Object> map1=new HashMap<>();
        map1.put("team",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 获取球队成员
     * @param context
     * @param teamId
     * @param page
     * @return
     */
    public static String QueryTeamUser(Context context,String teamId,String page){
        Map<String,Object> map=new HashMap<>();
        map.put("teamId",teamId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("teamUser",map);
        map1.put("page",page);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 根据球队成员手机号搜索好友
     * @param context
     * @param phoneNumber
     * @return
     */
    public static String QueryFriend(Context context,String phoneNumber){
        Map<String,Object> map=new HashMap<>();
        map.put("phonenumber",phoneNumber);
        Map<String,Object> map1=new HashMap<>();
        map1.put("user",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }
    /**
     * TODO 根据ID搜索好友
     * @param context
     * @param userid
     * @return
     */
    public static String QueryFriendByUserid(Context context,String userid){
        Map<String,Object> map=new HashMap<>();
        map.put("userid",userid);
        Map<String,Object> map1=new HashMap<>();
        map1.put("user",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 获取球队相册
     * @param context
     * @param teamId
     * @return
     */
    public static String QueryTeamEventActivityPhoto(Context context,String teamId){
        Map<String,Object> map=new HashMap<>();
        map.put("id",teamId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("team",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * RODO 获取球队相册详情
     * @param context
     * @param eventactivity_id
     * @return
     */
    public static String QueryTeamEventActivityPhotoDeatil(Context context, String eventactivity_id){
        Map<String,Object> map1=new HashMap<>();
        map1.put("Photoid",eventactivity_id);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 申请入球队处理
     * @param context
     * @param id
     * @param status 1 同意 2拒绝
     * @return
     */
    public static String EditTeamUserApply(Context context,String id,String status){
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        Map<String,Object> map1=new HashMap<>();
        map1.put("applyTeamUser",map);
        map1.put("status",status);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 获取申请入队数据
     * @param context
     * @param teamId
     * @param page
     * @return
     */
    public static String QueryTeamUserApply(Context context,String teamId,String page){
        Map<String,Object> map=new HashMap<>();
        map.put("id",teamId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("team",map);
        map1.put("page",page);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 签到和取消签到的方法 1取消签到，2签到
     * @param context
     * @param Signup_id
     * @param state
     * @return
     */
    public static String EditSignUp(Context context,String Signup_id,String state){
        Map<String,Object> map=new HashMap<>();
        map.put("signup_id",Signup_id);
        map.put("sign_upstate",state);
        Map<String,Object> map1=new HashMap<>();
        map1.put("signup",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }

    /**
     * TODO 通过赛事id处理扫码签到
     * @param context
     * @param eventIdStr
     * @return
     */
    public static  String SweepCodeSignUpByid(Context context,String eventIdStr){
        Map<String,Object> map=new HashMap<>();
        map.put("sign_activitiesid",eventIdStr);
        Map<String,Object> map1=new HashMap<>();
        map1.put("signup",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        return json;
    }

    /**
     * TODO 签到 通过凭证号 或者手机号
     * @param context
     * @param eventIdStr
     * @param voucherStr
     * @param type 1 凭证号签到 2手机号签到
     * @return
     */
    public static  String SweepCodeSignUp(Context context,String eventIdStr,String voucherStr,int type){
        Map<String,Object> map=new HashMap<>();
        map.put("sign_activitiesid",eventIdStr);
        if(type==1){
            map.put("sign_voucher",voucherStr);
        }else if(type==2){
            map.put("sign_phone",voucherStr);
        }
        Map<String,Object> map1=new HashMap<>();
        map1.put("signup",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        String requestJson = RequestAPI.getJson(context, json);
        return requestJson;
    }
    /**
     * TODO 确认上传头像
     */
    public static String editUserHeadPhoto(Context context, String customerphoto) {
        Map<String,Object> map=new HashMap<>();
        map.put("customerphoto",customerphoto);
        Map<String,Object> map1=new HashMap<>();
        map1.put("user",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        return getJson(context, json);
    }

    /**
     * TODO 修改昵称
     * @param context
     * @param nicknameStr
     * @return
     */
    public static String  editUserNickName(Context context,String nicknameStr){
        Map<String,Object> map=new HashMap<>();
        map.put("username",nicknameStr);
        Map<String,Object> map1=new HashMap<>();
        map1.put("user",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        return getJson(context, json);
    }
    /**
     * TODO 修改性别
     * @param context
     * @param sexcode
     * @return
     */
    public static String  editUserSex(Context context,String sexcode){
        Map<String,Object> map=new HashMap<>();
        map.put("gender",sexcode);
        Map<String,Object> map1=new HashMap<>();
        map1.put("user",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        return getJson(context, json);
    }
    /**
     * TODO 修改球龄
     * @param context
     * @param saveGolfAge
     * @return
     */
    public static String  editGolfAge(Context context,String saveGolfAge){
        Map<String,Object> map=new HashMap<>();
        map.put("golfage",saveGolfAge);
        Map<String,Object> map1=new HashMap<>();
        map1.put("user",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        return getJson(context, json);
    }

    /**
     * TODO 修改地区
     * @param context
     * @param addressid
     * @param addressName
     * @return
     */

    public static String  editUserCity(Context context,String addressid,String addressName){
        Map<String,Object> map=new HashMap<>();
        map.put("chinacity",addressid);
        map.put("chinacity_name",addressName);
        Map<String,Object> map1=new HashMap<>();
        map1.put("user",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        return getJson(context, json);
    }
    /**
     * TODO 获取城市区域信息
     *
     * @param context
     * @return
     */
    public static String getCitys(Context context) {
        Map<String,Object> map=new HashMap<>();
        map.put("pid",0+"");
        Map<String,Object> map1=new HashMap<>();
        map1.put("chinacity",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        return getJson(context, json);
    }
    /**
     * TODO 意见反馈
     *
     * @param context
     * @param feedbackcontext
     * @return
     */
    public static String getOpinion(Context context, String feedbackcontext) {
        Map<String,Object> map=new HashMap<>();
        map.put("feedbackcontext",feedbackcontext);
        map.put("feedbackdatetime",getCurrentTime());
        Map<String,Object> map1=new HashMap<>();
        map1.put("feedback",map);
        String json1= new GsonBuilder().serializeNulls().create().toJson(map1);
        String json=json1.substring(1,json1.length()-1);//去掉首尾的{}
        return getJson(context, json);
    }
}
