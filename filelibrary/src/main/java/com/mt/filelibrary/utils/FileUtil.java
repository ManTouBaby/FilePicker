package com.mt.filelibrary.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @author:MtBaby
 * @date:2020/06/04 9:47
 * @desc:
 */
public class FileUtil {
    public static String getStringByLength(long length) {
        StringBuilder stringBuilder = new StringBuilder();
        double v = length / 1024.0 / 1024.0;
        if (v > 1) {
            BigDecimal b = new BigDecimal(v);
            v = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            stringBuilder.append(v).append("MB");
        } else {
            v = length / 1024.0;
            BigDecimal b = new BigDecimal(v);
            v = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (v > 1) {
                stringBuilder.append(v).append("KB");
            } else {
                stringBuilder.append(length).append("B");
            }
        }
        return stringBuilder.toString();
    }

    public static String getDateStringByMilli(long milli) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  //设置日期格式
        return df.format(milli);
    }

    // 获取视频缩略图
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}
