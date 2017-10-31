package com.glorystudent.golflife.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.glorystudent.golflibrary.base.BaseFragment;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflibrary.widget.oywidget.MyListView;
import com.glorystudent.golflibrary.widget.upmarqueeview.UPMarqueeView;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.activity.EventActivity;
import com.glorystudent.golflife.activity.NewsDetailsActivity;
import com.glorystudent.golflife.activity.TeamManagementActivity;
import com.glorystudent.golflife.adapter.HomeListAdapter;
import com.glorystudent.golflife.customView.PullToRefreshLayout;
import com.glorystudent.golflife.customView.PullableScrollView;
import com.glorystudent.golflife.entity.AdEntity;
import com.glorystudent.golflife.entity.AdRequestEntity;
import com.glorystudent.golflife.entity.NewsEntity;
import com.glorystudent.golflife.entity.NewsRequestEntity;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.util.DialogUtil;
import com.glorystudent.golflife.api.RequestAPI;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Gavin.J on 2017/10/25.
 */

public class StudentHomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.vp_head)
    public ConvenientBanner vp_head;
    @Bind(R.id.tv_vp_head)
    public TextView tv_vp_head;
    @Bind(R.id.student_lv)
    public MyListView student_lv;
    @Bind(R.id.msv)
    public PullableScrollView msv;
    @Bind(R.id.center_layout)
    public LinearLayout center_layout;
    @Bind(R.id.head_layout)
    public LinearLayout head_layout;
    @Bind(R.id.home_line)
    public View home_line;
    @Bind(R.id.refresh_view)
    public PullToRefreshLayout refresh_view;

    private UPMarqueeView upview1;//纵向自动滚动

    private AdEntity adEntity;//广告
    private boolean isLoadingAD = false;//广告是否加载完成
    private boolean isLoadingHeadNews = false;//新闻头条是否加载完成
    private boolean isLoadingNews = false;//新闻是否加载完成
    private NewsEntity newsEntity;//新闻
    private int page = 1;//默认加载第1页
    private List<String> data = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private List<NewsEntity.ListnewsBean> newsBeanList;
    private List<NewsEntity.ListnewsBean> news;//新闻list集合
    private List<Integer> idDatas;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private HomeListAdapter homeListAdapter;//ListView设配器


    @Override
    protected int getContentId() {
        return R.layout.fragment_stu_home;
    }

    @Override
    protected void init(View view) {
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
                page++;
                loadDatas();
            }
        });
        showLoading();
        msv.smoothScrollTo(0, 0);
        student_lv.setFocusable(false);
        upview1 = (UPMarqueeView) view.findViewById(R.id.marqueeView);
        homeListAdapter = new HomeListAdapter(getActivity());
        student_lv.setAdapter(homeListAdapter);
        student_lv.setOnItemClickListener(this);
    }

    /**
     * TODO 获取数据
     */
    @Override
    protected void loadDatas() {
        //获取轮播广告
        AdRequestEntity adRequestEntity = new AdRequestEntity();
        AdRequestEntity.AdBean adBean = new AdRequestEntity.AdBean();
        adBean.setAd_enable("true");
        adBean.setAd_location("1");
        adRequestEntity.setAd(adBean);
        String request = new Gson().toJson(adRequestEntity);
        String requestJson = RequestAPI.getRequestJson(getActivity(), request);
        adEntity = new AdEntity();
        OkGo.post(ConstantsURL.QueryAD)
                .tag(this)
                .params("request", requestJson)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        System.out.println("广告URL："+request.getUrl());
                        System.out.println("广告Params："+request.getParams());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jo = new JSONObject(s);
                            System.out.println("广告jo："+jo);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if (statuscode.equals("1")) {
                                adEntity = new Gson().fromJson(jo.toString(), AdEntity.class);
                                if (adEntity.getListad() != null) {
                                    addHeadViewPager();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isLoadingAD = true;
                        if (isLoadingAD && isLoadingHeadNews && isLoadingNews) {
                            dismissLoading();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        isLoadingAD = true;
                        if (isLoadingAD && isLoadingHeadNews && isLoadingNews) {
                            dismissLoading();
                        }
                    }
                });

        //获取新闻头条
        newsEntity = new NewsEntity();
        NewsRequestEntity newsRequestEntity = new NewsRequestEntity();
        NewsRequestEntity.NewsBean newsBean = new NewsRequestEntity.NewsBean();
        newsBean.setNews_reviewed("1");
        newsBean.setNews_top("1");
        newsRequestEntity.setNews(newsBean);
        newsRequestEntity.setPage(page);

        String newsJson2 = new Gson().toJson(newsRequestEntity);
        String newsrequestJson2 = RequestAPI.getRequestJson(getActivity(), newsJson2);
        OkGo.post(ConstantsURL.QueryNews)
                .tag(this)
                .params("request", newsrequestJson2)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        System.out.println("新闻1URL："+request.getUrl());
                        System.out.println("新闻1Params："+request.getParams());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jo = new JSONObject(s);
                            System.out.println("新闻1URL："+jo);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if (statuscode.equals("1")) {
                                newsEntity = new Gson().fromJson(jo.toString(), NewsEntity.class);
                                if (newsEntity.getListnews() != null) {
                                    center_layout.setVisibility(View.VISIBLE);
                                    head_layout.setVisibility(View.VISIBLE);
                                    home_line.setVisibility(View.VISIBLE);
                                    initdata();
                                }
                            } else if (statuscode.equals("2")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isLoadingHeadNews = true;
                        if (isLoadingAD && isLoadingHeadNews && isLoadingNews) {
                            dismissLoading();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        isLoadingHeadNews = true;
                        if (isLoadingAD && isLoadingHeadNews && isLoadingNews) {
                            dismissLoading();
                        }
                    }
                });


        newsBean.setNews_top("0");
        String newsJson = new Gson().toJson(newsRequestEntity);
        String newsrequestJson = RequestAPI.getRequestJson(getActivity(), newsJson);
        OkGo.post(ConstantsURL.QueryNews)
                .tag(this)//
                .params("request", newsrequestJson)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        System.out.println("新闻2URL："+request.getUrl());
                        System.out.println("新闻2Params："+request.getParams());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jo = new JSONObject(s);
                            System.out.println("新闻2URL："+jo);
                            String statuscode = jo.getString("statuscode");
                            String statusmessage = jo.getString("statusmessage");
                            if (statuscode.equals("1")) {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                                newsEntity = new Gson().fromJson(jo.toString(), NewsEntity.class);
                                List<NewsEntity.ListnewsBean> listnews = newsEntity.getListnews();
                                if (listnews != null) {
                                    if (isRefresh) {
                                        homeListAdapter.setDatas(listnews);
                                    } else {
                                        homeListAdapter.addDatas(listnews);
                                    }
                                }
                            } else if (statuscode.equals("2")) {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.SUCCEED);
                            } else {
                                refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isLoadingNews = true;
                        if (isLoadingAD && isLoadingHeadNews && isLoadingNews) {
                            dismissLoading();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        isLoadingNews = true;
                        if (isLoadingAD && isLoadingHeadNews && isLoadingNews) {
                            dismissLoading();
                        }
                        addHeadViewPager();
                        center_layout.setVisibility(View.VISIBLE);
                        head_layout.setVisibility(View.VISIBLE);
                        home_line.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        refresh_view.setRefreshState(isRefresh, PullToRefreshLayout.FAIL);
                    }
                });

    }
    /**
     * TODO 初始化数据
     * 初始化数据
     */
    private void initdata() {
        data = new ArrayList<>();
        idDatas = new ArrayList<>();
        newsBeanList = new ArrayList<>();
        news = newsEntity.getListnews();
        for (int i = 0; i < news.size(); i++) {
            if (news.get(i).getNews_top().equals("1")) {
                //头条数据
                newsBeanList.add(news.get(i));
                data.add(news.get(i).getNews_title());
                idDatas.add(news.get(i).getNews_id());
            }
        }
        initView();
    }
    /**
     * 初始化界面程序
     */
    private void initView() {
        setView();
        upview1.setViews(views);
    }

    /**
     * 初始化需要循环的View
     * 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
     * 假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
     */
    private void setView() {
        for (int i = 0; i < data.size(); i = i + 2) {
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_home_hot, null);
            //初始化布局的控件
            TextView tv1 = (TextView) moreView.findViewById(R.id.tv1);
            TextView tv2 = (TextView) moreView.findViewById(R.id.tv2);
            tv1.setOnClickListener(this);
            tv2.setOnClickListener(this);
            if (data.size() % 2 != 0 && i == data.size() - 1) {
                tv1.setText(data.get(i).toString());
                tv2.setVisibility(View.GONE);
                tv1.setTag(i);
            } else {
                //进行对控件赋值
                tv1.setTag(i);
                tv2.setTag(i + 1);
                tv1.setText(data.get(i).toString());
                tv2.setText(data.get(i + 1).toString());
            }
            //添加到循环滚动数组里面去
            views.add(moreView);
        }
    }
    /**
     * TODO 添加头部自动轮播
     * 添加头部自动轮播
     */
    private void addHeadViewPager() {
        final List<AdEntity.ListadBean> listad;
        if (adEntity != null && adEntity.getListad() != null) {
            listad = adEntity.getListad();
        } else {
            List<AdEntity.ListadBean> list = new ArrayList<>();
            AdEntity.ListadBean listadBean = new AdEntity.ListadBean();
            list.add(listadBean);
            listad = list;
        }

        //设置自动轮播
        vp_head.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, listad)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.shape_min_whitecircle, R.drawable.shape_min_orangecircle})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL).startTurning(3000);
        //设置监听滑动监听
        vp_head.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_vp_head.setText(listad.get(position).getAd_tittle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    /**
     * TODO 自动轮播类
     * 自动轮播需要的类
     */
    public class LocalImageHolderView implements Holder<AdEntity.ListadBean> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, AdEntity.ListadBean data) {
            GlideUtil.loadImageView(context, data.getAd_picture(), imageView);
        }
    }

    /**
     * TODO 新闻头条点击事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();//点击头条的下标
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra("newsid", idDatas.get(position));
        Bundle bundle = new Bundle();
        bundle.putSerializable("newsBean", newsBeanList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 新闻点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsEntity.ListnewsBean data = homeListAdapter.getData(position);
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra("newsid", data.getNews_id());
        Bundle bundle = new Bundle();
        bundle.putSerializable("newsBean", data);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick({R.id.stu_home_competition, R.id.stu_home_team})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.stu_home_competition:
                //赛事活动
                startActivity(new Intent(getActivity(), EventActivity.class));
                break;
            case R.id.stu_home_team:
                //球队管理
                if (SharedUtil.getBoolean(Constants.LOGIN_STATE)) {
                    startActivity(new Intent(getActivity(), TeamManagementActivity.class));
                } else {
                    DialogUtil.GotoLoginDialog(getActivity());
                }
                break;
        }
    }

//    /**
//     * TODO 是否去登录弹窗
//     */
//    private void showLogin() {
//        new AlertDialog(getActivity()).builder()
//                .setTitle("此功能需要登录")
//                .setMsg("是否去登录")
//                .setCancelable(true)
//                .setPositiveButton("去登录", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(getActivity(), LoginActivity.class));
//                        getActivity().finish();
//                    }
//                })
//                .setNegativeButton("取消", null).show();
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
