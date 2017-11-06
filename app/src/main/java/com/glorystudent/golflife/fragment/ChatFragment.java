package com.glorystudent.golflife.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.glorystudent.golflibrary.base.BaseFragment;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.activity.AddFriendsActivity;
import com.glorystudent.golflife.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Gavin.J on 2017/10/25.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.tv_rb1)
    public TextView tv_rb1;
    @Bind(R.id.tv_rb2)
    public TextView tv_rb2;
    @Bind(R.id.tv_point_friend)
    public TextView tv_point_friend;
    @Bind(R.id.tv_point_message)
    public TextView tv_point_message;
    @Override
    protected int getContentId() {
        return R.layout.fragment_chat;
    }

    /**
     * TODO 初始化
     * @param view
     */
    @Override
    protected void init(View view) {
        int friendCount = SharedUtil.getInt(Constants.NEW_FRIENDS_COUNT);
        if (friendCount > 0) {
            tv_point_friend.setVisibility(View.VISIBLE);
        }
        EventBus.getDefault().register(this);
        tv_rb1.setOnClickListener(this);
        tv_rb2.setOnClickListener(this);
        tv_rb1.performClick();
    }

    /**
     * TODO 添加好友点击事件
     * @param view
     */
    @OnClick(R.id.iv_add_friends)
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_friends:
                //添加好友
                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
                break;
        }
    }

    /**
     * TODO 消息与通讯录切换
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_rb1:
                tv_rb2.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_rb1.setTextColor(getResources().getColor(R.color.colorOrange));
                //切换到消息
                showFragment(R.id.chat_fragment, new MessageFragment());
                break;
            case R.id.tv_rb2:
                tv_rb1.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_rb2.setTextColor(getResources().getColor(R.color.colorOrange));
                //切换到通讯录
                showFragment(R.id.chat_fragment, new AddressBookFragment());
                break;
        }
    }

    /**
     * TODO EventBus事件处理
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getStringEventBus(Map<String,String> map) {
        if (map.containsKey("newFriends")) {
            if (map.get("newFriends").equals("1")) {
                int friendCount = SharedUtil.getInt(Constants.NEW_FRIENDS_COUNT);
                if (friendCount > 0) {
                    tv_point_friend.setVisibility(View.VISIBLE);
                }
            }
            if (map.get("newFriends").equals("4")) {
                tv_point_friend.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
