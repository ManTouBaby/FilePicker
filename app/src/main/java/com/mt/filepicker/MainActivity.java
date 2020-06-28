package com.mt.filepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mt.filelibrary.FileMode;
import com.mt.filelibrary.FilePicker;
import com.mt.filelibrary.SelectMode;
import com.mt.filelibrary.base.FileBean;
import com.mt.filelibrary.base.OnSelectFinishListener;
import com.mt.filelibrary.base.OnSelectItemListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectFinishListener {

    private List<FileBean> fileBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mt_goto_file_picker).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setMaxCount(4)
                    .setTitle("文件")
                    .setFileMode(FileMode.FILE)
                    .setFileSelect(fileBeans)
                    .setSelectionMode(SelectMode.SELECT_MODE_MULTI)
                    .builder()
                    .setOnSelectFinishListener(this)
                    .openFilePicker(MainActivity.this);
        });
        findViewById(R.id.mt_goto_image_picker).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setMaxCount(4)
                    .setFileMode(FileMode.IMAGE)
                    .setTitle("图片")
                    .setShowCamera(true)
                    .setFileSelect(fileBeans)
                    .setSelectionMode(SelectMode.SELECT_MODE_MULTI)
                    .builder()
                    .setOnSelectFinishListener(this)
                    .openFilePicker(MainActivity.this);
        });
        findViewById(R.id.mt_goto_video_picker).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setMaxCount(4)
                    .setFileMode(FileMode.VIDEO)
                    .setShowCamera(true)
                    .setFileSelect(fileBeans)
                    .setTitle("视屏")
                    .setSelectionMode(SelectMode.SELECT_MODE_MULTI)
                    .builder()
                    .setOnSelectFinishListener(this)
                    .openFilePicker(MainActivity.this);
        });
        findViewById(R.id.mt_goto_image_video_picker).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setMaxCount(4)
                    .setFileMode(FileMode.IMAGE_VIDEO)
                    .setShowCamera(true)
                    .setFileSelect(fileBeans)
                    .setTitle("图片/视屏")
                    .setSelectionMode(SelectMode.SELECT_MODE_MULTI)
                    .builder()
                    .setOnSelectFinishListener(this)
                    .openFilePicker(MainActivity.this);
        });

        findViewById(R.id.mt_take_photo).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setFileMode(FileMode.TAKE_PHOTO)
                    .setSavePath(getPackageName())
                    .builder()
                    .setOnSelectFinishListener(this)
                    .openFilePicker(MainActivity.this);
        });
        findViewById(R.id.mt_take_video).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setFileMode(FileMode.TAKE_VIDEO)
                    .setSavePath(getPackageName())
                    .builder()
                    .setOnSelectFinishListener(this)
                    .openFilePicker(MainActivity.this);
        });
        findViewById(R.id.mt_take_video_photo).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setFileMode(FileMode.TAKE_PHOTO_IMAGE)
                    .setSavePath(getPackageName())
                    .builder()
                    .setOnSelectFinishListener(this)
                    .openFilePicker(MainActivity.this);
        });
    }


    @Override
    public void onSelectFinish(FileMode fileMode, List<FileBean> fileBeans) {
        this.fileBeans = fileBeans;
        for (FileBean fileBean : fileBeans) {
            System.out.println(fileBean.getFileName() + "--" + fileBean.getPath());
        }
    }
}
