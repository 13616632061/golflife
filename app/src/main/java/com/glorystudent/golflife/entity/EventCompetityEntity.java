package com.glorystudent.golflife.entity;

import com.glorystudent.golflibrary.adapter.AbsMoreBaseAdapter;

import java.util.List;

/**
 * TODO 赛事活动实体类
 * Created by Gavin.J on 2017/10/26.
 */

public class EventCompetityEntity {

    private Object version;
    private Object datetime;
    private Object accesstoken;
    private int statuscode;
    private String statusmessage;
    private Object totalrownum;
    private Object totalpagenum;
    private Object nowpagenum;
    private Object pagerownum;
    private List<ListeventactivityBean> listeventactivity;

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

    public List<ListeventactivityBean> getListeventactivity() {
        return listeventactivity;
    }

    public void setListeventactivity(List<ListeventactivityBean> listeventactivity) {
        this.listeventactivity = listeventactivity;
    }

    public static class ListeventactivityBean implements AbsMoreBaseAdapter.OnType {

        private int signUpNumber;
        private int orderstate;
        private int orderid;
        private Object cancelrefuse;
        private int eventActivity_id;
        private String eventactivity_name;
        private String eventactivity_begintime;
        private String eventactivity_endtime;
        private String eventactivity_detail;
        private int eventactivity_number;
        private String eventactivity_type;
        private String eventactivity_state;
        private String eventactivity_address;
        private String longitude;
        private String latitude;
        private String coachgrouplogo;
        private String coachgroupname;
        private Object eventactivity_champion;
        private Object eventactivity_level;
        private int eventactivity_bonus;
        private int eventactivity_bringguestsnum;
        private boolean eventactivity_ifbringguests;
        private String eventactivity_kickofftime;
        private boolean eventactivity_ifpublicly;
        private String eventactivity_pwd;
        private String eventactivity_publisherlogo;
        private int eventactivity_publisherid;
        private String eventactivity_publishertel;
        private String eventactivity_publishername;
        private String eventactivity_costtype;
        private double eventactivity_prepayment;
        private double eventactivity_cost;
        private double eventactivity_guestscost;
        private int eventactivity_binarynumber;
        private String eventactivity_signupbegintime;
        private String eventactivity_signupendtime;
        private String eventactivity_organizer;
        private String eventactivity_organizertel;
        private boolean eventactivity_ifshowsignname;
        private boolean eventactivity_ifshowphotowall;
        private int pga_id;
        private Object eventactivity_createtime;
        private double eventactivity_guestprepayment;
        private String eventactivity_guestcosttype;
        private int eventactivity_teamid;
        private int withdrawalsstatus;
        private int eventActivity_AddressID;
        private int listEventPicCount;
        private int listEventPicWallCount;
        private Object eventactivity_Cancel;

        private List<?> listPic;
        private List<?> listSignUp;
        private List<ListeventpicBean> listeventpic;
        private List<ListeventpicwallBean> listeventpicwall;
        private List<ListsignBean> listsign;

        public Object getEventactivity_Cancel() {
            return eventactivity_Cancel;
        }

        public void setEventactivity_Cancel(Object eventactivity_Cancel) {
            this.eventactivity_Cancel = eventactivity_Cancel;
        }

        public int getEventActivity_AddressID() {
            return eventActivity_AddressID;
        }

        public void setEventActivity_AddressID(int eventActivity_AddressID) {
            this.eventActivity_AddressID = eventActivity_AddressID;
        }

        public int getListEventPicWallCount() {
            return listEventPicWallCount;
        }

        public void setListEventPicWallCount(int listEventPicWallCount) {
            this.listEventPicWallCount = listEventPicWallCount;
        }

        public int getListEventPicCount() {
            return listEventPicCount;
        }

        public void setListEventPicCount(int listEventPicCount) {
            this.listEventPicCount = listEventPicCount;
        }

        public int getSignUpNumber() {
            return signUpNumber;
        }

        public void setSignUpNumber(int signUpNumber) {
            this.signUpNumber = signUpNumber;
        }

        public int getOrderstate() {
            return orderstate;
        }

        public void setOrderstate(int orderstate) {
            this.orderstate = orderstate;
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public Object getCancelrefuse() {
            return cancelrefuse;
        }

        public void setCancelrefuse(Object cancelrefuse) {
            this.cancelrefuse = cancelrefuse;
        }

        public int getEventActivity_id() {
            return eventActivity_id;
        }

        public void setEventActivity_id(int eventActivity_id) {
            this.eventActivity_id = eventActivity_id;
        }

        public String getEventactivity_name() {
            return eventactivity_name;
        }

        public void setEventactivity_name(String eventactivity_name) {
            this.eventactivity_name = eventactivity_name;
        }

        public String getEventactivity_begintime() {
            return eventactivity_begintime;
        }

        public void setEventactivity_begintime(String eventactivity_begintime) {
            this.eventactivity_begintime = eventactivity_begintime;
        }

        public String getEventactivity_endtime() {
            return eventactivity_endtime;
        }

        public void setEventactivity_endtime(String eventactivity_endtime) {
            this.eventactivity_endtime = eventactivity_endtime;
        }

        public String getEventactivity_detail() {
            return eventactivity_detail;
        }

        public void setEventactivity_detail(String eventactivity_detail) {
            this.eventactivity_detail = eventactivity_detail;
        }

        public int getEventactivity_number() {
            return eventactivity_number;
        }

        public void setEventactivity_number(int eventactivity_number) {
            this.eventactivity_number = eventactivity_number;
        }

        public String getEventactivity_type() {
            return eventactivity_type;
        }

        public void setEventactivity_type(String eventactivity_type) {
            this.eventactivity_type = eventactivity_type;
        }

        public String getEventactivity_state() {
            return eventactivity_state;
        }

        public void setEventactivity_state(String eventactivity_state) {
            this.eventactivity_state = eventactivity_state;
        }

        public String getEventactivity_address() {
            return eventactivity_address;
        }

        public void setEventactivity_address(String eventactivity_address) {
            this.eventactivity_address = eventactivity_address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getCoachgrouplogo() {
            return coachgrouplogo;
        }

        public void setCoachgrouplogo(String coachgrouplogo) {
            this.coachgrouplogo = coachgrouplogo;
        }

        public String getCoachgroupname() {
            return coachgroupname;
        }

        public void setCoachgroupname(String coachgroupname) {
            this.coachgroupname = coachgroupname;
        }

        public Object getEventactivity_champion() {
            return eventactivity_champion;
        }

        public void setEventactivity_champion(Object eventactivity_champion) {
            this.eventactivity_champion = eventactivity_champion;
        }

        public Object getEventactivity_level() {
            return eventactivity_level;
        }

        public void setEventactivity_level(Object eventactivity_level) {
            this.eventactivity_level = eventactivity_level;
        }

        public int getEventactivity_bonus() {
            return eventactivity_bonus;
        }

        public void setEventactivity_bonus(int eventactivity_bonus) {
            this.eventactivity_bonus = eventactivity_bonus;
        }

        public int getEventactivity_bringguestsnum() {
            return eventactivity_bringguestsnum;
        }

        public void setEventactivity_bringguestsnum(int eventactivity_bringguestsnum) {
            this.eventactivity_bringguestsnum = eventactivity_bringguestsnum;
        }

        public boolean isEventactivity_ifbringguests() {
            return eventactivity_ifbringguests;
        }

        public void setEventactivity_ifbringguests(boolean eventactivity_ifbringguests) {
            this.eventactivity_ifbringguests = eventactivity_ifbringguests;
        }

        public String getEventactivity_kickofftime() {
            return eventactivity_kickofftime;
        }

        public void setEventactivity_kickofftime(String eventactivity_kickofftime) {
            this.eventactivity_kickofftime = eventactivity_kickofftime;
        }

        public boolean isEventactivity_ifpublicly() {
            return eventactivity_ifpublicly;
        }

        public void setEventactivity_ifpublicly(boolean eventactivity_ifpublicly) {
            this.eventactivity_ifpublicly = eventactivity_ifpublicly;
        }

        public String getEventactivity_pwd() {
            return eventactivity_pwd;
        }

        public void setEventactivity_pwd(String eventactivity_pwd) {
            this.eventactivity_pwd = eventactivity_pwd;
        }

        public String getEventactivity_publisherlogo() {
            return eventactivity_publisherlogo;
        }

        public void setEventactivity_publisherlogo(String eventactivity_publisherlogo) {
            this.eventactivity_publisherlogo = eventactivity_publisherlogo;
        }

        public int getEventactivity_publisherid() {
            return eventactivity_publisherid;
        }

        public void setEventactivity_publisherid(int eventactivity_publisherid) {
            this.eventactivity_publisherid = eventactivity_publisherid;
        }

        public String getEventactivity_publishertel() {
            return eventactivity_publishertel;
        }

        public void setEventactivity_publishertel(String eventactivity_publishertel) {
            this.eventactivity_publishertel = eventactivity_publishertel;
        }

        public String getEventactivity_publishername() {
            return eventactivity_publishername;
        }

        public void setEventactivity_publishername(String eventactivity_publishername) {
            this.eventactivity_publishername = eventactivity_publishername;
        }

        public String getEventactivity_costtype() {
            return eventactivity_costtype;
        }

        public void setEventactivity_costtype(String eventactivity_costtype) {
            this.eventactivity_costtype = eventactivity_costtype;
        }

        public double getEventactivity_prepayment() {
            return eventactivity_prepayment;
        }

        public void setEventactivity_prepayment(double eventactivity_prepayment) {
            this.eventactivity_prepayment = eventactivity_prepayment;
        }

        public double getEventactivity_cost() {
            return eventactivity_cost;
        }

        public void setEventactivity_cost(double eventactivity_cost) {
            this.eventactivity_cost = eventactivity_cost;
        }

        public double getEventactivity_guestscost() {
            return eventactivity_guestscost;
        }

        public void setEventactivity_guestscost(double eventactivity_guestscost) {
            this.eventactivity_guestscost = eventactivity_guestscost;
        }

        public int getEventactivity_binarynumber() {
            return eventactivity_binarynumber;
        }

        public void setEventactivity_binarynumber(int eventactivity_binarynumber) {
            this.eventactivity_binarynumber = eventactivity_binarynumber;
        }

        public String getEventactivity_signupbegintime() {
            return eventactivity_signupbegintime;
        }

        public void setEventactivity_signupbegintime(String eventactivity_signupbegintime) {
            this.eventactivity_signupbegintime = eventactivity_signupbegintime;
        }

        public String getEventactivity_signupendtime() {
            return eventactivity_signupendtime;
        }

        public void setEventactivity_signupendtime(String eventactivity_signupendtime) {
            this.eventactivity_signupendtime = eventactivity_signupendtime;
        }

        public String getEventactivity_organizer() {
            return eventactivity_organizer;
        }

        public void setEventactivity_organizer(String eventactivity_organizer) {
            this.eventactivity_organizer = eventactivity_organizer;
        }

        public String getEventactivity_organizertel() {
            return eventactivity_organizertel;
        }

        public void setEventactivity_organizertel(String eventactivity_organizertel) {
            this.eventactivity_organizertel = eventactivity_organizertel;
        }

        public boolean isEventactivity_ifshowsignname() {
            return eventactivity_ifshowsignname;
        }

        public void setEventactivity_ifshowsignname(boolean eventactivity_ifshowsignname) {
            this.eventactivity_ifshowsignname = eventactivity_ifshowsignname;
        }

        public boolean isEventactivity_ifshowphotowall() {
            return eventactivity_ifshowphotowall;
        }

        public void setEventactivity_ifshowphotowall(boolean eventactivity_ifshowphotowall) {
            this.eventactivity_ifshowphotowall = eventactivity_ifshowphotowall;
        }

        public int getPga_id() {
            return pga_id;
        }

        public void setPga_id(int pga_id) {
            this.pga_id = pga_id;
        }

        public Object getEventactivity_createtime() {
            return eventactivity_createtime;
        }

        public void setEventactivity_createtime(Object eventactivity_createtime) {
            this.eventactivity_createtime = eventactivity_createtime;
        }

        public double getEventactivity_guestprepayment() {
            return eventactivity_guestprepayment;
        }

        public void setEventactivity_guestprepayment(double eventactivity_guestprepayment) {
            this.eventactivity_guestprepayment = eventactivity_guestprepayment;
        }

        public String getEventactivity_guestcosttype() {
            return eventactivity_guestcosttype;
        }

        public void setEventactivity_guestcosttype(String eventactivity_guestcosttype) {
            this.eventactivity_guestcosttype = eventactivity_guestcosttype;
        }

        public int getEventactivity_teamid() {
            return eventactivity_teamid;
        }

        public void setEventactivity_teamid(int eventactivity_teamid) {
            this.eventactivity_teamid = eventactivity_teamid;
        }

        public int getWithdrawalsstatus() {
            return withdrawalsstatus;
        }

        public void setWithdrawalsstatus(int withdrawalsstatus) {
            this.withdrawalsstatus = withdrawalsstatus;
        }

        public List<?> getListPic() {
            return listPic;
        }

        public void setListPic(List<?> listPic) {
            this.listPic = listPic;
        }

        public List<?> getListSignUp() {
            return listSignUp;
        }

        public void setListSignUp(List<?> listSignUp) {
            this.listSignUp = listSignUp;
        }

        public List<ListeventpicBean> getListeventpic() {
            return listeventpic;
        }

        public void setListeventpic(List<ListeventpicBean> listeventpic) {
            this.listeventpic = listeventpic;
        }

        public List<ListeventpicwallBean> getListeventpicwall() {
            return listeventpicwall;
        }

        public void setListeventpicwall(List<ListeventpicwallBean> listeventpicwall) {
            this.listeventpicwall = listeventpicwall;
        }

        public List<ListsignBean> getListsign() {
            return listsign;
        }

        public void setListsign(List<ListsignBean> listsign) {
            this.listsign = listsign;
        }

        @Override
        public int getType() {
            if (listeventpic != null && listeventpic.size() > 1) {
                return 1;
            } else {
                return 0;//只有一张赛事图片
            }
        }

        public static class ListeventpicBean {
            /**
             * eid : 397
             * eventactivity_picpath : http://glorygolflife.oss-cn-shenzhen.aliyuncs.com/Event/1/glof(4987).jpg
             * eventActivity_id : 1
             * eventactivity_type : 1
             * user_name : null
             * user_id : 0
             * upload_time : 2017-04-17T11:21:58
             */

            private int eid;
            private String eventactivity_picpath;
            private int eventactivity_id;
            private String eventactivity_type;
            private Object user_name;
            private int user_id;
            private String upload_time;

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getEventactivity_picpath() {
                return eventactivity_picpath;
            }

            public void setEventactivity_picpath(String eventactivity_picpath) {
                this.eventactivity_picpath = eventactivity_picpath;
            }

            public int getEventactivity_id() {
                return eventactivity_id;
            }

            public void setEventactivity_id(int eventactivity_id) {
                this.eventactivity_id = eventactivity_id;
            }

            public String getEventactivity_type() {
                return eventactivity_type;
            }

            public void setEventactivity_type(String eventactivity_type) {
                this.eventactivity_type = eventactivity_type;
            }

            public Object getUser_name() {
                return user_name;
            }

            public void setUser_name(Object user_name) {
                this.user_name = user_name;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUpload_time() {
                return upload_time;
            }

            public void setUpload_time(String upload_time) {
                this.upload_time = upload_time;
            }
        }

        public static class ListeventpicwallBean {
            /**
             * eid : 392
             * eventactivity_picpath : http://glorygolflife.oss-cn-shenzhen.aliyuncs.com/Event/1/000.jpg
             * eventActivity_id : 1
             * eventactivity_type : 2
             * user_name : null
             * user_id : 0
             * upload_time : 2017-03-30T13:30:34
             */

            private int eid;
            private String eventactivity_picpath;
            private int eventactivity_id;
            private String eventactivity_type;
            private Object user_name;
            private int user_id;
            private String upload_time;

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getEventactivity_picpath() {
                return eventactivity_picpath;
            }

            public void setEventactivity_picpath(String eventactivity_picpath) {
                this.eventactivity_picpath = eventactivity_picpath;
            }

            public int getEventactivity_id() {
                return eventactivity_id;
            }

            public void setEventactivity_id(int eventactivity_id) {
                this.eventactivity_id = eventactivity_id;
            }

            public String getEventactivity_type() {
                return eventactivity_type;
            }

            public void setEventactivity_type(String eventactivity_type) {
                this.eventactivity_type = eventactivity_type;
            }

            public Object getUser_name() {
                return user_name;
            }

            public void setUser_name(Object user_name) {
                this.user_name = user_name;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUpload_time() {
                return upload_time;
            }

            public void setUpload_time(String upload_time) {
                this.upload_time = upload_time;
            }
        }

        public static class ListsignBean {
            /**
             * orderid : 0
             * eventActivity_id : 0
             * sign_list : [{"signup_id":584,"sign_activitiesname":"丰顺高尔夫球队季度赛","sign_activitiesid":1,"sign_activitiestype":"2","sign_phone":"11","sign_logo":null,"sign_name":"1","sign_registrationfee":null,"sign_almost":0,"sign_ifbringguests":null,"sign_number":null,"sign_sex":"2","sign_cardnumber":null,"sign_company":null,"sign_team":null,"sign_club":null,"sign_member":null,"sign_state":"1","sign_upstate":"1","sign_datetime":"2017-03-30T09:08:14","sign_refuse":null,"sign_voucher":"636264616943857998","sign_ifallow":false,"sign_isguset":true,"sign_mainid":583,"userid":32,"sign_GuestRoom":"1","sign_Remark":null,"cancelCategory":"0"}]
             * order : null
             * eventActivity : null
             * eventPicList : null
             * eventPicWallList : null
             * sigUpNumber : 0
             * signup_id : 583
             * sign_activitiesname : 丰顺高尔夫球队季度赛
             * sign_activitiesid : 1
             * sign_activitiestype : 2
             * sign_phone : 18680178812
             * sign_logo : null
             * sign_name : 1
             * sign_registrationfee : null
             * sign_almost : 0
             * sign_ifbringguests : true
             * sign_number : 5
             * sign_sex : 1
             * sign_cardnumber : null
             * sign_company : null
             * sign_team : null
             * sign_club : null
             * sign_member : null
             * sign_state : 1
             * sign_upstate : 1
             * sign_datetime : 2017-03-30T09:08:14
             * sign_refuse : null
             * sign_voucher : 636264616943857998
             * sign_ifallow : false
             * sign_isguset : false
             * sign_mainid : 0
             * userid : null
             * sign_GuestRoom : null
             * sign_Remark : null
             * cancelCategory : 0
             */

            private int orderid;
            private int eventActivity_id;
            private Object order;
            private Object eventActivity;
            private Object eventPicList;
            private Object eventPicWallList;
            private int sigUpNumber;
            private int signup_id;
            private String sign_activitiesname;
            private int sign_activitiesid;
            private String sign_activitiestype;
            private String sign_phone;
            private Object sign_logo;
            private String sign_name;
            private Object sign_registrationfee;
            private double sign_almost;
            private boolean sign_ifbringguests;
            private int sign_number;
            private String sign_sex;
            private Object sign_cardnumber;
            private Object sign_company;
            private Object sign_team;
            private Object sign_club;
            private Object sign_member;
            private String sign_state;
            private String sign_upstate;
            private String sign_datetime;
            private Object sign_refuse;
            private String sign_voucher;
            private boolean sign_ifallow;
            private boolean sign_isguset;
            private int sign_mainid;
            private Object userid;
            private Object sign_GuestRoom;
            private Object sign_Remark;
            private String cancelCategory;
            private List<SignListBean> sign_list;

            public int getOrderid() {
                return orderid;
            }

            public void setOrderid(int orderid) {
                this.orderid = orderid;
            }

            public int getEventActivity_id() {
                return eventActivity_id;
            }

            public void setEventActivity_id(int eventActivity_id) {
                this.eventActivity_id = eventActivity_id;
            }

            public Object getOrder() {
                return order;
            }

            public void setOrder(Object order) {
                this.order = order;
            }

            public Object getEventActivity() {
                return eventActivity;
            }

            public void setEventActivity(Object eventActivity) {
                this.eventActivity = eventActivity;
            }

            public Object getEventPicList() {
                return eventPicList;
            }

            public void setEventPicList(Object eventPicList) {
                this.eventPicList = eventPicList;
            }

            public Object getEventPicWallList() {
                return eventPicWallList;
            }

            public void setEventPicWallList(Object eventPicWallList) {
                this.eventPicWallList = eventPicWallList;
            }

            public int getSigUpNumber() {
                return sigUpNumber;
            }

            public void setSigUpNumber(int sigUpNumber) {
                this.sigUpNumber = sigUpNumber;
            }

            public int getSignup_id() {
                return signup_id;
            }

            public void setSignup_id(int signup_id) {
                this.signup_id = signup_id;
            }

            public String getSign_activitiesname() {
                return sign_activitiesname;
            }

            public void setSign_activitiesname(String sign_activitiesname) {
                this.sign_activitiesname = sign_activitiesname;
            }

            public int getSign_activitiesid() {
                return sign_activitiesid;
            }

            public void setSign_activitiesid(int sign_activitiesid) {
                this.sign_activitiesid = sign_activitiesid;
            }

            public String getSign_activitiestype() {
                return sign_activitiestype;
            }

            public void setSign_activitiestype(String sign_activitiestype) {
                this.sign_activitiestype = sign_activitiestype;
            }

            public String getSign_phone() {
                return sign_phone;
            }

            public void setSign_phone(String sign_phone) {
                this.sign_phone = sign_phone;
            }

            public Object getSign_logo() {
                return sign_logo;
            }

            public void setSign_logo(Object sign_logo) {
                this.sign_logo = sign_logo;
            }

            public String getSign_name() {
                return sign_name;
            }

            public void setSign_name(String sign_name) {
                this.sign_name = sign_name;
            }

            public Object getSign_registrationfee() {
                return sign_registrationfee;
            }

            public void setSign_registrationfee(Object sign_registrationfee) {
                this.sign_registrationfee = sign_registrationfee;
            }

            public double getSign_almost() {
                return sign_almost;
            }

            public void setSign_almost(double sign_almost) {
                this.sign_almost = sign_almost;
            }

            public boolean isSign_ifbringguests() {
                return sign_ifbringguests;
            }

            public void setSign_ifbringguests(boolean sign_ifbringguests) {
                this.sign_ifbringguests = sign_ifbringguests;
            }

            public int getSign_number() {
                return sign_number;
            }

            public void setSign_number(int sign_number) {
                this.sign_number = sign_number;
            }

            public String getSign_sex() {
                return sign_sex;
            }

            public void setSign_sex(String sign_sex) {
                this.sign_sex = sign_sex;
            }

            public Object getSign_cardnumber() {
                return sign_cardnumber;
            }

            public void setSign_cardnumber(Object sign_cardnumber) {
                this.sign_cardnumber = sign_cardnumber;
            }

            public Object getSign_company() {
                return sign_company;
            }

            public void setSign_company(Object sign_company) {
                this.sign_company = sign_company;
            }

            public Object getSign_team() {
                return sign_team;
            }

            public void setSign_team(Object sign_team) {
                this.sign_team = sign_team;
            }

            public Object getSign_club() {
                return sign_club;
            }

            public void setSign_club(Object sign_club) {
                this.sign_club = sign_club;
            }

            public Object getSign_member() {
                return sign_member;
            }

            public void setSign_member(Object sign_member) {
                this.sign_member = sign_member;
            }

            public String getSign_state() {
                return sign_state;
            }

            public void setSign_state(String sign_state) {
                this.sign_state = sign_state;
            }

            public String getSign_upstate() {
                return sign_upstate;
            }

            public void setSign_upstate(String sign_upstate) {
                this.sign_upstate = sign_upstate;
            }

            public String getSign_datetime() {
                return sign_datetime;
            }

            public void setSign_datetime(String sign_datetime) {
                this.sign_datetime = sign_datetime;
            }

            public Object getSign_refuse() {
                return sign_refuse;
            }

            public void setSign_refuse(Object sign_refuse) {
                this.sign_refuse = sign_refuse;
            }

            public String getSign_voucher() {
                return sign_voucher;
            }

            public void setSign_voucher(String sign_voucher) {
                this.sign_voucher = sign_voucher;
            }

            public boolean isSign_ifallow() {
                return sign_ifallow;
            }

            public void setSign_ifallow(boolean sign_ifallow) {
                this.sign_ifallow = sign_ifallow;
            }

            public boolean isSign_isguset() {
                return sign_isguset;
            }

            public void setSign_isguset(boolean sign_isguset) {
                this.sign_isguset = sign_isguset;
            }

            public int getSign_mainid() {
                return sign_mainid;
            }

            public void setSign_mainid(int sign_mainid) {
                this.sign_mainid = sign_mainid;
            }

            public Object getUserid() {
                return userid;
            }

            public void setUserid(Object userid) {
                this.userid = userid;
            }

            public Object getSign_GuestRoom() {
                return sign_GuestRoom;
            }

            public void setSign_GuestRoom(Object sign_GuestRoom) {
                this.sign_GuestRoom = sign_GuestRoom;
            }

            public Object getSign_Remark() {
                return sign_Remark;
            }

            public void setSign_Remark(Object sign_Remark) {
                this.sign_Remark = sign_Remark;
            }

            public String getCancelCategory() {
                return cancelCategory;
            }

            public void setCancelCategory(String cancelCategory) {
                this.cancelCategory = cancelCategory;
            }

            public List<SignListBean> getSign_list() {
                return sign_list;
            }

            public void setSign_list(List<SignListBean> sign_list) {
                this.sign_list = sign_list;
            }

            public static class SignListBean {
                /**
                 * signup_id : 584
                 * sign_activitiesname : 丰顺高尔夫球队季度赛
                 * sign_activitiesid : 1
                 * sign_activitiestype : 2
                 * sign_phone : 11
                 * sign_logo : null
                 * sign_name : 1
                 * sign_registrationfee : null
                 * sign_almost : 0
                 * sign_ifbringguests : null
                 * sign_number : null
                 * sign_sex : 2
                 * sign_cardnumber : null
                 * sign_company : null
                 * sign_team : null
                 * sign_club : null
                 * sign_member : null
                 * sign_state : 1
                 * sign_upstate : 1
                 * sign_datetime : 2017-03-30T09:08:14
                 * sign_refuse : null
                 * sign_voucher : 636264616943857998
                 * sign_ifallow : false
                 * sign_isguset : true
                 * sign_mainid : 583
                 * userid : 32
                 * sign_GuestRoom : 1
                 * sign_Remark : null
                 * cancelCategory : 0
                 */

                private int signup_id;
                private String sign_activitiesname;
                private int sign_activitiesid;
                private String sign_activitiestype;
                private String sign_phone;
                private Object sign_logo;
                private String sign_name;
                private Object sign_registrationfee;
                private double sign_almost;
                private Object sign_ifbringguests;
                private Object sign_number;
                private String sign_sex;
                private Object sign_cardnumber;
                private Object sign_company;
                private Object sign_team;
                private Object sign_club;
                private Object sign_member;
                private String sign_state;
                private String sign_upstate;
                private String sign_datetime;
                private Object sign_refuse;
                private String sign_voucher;
                private boolean sign_ifallow;
                private boolean sign_isguset;
                private int sign_mainid;
                private int userid;
                private String sign_GuestRoom;
                private Object sign_Remark;
                private String cancelCategory;

                public int getSignup_id() {
                    return signup_id;
                }

                public void setSignup_id(int signup_id) {
                    this.signup_id = signup_id;
                }

                public String getSign_activitiesname() {
                    return sign_activitiesname;
                }

                public void setSign_activitiesname(String sign_activitiesname) {
                    this.sign_activitiesname = sign_activitiesname;
                }

                public int getSign_activitiesid() {
                    return sign_activitiesid;
                }

                public void setSign_activitiesid(int sign_activitiesid) {
                    this.sign_activitiesid = sign_activitiesid;
                }

                public String getSign_activitiestype() {
                    return sign_activitiestype;
                }

                public void setSign_activitiestype(String sign_activitiestype) {
                    this.sign_activitiestype = sign_activitiestype;
                }

                public String getSign_phone() {
                    return sign_phone;
                }

                public void setSign_phone(String sign_phone) {
                    this.sign_phone = sign_phone;
                }

                public Object getSign_logo() {
                    return sign_logo;
                }

                public void setSign_logo(Object sign_logo) {
                    this.sign_logo = sign_logo;
                }

                public String getSign_name() {
                    return sign_name;
                }

                public void setSign_name(String sign_name) {
                    this.sign_name = sign_name;
                }

                public Object getSign_registrationfee() {
                    return sign_registrationfee;
                }

                public void setSign_registrationfee(Object sign_registrationfee) {
                    this.sign_registrationfee = sign_registrationfee;
                }

                public double getSign_almost() {
                    return sign_almost;
                }

                public void setSign_almost(double sign_almost) {
                    this.sign_almost = sign_almost;
                }

                public Object getSign_ifbringguests() {
                    return sign_ifbringguests;
                }

                public void setSign_ifbringguests(Object sign_ifbringguests) {
                    this.sign_ifbringguests = sign_ifbringguests;
                }

                public Object getSign_number() {
                    return sign_number;
                }

                public void setSign_number(Object sign_number) {
                    this.sign_number = sign_number;
                }

                public String getSign_sex() {
                    return sign_sex;
                }

                public void setSign_sex(String sign_sex) {
                    this.sign_sex = sign_sex;
                }

                public Object getSign_cardnumber() {
                    return sign_cardnumber;
                }

                public void setSign_cardnumber(Object sign_cardnumber) {
                    this.sign_cardnumber = sign_cardnumber;
                }

                public Object getSign_company() {
                    return sign_company;
                }

                public void setSign_company(Object sign_company) {
                    this.sign_company = sign_company;
                }

                public Object getSign_team() {
                    return sign_team;
                }

                public void setSign_team(Object sign_team) {
                    this.sign_team = sign_team;
                }

                public Object getSign_club() {
                    return sign_club;
                }

                public void setSign_club(Object sign_club) {
                    this.sign_club = sign_club;
                }

                public Object getSign_member() {
                    return sign_member;
                }

                public void setSign_member(Object sign_member) {
                    this.sign_member = sign_member;
                }

                public String getSign_state() {
                    return sign_state;
                }

                public void setSign_state(String sign_state) {
                    this.sign_state = sign_state;
                }

                public String getSign_upstate() {
                    return sign_upstate;
                }

                public void setSign_upstate(String sign_upstate) {
                    this.sign_upstate = sign_upstate;
                }

                public String getSign_datetime() {
                    return sign_datetime;
                }

                public void setSign_datetime(String sign_datetime) {
                    this.sign_datetime = sign_datetime;
                }

                public Object getSign_refuse() {
                    return sign_refuse;
                }

                public void setSign_refuse(Object sign_refuse) {
                    this.sign_refuse = sign_refuse;
                }

                public String getSign_voucher() {
                    return sign_voucher;
                }

                public void setSign_voucher(String sign_voucher) {
                    this.sign_voucher = sign_voucher;
                }

                public boolean isSign_ifallow() {
                    return sign_ifallow;
                }

                public void setSign_ifallow(boolean sign_ifallow) {
                    this.sign_ifallow = sign_ifallow;
                }

                public boolean isSign_isguset() {
                    return sign_isguset;
                }

                public void setSign_isguset(boolean sign_isguset) {
                    this.sign_isguset = sign_isguset;
                }

                public int getSign_mainid() {
                    return sign_mainid;
                }

                public void setSign_mainid(int sign_mainid) {
                    this.sign_mainid = sign_mainid;
                }

                public int getUserid() {
                    return userid;
                }

                public void setUserid(int userid) {
                    this.userid = userid;
                }

                public String getSign_GuestRoom() {
                    return sign_GuestRoom;
                }

                public void setSign_GuestRoom(String sign_GuestRoom) {
                    this.sign_GuestRoom = sign_GuestRoom;
                }

                public Object getSign_Remark() {
                    return sign_Remark;
                }

                public void setSign_Remark(Object sign_Remark) {
                    this.sign_Remark = sign_Remark;
                }

                public String getCancelCategory() {
                    return cancelCategory;
                }

                public void setCancelCategory(String cancelCategory) {
                    this.cancelCategory = cancelCategory;
                }
            }
        }
    }
}
