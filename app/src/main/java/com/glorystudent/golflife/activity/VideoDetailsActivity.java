package com.glorystudent.golflife.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.DensityUtil;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.VideoDetailsListAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.customView.PullableNoUpListView;
import com.glorystudent.golflife.customView.SharePopupWindow;
import com.glorystudent.golflife.entity.CollectEntity;
import com.glorystudent.golflife.entity.ShareModel;
import com.glorystudent.golflife.entity.VideoEntity;
import com.glorystudent.golflife.entity.VideoReviewEntity;
import com.glorystudent.golflife.util.onekeyshare.OnekeyShare;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import okhttp3.Call;
import okhttp3.Response;

/**
 * TODO 视频详情页面
 */
public class VideoDetailsActivity extends BaseActivity implements VideoDetailsListAdapter.OnLikeCheckedChangeListener, AdapterView.OnItemClickListener, View.OnClickListener, UniversalVideoView.VideoViewCallback, OkGoRequest.OnOkGoUtilListener, PlatformActionListener {


    @Bind(R.id.video_details_lv)
    public PullableNoUpListView video_details_lv;
    @Bind(R.id.published)
    public LinearLayout published;
    @Bind(R.id.rl_root)
    public RelativeLayout rl_root;
    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;

    private final static String TAG = "VideoDetailsActivity";
    private ImageView iv_play;//播放按钮
    private TextView teaching_video_name;//视频标题
    private ImageView iv_first_frame;//第一帧

    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private  String VIDEO_URL = "https://glorygolflife.oss-cn-shenzhen.aliyuncs.com/CoBg87lzza67JwHlppWA%3D%3D/videos/d8060db5a38e966bc6e96b8887151567.mp4";
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;
    View mBottomLayout;
    View mVideoLayout;
    TextView mStart;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    private VideoEntity.ListTeachingVideoBean video;
    private SurfaceView sv;
    private MediaPlayer mp;
    private boolean playState = false; //true播放 false暂停
    private TextView tv_video_level;//视频等级
    private TextView tv_time_count;//总时长
    private TextView tv_video_content;//视频描述
    private ImageView back;//返回
    private ImageView video_collect;//收藏
    private ImageView video_share;//分享
    private PopupWindow window;
    private EditText et_comment;
    private VideoDetailsListAdapter videoDetailsListAdapter;//视频详情列表适配器
    private boolean isCollect;
    private int collectid;
    private SharePopupWindow share;
    private OnekeyShare oks;
    private File file;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    @Override
    protected int getContentId() {
        return R.layout.activity_video_details;
    }
    @Override
    protected void init() {
        refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新回调
                isRefresh = true;
                loadDatas();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载回调
                isRefresh = false;
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        video = (VideoEntity.ListTeachingVideoBean) bundle.getSerializable("video");
        collectid = bundle.getInt("collectid", -1);
        videoDetailsListAdapter = new VideoDetailsListAdapter(this);
        videoDetailsListAdapter.setOnLikeCheckedChangeListener(this);
        video_details_lv.setAdapter(videoDetailsListAdapter);
        video_details_lv.setOnItemClickListener(this);
        published.setOnClickListener(this);
        if (video != null) {
            VIDEO_URL = video.getTeachingvideo_path();
            initHead();
        }
    }
    /**
     * 初始化ListView的头部
     */
    private void initHead() {
        View head = LayoutInflater.from(this).inflate(R.layout.item_video_details_head, null);
        video_details_lv.addHeaderView(head);
        mVideoView = (UniversalVideoView) head.findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) head.findViewById(R.id.media_controller);
        mVideoLayout = head.findViewById(R.id.video_layout);
        mBottomLayout = head.findViewById(R.id.bottom_layout);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);


        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });


        iv_play = (ImageView) head.findViewById(R.id.iv_play);
        teaching_video_name = (TextView) head.findViewById(R.id.teaching_video_name);
        iv_first_frame = (ImageView) head.findViewById(R.id.iv_first_frame);
        tv_video_level = (TextView) head.findViewById(R.id.tv_video_level);
        tv_time_count = (TextView) head.findViewById(R.id.tv_time_count);
        tv_video_content = (TextView) head.findViewById(R.id.tv_video_content);
        back = (ImageView) head.findViewById(R.id.back);
        back.setOnClickListener(this);
        video_collect = (ImageView) head.findViewById(R.id.video_collect);
        if(collectid != -1){
            video_collect.setImageResource(R.drawable.nav_home_star_h);
            isCollect = true;
        }
        video_collect.setOnClickListener(this);
        video_share = (ImageView) head.findViewById(R.id.video_share);
        video_share.setOnClickListener(this);
        if (video != null) {
            teaching_video_name.setText(video.getTeachingvideo_tittle());
            tv_video_level.setText(video.getTeachingvideo_level());
            tv_time_count.setText((String) video.getTeachingvideo_length());
            tv_video_content.setText(video.getTeachingvideo_context());
            GlideUtil.loadImageView(this, video.getTeachingvideo_picture(), iv_first_frame);
            iv_play.setOnClickListener(this);
        }
    }

    /**
     * TODO 获取视频评论数据
     */
    @Override
    protected void loadDatas() {
        String request = RequestAPI.queryTVideoComment(this, video.getTeachingvideo_id()+"");
        new OkGoRequest().setOnOkGoUtilListener(this).getEntityData(this,ConstantsURL.QueryTVideoComment, request);
    }

    /**
     * TODO 点击事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                iv_first_frame.setVisibility(View.GONE);
                iv_play.setVisibility(View.GONE);
                if (mSeekPosition > 0) {
                    mVideoView.seekTo(mSeekPosition);
                }
                mVideoView.start();
                break;
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.video_collect:
                //收藏
                if (isCollect = !isCollect) {
                    //收藏
                    String requestJson = createRequestJson();
                    OkGo.post(ConstantsURL.AddCollect)
                            .tag(this)
                            .params("request", requestJson)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    try {
                                        JSONObject jo = new JSONObject(s);
                                        String statuscode = jo.getString("statuscode");
                                        String statusmessage = jo.getString("statusmessage");
                                        if(statuscode.equals("1")){
                                            collectid = jo.getInt("collectid");
                                            Toast.makeText(VideoDetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                            video_collect.setImageResource(R.drawable.nav_home_star_h);
                                        }else{
                                            Toast.makeText(VideoDetailsActivity.this, "收藏失败，错误码:" + statusmessage, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }else{
                    //取消收藏
                    CollectEntity collectEntity = new CollectEntity();
                   CollectEntity.CollectBean collectBean = new CollectEntity.CollectBean();
                    collectBean.setCollectid(collectid);
                    collectEntity.setCollect(collectBean);
                    String json = new Gson().toJson(collectEntity);
                    String requestJson = RequestAPI.getRequestJson(this, json);
                    OkGo.post(ConstantsURL.DeleteCollect )
                            .tag(this)
                            .params("request", requestJson)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    try {
                                        JSONObject jo = new JSONObject(s);
                                        String statuscode = jo.getString("statuscode");
                                        String statusmessage = jo.getString("statusmessage");
                                        if(statuscode.equals("1")){
                                            Toast.makeText(VideoDetailsActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                            video_collect.setImageResource(R.drawable.nav_home_star_n);
                                        }else{
                                            Toast.makeText(VideoDetailsActivity.this, "取消收藏失败，错误码:" + statusmessage, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
                break;
            case R.id.video_share:
                //分享
                share = new SharePopupWindow(this);
                share.setPlatformActionListener(this);
                ShareModel model = new ShareModel();
                model.setImageUrl(video.getTeachingvideo_picture());
                model.setText(video.getTeachingvideo_context());
                model.setTitle(video.getTeachingvideo_tittle());
                model.setUrl(ConstantsURL.share_video + video.getTeachingvideo_path());
                share.initShareParams(model);
                share.showShareWindow();
                // 显示窗口 (设置layout在PopupWindow中显示的位置)
                share.showAtLocation(this.findViewById(R.id.video_share),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f;
                getWindow().setAttributes(lp);
                share.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                    }
                });
                break;
            case R.id.published:
                //发表评论
                showPopwindow();
                break;
        }
    }

    /**
     * TODO 收藏请求参数
     * @return
     */
    private String createRequestJson() {
        CollectEntity collectEntity = new CollectEntity();
        CollectEntity.CollectBean collect = new CollectEntity.CollectBean();
        collect.setCollectobjectid(video.getTeachingvideo_id());
        collect.setCollecttype(5);
        collect.setCollectdate(RequestAPI.getCurrentTime());
        collect.setCollecturl(video.getTeachingvideo_path());
        collect.setCollecttag(video.getTeachingvideo_context());
        collect.setCollecttitle(video.getTeachingvideo_tittle());
        collect.setCollectpicurl(video.getTeachingvideo_picture());
        collectEntity.setCollect(collect);
        String json = new Gson().toJson(collectEntity);
        return RequestAPI.getRequestJson(this, json);
    }

    /**
     * TODO 请求数据处理
     * @param json
     */
    @Override
    public void parseDatas(String json) {
        JSONObject jo = null;
        try {
            jo = new JSONObject(json);
            String statuscode = jo.getString("statuscode");
            if(statuscode.equals("1")){
                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                VideoReviewEntity videoReviewEntity = new Gson().fromJson(jo.toString(), VideoReviewEntity.class);
                Map<Integer, Boolean> likeMap = new HashMap<>();
                List<VideoReviewEntity.ListTVideoCommentBean> listTVideoComment = videoReviewEntity.getListTVideoComment();
                List<VideoReviewEntity.ListtvideocommentlikeBean> listtvideocommentlike = videoReviewEntity.getListtvideocommentlike();
                for (VideoReviewEntity.ListtvideocommentlikeBean listtvideocommentlikeBean : listtvideocommentlike) {
                    likeMap.put(listtvideocommentlikeBean.getComment_tvideoid(), true);
                }
                if (listTVideoComment != null) {
                    videoDetailsListAdapter.setauxiliaryDatas(likeMap);
                    videoDetailsListAdapter.setDatas(listTVideoComment);
                }
            }else if(statuscode.equals("2")){
                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
            }else {
                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestFailed() {
        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
    }

    /**
     * TODO 显示popupWindow
     */
    private void showPopwindow() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_pop_comment, null);
        et_comment = (EditText) view.findViewById(R.id.et_comment);

        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        TextView tv_published = (TextView) view.findViewById(R.id.tv_published);
        tv_published.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_comment.getText().toString().isEmpty()) {
                    Toast.makeText(VideoDetailsActivity.this, "评论不能为空哟～", Toast.LENGTH_SHORT).show();
                }else{
                    window.dismiss();
                    String request = RequestAPI.addTVideoComment(VideoDetailsActivity.this,et_comment.getText().toString(), video.getTeachingvideo_id()+"");
                    Log.d(TAG, "onClick: -->" + request);
                    OkGo.post(ConstantsURL.AddTVideoComment)
                            .tag(this)
                            .params("request", request)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    try {
                                        JSONObject jo = new JSONObject(s);
                                        String statuscode = jo.getString("statuscode");
                                        String statusmessage = jo.getString("statusmessage");
                                        if(statuscode.equals("1")){
                                            Log.d(TAG, "onSuccess: -->" + s);
                                            Toast.makeText(VideoDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                            loadDatas();
                                        }else{
                                            Toast.makeText(VideoDetailsActivity.this, "评论失败，错误码:" + statusmessage, Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xff000000);
        window.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(VideoDetailsActivity.this.findViewById(R.id.published),
                Gravity.CENTER, 0, 0);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果开启
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.showSoftInput(et_comment,InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * TODO 视频点赞监听
     * @param buttonView
     * @param isChecked
     * @param comment_id
     */
    @Override
    public void onlikeCheckedChanged(CompoundButton buttonView, boolean isChecked, int comment_id) {
        final int commentpraise;//1点赞 -1取消点赞
        if (isChecked) {
            commentpraise = 1;
        }else{
            commentpraise = -1;
        }
        String request = RequestAPI.editTVideoCommentBy(this, comment_id + "", commentpraise + "");
        OkGo.post(ConstantsURL.EditTVideoCommentBy)
                .tag(this)
                .params("request", request)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jo = new JSONObject(s);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if(statuscode.equals("1")){
                                if(commentpraise == 1){
                                    Toast.makeText(VideoDetailsActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(VideoDetailsActivity.this, "取消点赞", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(VideoDetailsActivity.this, "点赞失败，错误码:" + statusmessage, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * TODO 视频暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
    }

    /**
     * TODO 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(VIDEO_URL);
                mVideoView.requestFocus();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
    }

    /**
     * TODO 视频全屏非全屏改变
     * @param isFullscreen
     */
    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int widthPixels = displayMetrics.widthPixels;
            int heightPixels = displayMetrics.heightPixels;
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = widthPixels;
            layoutParams.height = heightPixels + DensityUtil.dip2px(this, 10);
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);
            published.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            mMediaController.setTitle(video.getTeachingvideo_tittle());
        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);
            published.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            mMediaController.setTitle("");
        }

        switchTitleBar(!isFullscreen);
    }

    /**
     * TODO switchBar处理
     * @param show
     */
    private void switchTitleBar(boolean show) {
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.d(TAG, "onComplete: 成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.d(TAG, "onError: 失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

}
