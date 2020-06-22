package com.mt.filelibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.mt.filelibrary.base.FileBean;
import com.mt.filelibrary.page.ACPreMediaShow;

import java.util.ArrayList;

/**
 * @author:MtBaby
 * @date:2020/06/10 10:22
 * @desc:
 */
public class MediaPreShow {
    public static void showPre(Activity context, View preView) {
        Intent intent = new Intent(context, ACPreMediaShow.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, preView, "ABC");
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    public static void showPre(Activity context, View clickView,FileBean fileBean, ArrayList<FileBean> fileBeans) {
        Intent intent = new Intent(context, ACPreMediaShow.class);
        intent.putExtra("FileList", fileBeans);
        intent.putExtra("FileClick", fileBean);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, clickView, "ABC");
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
