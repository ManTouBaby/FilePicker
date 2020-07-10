package com.hy.filelibrary.camera;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;


import com.hy.filelibrary.R;
import com.hy.filelibrary.base.FileBean;
import com.hy.filelibrary.base.FileSelect;

import java.io.File;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class CameraCaptureActivity extends AppCompatActivity implements CameraCaptureInterface {
    public static final String MODE = "MODE";
    public static final String DURATION = "DURATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.camera_capture_main_framelayout, new CameraCaptureRecordFragment())
                .commit();
    }


    @Override
    public void onBackPressed() {
        try {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            ((BaseFragment) fragmentList.get(fragmentList.size() - 1)).finish();
        } catch (Exception e) {
            finish();
        }
    }

    // ——————————————————————————————————————————————————————————————————
    @Override
    public void returnPath(int type, final String path, final long duration) {
        final FileSelect instance = FileSelect.getInstance();
        if (type == Util.Const.MODE_PHOTO) {
            // 开始压缩
            final ProgressDialog dialog = ProgressDialog.show(CameraCaptureActivity.this, "提示", "正在压缩图片中...", false, false);
            Luban.with(this)
                    .load(path)
                    .ignoreBy(300)
                    .setTargetDir(getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath())
                    .setRenameListener(filePath -> Util.randomName() + ".jpg")
                    .filter(s -> !TextUtils.isEmpty(s))
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            if (dialog != null) dialog.dismiss();
                            // 压缩成功
                            FileBean fileBean = new FileBean();
                            fileBean.setFileName(file.getName());
                            fileBean.setFileSize(file.length());
                            fileBean.setMimePath(file.getAbsolutePath());
                            fileBean.setPath(path);
                            instance.setFileBean(fileBean);
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (dialog != null) dialog.dismiss();
                            // 压缩失败 返回原图
                            File file = new File(path);
                            FileBean fileBean = new FileBean();
                            fileBean.setFileName(file.getName());
                            fileBean.setFileSize(file.length());
                            fileBean.setMimePath(file.getAbsolutePath());
                            fileBean.setPath(path);
                            instance.setFileBean(fileBean);
                            finish();
                        }
                    })
                    .launch();

        } else if (type == Util.Const.MODE_VIDEO) {
            // 直接返回
            File file = new File(path);
            FileBean fileBean = new FileBean();
            fileBean.setMimePath(file.getAbsolutePath());
            fileBean.setFileName(file.getName());
            fileBean.setFileSize(file.length());
            fileBean.setDuration(duration);
            fileBean.setPath(path);
            instance.setFileBean(fileBean);
            finish();
        }
    }


    // ——————————————————————————————————————————————————————————————————


}
