package yhr.git.com.easyutil.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Description: 日期工具类<br>
 *
 * @author sunfengbiao
 * @version 2.0
 * @since 2016/1/6
 */
public class DateFormatUtil {
    private static final long oneDay = 24 * 60 * 60 * 1000L;

    public enum Format {
        MM_dd("MM.dd"),
        MM_dd1("MM月dd日"),
        dd("dd"),
        mm_ss("mm:ss"),
        hh_MM("HH:mm"),
        yyyy_MM("yyyy-MM"),
        HH_mm_ss("HH:mm:ss"),
        HH_mm_ss1("HH_mm_ss"),
        HH_mm_ss_audio("HH°mm′ss″"),

        MM_dd_HH_mm("MM/dd HH:mm"),
        MM_dd_HH_mm2("MM-dd HH:mm"),
        MM_dd_HH_mm3("MM.dd HH:mm"),
        MM_dd_HH_mm4("MM.dd_HH:mm"),
        yyyyMMddHHmmss("yyyyMMddHHmmss"),
        yyyy_MM_dd_HH_mm_ss("yyyy/MM/dd HH:mm:ss"),
        yyyy_MM_dd__HH_mm("yyyy.MM.dd  HH:mm"),
        yyyy$MM$dd_HH_mm("yyyy年MM月dd日 HH:mm更新"),
        yyyy$MM$dd("yyyy年MM月dd日"),
        MM$dd("MM月dd日"),
        yyyy_MM_dd("yyyy-MM-dd"),
        MMdd("MMdd"),
        yyyy_MM_dd_HH_mm_ss2("yyyy-MM-dd HH:mm:ss"),
        yyyy_MM_dd_HH_mm_ss3("yyyy-MM-dd HH:mm"),
        yyyy_MM_dd_dot("yyyy.MM.dd"),
        yyyy_MM_dd_HH_mm("yyyy/MM/dd HH:mm"),;

        public String pattern;

        Format(String pattern) {
            this.pattern = pattern;
        }
    }

    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("", Locale.getDefault());

    public static String format(Format format, long time) {
        if (Check.isNull(format)) {
            throw new NullPointerException("format == null!");
        }
        synchronized (sDateFormat) {
            try {
                sDateFormat.applyPattern(format.pattern);
                return sDateFormat.format(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
                return sDateFormat.format(new Date(0));
            }
        }
    }




    public static String format(Format format, Date date) {
        if (Check.isNull(date)) return "";
        if (Check.isNull(format)) {
            throw new NullPointerException("format == null!");
        }
        synchronized (sDateFormat) {
            sDateFormat.applyPattern(format.pattern);
            try {
                return sDateFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                return sDateFormat.format(new Date(0));
            }
        }
    }

    public static long format(Format format, String time) {
        if (Check.isNull(time)) return 0L;
        if (Check.isNull(format)) {
            throw new NullPointerException("format == null!");
        }
        synchronized (sDateFormat) {
            try {
                sDateFormat.applyPattern(format.pattern);
                Date date = sDateFormat.parse(time);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }


    /**
     * 显示时间，如果与当前时间差别小于一天，则自动用**秒(分，小时)前，如果大于一天则用format规定的格式显示
     *
     * @param date
     * @return
     */
    public static String showTime(long date) {
        String r = "1970-1-1 00:00";
        if (date <= 0l) return r;

        long nowtimelong = System.currentTimeMillis();
        long result = Math.abs(nowtimelong - date);

        if (result < 60000) {// 一分钟内
            long seconds = result / 1000;
            if (seconds == 0) {
                r = "刚刚";
            } else {
                r = seconds + "秒前";
            }
        } else if (result >= 60000 && result < 3600000) {// 一小时内
            long seconds = result / 60000;
            r = seconds + "分钟前";
        } else if (result >= 3600000 && result < 86400000) {// 一天内
            long seconds = result / 3600000;
            r = seconds + "小时前";
        } else if (result >= 86400000 && result < 1702967296) {// 三十天内
            long seconds = result / 86400000;
            r = seconds + "天前";
        } else {// 日期格式
            String format = "yyyy-MM-dd";
            SimpleDateFormat df = new SimpleDateFormat(format);
            r = df.format(date).toString();
        }
        return r;
    }


    private static Map<String, SimpleDateFormat> formatMap = new HashMap<String, SimpleDateFormat>();

    /**
     * 将要字符串转换成Date类型
     * <p/>
     *
     * @param str    要转换的字符串
     * @param format 转换格式 例如：yyyy-MM-dd HH:mm:ss
     * @return <p/>
     */
    public static Date parseDate(String str, String format) {
        if (str == null || "".equals(str)) {
            return null;
        }
        SimpleDateFormat sdf = formatMap.get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.CHINA);
            formatMap.put(format, sdf);
        }
        try {
            synchronized (sdf) {
                return sdf.parse(str);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把Date类型的时间转换成字符串
     * <p/>
     *
     * @param date   要转换的时间
     * @param format 转换格式 例如：yyyy-MM-dd HH:mm:ss
     * @return <p/>
     */
    @NonNull
    public static String formatDate(@Nullable Date date, @NonNull String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = formatMap.get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.CHINA);
            formatMap.put(format, sdf);
        }
        synchronized (sdf) {
            return sdf.format(date);
        }
    }

    /**
     * 把时间戳类型的时间转换成字符串
     * <p/>
     *
     * @param time   要转换的时间
     * @param format 转换格式 例如：yyyy-MM-dd HH:mm:ss
     * @return <p/>
     */
    public static String formatDate(long time, String format) {
        return formatDate(new Date(time), format);
    }


    /**
     * 是否是同一年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameYear(Date date1, Date date2) {

        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.setTime(date2);
        int year2 = calendar.get(Calendar.YEAR);
        return year1 == year2;
    }

    /**
     * 是否是同一月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int month1 = calendar.get(Calendar.MONTH);
        calendar.clear();
        calendar.setTime(date2);
        int month2 = calendar.get(Calendar.MONTH);
        return month1 == month2;

    }

    /**
     * 是否是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.clear();
        calendar.setTime(date2);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);
        return day1 == day2;
    }

    /**
     * 是不是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(long date1, long date2) {
        return isSameDay(new Date(date1), new Date(date2));
    }


    /**
     * 相差天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long differenceDay(long date1, long date2) {
        long offsets = Math.abs(date1 - date2);
        return offsets / (1000 * 60 * 60 * 24);
    }


    /**
     * 是不是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(long date) {
        return isSameDay(new Date().getTime(), date);
    }

    /**
     * 获取时长
     *
     * @param duration
     * @return
     */
    public static String getDuration(long duration) {

        String str = "0秒";
        if (duration != 0) {
            long second = duration / 1000;
            long minute = second / 60;
            str = minute != 0 ? second / 60 + "分" + (second - 60 * minute) + "秒" : (second - 60 * minute) + "秒";
        }
        return str;
    }

    /**
     * 获取时间： 是今年返回月日 时分，不是返回年月日时分
     *
     * @param date
     * @return
     */
    public static String transferDateToStrRole1(long date) {

        Date currentTime = new Date();
        Date transferDate = new Date(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transferDate);


        String str = "";

        if (isSameDay(currentTime, transferDate)) {
            str = "今天";
        } else if (isSameYear(currentTime, transferDate)) {
            str = calendar.get(Calendar.MONTH) + 1 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + "  " + formatDate(transferDate, "mm:ss");
        } else {
            str = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + 1 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + "  " + formatDate(transferDate, "mm:ss");
        }
        return str;
    }


    /**
     * 秒转化位时分秒
     *
     * @param time
     * @return
     */
    public static String secToTime(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return "0分0秒";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99小时59分59秒";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String getAuidoDuration(long duration) {
        long eight = 8 * 60 * 60 * 1000 * -1;
        String durationStr = DateFormatUtil.format(Format.HH_mm_ss_audio, eight + duration);
        //截掉时
        if ("00".equals(durationStr.substring(0, 2))) {
            durationStr = durationStr.substring(3);
        }
        //截掉分
        if ("00".equals(durationStr.substring(0, 2))) {
            durationStr = durationStr.substring(3);
        }
        return durationStr;
    }

    /**
     * 获取当天0点时间戳
     *
     * @return
     */
    public static long getToday0Hours() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static String getWeekday(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        String weekdayStr = "";
        switch (weekday) {
            case Calendar.SUNDAY:
                weekdayStr = "星期日";
                break;
            case Calendar.MONDAY:
                weekdayStr = "星期一";
                break;
            case Calendar.TUESDAY:
                weekdayStr = "星期二";
                break;
            case Calendar.WEDNESDAY:
                weekdayStr = "星期三";
                break;
            case Calendar.THURSDAY:
                weekdayStr = "星期四";
                break;
            case Calendar.FRIDAY:
                weekdayStr = "星期五";
                break;
            case Calendar.SATURDAY:
                weekdayStr = "星期六";
                break;
        }
        return weekdayStr;
    }

    public static String getDayStr(long time) {
        long today0Hours = getToday0Hours();
        if (time >= today0Hours) return "今天";
        if (time >= today0Hours - oneDay) return "昨天";
        return format(Format.yyyy_MM_dd, time);
    }




}
