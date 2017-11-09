package com.glorystudent.golflife.adapter;

import android.content.Context;

import com.glorystudent.golflibrary.adapter.AbsBaseAdapter;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.VideoEntity;

/**
 * TODO 视频列表适配器
 * Created by Gavin.J on 2017/11/9.
 */

public class VideoListAdapter extends AbsBaseAdapter<VideoEntity.ListTeachingVideoBean> {
    public VideoListAdapter(Context context) {
        super(context, R.layout.item_video_list);
    }

    @Override
    public void bindView(ViewHolder viewHolder, VideoEntity.ListTeachingVideoBean data) {
        viewHolder.setImageView(R.id.video_lv_iv, data.getTeachingvideo_picture(), R.drawable.pic_placeholder);
        viewHolder.setTextView(R.id.video_lv_title, data.getTeachingvideo_tittle());
        viewHolder.setTextView(R.id.video_lv_context, data.getTeachingvideo_context());
    }
}
