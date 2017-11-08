package com.glorystudent.golflife.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
     * TODO 根据指定格式获取时间毫秒数，格式：yyyy年MM月dd日 HH:mm
     *
     * @param str
     * @return
     */
    public static Long getMilliseconds(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        try {
            Date date = format.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
    /**
     * TODO 根据指定格式获取时间毫秒数，格式：yyyy年MM月dd日
     *
     * @param str
     * @return
     */
    public static Long getMilliseconds2(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date date = format.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
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
    public static String getEventTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse = format.parse(time);
            String sTime = format.format(parse);
            return sTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * TODO 根据赛事活动时间返回date类型
     *
     * @return
     */
    public static Date getDateFromEvent(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * TODO 获取yyyy/MM/dd HH:mm:ss 格式的时间
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
    /**
     * TODO 获取时间，"yyyy-MM-dd'T'HH:mm:ss"类型
     *
     * @param time
     * @return
     */
    public static Date getStandardDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * TODO 发布活动的上传时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getUploadingTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sTime = format.format(date);
        return sTime;
    }
    /**
     * TODO 根据上传时间格式返回日期
     *
     * @param time
     * @return
     */
    public static Date getDateFromUploading(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * TODO 获取预览显示时间
     *
     * @param time
     * @return
     */
    public static String getPreviewTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String pTime = "";
        try {
            Date date = format.parse(time);
            pTime = getPreviewTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pTime;
    }
    /**
     * TODO 获取预览显示时间
     *
     * @param date
     * @return
     */
    public static String getPreviewTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String sTime = format.format(date);
        return sTime;
    }
    /**
     * TODO 获取明天的日期
     *
     * @return
     */
    public static Date getTomorrowDate() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(calendar.DATE, 1);//把日期向后推一天
        date = calendar.getTime();
        return date;
    }

    /**
     * TODO 创建活动发布时的时间
     *
     * @param date
     * @param text
     * @return
     */
    public static String getReleasedTime(Date date, String text) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String sTime = format.format(date);
        return sTime + " " + text;
    }

    public static String getReleasedTime2(Date date, String text) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String sTime = format.format(date);
        return sTime + " " + text;
    }

    /**
     * TODO 获取图片命名日期格式
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getImageNameTime(Date date) {
        if (date == null)
            date = new Date();
        String formatStr = new String();
        SimpleDateFormat matter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        formatStr = matter.format(date);
        return formatStr;
    }
    /**
     * TODO 获取查看凭证中的日期格式
     *
     * @return
     */
    public static String getCertificateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String STime = format.format(date);
        String week = getWeek(date);
        week.replace("星期", "周");
        return STime + "(" + week + ")";
    }
    /**
     * TODO 根据传入的Date获取星期几
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);
        return week;
    }

    /**
     * TODO yyyy.MM.dd格式时间
     * @param date
     * @return
     */
    public static String getTeamAlbumTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        String time = format.format(date);
        return time;
    }

    /**
     * TODO 获取时间的方法
     * @param date
     * @param mode
     * @param locale
     * @return
     */
   public static String getTimelocale(Date date, String mode, Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat(mode, locale);
        return format.format(date);
    }
}
