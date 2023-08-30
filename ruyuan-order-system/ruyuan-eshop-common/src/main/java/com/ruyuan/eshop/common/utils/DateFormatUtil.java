package com.ruyuan.eshop.common.utils;

import com.ruyuan.eshop.common.constants.CoreConstant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * java.util.Date日期格式化工具类
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class DateFormatUtil {

    private static DateFormat dateFormat = new SimpleDateFormat(CoreConstant.DATE_FORMAT_PATTERN);

    private static DateFormat dateTimeFormat = new SimpleDateFormat(CoreConstant.DATE_TIME_FORMAT_PATTERN);

    private static DateFormat timeFormat = new SimpleDateFormat(CoreConstant.TIME_FORMAT_PATTERN);

    /**
     * 日期格式化(不包含时间) yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return dateFormat.format(date);
    }

    /**
     * 日期格式化(包含时间) yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * 时间格式化(不包含年月日) HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return timeFormat.format(date);
    }

    /**
     * 日期格式化
     *
     * @param date    日期
     * @param pattern 自定义格式
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}