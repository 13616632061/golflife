package com.glorystudent.golflife.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.TeamAlbumDetailEntity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * TODO 球队相册照片页面
 */
public class TeamAlbumPhotoActivity extends BaseActivity {

    private static final String TAG = "TeamAlbumPhotoActivity";
    @Bind(R.id.iv_team_album_photo)
    ImageView photo;
    @Bind(R.id.convenient_banner)
    ConvenientBanner convenientBanner;
    private int position;
    private List<TeamAlbumDetailEntity.PhotolistBean> datas;

    @Override
    protected int getContentId() {
        return R.layout.activity_team_album_photo;
    }
    @Override
    protected void init() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        if (datas != null && datas.size() > 1) {
            photo.setVisibility(View.GONE);
            convenientBanner.setVisibility(View.VISIBLE);
            initImageBanner();
            convenientBanner.setcurrentitem(position);
        } else if (datas != null && datas.size() == 1) {
            photo.setVisibility(View.VISIBLE);
            convenientBanner.setVisibility(View.GONE);
            GlideUtil.loadImageView(this, datas.get(position).getEventactivity_picpath(), photo);
        }
    }

    /**
     * TODO 初始化顶部轮播
     */
    private void initImageBanner() {
        convenientBanner.setPages(new CBViewHolderCreator<LoaclViewHolder>() {
            @Override
            public LoaclViewHolder createHolder() {
                return new LoaclViewHolder();
            }
        }, datas)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.shape_min_whitecircle, R.drawable.shape_min_orangecircle})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        finish();
                    }
                });
    }

    /**
     * TODO 自动轮播类
     * 自动轮播需要的类
     */
    public class LoaclViewHolder implements Holder<TeamAlbumDetailEntity.PhotolistBean> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, TeamAlbumDetailEntity.PhotolistBean data) {
            GlideUtil.loadImageView(context, data.getEventactivity_picpath(), imageView);
        }

    }

    /**
     * TODO 处理EvenBus返回
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEvent(Map<String, List<TeamAlbumDetailEntity.PhotolistBean>> map) {
        if (map.containsKey("TeamAlbumPhotoActivity")) {
            datas = map.get("TeamAlbumPhotoActivity");
        }
    }
}
