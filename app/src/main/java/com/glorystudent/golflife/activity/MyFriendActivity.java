package com.glorystudent.golflife.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.MyFriendAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.MyListView;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.entity.FriendEntity;
import com.glorystudent.golflife.entity.FriendsRequestEntity;
import com.glorystudent.golflife.entity.GolfFriendsEntity;
import com.glorystudent.golflife.entity.GroupEntity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyFriendActivity extends BaseActivity implements TextWatcher, AdapterView.OnItemClickListener {

    @Bind(R.id.elv)
    public MyListView elv;
    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;

    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private List<FriendEntity> friendEntities1;
    private List<GolfFriendsEntity.ListusersBean> listusers;
    private SQLiteDatabase sqLiteDatabase;
    private MyFriendAdapter myFriendAdapter;
    private int video_id;//视频id


    @Override
    protected int getContentId() {
        return R.layout.activity_my_friend;
    }

    @Override
    protected void init() {
        friendEntities1 = new ArrayList<>();
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath("video.db"), null);

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
        getGolfFriends();
        myFriendAdapter = new MyFriendAdapter(this);
        elv.setAdapter(myFriendAdapter);
        //搜索
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_search_list, null);
        EditText search = (EditText) inflate.findViewById(R.id.search_address);
        search.addTextChangedListener(this);
        elv.addHeaderView(inflate);
        elv.setOnItemClickListener(this);

        Intent intent=getIntent();
        if(intent!=null){
            video_id=intent.getIntExtra("id",-1);
        }



    }

    /**
     * TODO 获取golf好友
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
                System.out.println("获取golf好友:" + json);
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
                        myFriendAdapter.setDatas(friendEntities1);
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
                Toast.makeText(MyFriendActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
            }
        }).getEntityData(MyFriendActivity.this, ConstantsURL.QueryFriends, requestJson);
    }

    /**
     * TODO 输入框变化监听
     *
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
        List<FriendEntity> friends2 = new ArrayList<>();
        if (friendEntities1 != null) {
            for (FriendEntity friend : friendEntities1) {
                if (friend.getName().contains(text)) {
                    friends2.add(friend);
                }
            }
        }
        myFriendAdapter.setDatas(friends2);
    }

    /**
     * TODO 好友列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(MyFriendActivity.this,FriendChatActivity.class);
        System.out.println("friendEntities1 : "+friendEntities1);
        System.out.println("position : "+position);
            intent.putExtra("username", friendEntities1.get(position-1).getName());
            intent.putExtra("phonenumber", friendEntities1.get(position-1).getPhoneNumber());
            intent.putExtra("customerphoto", friendEntities1.get(position-1).getCustomerphoto());
            intent.putExtra("id",video_id);
            startActivity(intent);
    }

    /**
     * TODO  点击事件监听
     * @param view
     */
    @OnClick(R.id.back)
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
