package com.ruyuan.eshop.common.core;

import java.util.Date;

/**
 * 日期辅助组件接口
 *
 * @author zhonghuashishan
 */
public interface DateProvider {

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    Date getCurrentTime();

    /**
     * 将Date对象格式化成：yyyy-MM-dd HH:mm:ss
     *
     * @param date Date对象
     * @return 格式化日期字符串
     */
    String formatDatetime(Date date);

    /**
     * 将日期字符串转化为Date对象
     *
     * @param datetime 日期字符串
     * @return date对象
     */
    Date parseDatetime(String datetime);

}
