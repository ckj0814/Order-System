package com.ruyuan.eshop.common.utils;

import java.util.Random;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class RandomUtil {

    /**
     * 生成指定长度的随机数
     *
     * @param length
     * @return
     */
    public static String genRandomNumber(int length) {

        String sources = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            sb.append(sources.charAt(random.nextInt(9)));
        }
        return sb.toString();
    }


    private static final String ALL_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 生成指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String genRandomNumberStr(int length) {
        Random random = new Random();
        StringBuilder saltString = new StringBuilder(length);
        for (int i = 1; i <= length; ++i) {
            saltString.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }
        return saltString.toString();
    }

}