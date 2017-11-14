package com.glorystudent.golflife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflibrary.widget.circleview.CircleImageView;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.entity.ApplyFriendsEntity;
import com.glorystudent.golflife.entity.FriendEntity;
import com.glorystudent.golflife.entity.GroupEntity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin.J on 2017/11/7.
 */

public class GolfChatAdapter extends BaseExpandableListAdapter {
    private final static String TAG = "GolfChatAdapter";
    private Context context;
    private List<GroupEntity> datas;

    public GolfChatAdapter(Context context) {
        this.context = context;
        this.datas = new ArrayList<>();
    }

    public void setDatas(List<GroupEntity> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).getFriends().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).getFriends().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }



    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if(groupViewHolder != null){
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }else{
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_group, parent, false);
            groupViewHolder.iv_chat_headpic = (CircleImageView) convertView.findViewById(R.id.iv_chat_headpic);
            groupViewHolder.tv_groupname = (TextView) convertView.findViewById(R.id.tv_groupname);
            groupViewHolder.iv_expanded = (ImageView) convertView.findViewById(R.id.iv_expanded);
            convertView.setTag(groupViewHolder);
        }

        GroupEntity groupEntity = datas.get(groupPosition);
        String groupName = groupEntity.getGroupName();
        if("GolfLife好友".equals(groupName)){
            groupViewHolder.iv_chat_headpic.setImageResource(R.drawable.icon_chat_golffriend);
        }
        if(isExpanded){
            groupViewHolder.iv_expanded.setImageResource(R.drawable.btn_chat_down_triangle);
        }else{
            groupViewHolder.iv_expanded.setImageResource(R.drawable.btn_home_more);
        }

        groupViewHolder.tv_groupname.setText(datas.get(groupPosition).getGroupName());
        return convertView;
    }
    public class GroupViewHolder{
        CircleImageView iv_chat_headpic;
        TextView tv_groupname;
        ImageView iv_expanded;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FriendEntity friendEntity = datas.get(groupPosition).getFriends().get(childPosition);
        ChildViewHolder childViewHolder = null;
        if (childViewHolder != null) {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }else{
            childViewHolder = new ChildViewHolder();
            String groupName = datas.get(groupPosition).getGroupName();
            if("GolfLife好友".equals(groupName)){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_golf_friend, parent, false);
                childViewHolder.iv_header = (CircleImageView) convertView.findViewById(R.id.iv_header);
                childViewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                convertView.setTag(childViewHolder);
                GlideUtil.loadCircleImageView(context, friendEntity.getCustomerphoto(), childViewHolder.iv_header, R.drawable.icon_chat_golffriend);
            }
        }
        String remarkname = friendEntity.getRemarkname();
        if (remarkname != null && !remarkname.isEmpty()) {
            childViewHolder.tv_nickname.setText(friendEntity.getRemarkname());
        }else{
            childViewHolder.tv_nickname.setText(friendEntity.getName());
        }
        return convertView;
    }

    public class ChildViewHolder{
        CircleImageView iv_header;
        TextView tv_nickname, tv_singra, tv_add_book_friend;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
