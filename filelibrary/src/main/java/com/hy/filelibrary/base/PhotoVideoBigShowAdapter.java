package com.hy.filelibrary.base;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.hy.filelibrary.R;
import com.hy.filelibrary.utils.GlideUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * @author:MtBaby
 * @date:2020/04/09 11:04
 * @desc:
 */
public class PhotoVideoBigShowAdapter extends BaseAdapter<FileBean> {
    private RecyclerView mRecyclerView;
    Map<Integer, Jzvd> stringJzvdMap = new HashMap<>();

    private static final int IMAGE = 0x100;
    private static final int VIDEO = 0x200;

    public PhotoVideoBigShowAdapter() {
        super(R.layout.item_type_video);
    }

    public PhotoVideoBigShowAdapter(List<FileBean> mDates) {
        super(mDates, R.layout.item_type_video);
    }

    @Override
    public int getItemViewType(int position) {
        FileBean fileBean = mDates.get(position);
        if (fileBean.getDuration() > 0) {
            return VIDEO;
        } else {
            return IMAGE;
        }
    }

    @Override
    public SmartVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mi_big_photo, null);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mi_big_video, null);
        }
        return new SmartVH(view);
    }

    @Override
    protected void onBindView(SmartVH holder, FileBean data, int position) {
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        holder.itemView.setLayoutParams(layoutParams);
        if (data.getDuration() > 0) {//视屏
            showVideo(holder, data.getPath(), data.getMimePath());
        } else {//图片0
            showImage(holder, data.getPath());
        }

    }


    private void showVideo(SmartVH holder, String videoUrl, String mimePath) {
        JzvdStd jzvdStd = holder.getViewById(R.id.mi_video_play);
        jzvdStd.fullscreenButton.setVisibility(View.GONE);
        jzvdStd.setUp(videoUrl, "", Jzvd.SCREEN_NORMAL);
        jzvdStd.posterImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (TextUtils.isEmpty(mimePath)) {
            GlideUtil.loadImage(jzvdStd.posterImageView, videoUrl);
        } else {
            GlideUtil.loadImage(jzvdStd.posterImageView, mimePath);
        }
        if (!stringJzvdMap.containsKey(jzvdStd.hashCode()))
            stringJzvdMap.put(jzvdStd.hashCode(), jzvdStd);
    }

    private void showImage(SmartVH holder, String imageUrl) {
        SubsamplingScaleImageView scaleImageView = holder.getViewById(R.id.mi_big_pic);
        GlideUtil.loadImage(holder.itemView.getContext(), imageUrl, new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                scaleImageView.setImage(ImageSource.bitmap(bitmapDrawable.getBitmap()));
            }
        });
    }

    public void showPosition(FileBean fileBean) {
        int clickPosition = -1;
        if (mDates != null) for (int i = 0; i < mDates.size(); i++) {
            FileBean message = mDates.get(i);
            if (message.getFileID() == fileBean.getFileID()) {
                clickPosition = i;
            }
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (clickPosition != -1) layoutManager.scrollToPosition(clickPosition);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.mi_video_play);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null && jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });
    }

    public void onDestroy() {
        for (Map.Entry<Integer, Jzvd> jzvdEntry : stringJzvdMap.entrySet()) {
            Jzvd jzvd = jzvdEntry.getValue();
            if (jzvd != null && Jzvd.CURRENT_JZVD != null && jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                    Jzvd.releaseAllVideos();
                }
            }
        }
        stringJzvdMap.clear();
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }


}
