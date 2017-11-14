package com.glorystudent.golflife.adapter;

import android.content.Context;

import com.glorystudent.golflibrary.adapter.AbsBaseAdapter;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.FriendEntity;

/**
 * TODO 我的好友适配器
 * Created by Gavin.J on 2017/11/13.
 */

public class MyFriendAdapter extends AbsBaseAdapter<FriendEntity> {

    public MyFriendAdapter(Context context) {
        super(context, R.layout.item_chat_golf_friend);
    }

    @Override
    public void bindView(ViewHolder viewHolder, FriendEntity data) {
        if(data!=null){
            viewHolder.setTextView(R.id.tv_nickname,data.getName());
            viewHolder.setImageView(R.id.iv_header,data.getCustomerphoto(),R.drawable.icon_chat_golffriend);
        }

    }
}
