package com.ruyuan.eshop.common.core;

import com.ruyuan.eshop.common.constants.CoreConstant;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期辅助组件
 *
 * @author zhonghuashishan
 */
@Slf4j
public class DateProviderImpl implements DateProvider {

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    @Override
    public Date getCurrentTime() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(CoreConstant.DATE_TIME_FORMAT_PATTERN);
        try {
            return dateFormatter.parse(dateFormatter.format(new Date()));
        } catch (Exception e) {
            log.warn("getCurrentTime error");
        }
        return null;
    }

    /**
     * 将Date对象格式化成：yyyy-MM-dd HH:mm:ss
     *
     * @param date Date对象
     * @return 格式化日期字符串
     */
    @Override
    public String formatDatetime(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(CoreConstant.DATE_TIME_FORMAT_PATTERN);
        return dateFormatter.format(date);
    }

    /**
     * 将日期字符串转化为Date对象
     *
     * @param datetime 日期字符串
     * @return date对象
     */
    @Override
    public Date parseDatetime(String datetime) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(CoreConstant.DATE_TIME_FORMAT_PATTERN);
        try {
            return dateFormatter.parse(datetime);
        } catch (Exception e) {
            log.warn("parseDatetime error");
        }
        return null;

    }

}
