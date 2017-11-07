package com.glorystudent.golflife.util;

/**
 *TODO 静态常量
 * Created by Gavin.J on 2017/10/24.
 */

public interface Constants {

    String LOGIN_STATE = "logined";//登录状态
    String USER_ID = "userid";//用户id
    String GROUP_ID = "groupid";//学院ID
    String ACCESS_TOKEN = "accesstoken";//帐户令牌
    String DEVICE_TYPE="4";//设备类型  4表示安卓设备
    String REGISTRATION_ID = "registrationid";
    String NUMBER_ID = "numberid";//用户id的数字形态
    String PHONE_NUMBER = "phonenumber";//手机号(账号)
    String HEAD_PORTRAIT = "customerphoto";//头像
    String NICKNAME = "username";//昵称
    String USER_TYPE = "usertype";//用户类型
    String SEX = "gender";//性别
    String VETERAN = "golfage";//球龄
    String ADDRESS = "address";//所在地区(所在城市)
    String EMClient_password="123456";//环信登录密码
    String REFRESH_TIME = "refreshtime";//下拉刷新时间
    String ORDER_ID = "orderId";//订单id
    String[] SETTING = new String[]{"setting1", "setting2", "setting3", "setting4"};
    String DEFAULT_USERNAME = "用户";
    String NOTIFICATION = "NotificationState";//新消息通知状态
    String PROVINCES_NAME = "provincesname";//省名字
    String CITY_NAME = "cityname";//城市名字
    String COUNTY_NAME = "countyname";//县区名字
    String CLOSE = "close";//关闭activity
    String NEW_FRIENDS_COUNT = "newfriendscount";//新的朋友条数


    String WX_APPID = "wxd2ec5fc5fab63695";// 获取缓存微信分享的秘钥key值
    String WX_AppSecret = "5275c0374acde6a637d15560dffc6314";  // 获取缓存微信分享的秘钥key值
    String QQ_APPID = "1105909466";//QQ分享的APPid
    String QQ_APPKEY = "V3H7UNp3juSUCPQv";//QQ分享的key

    int[] FACE = new int[]{0x1F60a, 0x1F603, 0x1F609, 0x1F62e, 0x1F60b, 0x1F60e, 0x1F621, 0x1F616, 0x1F633,
            0x1F61e, 0x1F62d, 0x1F610, 0x1F607, 0x1F62c, 0x1F606, 0x1F631, 0x1F385, 0x1F634, 0x1F615, 0x1F637,
            0x1F62f, 0x1F60f, 0x1F611, 0x1F496, 0x1F494, 0x1F319, 0x1f31f, 0x1f31e, 0x1F308, 0x1F60d, 0x1F61a,
            0x1F48b, 0x1F339, 0x1F342, 0x1F44d, 0x1F602, 0x1F603, 0x1F604, 0x1F609, 0x1F613, 0x1F614, 0x1F616,
            0x1F618, 0x1F61a, 0x1F61c, 0x1F61d, 0x1F61e, 0x1F620, 0x1F621, 0x1F622, 0x1F623, 0x1F628, 0x1F62a,
            0x1F62d, 0x1F630, 0x1F631, 0x1F632, 0x1F633, 0x1F645, 0x1F646, 0x1F647, 0x1F64c, 0x1F6a5, 0x1F6a7,
            0x1F6b2, 0x1F6b6, 0x1F302, 0x1F319, 0x1F31f};
}
