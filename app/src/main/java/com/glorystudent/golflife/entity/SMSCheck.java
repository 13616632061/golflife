package com.glorystudent.golflife.entity;

/**
 * TODO 虚拟验证码实体类
 * Created by Gavin.J on 2017/10/24.
 */

public class SMSCheck {

    private  String userid;//用户id
    private  String coachgroupid;//学院ID
    private  String accesstoken;//帐户令牌
    private  String version;//应用版本号
    private  String datetime;//发送时间
    private  String messagetoken;//消息令牌
    private  String phonenum;//手机号码
    private  String DeviceType;//设备类型  4表示安卓设备
    private  String ipaddress;//App类型

    public SMSCheck(String userid, String coachgroupid, String accesstoken, String version, String datetime, String messagetoken, String phonenum, String deviceType, String ipaddress) {
        this.userid = userid;
        this.coachgroupid = coachgroupid;
        this.accesstoken = accesstoken;
        this.version = version;
        this.datetime = datetime;
        this.messagetoken = messagetoken;
        this.phonenum = phonenum;
        DeviceType = deviceType;
        this.ipaddress = ipaddress;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCoachgroupid() {
        return coachgroupid;
    }

    public void setCoachgroupid(String coachgroupid) {
        this.coachgroupid = coachgroupid;
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

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    @Override
    public String toString() {
        return "SMSCheck{" +
                "userid='" + userid + '\'' +
                ", coachgroupid='" + coachgroupid + '\'' +
                ", accesstoken='" + accesstoken + '\'' +
                ", version='" + version + '\'' +
                ", datetime='" + datetime + '\'' +
                ", messagetoken='" + messagetoken + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", DeviceType='" + DeviceType + '\'' +
                ", ipaddress='" + ipaddress + '\'' +
                '}';
    }
}
