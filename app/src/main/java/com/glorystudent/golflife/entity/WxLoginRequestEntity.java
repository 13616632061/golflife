package com.glorystudent.golflife.entity;

/**
 * TODO 绑定微信登陆实体类
 * Created by Gavin.J on 2017/10/30.
 */

public class WxLoginRequestEntity {
    private WxLoginBean wxlogin;
    private String phonenum;
    private String phonencode;
    public static class WxLoginBean{
        private String openid;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }

    public String getPhonencode() {
        return phonencode;
    }

    public void setPhonencode(String phonencode) {
        this.phonencode = phonencode;
    }

    public WxLoginBean getWxlogin() {
        return wxlogin;
    }

    public void setWxlogin(WxLoginBean wxlogin) {
        this.wxlogin = wxlogin;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }
}
