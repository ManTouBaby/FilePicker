package com.hy.filelibrary.camera;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.hy.filelibrary.R;


import java.io.File;

/**
 * Description: 预览拍摄的照片和视频的fragment
 */
@SuppressLint("ValidFragment")
public class CameraCapturePreviewFragment extends BaseFragment {

    private SubsamplingScaleImageView viewImage;
    private ImageView viewImageBack;
    private TextView viewTextConfirm;
    private VideoView viewVideoView;


    private int type;
    private String filePath;
    private long realDuration;

    public CameraCapturePreviewFragment(int type, String filePath, long duration) {
        this.type = type;
        this.filePath = filePath;
        this.realDuration = duration;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.camera_fragment_capture_preview, container, false);
        viewImage = view.findViewById(R.id.camera_capture_preview_image);
        viewVideoView = view.findViewById(R.id.camera_capture_preview_video);
        viewImageBack = view.findViewById(R.id.camera_capture_preview_iv_back);
        viewTextConfirm = view.findViewById(R.id.camera_capture_preview_tv_confirm);

        // 根据类型
        if (type == Util.Const.MODE_PHOTO) {
            viewImage.setVisibility(View.VISIBLE);
            // 查看图片
            viewImage.setMinimumDpi(50);
            viewImage.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
            viewImage.setImage(ImageSource.uri(Uri.fromFile(new File(filePath))));
        } else {
            viewVideoView.setVisibility(View.VISIBLE);
            // 播放视频
            viewVideoView.setVideoPath(filePath);
            // viewVideoView.setMediaController(new MediaController(getContext()));
            viewVideoView.setMediaController(null);
            viewVideoView.setOnPreparedListener(mp -> viewVideoView.start());

            viewVideoView.setOnCompletionListener(mp -> viewVideoView.start());

        }

        // ——————————————————————————————————点击事件——————————————————————————————————————
        viewImageBack.setOnClickListener(v -> finish());
        viewTextConfirm.setOnClickListener(v -> {
            // 通知刷新相册
            Util.notifyAlbumDataChanged(getContext(), new File(filePath));
            // 确认并返回
            ((CameraCaptureActivity) getActivity()).returnPath(type, filePath, realDuration);
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Util.setFullScreen(getActivity());
    }

    @Override
    public void finish() {
        // 删除原来的
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
