package com.mt.filelibrary.utils;

/**
 * @author:MtBaby
 * @date:2020/06/10 14:37
 * @desc:
 */
public class DateUtil {
    public static String getStrByLong(long durationLong) {
        String duration = null;
        long mine = (durationLong / 1000) / 60;
        long second = (durationLong / 1000) % 60;
        String mineStr = mine > 9 ? mine + "" : "0" + mine;
        String secondStr = second > 9 ? second + "" : "0" + second;
        return mineStr + ":" + secondStr;
    }
}
