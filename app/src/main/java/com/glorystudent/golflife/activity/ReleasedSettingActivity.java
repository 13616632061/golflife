package com.glorystudent.golflife.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.widget.switchbutton.SwitchView;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.entity.ReleasedRequestEntity;
import com.glorystudent.golflife.util.PickerViewUtil;
import com.glorystudent.golflife.util.TimeUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 高级设置页面
 */
public class ReleasedSettingActivity extends BaseActivity {

    private static final String TAG = "ReleasedSettingActivity";
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_apply_begin_time)
    TextView beginTime;
    @Bind(R.id.tv_apply_end_time)
    TextView endTime;
    @Bind(R.id.et_sponsor)
    EditText sponsor;
    @Bind(R.id.switch_view_apply_list)
    SwitchView applyList;
    @Bind(R.id.switch_view_photo_wall)
    SwitchView photoWall;
    private PickerViewUtil pickerViewUtil;
    private ReleasedRequestEntity entity;

    @Override
    protected int getContentId() {
        return R.layout.activity_released_setting;
    }

    @Override
    protected void init() {
        pickerViewUtil = new PickerViewUtil(this);
        entity = EventReleasedActivity.releasedRequestEntity;
        initValues();
    }
    /**
     * 赋初始值
     */
    private void initValues() {
        if (entity != null) {
            String beginTimeStr = entity.getEventactivity().getEventactivity_signupbegintime();
            Log.i(TAG, "init: " + beginTimeStr);
            if (null != beginTimeStr && !"".equals(beginTimeStr)) {
                beginTime.setText(TimeUtil.getReleasedTime(TimeUtil.getDateFromUploading(beginTimeStr), "开始"));
            } else {
                beginTime.setText("");
            }
            String endTimeStr = entity.getEventactivity().getEventactivity_signupendtime();
            if (null != endTimeStr && !"".equals(endTimeStr)) {
                endTime.setText(TimeUtil.getReleasedTime(TimeUtil.getDateFromUploading(endTimeStr), "结束"));
            } else {
                endTime.setText("");
            }
            String organizerStr = entity.getEventactivity().getEventactivity_organizer();
            if (organizerStr != null) {
                sponsor.setText(organizerStr);
            } else {
                sponsor.setText("");
            }
            applyList.setOpened(!entity.getEventactivity().getEventactivity_ifshowsignname());
            photoWall.setOpened(!entity.getEventactivity().getEventactivity_ifshowphotowall());
        }
    }

    /**
     * TODO 点击事件监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_apply_begin_time, R.id.tv_apply_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                close();
                break;
            case R.id.tv_apply_begin_time:
                applyBeginTime();
                break;
            case R.id.tv_apply_end_time:
                applyEndTime();
                break;
        }
    }

    /**
     * TODO 关闭
     */
    private void close() {
        //获取所有填入的数据
        acquireData();
        //发布活动进入时直接关闭
        finish();
    }

    /**
     * TODO 获取数据
     */
    private void acquireData() {
        entity.getEventactivity().setEventactivity_organizer(sponsor.getText().toString().trim());
        entity.getEventactivity().setEventactivity_ifshowphotowall(!photoWall.isOpened());
        entity.getEventactivity().setEventactivity_ifshowsignname(!applyList.isOpened());
    }

    /**
     * TODO 报名开始时间
     */
    private void applyEndTime() {
        Calendar currentCalendar = null;
        if (entity.getEventactivity().getEventactivity_signupendtime() != null) {
            Date currentDate = TimeUtil.getDateFromUploading(entity.getEventactivity().getEventactivity_signupendtime());
            currentCalendar = new GregorianCalendar();
            currentCalendar.setTime(currentDate);
        }
        pickerViewUtil.showTimePickerView("报名截止时间", currentCalendar, new PickerViewUtil.TimeLisener() {
            @Override
            public void onSubmit(Date date, View v) {
                endTime.setText(TimeUtil.getReleasedTime(date, "结束"));
                entity.getEventactivity().setEventactivity_signupendtime(TimeUtil.getUploadingTime(date));
            }
        });
    }

    /**
     * TODO 报名结束时间
     */
    private void applyBeginTime() {
        Calendar currentCalendar = null;
        if (entity.getEventactivity().getEventactivity_signupendtime() != null) {
            Date currentDate = TimeUtil.getDateFromUploading(entity.getEventactivity().getEventactivity_signupbegintime());
            currentCalendar = new GregorianCalendar();
            currentCalendar.setTime(currentDate);
        }
        pickerViewUtil.showTimePickerView("报名开始时间", currentCalendar, new PickerViewUtil.TimeLisener() {
            @Override
            public void onSubmit(Date date, View v) {
                beginTime.setText(TimeUtil.getReleasedTime(date, "开始"));
                entity.getEventactivity().setEventactivity_signupbegintime(TimeUtil.getUploadingTime(date));
            }
        });
    }
    /**
     * TODO 返回键监听
     */
    @Override
    public void onBackPressed() {
        close();
    }
}
