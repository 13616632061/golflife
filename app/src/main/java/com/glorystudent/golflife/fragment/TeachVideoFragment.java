package com.glorystudent.golflife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseFragment;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.activity.VideoDetailsActivity;
import com.glorystudent.golflife.adapter.VideoListAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.customView.PullableNoUpListView;
import com.glorystudent.golflife.entity.VideoEntity;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Gavin.J on 2017/11/9.
 */

public class TeachVideoFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static TeachVideoFragment getInstance(String id){
        TeachVideoFragment teachVideoFragment = new TeachVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        teachVideoFragment.setArguments(bundle);
        return teachVideoFragment;
    }

    @Bind(R.id.teach_video_lv)
    public PullableNoUpListView teach_video_lv;
    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;
    @Bind(R.id.ll_empty)
    public LinearLayout ll_empty;

    private int page = 1;//默认加载第一页
    private String teachingvideo_id;//视频分类id
    private VideoEntity videoEntity;
    private VideoListAdapter videoListAdapter;
    private List<VideoEntity.ListvideocollectBean> listvideocollect;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private boolean isFirst=true;//是否第一次加载


    @Override
    protected int getContentId() {
        return R.layout.fragment_teach_video;
    }
    @Override
    protected void init(View view) {
        ll_empty.setVisibility(View.GONE);
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新回调
                isRefresh = true;
                getDatas();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载回调
                isRefresh = false;
            }
        });
        showLoading();
        videoListAdapter = new VideoListAdapter(getActivity());
        teach_video_lv.setAdapter(videoListAdapter);
        teach_video_lv.setOnItemClickListener(this);
        ll_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatas();
            }
        });
    }

    /**
     * TODO 视频列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoEntity.ListTeachingVideoBean datas = videoListAdapter.getDatas(position);
        Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("video", datas);
        if (listvideocollect != null) {
            for (VideoEntity.ListvideocollectBean listvideocollectBean : listvideocollect) {
                if(listvideocollectBean.get_CollectObjectID() == datas.getTeachingvideo_id()){
                    bundle.putInt("collectid", listvideocollectBean.getCollectid());
                }
            }
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected void getBundle(Bundle bundle) {
        teachingvideo_id = bundle.getString("id");
        getDatas();
    }

    /**
     * TODO 获取分类视频数据
     */
    public void getDatas() {
        String videoJson = RequestAPI.getTeachVideo(getActivity(),teachingvideo_id, page+"");
        OkGo.post(ConstantsURL.QueryTeachingVideo)
                .tag(this)//
                .params("request", videoJson)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        System.out.println("教学视频Url: "+request.getUrl());
                        System.out.println("教学视频Params: "+request.getParams());
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        System.out.println("教学视频: "+s);
                        try {
                            JSONObject jo = new JSONObject(s);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if(statuscode.equals("1")){
                                ll_empty.setVisibility(View.GONE);
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                                videoEntity = new Gson().fromJson(jo.toString(), VideoEntity.class);
                                List<VideoEntity.ListTeachingVideoBean> listTeachingVideo = videoEntity.getListTeachingVideo();
                                listvideocollect = videoEntity.getListvideocollect();
                                if(listTeachingVideo != null){
                                    videoListAdapter.setDatas(listTeachingVideo);
                                }
                            } else if (statuscode.equals("2")) {
                                ll_empty.setVisibility(View.VISIBLE);
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                            } else {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dismissLoading();
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dismissLoading();
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                        ll_empty.setVisibility(View.VISIBLE);
                    }
                });
    }
}
