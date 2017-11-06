package com.glorystudent.golflife.api;

/**
 * TODO 请求的URL
 * Created by Gavin.J on 2017/10/24.
 */

public interface ConstantsURL {
    String BASE_URL2 = "http://app.pgagolf.cn";
    String BASE_URL3 = "https://192.168.1.168:4431";
    String BASE_URL4 = "http://192.168.1.199:8000";
    String BASE_URL = BASE_URL4;//请求URL
    //赛事活动默认的图片
    String  imgUrl="http://glorygolflife.oss-cn-shenzhen.aliyuncs.com/XXQ2Lsd0yq3b72wWdapS4a5Rg24MhxF6cxEOYyODBFQ%3D%2Fcompetitions%2Fbanners%2Faf2026710e2859c421fc19f67eca1fc8.png";

    String GetSMSCheck=BASE_URL+"/api/APISMSCheck/GetSMSCheck";//获取虚拟短信验证码接口URL
    String SMSLogin=BASE_URL+ "/api/APISMSLogin/SMSLogin";//登录接口URL
    String SERVICE_TERMS = BASE_URL + "/home/license";//服务条款URL
    String QueryAD = BASE_URL +"/api/APIAD/QueryAD";//获取广告URL
    String QueryNews = BASE_URL +"/api/APINews/QueryNews";//获取新闻URL
    String NEWS_URL = BASE_URL + "/home/newsDetail?id=%d";//新闻详情
    String QueryEventActivity =BASE_URL+ "/Public/APIPublicEventActivity/QueryEventActivity";//活动赛事URL
    String EVENT_SHARE_URL = BASE_URL + "/Activity/eventActivities?eventActivity_id=%d";//分享赛事活动URL
    String EVENT_DETAIL_URL = BASE_URL + "/ActivityAPP/EventActivities?eventActivity_id=%d&userId=%s&isAndroid=Android";//查看赛事活动URL
//    String EVENT_DETAIL_URL ="http://www.pgagolf.cn/ActivityAPP/EventActivities?eventActivity_id=83&userId=sZDlfuhZvDYchAc0GqKrmw==&isIos=Android";//查看赛事活动URL
    String WXPayAPP= BASE_URL + "/Activity/WXPayAPP";//微信统一下单URL
    String BindingWX=BASE_URL+"/api/APIWXLogin/BindingWX";//绑定微信URL
    String QueryUser=BASE_URL+"/Public/APIPublicUser/QueryUser";//获取用户信息URL
    String QuerySignUpByMy=BASE_URL+"/api/APISignUp/QuerySignUpByMy";//获取我参加的赛事活动URL
    String EVENT_PREVIEW_URL = BASE_URL + "/ActivityAPP/EventActivitiesShow?eventActivity_id=%d";//预览赛事活动URL
    String QueryEventActivityByID=BASE_URL+"/Public/APIPublicEventActivity/QueryEventActivityByID";//通过ID获取赛事活动数据URL
    String QuerySignDefinition=BASE_URL+"/api/APISignDefinition/QuerySignDefinition";////新增时请求网络获取定义信息URL
    String QueryCourt=BASE_URL+"/public/APIPublicCourt/QueryCourt";//获取附近球场位置信息URL
    String EditEventActivity=BASE_URL+"/Public/APIPublicEventActivity/EditEventActivity";//修改赛事活动URL
    String AddEventActivity=BASE_URL+"/Public/APIPublicEventActivity/AddEventActivity";//新增活动赛事URL
    String SelectAllByOSS=BASE_URL+ "/api/APISettings/SelectAllByOSS";//AliyunOSS请求URL
    String QueryEventActivityByEventActivityID=BASE_URL+"/Public/APIPublicEventActivity/QueryEventActivityByEventActivityID";//查看活动详情URL
    String CancelEventActivity=BASE_URL+"/Public/APIPublicEventActivity/CancelEventActivity";//取消赛事活动URL
    String QueryEventActivityPicWall=BASE_URL+"/Public/APIPublicEventActivityPic/QueryEventActivityPicWall";//获取照片墙数据URL
    String AddEventActivityPic=BASE_URL+"/Public/APIPublicEventActivityPic/AddEventActivityPic";//赛事活动图片添加URL
    String AgreeSignUp=BASE_URL+"/api/APISignUp/AgreeSignUp";//是否允许报名赛事活动URL
    String WXPayReFundAPP=BASE_URL+"/api/APIWXPay/WXPayReFundAPP";//取消赛事报名URL
    String EditSignUp=BASE_URL+"/api/APISignUp/EditSignUp";//签到URL
    String QuerySignUpByEventID=BASE_URL+"/api/APISignUp/QuerySignUpByEventID";//通过赛事ID获取对应的报名名单URL
    String QueryTeam=BASE_URL+"/api/APITeam/QueryTeam";//获取球队URL
    String QueryEventActivityVoucher=BASE_URL+"/Public/APIPublicEventActivity/QueryEventActivityVoucher";//获取报名凭证信息URL
    String CreateTeam=BASE_URL+"/api/APITeam/CreateTeam";//创建球队URL
    String QueryTeamDetail=BASE_URL+"/api/APITeam/QueryTeamDetail";//获取球队详情URL
    String SignOutTeam=BASE_URL+"/api/APITeam/SignOutTeam";//退队或球队解散URL
    String EditTeamInfo=BASE_URL+"/api/APITeam/EditTeamInfo";//更改球队队长URL
    String QueryTeamUser="/api/APITeam/QueryTeamUser";//获取球队成员URL
    String QueryFriend=BASE_URL+"/api/APIFriends/QueryFriend";//根据队员手机号搜索URL
    String EditApplyFriends=BASE_URL+"/api/APIApplyFriends/EditApplyFriends";//更改好友申请状态URL
    String DeleteFriends=BASE_URL+"/api/APIFriends/DeleteFriends";//删除好友URL
    String AddApplyFriends=BASE_URL+"/api/APIApplyFriends/AddApplyFriends";//添加申请好友URL
    String EditFriends=BASE_URL+"/api/APIFriends/EditFriends";//修改好友信息URL
    String AddTeam=BASE_URL+"/api/APITeam/AddTeam";//申请加入球队URL
    String QueryTeamEventActivityPhoto=BASE_URL+"/api/APITeam/QueryTeamEventActivityPhoto";//获取球队相册URL
    String QueryTeamEventActivityPhotoDeatil=BASE_URL+"/api/APITeam/QueryTeamEventActivityPhotoDeatil";//获取球队相册详情URL
    String EditTeamUserApply=BASE_URL+"/api/APITeam/EditTeamUserApply";//申请入球队处理URL
    String QueryTeamUserApply=BASE_URL+"/api/APITeam/QueryTeamUserApply";//获取申请入队数据URL
    String SweepCodeSignUp=BASE_URL+"/api/APISignUp/SweepCodeSignUp";//签到URL
    String EditUser=BASE_URL+"/api/APIUser/EditUser";//编辑用户信息URL
    String QueryChinaCity=BASE_URL+"/api/APIChinaCity/QueryChinaCity";//获取城市区域信息URL
    String AddFeedBack=BASE_URL+"/api/APIFeedBack/AddFeedBack";//意见反馈URL
    String ABOUT_US_URL = "https://www.pgagolf.cn/home/about?VersionNumber=%s";//关于我们URL
    String GetUserApplyCashInfo=BASE_URL+"/api/APIApplyCash/GetUserApplyCashInfo";//获取用户钱包资金信息URL
    String QueryUserBank=BASE_URL+"/api/APIUserBankInfo/QueryUserBank";//获取用户银行卡信息URL
    String DeleteUserBank=BASE_URL+"/api/APIUserBankInfo/DeleteUserBank";//删除用户银行卡信息URL
    String AddUserBank=BASE_URL+"/api/APIUserBankInfo/AddUserBank";//添加银行卡信息URL
    String QueryApplyCash=BASE_URL+"/api/APIApplyCash/QueryApplyCash";//获取提现记录URL
    String UpdateApplyCash=BASE_URL+"/api/APIApplyCash/UpdateApplyCash";//更新提现申请状态URL
    String GetApplyCash=BASE_URL+"/api/APIApplyCash/GetApplyCash";//获取提现记录详情URL
    String AddApplyCash=BASE_URL+"/api/APIApplyCash/AddApplyCash";//提现申请URL
    String QueryCollect=BASE_URL+"/api/APICollect/QueryCollect";//获取收藏数据URL
    String QueryTVideoComment=BASE_URL+"/api/APITVideoComment/QueryTVideoComment";//获取视频评论URL
    String AddCollect=BASE_URL+"/api/APICollect/AddCollect";//添加收藏URL
    String DeleteCollect=BASE_URL+"/api/APICollect/DeleteCollect";//取消收藏URL
    String  share_video="http://www.pgagolf.cn/home/video?variable=";//分享视频URL
    String AddTVideoComment=BASE_URL+"/api/APITVideoComment/AddTVideoComment";//添加视频评论URL
    String EditTVideoCommentBy=BASE_URL+"/api/APITVideoComment/EditTVideoCommentBy";//修改视频点赞URL
    String QueryUsersByUserID=BASE_URL+"/api/APIUser/QueryUsersByUserID";//通过id查询用户URL
    String QueryFriends=BASE_URL+"/api/APIFriends/QueryFriends";//获取所有好友URL
}
