package com.glorystudent.golflife.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseFragment;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.activity.EventSignUpActivity;
import com.glorystudent.golflife.adapter.VideoFragmentrAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.entity.TeachingVideoCategoryEntity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Gavin.J on 2017/10/25.
 */

public class StudentVideoFragment extends BaseFragment {
    @Bind(R.id.video_tab)
    public TabLayout tabLayout;
    @Bind(R.id.video_vp)
    public ViewPager video_vp;
    private VideoFragmentrAdapter videoViewPagerAdapter;
    private List<TeachingVideoCategoryEntity.TeachingVideoCategory> datas;
    @Override
    protected int getContentId() {
        return R.layout.fragment_stu_video;
    }
    /**
     * TODO 获取教学视频分类数据
     */
    @Override
    protected void loadDatas() {
        showLoading();
        String requestJson= RequestAPI.getJson(getActivity(),"");
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                System.out.println("教学视频分类: "+json);
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if("1".equals(statuscode)){
                        TeachingVideoCategoryEntity teachingVideoCategoryEntity=new Gson().fromJson(jo.toString(),TeachingVideoCategoryEntity.class);
                        datas=new ArrayList<>();
                        if(teachingVideoCategoryEntity!=null){
                            datas=teachingVideoCategoryEntity.getListTeachingVideoCategory();
                            initView();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoading();
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        }).getEntityData(getActivity(), ConstantsURL.QueryTeachingVideoCategoryAll, requestJson);
    }

    /**
     * todo 初始化视图数据
     */
    private void  initView(){
        for(int i=0;i<datas.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(datas.get(i).getName().toString()));
        }
        videoViewPagerAdapter = new VideoFragmentrAdapter(getChildFragmentManager(), datas);
        video_vp.setAdapter(videoViewPagerAdapter);
        video_vp.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(video_vp);
        tabLayout.setTabsFromPagerAdapter(videoViewPagerAdapter);
    }
}
