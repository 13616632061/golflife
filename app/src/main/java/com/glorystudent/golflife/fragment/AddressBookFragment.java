package com.glorystudent.golflife.fragment;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.base.BaseFragment;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.activity.FriendChatActivity;
import com.glorystudent.golflife.activity.NewFriendActivity;
import com.glorystudent.golflife.adapter.GolfChatAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.entity.FriendEntity;
import com.glorystudent.golflife.entity.FriendsRequestEntity;
import com.glorystudent.golflife.entity.GolfFriendsEntity;
import com.glorystudent.golflife.entity.GroupEntity;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * TODO 通讯录模块
 * Created by Gavin.J on 2017/11/6.
 */

public class AddressBookFragment extends BaseFragment implements ExpandableListView.OnChildClickListener, TextWatcher {

    @Bind(R.id.elv)
    public ExpandableListView elv;
    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;

    private GolfChatAdapter golfChatAdapter;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private TextView tv_friends_number;
    private SQLiteDatabase sqLiteDatabase;
    private List<GolfFriendsEntity.ListusersBean> listusers;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private List<GroupEntity> golf;
    private GroupEntity groupEntity;
    private List<FriendEntity> friendEntities1;
    private List<FriendEntity> friendEntities2;
    /**
     * todo 通知时间处理
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case "com.glory.broadcast.RefreshFriend":
                    getGolfFriends();
                    break;
            }
        }
    };
    @Override
    protected int getContentId() {
        return R.layout.fragment_address_book;
    }
    @Override
    protected void init(View view) {
        EventBus.getDefault().register(this);
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新回调
                isRefresh = true;
                getGolfFriends();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载回调
                isRefresh = false;

            }
        });
        showLoading();
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getActivity().getDatabasePath("video.db"), null);
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.glory.broadcast.RefreshFriend");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        golfChatAdapter = new GolfChatAdapter(getActivity());

        golf = new ArrayList<>();
        groupEntity = new GroupEntity();
        groupEntity.setGroupName("GolfLife好友");
        friendEntities1 = new ArrayList<>();
        getGolfFriends();
        groupEntity.setFriends(friendEntities1);
        golf.add(groupEntity);

        golfChatAdapter.setDatas(golf);
        elv.setAdapter(golfChatAdapter);
        elv.setGroupIndicator(null);
        elv.expandGroup(0);//默认展开GolfLife好友
        elv.setOnChildClickListener(this);
        //搜索
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.item_address_book_head, null);
        EditText search = (EditText) inflate.findViewById(R.id.search_address);
        tv_friends_number = (TextView) inflate.findViewById(R.id.tv_friends_number);
        search.addTextChangedListener(this);
        elv.addHeaderView(inflate);
        RelativeLayout rl_new_friend = (RelativeLayout) inflate.findViewById(R.id.rl_new_friend);
        //新朋友
        rl_new_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedUtil.putInt(Constants.NEW_FRIENDS_COUNT, 0);
                tv_friends_number.setText("0");
                tv_friends_number.setVisibility(View.GONE);
                sqLiteDatabase.execSQL("delete from friends");
                EventBus.getDefault().post(EventBusMapUtil.getStringMap("newFriends", "4"));
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                startActivity(intent);
            }
        });

        int friendCount = SharedUtil.getInt(Constants.NEW_FRIENDS_COUNT);
        if (friendCount > 0) {
            tv_friends_number.setText(friendCount + "");
            tv_friends_number.setVisibility(View.VISIBLE);
        }
    }

    /**
     * TODO GolfLife好友列表点击事件
     * @param parent
     * @param v
     * @param groupPosition
     * @param childPosition
     * @param id
     * @return
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (groupPosition == 0) {
            //GolfLife好友
            FriendEntity child = (FriendEntity) golfChatAdapter.getChild(groupPosition, childPosition);
            GolfFriendsEntity.ListusersBean user = listusers.get(childPosition);
            Intent intent = new Intent(getActivity(), FriendChatActivity.class);
            intent.putExtra("username", child.getName());
            intent.putExtra("phonenumber", child.getPhoneNumber());
            intent.putExtra("customerphoto", child.getCustomerphoto());
            startActivity(intent);
        }
        return true;
    }
    /**
     * TODO 获取golf好友
     */
    private void getGolfFriends() {
        FriendsRequestEntity friendsRequestEntity = new FriendsRequestEntity();
        FriendsRequestEntity.FriendsBean friendsBean = new FriendsRequestEntity.FriendsBean();
        friendsRequestEntity.setFriends(friendsBean);
        String request = new Gson().toJson(friendsRequestEntity);
        String requestJson = RequestAPI.getRequestJson(getActivity(), request);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                System.out.println("获取golf好友:"+json);
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {
                        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                        friendEntities1.clear();
                        GolfFriendsEntity golfFriendsEntity = new Gson().fromJson(jo.toString(), GolfFriendsEntity.class);
                        listusers = golfFriendsEntity.getListusers();
                        if (listusers != null) {
                            for (GolfFriendsEntity.ListusersBean listuser : listusers) {
                                FriendEntity friendEntity = new FriendEntity();
                                friendEntity.setName(listuser.getUsername());
                                String remark = listuser.getRemarkName();
                                if (remark != null && !remark.isEmpty()) {
                                    friendEntity.setName(remark);
                                }
                                friendEntity.setRemarkname(remark);
                                friendEntity.setPhoneNumber(listuser.getPhonenumber());
                                friendEntity.setCustomerphoto(listuser.getCustomerphoto());
                                friendEntities1.add(friendEntity);

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
//                        contrastPhoneNumber();
                        golfChatAdapter.setDatas(golf);
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
     * TODO 输入框变化监听
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        if (text != null && !text.isEmpty()) {
            List<GroupEntity> golfFriends = new ArrayList<>();
            for (int i = 0; i < golf.size(); i++) {
                GroupEntity groupEntity2 = new GroupEntity();
                GroupEntity groupEntity = golf.get(i);
                groupEntity2.setGroupName(groupEntity.getGroupName());
                List<FriendEntity> friends = groupEntity.getFriends();
                List<FriendEntity> friends2 = new ArrayList<>();
                if (friends != null) {
                    for (FriendEntity friend : friends) {
                        if (friend.getName().contains(text)) {
                            friends2.add(friend);
                        }
                    }
                }
                groupEntity2.setFriends(friends2);
                golfFriends.add(groupEntity2);
            }
            golfChatAdapter.setDatas(golfFriends);
        } else {
            golfChatAdapter.setDatas(golf);
        }
    }

    /**
     * TODO EventBus处理
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBus(Map<Integer, Integer> map) {
        if (map.containsKey(2)) {
            if (map.get(2) == 1) {
                getGolfFriends();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getStringEventBus(Map<String, String> map) {
        if (map.containsKey("newFriends")) {
            if (map.get("newFriends").equals("1")) {
                int friendCount = SharedUtil.getInt(Constants.NEW_FRIENDS_COUNT);
                if (friendCount > 0) {
                    if (tv_friends_number != null) {
                        tv_friends_number.setText(friendCount + "");
                        tv_friends_number.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        sqLiteDatabase.close();
        EventBus.getDefault().unregister(this);
    }
}
