package com.glorystudent.golflife.adapter;

import android.content.Context;

import com.glorystudent.golflibrary.adapter.AbsBaseAdapter;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.TeamCityEntity;

/**
 * TODO 热门城市适配器
 * Created by Gavin.J on 2017/11/1.
 */

public class HotCityGridAdapter extends AbsBaseAdapter<TeamCityEntity.HotCityListBean> {
    public HotCityGridAdapter(Context context) {
        super(context, R.layout.item_province_grid);
    }

    @Override
    public void bindView(ViewHolder viewHolder, TeamCityEntity.HotCityListBean data) {
        viewHolder.setTextView(R.id.tv_city_name, data.getName());
    }
}
