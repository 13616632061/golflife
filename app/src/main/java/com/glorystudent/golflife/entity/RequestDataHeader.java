package com.glorystudent.golflife.entity;

/**
 * TODO 请求数据包头实体类
 * Created by Gavin.J on 2017/10/24.
 */

public class RequestDataHeader {
    private  String userid;//用户id
    private  String groupid;//学院ID
    private  String accesstoken;//帐户令牌
    private  String version;//应用版本号
    private  String datetime;//发送时间
    private  String messagetoken;//消息令牌
    private  String devicetype;//设备类型  4表示安卓设备
    private String appType;//app类型

    public RequestDataHeader(String userid, String groupid, String accesstoken, String version, String datetime, String messagetoken, String devicetype, String appType) {
        this.userid = userid;
        this.groupid = groupid;
        this.accesstoken = accesstoken;
        this.version = version;
        this.datetime = datetime;
        this.messagetoken = messagetoken;
        this.devicetype = devicetype;
        this.appType = appType;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMessagetoken() {
        return messagetoken;
    }

    public void setMessagetoken(String messagetoken) {
        this.messagetoken = messagetoken;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    @Override
    public String toString() {
        return "RequestDataHeader{" +
                "userid='" + userid + '\'' +
                ", groupid='" + groupid + '\'' +
                ", accesstoken='" + accesstoken + '\'' +
                ", version='" + version + '\'' +
                ", datetime='" + datetime + '\'' +
                ", messagetoken='" + messagetoken + '\'' +
                ", devicetype='" + devicetype + '\'' +
                ", appType='" + appType + '\'' +
                '}';
    }
}
