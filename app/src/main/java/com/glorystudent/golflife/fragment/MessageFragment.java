package com.glorystudent.golflife.fragment;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseFragment;
import com.glorystudent.golflibrary.util.DensityUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.activity.FriendChatActivity;
import com.glorystudent.golflife.activity.MainActivity;
import com.glorystudent.golflife.adapter.MessageRecyclerAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.entity.FriendEntity;
import com.glorystudent.golflife.entity.FriendsRequestEntity;
import com.glorystudent.golflife.entity.GolfFriendsEntity;
import com.glorystudent.golflife.entity.MessageChatEntity;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.glorystudent.golflife.util.RecycleViewDivider;
import com.glorystudent.golflife.util.TimeUtil;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 消息模块
 * Created by Gavin.J on 2017/11/6.
 */

public class MessageFragment extends BaseFragment implements OnSwipeMenuItemClickListener, MessageRecyclerAdapter.OnRecyclerItemClickListener {
    private final static String TAG = "MessageFragment";
    @Bind(R.id.swipe_recycler)
    public SwipeMenuRecyclerView swipe_recycler;
    @Bind(R.id.rl_system_message)
    public RelativeLayout rl_system_message;
    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;
    @Bind(R.id.tv_system_number)
    public TextView tv_system_number;

    private List<FriendEntity> friendEntities;
    private MessageRecyclerAdapter messageListAdapter;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private TextView tv_message_num;
    private SQLiteDatabase sqLiteDatabase;
    private int count = 0;
    @Override
    protected int getContentId() {
        return R.layout.fragment_message_chat;
    }
    @Override
    protected void init(View view) {
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getActivity().getDatabasePath("video.db"), null);
        showLoading();
        MainActivity activity = (MainActivity) getActivity();
        tv_message_num = (TextView) activity.findViewById(R.id.tv_message_num);
        EventBus.getDefault().register(this);
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新回调
                isRefresh = true;
                loadDatas();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载回调
                isRefresh = false;
                loadDatas();
            }
        });


        messageListAdapter = new MessageRecyclerAdapter(getActivity());
        swipe_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        swipe_recycler.setAdapter(messageListAdapter);
        swipe_recycler.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.divider_mileage));
        swipe_recycler.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackgroundDrawable(getResources().getDrawable(R.color.colorRed))
                        .setText("删除") // 文字。
                        .setTextColor(Color.WHITE) // 文字颜色。
                        .setTextSize(16) // 文字大小。
                        .setWidth(DensityUtil.dip2px(getActivity(), 90))
                        .setHeight(DensityUtil.dip2px(getActivity(), 56));
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单
            }
        });
        swipe_recycler.setSwipeMenuItemClickListener(this);
        messageListAdapter.setOnRecyclerItemClickListener(this);
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation("00000");
        if (conversation != null) {
            int unreadMsgCount = conversation.getUnreadMsgCount();
            if (unreadMsgCount > 0) {
                tv_system_number.setVisibility(View.VISIBLE);
                tv_system_number.setText(unreadMsgCount + "");
            } else {
                tv_system_number.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void loadDatas() {
        count = 0;
        FriendsRequestEntity friendsRequestEntity = new FriendsRequestEntity();
        final FriendsRequestEntity.FriendsBean friendsBean = new FriendsRequestEntity.FriendsBean();
        friendsRequestEntity.setFriends(friendsBean);
        String request = new Gson().toJson(friendsRequestEntity);
        String requestJson = RequestAPI.getRequestJson(getActivity(), request);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                System.out.println("好友："+json);
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {
                        friendEntities = new ArrayList<>();
                        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                        GolfFriendsEntity golfFriendsEntity = new Gson().fromJson(jo.toString(), GolfFriendsEntity.class);
                        List<GolfFriendsEntity.ListusersBean> listusers = golfFriendsEntity.getListusers();
                        if (listusers != null) {
                            for (GolfFriendsEntity.ListusersBean listuser : listusers) {
                                FriendEntity friendEntity = new FriendEntity();
                                friendEntity.setName(listuser.getUsername());
                                friendEntity.setRemarkname(listuser.getRemarkName());
                                if (friendEntity.getRemarkname() != null && !friendEntity.getRemarkname().isEmpty()) {
                                    friendEntity.setName(friendEntity.getRemarkname());
                                }
                                friendEntity.setPhoneNumber(listuser.getPhonenumber());
                                friendEntity.setCustomerphoto(listuser.getCustomerphoto());
                                friendEntities.add(friendEntity);

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
                            initView();
                        }
                    } else if (statuscode.equals("2")) {
                        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                    } else {
                        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoading();
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
            }
        }).getEntityData(getActivity(), ConstantsURL.QueryFriends, requestJson);
    }

    /**
     * TODO 初始化
     */
    private void initView() {
        try {
            List<MessageChatEntity> chatList = new ArrayList<>();
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            Iterator<Map.Entry<String, EMConversation>> iterator = conversations.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, EMConversation> next = iterator.next();
                String phoneNumber = next.getKey();
                for (int i = 0; i < friendEntities.size(); i++) {
                    FriendEntity friendEntity = friendEntities.get(i);
                    if (phoneNumber.equals(friendEntity.getPhoneNumber())) {
                        MessageChatEntity messageChatEntity = new MessageChatEntity();
                        messageChatEntity.setName(friendEntity.getName());
                        messageChatEntity.setRemarkName(friendEntity.getRemarkname());
                        messageChatEntity.setCustomerPhoto(friendEntity.getCustomerphoto());
                        messageChatEntity.setPhoneNumber(phoneNumber);
                        String week = TimeUtil.getWeek(new Date(next.getValue().getLastMessage().getMsgTime()));
                        messageChatEntity.setLastDate(week);
                        String text = next.getValue().getLastMessage().getBody().toString();
                        EMMessage.Type type = next.getValue().getLastMessage().getType();
                        String sendType = type.toString();
                        switch (sendType) {
                            case "TXT":
                                int position = text.indexOf("\"");
                                String subText = text.substring(position + 1, text.length() - 1);
                                text = subText;
                                break;
                            case "IMAGE":
                                Map<String, Object> ext = next.getValue().getLastMessage().ext();
                                if (ext != null && !ext.isEmpty()) {
                                    text = "[视频]";
                                } else {
                                    text = "[图片]";
                                }
                                break;
                            case "VOICE":
                                text = "[语音]";
                                break;
                            default:
                                text = " ";
                                break;
                        }
                        messageChatEntity.setLastMessage(text);
                        int unreadMsgCount = next.getValue().getUnreadMsgCount();
                        messageChatEntity.setUnReadCount(unreadMsgCount);
                        count = count + unreadMsgCount;
                        chatList.add(messageChatEntity);
                        break;
                    }
                }


            }
            messageListAdapter.setDatas(chatList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * TODO 好友列表点击事件
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        MessageChatEntity data = messageListAdapter.getData(position);
        int unReadCount = data.getUnReadCount();
        messageListAdapter.setUnRead(position);
        Intent intent = new Intent(getActivity(), FriendChatActivity.class);
        String customerPhoto = data.getCustomerPhoto();
        intent.putExtra("customerphoto", customerPhoto);
        if (data.getRemarkName() != null && !data.getRemarkName().isEmpty()) {
            intent.putExtra("username", data.getRemarkName());
        } else {
            intent.putExtra("username", data.getName());
        }
        intent.putExtra("phonenumber", data.getPhoneNumber());
        startActivity(intent);
    }

    @Override
    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
        dialog(getActivity(), closeable, adapterPosition);
    }

    /**
     * TODO 删除dialog
     * @param context
     * @param closeable
     * @param position
     */
    private void dialog(Context context, final Closeable closeable, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要删除吗?");
        builder.setTitle("提示");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        EMClient.getInstance().chatManager().deleteConversation(messageListAdapter.getData(position).getPhoneNumber(), false);
                        messageListAdapter.delete(position);
                        closeable.smoothCloseRightMenu();
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * TODO 系统消息点击事件
     * @param view
     */
    @OnClick({R.id.rl_system_message})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.rl_system_message:
                //系统消息
                tv_system_number.setVisibility(View.GONE);
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation("00000");
                if (conversation != null) {
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();
                }
                EventBus.getDefault().post(EventBusMapUtil.getIntMap(11, 1));
                Intent intent = new Intent(getActivity(), FriendChatActivity.class);
                intent.putExtra("username", "系统消息");
                intent.putExtra("phonenumber", "00000");
                startActivity(intent);
                break;
        }
    }

    /**
     * TODO EventBus处理
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBus(Map<Integer, Integer> map) {
        if (map.containsKey(2)) {
            if (map.get(2) == 1) {
                loadDatas();
            }
        }
        if (map.containsKey(13)) {
            if (map.get(13) == 1) {
                initView();
            }
        }
    }

    /**
     * TODO 获取消息
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(EMMessage message) {
        // 循环遍历当前收到的消息
        if (message.getFrom().equals("00000")) {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation("00000");
            final int unreadMsgCount = conversation.getUnreadMsgCount();
            tv_system_number.post(new Runnable() {
                @Override
                public void run() {
                    tv_system_number.setText(unreadMsgCount + "");
                    tv_system_number.setVisibility(View.VISIBLE);
                }
            });
        } else {
            loadDatas();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
