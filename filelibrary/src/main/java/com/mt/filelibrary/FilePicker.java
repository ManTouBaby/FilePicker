package com.mt.filelibrary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;

import com.mt.filelibrary.base.FileBean;
import com.mt.filelibrary.base.FileSelect;
import com.mt.filelibrary.base.OnCameraListener;
import com.mt.filelibrary.base.OnSelectFinishListener;
import com.mt.filelibrary.camera.CameraHelper;
import com.mt.filelibrary.camera.CameraOpenType;
import com.mt.filelibrary.page.ACFileShow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/05/18 11:34
 * @desc:
 */
public class FilePicker implements OnCameraListener {
    private static Builder mBuilder;

    private FilePicker(Builder builder) {
        mBuilder = builder;
    }

    public static Builder getBuilder() {
        return mBuilder;
    }

    @Override
    public void onCamera(FileBean fileBean) {
        FileSelect.getInstance().addFileBean(fileBean);
        FileSelect.getInstance().finishSelect();
    }

    public FilePicker setOnSelectFinishListener(OnSelectFinishListener onSelectFinishListener) {
        FileSelect.getInstance().setOnSelectFinishListener(onSelectFinishListener);
        return this;
    }

    public void openFilePicker(Context context) {
        FileMode fileMode = mBuilder.getFileMode();
        if (fileMode == FileMode.TAKE_PHOTO) {
            FileSelect.getInstance().clearSelect();
            new CameraHelper.Builder()
                    .setCameraOpenType(CameraOpenType.TAKE_PHOTO)
                    .build()
                    .setOnCameraListener(this)
                    .openCamera(context);
        } else if (fileMode == FileMode.TAKE_VIDEO) {
            FileSelect.getInstance().clearSelect();
            new CameraHelper.Builder()
                    .setCameraOpenType(CameraOpenType.TAKE_VIDEO)
                    .build()
                    .setOnCameraListener(this)
                    .openCamera(context);
        } else if (fileMode == FileMode.TAKE_PHOTO_IMAGE) {
            FileSelect.getInstance().clearSelect();
            new CameraHelper.Builder()
                    .setCameraOpenType(CameraOpenType.TAKE_PHOTO_IMAGE)
                    .build()
                    .setOnCameraListener(this)
                    .openCamera(context);
        } else {
            if (mBuilder.getSelectionMode() == SelectMode.SELECT_MODE_SINGLE) mBuilder.setMaxCount(1);
            context.startActivity(new Intent(context, ACFileShow.class));
        }
    }

    public static class Builder {
        private FileMode fileMode = FileMode.IMAGE_VIDEO;
        private String title;//标题
        private SelectMode selectionMode= SelectMode.SELECT_MODE_SINGLE;//选择模式，默认单选
        private int maxCount = 1;//最大选择数量，默认为1
        private ArrayList<FileBean> fileSelect = new ArrayList<>();//上一次选择的图片地址集合

        private boolean showCamera = false;//是否开启拍照
        private int videoDuration = 15000;
        private String savePath;

        public String getSavePath() {
            return savePath;
        }

        public Builder setSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public Builder setFileMode(FileMode fileMode) {
            this.fileMode = fileMode;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSelectionMode(SelectMode selectionMode) {
            this.selectionMode = selectionMode;
            return this;
        }

        public int getVideoDuration() {
            return videoDuration;
        }

        public Builder setVideoDuration(int videoDuration) {
            this.videoDuration = videoDuration;
            return this;
        }

        public boolean isShowCamera() {
            return showCamera;
        }

        public Builder setShowCamera(boolean showCamera) {
            this.showCamera = showCamera;
            return this;
        }

        public Builder setMaxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder setFileSelect(List<FileBean> fileSelect) {
            this.fileSelect = (ArrayList<FileBean>) fileSelect;
            return this;
        }

        public FileMode getFileMode() {
            return fileMode;
        }

        public String getTitle() {
            return title;
        }

        public SelectMode getSelectionMode() {
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

}
