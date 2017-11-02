package com.glorystudent.golflife.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.dialog.iosdialog.ActionSheetDialog;
import com.glorystudent.golflibrary.dialog.iosdialog.AlertDialog;
import com.glorystudent.golflibrary.util.GlideUtil;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.entity.TeamDetailEntity;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.glorystudent.golflife.util.TimeUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO
 */
public class TeamDetailActivity extends BaseActivity{


    private static final String TAG = "TeamDetailActivity";
    private static final int REQUEST_CAPTAIN_CODE = 0x111;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_team_call)
    TextView teamCall;
    @Bind(R.id.tv_activity_name)
    TextView activityName;
    @Bind(R.id.tv_team_join)
    TextView teamJoin;
    @Bind(R.id.iv_team_image)
    ImageView teamImage;
    @Bind(R.id.iv_team_logo)
    ImageView teamLogo;
    @Bind(R.id.tv_team_captain)
    TextView teamCaptain;
    @Bind(R.id.tv_team_address)
    TextView teamAddress;
    @Bind(R.id.tv_team_date)
    TextView teamDate;
    @Bind(R.id.tv_team_scale)
    TextView teamScale;
    @Bind(R.id.rl_team_member)
    RelativeLayout teamMember;
    @Bind(R.id.rl_team_event)
    RelativeLayout teamEvent;
    @Bind(R.id.rl_team_album)
    RelativeLayout teamAlbum;
    @Bind(R.id.rl_team_introduction)
    RelativeLayout teamIntroduction;
    @Bind(R.id.tv_menu)
    ImageView menu;
    @Bind(R.id.ll_bottom)
    LinearLayout bottom;
    @Bind(R.id.rl_team_apply)
    RelativeLayout teamApplyLayout;
    @Bind(R.id.ll_team_apply)
    LinearLayout llTeamApply;
    private int teamId;
    private TeamDetailEntity.TeamDetailBean teamDetail;//球队详情实体类
    private boolean isCaptain;//是否是队长
    private boolean isUser;//是否是队员
    private String telphone;//球队联系电话
    @Override
    protected int getContentId() {
        return R.layout.activity_team_detail;
    }
    @Override
    protected void init() {
        Intent intent = getIntent();
        teamId = intent.getIntExtra("id", -1);
        showLoading();
    }

    /**
     * TODO 获取球队详情信息
     */
    @Override
    protected void loadDatas() {
        String requestJson = RequestAPI.QueryTeamDetail(this, teamId+"");
        Log.i(TAG, "loadDatas: " + requestJson);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {//正常
                        TeamDetailEntity teamDetailEntity = new Gson().fromJson(jo.toString(), TeamDetailEntity.class);
                        if (teamDetailEntity != null) {
                            teamDetail = teamDetailEntity.getTeamDetail();
                            if (teamDetail != null) {
                                initValue();
                            }
                        }
                    } else if (statuscode.equals("2")) {//暂无数据
                        Log.i(TAG, "parseDatas: " + statusmessage);
                    } else {
                        Log.i(TAG, "parseDatas: " + statusmessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoading();
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(TeamDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        }).getEntityData(this, ConstantsURL.QueryTeamDetail, requestJson);
    }
    /**
     * 赋初始值
     */
    private void initValue() {
        teamCaptain.setText(teamDetail.getCaptainName());
        teamAddress.setText(teamDetail.getRegionText());
        teamDate.setText(TimeUtil.getPreviewTime(TimeUtil.getStandardDate(teamDetail.getCreatedate())));
        teamScale.setText(teamDetail.getPersonCount() + "人");
        activityName.setText(teamDetail.getTitle());
        GlideUtil.loadImageView(this, teamDetail.getLogo(), teamLogo);
        GlideUtil.loadImageView(this, teamDetail.getPic(), teamImage);
        isCaptain = teamDetail.isIsCaptain();
        isUser = teamDetail.isIsUser();
        telphone = teamDetail.getTelphone();
        if (isUser) {//是队员显示菜单
            menu.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
        } else {//不是队员隐藏菜单
            menu.setVisibility(View.GONE);
            bottom.setVisibility(View.VISIBLE);
        }
        if (isCaptain) {//是队长显示审核
            llTeamApply.setVisibility(View.VISIBLE);
        } else {//不是则隐藏
            llTeamApply.setVisibility(View.GONE);
        }
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_team_call, R.id.tv_team_join, R.id.rl_team_member, R.id.rl_team_event,
            R.id.rl_team_album, R.id.rl_team_introduction, R.id.tv_menu, R.id.rl_team_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
        if (teamDetail != null) {
            switch (view.getId()) {
                case R.id.tv_menu:
                    //菜单
                    if (isCaptain) {//队长
                        new ActionSheetDialog(this)
                                .builder()
                                .setCancelable(true)
                                .setCanceledOnTouchOutside(true)
                                .addSheetItem("编辑球队", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        editTeam();//编辑球队
                                    }
                                })
                                .addSheetItem("转让队长", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        transferCaptain();
                                    }
                                })
                                .addSheetItem("解散", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        quitOrDismiss();
                                    }
                                }).show();
                    } else {
                        new ActionSheetDialog(this)
                                .builder()
                                .setCancelable(true)
                                .setCanceledOnTouchOutside(true)
                                .addSheetItem("退队", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        quitOrDismiss();
                                    }
                                }).show();
                    }
                    break;
                case R.id.tv_team_call:
                    //咨询电话
                    new AlertDialog(this)
                            .builder()
                            .setTitle(telphone)
                            .setPositiveButton("呼叫", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    callPhone();
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                    break;
                case R.id.tv_team_join:
                    //申请加入
                    startApplicationMaterial();
                    break;
                case R.id.rl_team_member:
                    //球队队员
                    if (isUser) {
                        startTeamMember();
                    } else {
                        Toast.makeText(this, "您没有查看权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.rl_team_event:
                    //球队活动
                    startTeamEvent();
                    break;
                case R.id.rl_team_album:
                    //球队相册
                    startTeamAlbum();
                    break;
                case R.id.rl_team_apply:
                    //申请审核
                    startApplicationCheck();
                    break;
                case R.id.rl_team_introduction:
                    //球队信息
                    startTeamInfo();
                    break;
            }
        }
    }
    /**
     * TODO 编辑球队
     */
    private void editTeam() {
        Map<String, TeamDetailEntity.TeamDetailBean> map = new HashMap<>();
        map.put("EditTeamActivity", teamDetail);
        EventBus.getDefault().postSticky(map);
        Intent intent = new Intent(this, EditTeamActivity.class);
        startActivity(intent);
    }

    /**
     * TODO 转让队长
     */

    private void transferCaptain() {
        Intent intent = new Intent(this, TeamMemberActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("captainId", teamDetail.getCaptainid());
        intent.putExtra("isTransfer", true);//设置后进入不显示队长
        startActivityForResult(intent, REQUEST_CAPTAIN_CODE);
    }
    /**
     * TODO 打开申请入队资料
     */
    private void startApplicationMaterial() {
        Intent intent = new Intent(this, ApplicationMaterialActivity.class);
        intent.putExtra("id", teamId);
        startActivity(intent);
    }
    /**
     * TODO 打开球队队员页面
     */
    private void startTeamMember() {
        Intent intent = new Intent(this, TeamMemberActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("captainId", teamDetail.getCaptainid());
        startActivity(intent);
    }
    /**
     * TODO 打开球队活动
     */
    private void startTeamEvent() {
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("teamId", teamId);
        startActivity(intent);
    }
    /**
     *TODO  打开球队相册页面
     */
    private void startTeamAlbum() {
        Intent intent = new Intent(this, TeamAlbumActivity.class);
        intent.putExtra("teamId", teamId);
        startActivity(intent);
    }
    /**
     * TODO 打开申请审核
     */
    private void startApplicationCheck() {
        Intent intent = new Intent(this, ApplicationCheckActivity.class);
        intent.putExtra("id", teamId);
        startActivity(intent);
    }
    /**
     * TODO 打开球队信息
     */
    private void startTeamInfo() {
        Intent intent = new Intent(this, TeamInfoActivity.class);
        intent.putExtra("summary", teamDetail.getSummary());
        startActivity(intent);
    }
    /**
     * TODO 退队或解散
     */
    private void quitOrDismiss() {
        String requestJson = RequestAPI.SignOutTeam(this, teamId+"");
        Log.i(TAG, "quitOrDismiss: " + requestJson);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {//正常
                        Toast.makeText(TeamDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(EventBusMapUtil.getStringMap("TeamManagementActivity", "refresh"));
                        finish();
                    } else if (statuscode.equals("2")) {//暂无数据
                        Log.i(TAG, "parseDatas: " + statusmessage);
                    } else {
                        Log.i(TAG, "parseDatas: " + statusmessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoading();
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(TeamDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        }).getEntityData(this,ConstantsURL.SignOutTeam, requestJson);
    }
    /**
     * TODO 拨打电话的方法
     */

    private void callPhone() {
        requestPermission(new String[]{android.Manifest.permission.CALL_PHONE}, true, new PermissionsResultListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + telphone));
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(TeamDetailActivity.this, "没有拨号权限", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * TODO 更改队长
     *
     * @param captainid
     */
    private void requestTransferCaptain(int captainid) {
        String requestJson = RequestAPI.EditTeamInfo(this, teamId+"",captainid +"");
        Log.i(TAG, "quitOrDismiss: " + requestJson);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {//正常
                        Toast.makeText(TeamDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(EventBusMapUtil.getStringMap("TeamManagementActivity", "refresh"));
                        finish();
                    } else if (statuscode.equals("2")) {//暂无数据
                        Log.i(TAG, "parseDatas: " + statusmessage);
                    } else {
                        Log.i(TAG, "parseDatas: " + statusmessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoading();
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(TeamDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        }).getEntityData(this,ConstantsURL.EditTeamInfo, requestJson);
    }

    /**
     * TODO 处理EvenBus返回
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("TeamDetailActivity")) {
            if (map.get("TeamDetailActivity").equals("refresh")) {
                loadDatas();
            }
        }
    }

    /**
     * TODO 页面返回数据处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAPTAIN_CODE) {
                if (data != null) {
                    int userid = data.getIntExtra("userid", -1);
                    if (userid != -1) {
                        requestTransferCaptain(userid);
                    }
                }
            }
        }
    }
}
