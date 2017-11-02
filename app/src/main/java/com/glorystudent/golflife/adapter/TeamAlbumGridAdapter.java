package com.glorystudent.golflife.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.glorystudent.golflibrary.adapter.AbsBaseAdapter;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.TeamAlbumEntity;
import com.glorystudent.golflife.util.TimeUtil;

/**
 * TODO 球队相册适配器
 * Created by Gavin.J on 2017/11/2.
 */

public class TeamAlbumGridAdapter extends AbsBaseAdapter<TeamAlbumEntity.EventactivityphotolistBean> {

    public TeamAlbumGridAdapter(Context context) {
        super(context, R.layout.item_team_album_grid);
    }

    @Override
    public void bindView(ViewHolder viewHolder, TeamAlbumEntity.EventactivityphotolistBean data) {
        viewHolder.setTextView(R.id.tv_album_name, data.getEventactivity_name());
        viewHolder.setTextView(R.id.tv_album_number, data.getPhoto_count() + "张");
        viewHolder.setTextView(R.id.tv_album_date, TimeUtil.getTeamAlbumTime(TimeUtil.getStandardDate(data.getEventactivity_begintime())));
        GlideUtil.loadImageView(context, data.getPhoto_url(), (ImageView) viewHolder.getView(R.id.iv_album_image));
    }
}
