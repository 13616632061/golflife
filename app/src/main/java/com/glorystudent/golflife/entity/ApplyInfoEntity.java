package com.glorystudent.golflife.entity;

import java.util.List;

/**
 * todo 新朋友申请实体类
 * Created by Gavin.J on 2017/11/7.
 */

public class ApplyInfoEntity {
    /**
     * listapplyfriends : [{"_ApplyUserID":1255,"_AnswerUserID":1217,"applyname":"就这样","applyphoto":"https://192.168.1.168:4431/images/headfiles/2017-5-25/1495690719485.png","applyfriends_id":1135,"applyfriends_type":"\u0000","applyuserid":1255,"answeruserid":1217,"applyremark":"哈哈","applystatus":"0","refuseremark":null,"applytype":"\u0000"},{"_ApplyUserID":1259,"_AnswerUserID":1217,"applyname":"新用户_514077","applyphoto":"http://app.pgagolf.cn/img/newUser.jpg","applyfriends_id":1134,"applyfriends_type":"\u0000","applyuserid":1259,"answeruserid":1217,"applyremark":"你好","applystatus":"0","refuseremark":null,"applytype":"\u0000"}]
     * version : null
     * datetime : null
     * accesstoken : null
     * statuscode : 1
     * statusmessage : 消息处理成功
     * totalrownum : null
     * totalpagenum : null
     * nowpagenum : null
     * pagerownum : null
     */

    private Object version;
    private Object datetime;
    private Object accesstoken;
    private int statuscode;
    private String statusmessage;
    private Object totalrownum;
    private Object totalpagenum;
    private Object nowpagenum;
    private Object pagerownum;
    private List<ListapplyfriendsBean> listapplyfriends;

    public Object getVersion() {
        return version;
    }

    public void setVersion(Object version) {
        this.version = version;
    }

    public Object getDatetime() {
        return datetime;
    }

    public void setDatetime(Object datetime) {
        this.datetime = datetime;
    }

    public Object getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(Object accesstoken) {
        this.accesstoken = accesstoken;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public String getStatusmessage() {
        return statusmessage;
    }

    public void setStatusmessage(String statusmessage) {
        this.statusmessage = statusmessage;
    }

    public Object getTotalrownum() {
        return totalrownum;
    }

    public void setTotalrownum(Object totalrownum) {
        this.totalrownum = totalrownum;
    }

    public Object getTotalpagenum() {
        return totalpagenum;
    }

    public void setTotalpagenum(Object totalpagenum) {
        this.totalpagenum = totalpagenum;
    }

    public Object getNowpagenum() {
        return nowpagenum;
    }

    public void setNowpagenum(Object nowpagenum) {
        this.nowpagenum = nowpagenum;
    }

    public Object getPagerownum() {
        return pagerownum;
    }

    public void setPagerownum(Object pagerownum) {
        this.pagerownum = pagerownum;
    }

    public List<ListapplyfriendsBean> getListapplyfriends() {
        return listapplyfriends;
    }

    public void setListapplyfriends(List<ListapplyfriendsBean> listapplyfriends) {
        this.listapplyfriends = listapplyfriends;
    }

    public static class ListapplyfriendsBean {
        /**
         * _ApplyUserID : 1255
         * _AnswerUserID : 1217
         * applyname : 就这样
         * applyphoto : https://192.168.1.168:4431/images/headfiles/2017-5-25/1495690719485.png
         * applyfriends_id : 1135
         * applyfriends_type :
         * applyuserid : 1255
         * answeruserid : 1217
         * applyremark : 哈哈
         * applystatus : 0
         * refuseremark : null
         * applytype :
         */

        private int _ApplyUserID;
        private int _AnswerUserID;
        private String applyname;
        private String applyphoto;
        private int applyfriends_id;
        private String applyfriends_type;
        private int applyuserid;
        private int answeruserid;
        private String applyremark;
        private String applystatus;
        private Object refuseremark;
        private String applytype;

        public int get_ApplyUserID() {
            return _ApplyUserID;
        }

        public void set_ApplyUserID(int _ApplyUserID) {
            this._ApplyUserID = _ApplyUserID;
        }

        public int get_AnswerUserID() {
            return _AnswerUserID;
        }

        public void set_AnswerUserID(int _AnswerUserID) {
            this._AnswerUserID = _AnswerUserID;
        }

        public String getApplyname() {
            return applyname;
        }

        public void setApplyname(String applyname) {
            this.applyname = applyname;
        }

        public String getApplyphoto() {
            return applyphoto;
        }

        public void setApplyphoto(String applyphoto) {
            this.applyphoto = applyphoto;
        }

        public int getApplyfriends_id() {
            return applyfriends_id;
        }

        public void setApplyfriends_id(int applyfriends_id) {
            this.applyfriends_id = applyfriends_id;
        }

        public String getApplyfriends_type() {
            return applyfriends_type;
        }

        public void setApplyfriends_type(String applyfriends_type) {
            this.applyfriends_type = applyfriends_type;
        }

        public int getApplyuserid() {
            return applyuserid;
        }

        public void setApplyuserid(int applyuserid) {
            this.applyuserid = applyuserid;
        }

        public int getAnsweruserid() {
            return answeruserid;
        }

        public void setAnsweruserid(int answeruserid) {
            this.answeruserid = answeruserid;
        }

        public String getApplyremark() {
            return applyremark;
        }

        public void setApplyremark(String applyremark) {
            this.applyremark = applyremark;
        }

        public String getApplystatus() {
            return applystatus;
        }

        public void setApplystatus(String applystatus) {
            this.applystatus = applystatus;
        }

        public Object getRefuseremark() {
            return refuseremark;
        }

        public void setRefuseremark(Object refuseremark) {
            this.refuseremark = refuseremark;
        }

        public String getApplytype() {
            return applytype;
        }

        public void setApplytype(String applytype) {
            this.applytype = applytype;
        }
    }
}
