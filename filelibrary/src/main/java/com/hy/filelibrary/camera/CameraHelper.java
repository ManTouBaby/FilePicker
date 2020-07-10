package com.hy.filelibrary.camera;

import android.content.Context;
import android.content.Intent;
import com.hy.filelibrary.base.FileSelect;
import com.hy.filelibrary.base.OnCameraListener;

public class CameraHelper {
    private static Builder mBuilder;

    public void openCamera(Context context) {
        context.startActivity(new Intent(context, CameraCaptureActivity.class));
    }

    public CameraHelper setOnCameraListener(OnCameraListener mOnCameraListener) {
        FileSelect.getInstance().setOnCameraListener(mOnCameraListener);
        return this;
    }

    public static Builder getBuilder() {
        return mBuilder;
    }

    public CameraHelper(Builder builder) {
        mBuilder = builder;
    }

    public static class Builder {
        private CameraOpenType mCameraOpenType;
        private long mDuration = 15000;
        private String savePath;

        public String getSavePath() {
            return savePath;
        }

        public Builder setSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public CameraOpenType getCameraOpenType() {
            return mCameraOpenType;
        }

        public Builder setCameraOpenType(CameraOpenType mCameraOpenType) {
            this.mCameraOpenType = mCameraOpenType;
            return this;
        }

        public long getDuration() {
            return mDuration;
        }

        public Builder setDuration(long mDuration) {
            this.mDuration = mDuration;
            return this;
        }

        public CameraHelper build() {
            return new CameraHelper(this);
        }
    }

}
