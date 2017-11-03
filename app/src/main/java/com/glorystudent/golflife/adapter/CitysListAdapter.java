package com.glorystudent.golflife.adapter;

import android.content.Context;

import com.glorystudent.golflibrary.adapter.AbsBaseAdapter;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.CitysEntity;

/**
 * Created by Gavin.J on 2017/11/3.
 */

public class CitysListAdapter extends AbsBaseAdapter<CitysEntity.ListchinacityBean> {

    public CitysListAdapter(Context context) {
        super(context, R.layout.item_city_list);
    }

    @Override
    public void bindView(ViewHolder viewHolder, CitysEntity.ListchinacityBean data) {
        viewHolder.setTextView(R.id.lv_tv_cicy, data.getName());
    }
}

