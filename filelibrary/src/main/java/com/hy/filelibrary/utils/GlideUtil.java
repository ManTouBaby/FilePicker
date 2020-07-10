package com.hy.filelibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * @author:MtBaby
 * @date:2020/06/19 9:57
 * @desc:
 */
public class GlideUtil {
    public static void loadImage(ImageView imageView, String url) {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.NONE);//全部使用磁盘缓存
//        Glide.get(imageView.getContext()).clearDiskCache();
        new Thread(()-> Glide.get(imageView.getContext()).clearDiskCache()).start();
        Glide.with(imageView.getContext()).load(url).apply(options).into(imageView);
    }

    public static void loadImageWithCache(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public static void loadImage(Context context, String url, SimpleTarget<Drawable> simpleTarget) {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(true);
        options.diskCacheStrategy(DiskCacheStrategy.NONE);//全部使用磁盘缓存
//        Glide.get(context).clearDiskCache();
        new Thread(()-> Glide.get(context).clearDiskCache()).start();
        Glide.with(context).load(url).apply(options).into(simpleTarget);
    }
}
