package com.hy.filelibrary.utils;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author:MtBaby
 * @date:2020/06/10 10:49
 * @desc:
 */
public class ActivityStatusUtils {

    public static void setStatusColor(Activity activity, @ColorRes int statusColor, boolean isBlack) {
        //在5.0系统以上设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(statusColor));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (isBlack) {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }
}
