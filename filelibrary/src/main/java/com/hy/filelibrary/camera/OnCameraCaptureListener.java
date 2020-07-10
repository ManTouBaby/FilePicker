package com.hy.filelibrary.camera;

/**
 * Description:
 */
interface OnCameraCaptureListener {

    void onToggleSplash(String flashMode);

    void onFocusSuccess(float x, float y);

    void onCapturePhoto(String photoPath);

    void onCaptureRecord(String filePath);

    void onError(Throwable throwable);
}
