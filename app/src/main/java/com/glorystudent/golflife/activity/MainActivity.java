package com.glorystudent.golflife.activity;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.dialog.iosdialog.AlertDialog;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.database.MyDataBase;
import com.glorystudent.golflife.entity.ApplyFriendsEntity;
import com.glorystudent.golflife.entity.ApplyInfoEntity;
import com.glorystudent.golflife.entity.FriendEntity;
import com.glorystudent.golflife.entity.FriendsRequestEntity;
import com.glorystudent.golflife.entity.GolfFriendsEntity;
import com.glorystudent.golflife.entity.UserInformationEntity;
import com.glorystudent.golflife.entity.UserRequestEntity;
import com.glorystudent.golflife.fragment.ChatFragment;
import com.glorystudent.golflife.fragment.StudentHomeFragment;
import com.glorystudent.golflife.fragment.StudentMyFragment;
import com.glorystudent.golflife.fragment.StudentVideoFragment;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.google.gson.Gson;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private final static String TAG = "MainActivity";
    @Bind(R.id.rb_home)
    RadioButton rbHome;
    @Bind(R.id.rb_video)
    RadioButton rbVideo;
    @Bind(R.id.rb_chat)
    RadioButton rbChat;
    @Bind(R.id.rb_my)
    RadioButton rbMy;
    @Bind(R.id.rg)
    RadioGroup rg;
    @Bind(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @Bind(R.id.iv_rec)
    ImageView ivRec;
    @Bind(R.id.fragment)
    FrameLayout fragment;
    @Bind(R.id.tv_message_num)
    public TextView tv_message_num;

    private MyDataBase myDataBase;
    private SQLiteDatabase sqLiteDatabase;
    private int page = 1;//查询好友申请的页码
    private int count;

    private Map<String, EMConversation> conversations;
    private final static int INVITED = 0x081;
    private final static int MESSAGE = 0x092;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INVITED:
                    String phonenumber = (String) msg.obj;
                    Cursor cursor4 = null;
                    if (sqLiteDatabase != null) {
                        cursor4 = sqLiteDatabase.rawQuery("select * from golffriends where phonenumber = ?", new String[]{phonenumber});
                    }
                    String nickname = null;
                    if (cursor4 != null) {
                        while (cursor4.moveToNext()) {
                            nickname = cursor4.getString(cursor4.getColumnIndex("username"));
                        }
                    }
                    if (nickname == null) {
                        getUserName(phonenumber);
                    } else {
                        if (SharedUtil.getBoolean(Constants.NOTIFICATION)) {
                            setNotificationBuilder("高尔夫人生", nickname + "申请添加您为好友");
                        }
                        setAddressBookState(phonenumber);
                    }
                    break;
                case MESSAGE:
                    //接收到消息
                    EMMessage message = (EMMessage) msg.obj;
                    String phone = message.getFrom();
                    Cursor cursor3 = sqLiteDatabase.rawQuery("select * from golffriends where phonenumber = ?", new String[]{phone});
                    String username = " ";
                    if (cursor3 != null) {
                        while (cursor3.moveToNext()) {
                            username = cursor3.getString(cursor3.getColumnIndex("username"));
                        }
                    }

                    if (phone.equals("00000")) {
                        username = "系统消息";
                    }

                    String type = message.getType().toString();
                    String subText = null;

                    switch (type) {
                        case "TXT":
                            String text = message.getBody().toString();
                            int position = text.indexOf("\"");
                            subText = text.substring(position + 1, text.length() - 1);
                            break;
                        case "IMAGE":
                            Map<String, Object> ext = message.ext();
                            if (ext != null && !ext.isEmpty()) {
                                subText = "[视频]";
                            } else {
                                subText = "[图片]";
                            }
                            break;
                        case "VOICE":
                            subText = "[语音]";
                            break;
                    }

                    if (SharedUtil.getBoolean(Constants.NOTIFICATION)) {
                        setNotificationBuilder(username, subText);
                    }

                    setUnRead();
                    EventBus.getDefault().post(message);
                    break;
            }
        }
    };

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        //创建需要用到的数据库
        myDataBase = MyDataBase.getInstance(MainActivity.this);
        sqLiteDatabase = myDataBase.getReadableDatabase();
        SharedUtil.putBoolean(Constants.NOTIFICATION, true);
        getGolfFriends();
        EMClientInstanceListener();

        getNewsFriends();

        count = 0;
        setUnRead();

        rg.setOnCheckedChangeListener(this);
        rg.getChildAt(0).performClick();//模拟点击首页
    }

    /**
     * TODO 初始化环信链接监听
     */
    private void EMClientInstanceListener(){

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
            }

            @Override
            public void onContactDeleted(String s) {

            }

            @Override
            public void onContactInvited(String s, String s1) {
                Message message = new Message();
                message.what = INVITED;
                message.obj = s;
                handler.sendMessage(message);
            }

            @Override
            public void onFriendRequestAccepted(String s) {

            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }
        });

        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                Log.d(TAG, "onMessageReceived: ---->收到消息");
                Message message = new Message();
                message.what = MESSAGE;
                message.obj = list.get(list.size() - 1);
                handler.sendMessage(message);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                Log.d(TAG, "onMessageChanged: ---->收到透传消息");
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                Log.d(TAG, "onMessageChanged: ---->收到已读回执");
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                Log.d(TAG, "onMessageChanged: ---->收到已送达回执");
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                Log.d(TAG, "onMessageChanged: ---->消息状态变动");
            }
        });
    }
    /**
     * todo  设置未读消息
     */
    private void setUnRead() {
        count = 0;
        conversations = EMClient.getInstance().chatManager().getAllConversations();
        Iterator<Map.Entry<String, EMConversation>> iterator = conversations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, EMConversation> next = iterator.next();
            int unreadMsgCount = next.getValue().getUnreadMsgCount();
            count = count + unreadMsgCount;
        }
        if (count > 0) {
            tv_message_num.setText(count + "");
            tv_message_num.setVisibility(View.VISIBLE);
        } else {
            tv_message_num.setVisibility(View.GONE);
        }
    }
    /**
     * TODO 监听底部导航栏
     * 监听底部导航栏
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //设置底部导航栏字体颜色
        RadioButton rb = (RadioButton) group.findViewWithTag("checked");
        if (rb != null) {
            rb.setTextColor(rb.getResources().getColor(R.color.colorWhite));
            rb.setTag(null);
        }
        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
        radioButton.setTag("checked");
        radioButton.setTextColor(radioButton.getResources().getColor(R.color.colorOrange));
        switch (checkedId) {
            case R.id.rb_home:
                //首页模块
                showFragment(R.id.fragment, new StudentHomeFragment());
                break;
            case R.id.rb_video:
                //视频模块
                if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {
                    showFragment(R.id.fragment, new StudentVideoFragment());
                } else {
                    showLogin();
                    rg.getChildAt(0).performClick();
                }
                break;
            case R.id.rb_chat:
                //消息模块
                if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {
                    showFragment(R.id.fragment, new ChatFragment());
                } else {
                    showLogin();
                    rg.getChildAt(0).performClick();

                }
                break;
            case R.id.rb_my:
                //我的模块
                if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {
                    showFragment(R.id.fragment, new StudentMyFragment());
                } else {
                    showLogin();
                    rg.getChildAt(0).performClick();
                }
                break;
        }
    }

    /**
     * TODO 录制按钮监听
     * @param view
     */
    @OnClick({R.id.iv_rec})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_rec:
                //录制
                requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        true, new PermissionsResultListener() {
                            @Override
                            public void onPermissionGranted() {
//                                Intent intent = new Intent(MainActivity.this, RecActivity.class);
//                                startActivityForAnimation(intent, R.anim.activity_bottom_top, R.anim.activity_no_anim);
//                                rg.findViewById(R.id.rb_video).performClick();
                            }

                            @Override
                            public void onPermissionDenied() {
                                Toast.makeText(MainActivity.this, "请赋予权限", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
    /**
     * TODO 是否去登录提示
     */
    private void showLogin() {
        new AlertDialog(this).builder()
                .setTitle("此功能需要登录")
                .setMsg("是否去登录")
                .setCancelable(true)
                .setPositiveButton("去登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("取消", null).show();
    }
    /**
     * TODO 获取用户信息
     */
    private void getUserName(String pNumber) {
        UserRequestEntity userRequestEntity = new UserRequestEntity();
        UserRequestEntity.UserBean userBean = new UserRequestEntity.UserBean();
        userBean.setPhonenumber(pNumber);
        userRequestEntity.setUser(userBean);
        String request = new Gson().toJson(userRequestEntity);
        String requestJson = RequestAPI.getRequestJson(this, request);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {
                        UserInformationEntity userInformationEntity = new Gson().fromJson(jo.toString(), UserInformationEntity.class);
                        String username = userInformationEntity.getListUsers().get(0).getUsername();
                        String phonenumber = userInformationEntity.getListUsers().get(0).getPhonenumber();
                        if (SharedUtil.getBoolean(Constants.NOTIFICATION)) {
                            setNotificationBuilder("高尔夫人生", username + "申请添加您为好友");
                            setAddressBookState(phonenumber);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailed() {

            }
        }).getEntityData(this, ConstantsURL.QueryUsersBy, requestJson);
    }

    /**
     * TODO 设置通讯录数据更新
     * @param phonenumber
     */
    private void setAddressBookState(String phonenumber) {
        boolean isExist = true;
        Cursor cursor = sqLiteDatabase.rawQuery("select * from friends", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String pNumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
                if (phonenumber.equals(pNumber)) {
                    isExist = false;
                    break;
                }
            }
        }
        cursor.close();
        if (isExist) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("phonenumber", phonenumber);//申请的好友手机号
            sqLiteDatabase.insert("friends", null, contentValues);
        }

        Cursor cursor2 = sqLiteDatabase.rawQuery("select * from friends", null);
        int friendsCount = cursor2.getCount();
        cursor2.close();
        SharedUtil.putInt(Constants.NEW_FRIENDS_COUNT, friendsCount);
        EventBus.getDefault().postSticky(EventBusMapUtil.getStringMap("newFriends", "1"));
    }
    /**
     * todo 查找新的朋友申请列表
     */
    private void getNewsFriends() {

        ApplyFriendsEntity applyFriendsEntity = new ApplyFriendsEntity();
        ApplyFriendsEntity.ApplyFriendsBean applyFriendsBean = new ApplyFriendsEntity.ApplyFriendsBean();
        applyFriendsEntity.setApplyfriends(applyFriendsBean);
        applyFriendsEntity.setPage(page);
        String json = new Gson().toJson(applyFriendsEntity);
        String requestJson = RequestAPI.getRequestJson(this, json);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    int count = 0;
                    if (statuscode.equals("1")) {
                        ApplyInfoEntity applyInfoEntity = new Gson().fromJson(jo.toString(), ApplyInfoEntity.class);
                        List<ApplyInfoEntity.ListapplyfriendsBean> listapplyfriends = applyInfoEntity.getListapplyfriends();
                        if (listapplyfriends != null) {
                            for (int i = 0; i < listapplyfriends.size(); i++) {
                                if (listapplyfriends.get(i).getApplystatus().equals("0")) {
                                    count++;
                                }
                            }
                        }
                        if (count > 0) {
                            SharedUtil.putInt(Constants.NEW_FRIENDS_COUNT, count);
                            EventBus.getDefault().postSticky(EventBusMapUtil.getStringMap("newFriends", "1"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailed() {

            }
        }).getEntityData(this,ConstantsURL.QueryApplyFriends, requestJson);
    }
    /**
     * TODO 获取golf 好友数据
     */
    private void getGolfFriends() {
        FriendsRequestEntity friendsRequestEntity = new FriendsRequestEntity();
        FriendsRequestEntity.FriendsBean friendsBean = new FriendsRequestEntity.FriendsBean();
        friendsRequestEntity.setFriends(friendsBean);
        String request = new Gson().toJson(friendsRequestEntity);
        String requestJson = RequestAPI.getRequestJson(this, request);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {
                        GolfFriendsEntity golfFriendsEntity = new Gson().fromJson(jo.toString(), GolfFriendsEntity.class);
                        List<GolfFriendsEntity.ListusersBean> listusers = golfFriendsEntity.getListusers();
                        if (listusers != null) {
                            for (GolfFriendsEntity.ListusersBean listuser : listusers) {
                                String remark = listuser.getRemarkName();
                                FriendEntity friendEntity = new FriendEntity();
                                friendEntity.setName(listuser.getUsername());
                                if (remark != null && !remark.isEmpty()) {
                                    friendEntity.setName(remark);
                                }
                                friendEntity.setPhoneNumber(listuser.getPhonenumber());

                                Cursor cursor = sqLiteDatabase.rawQuery("select * from golffriends where phonenumber = ?", new String[]{friendEntity.getPhoneNumber()});
                                if (cursor != null && cursor.getCount() > 0) {
                                    sqLiteDatabase.execSQL("update golffriends set username = ? where phonenumber = ?",
                                            new String[]{friendEntity.getName(), friendEntity.getPhoneNumber()});
                                } else {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("phonenumber", friendEntity.getPhoneNumber());//好友手机号
                                    contentValues.put("username", friendEntity.getName());//好友名称
                                    sqLiteDatabase.insert("golffriends", null, contentValues);
                                }
                                if (cursor != null) {
                                    cursor.close();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestFailed() {

            }
        }).getEntityData(this,ConstantsURL.QueryFriends, requestJson);
    }
    /**
     * TODO 设置通知
     * @param username
     * @param body
     */
    protected void setNotificationBuilder(String username, String body) {
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, MainActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(username)
                .setContentText(body)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .build();// getNotification()

        mNotifyMgr.notify(1, notification);
    }
    /**
     * TODO Event返回处理
     *
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("MainActivity")) {
            if (map.get("MainActivity").equals("finish")) {
                finish();
            }
        }
    }
}
