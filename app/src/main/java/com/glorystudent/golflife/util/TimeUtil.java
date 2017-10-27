package com.glorystudent.golflife.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gavin.J on 2017/10/25.
 */

public class TimeUtil {
    /**
     * TODO 给定毫秒数，获取HH:mm格式时间
     *
     * @param time
     * @return
     */
    public static String getChatTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String t = sdf.format(date);
        return t;
    }
    /**
     * TODO 转换UTC时间为本地时间，当前时间-得到的本地时间 = 时间差（小时为单位）
     *
     * @param time
     * @return
     */
    public static String getTime(String time) {
        long hours = 0;
        try {
            String ts = time.replace("Z", "UTC");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date dt = sdf.parse(ts);
            Date currentDate = new Date();
            long leadTime = currentDate.getTime() - dt.getTime();
            hours = leadTime / 1000 / 60 / 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (hours <= 0) {
            return "刚刚";
        } else if (hours >= 24) {
            return hours / 24 + "天前";
        }
        return hours + "小时前";
    }
    /**
     * TODO 获取赛事活动的展示时间
     *
     * @param time
     * @return
     */
    public static String getCompetitionTime(String time) {
        SimpleDateFormat formatOld = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat formatNew = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        try {
            Date parse = formatOld.parse(time);
            Log.d("print", "getCompetitionTime: " + parse);
            String sTime = formatNew.format(parse);
            return sTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * TODO 获取yyyy-MM-dd格式日期
     *
     * @param date
     * @return
     */
    public static String getEventTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sTime = format.format(date);
        return sTime;
    }
    /**
     * 获取yyyy/MM/dd HH:mm:ss 格式的时间
     *
     * @param time
     * @return
     */
    public static Date getDateFromJoin(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
