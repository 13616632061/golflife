package com.glorystudent.golflife.adapter;

import android.content.Context;

import com.glorystudent.golflibrary.adapter.AbsBaseAdapter;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.CollectVideoEntity;

/**
 * TODO 收藏列表适配器
 * Created by Gavin.J on 2017/11/6.
 */

public class CollectListAdapter extends AbsBaseAdapter<CollectVideoEntity.ListCollectBean> {
    public CollectListAdapter(Context context) {
        super(context, R.layout.item_video_list);
    }

    @Override
    public void bindView(ViewHolder viewHolder, CollectVideoEntity.ListCollectBean data) {
        viewHolder.setImageView(R.id.video_lv_iv, data.getCollectpicurl(), R.drawable.pic_placeholder);
        viewHolder.setTextView(R.id.video_lv_title, data.getCollecttitle());
        viewHolder.setTextView(R.id.video_lv_context, data.getCollecttag());
    }
}
