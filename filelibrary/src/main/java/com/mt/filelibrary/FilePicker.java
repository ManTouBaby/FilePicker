package com.mt.filelibrary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;

import com.mt.filelibrary.base.FileBean;
import com.mt.filelibrary.page.ACFileShow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * @author:MtBaby
 * @date:2020/05/18 11:34
 * @desc:
 */
public class FilePicker {
    private static Builder mBuilder;

    private FilePicker(Builder builder) {
        mBuilder = builder;
    }

    public static Builder getBuilder() {
        return mBuilder;
    }

    @IntDef({SELECT_MODE_SINGLE, SELECT_MODE_MULTI})
    @Retention(RetentionPolicy.SOURCE)
    @interface SelectMode {
    }

    public final static int SELECT_MODE_SINGLE = 0;
    public final static int SELECT_MODE_MULTI = 1;


    public void openFilePicker(Context context) {
        context.startActivity(new Intent(context, ACFileShow.class));
    }

    public static class Builder {
        private FileMode fileMode = FileMode.IMAGE_VIDEO;
        private String title;//标题
        private int selectionMode = SELECT_MODE_SINGLE;//选择模式，默认单选
        private int maxCount = 1;//最大选择数量，默认为1
        private ArrayList<FileBean> fileSelect = new ArrayList<>();//上一次选择的图片地址集合

        public Builder setFileMode(FileMode fileMode) {
            this.fileMode = fileMode;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSelectionMode(@SelectMode int selectionMode) {
            this.selectionMode = selectionMode;
            return this;
        }

        public Builder setMaxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder setFileSelect(ArrayList<FileBean> fileSelect) {
            this.fileSelect = fileSelect;
            return this;
        }

        public FileMode getFileMode() {
            return fileMode;
        }

        public String getTitle() {
            return title;
        }

        public int getSelectionMode() {
            return selectionMode;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public ArrayList<FileBean> getFileSelect() {
            return fileSelect;
        }

        public FilePicker builder() {
            return new FilePicker(this);
        }
    }

    public static class ImageVideoBuilder extends Builder {
        private boolean showCamera;//是否显示拍照Item，默认不显示
        private boolean showImage = true;//是否显示图片，默认显示
        private boolean showVideo = true;//是否显示视频，默认显示
        private boolean filterGif = false;//是否过滤GIF图片，默认不过滤
        private boolean singleType;//是否只支持选单类型（图片或者视频）
    }
}
