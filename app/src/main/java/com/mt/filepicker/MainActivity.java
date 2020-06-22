package com.mt.filepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mt.filelibrary.FileMode;
import com.mt.filelibrary.FilePicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mt_goto_file_picker).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setMaxCount(4)
                    .setTitle("文件")
                    .setFileMode(FileMode.FILE)
                    .setSelectionMode(FilePicker.SELECT_MODE_MULTI)
                    .builder()
                    .openFilePicker(MainActivity.this);
        });
        findViewById(R.id.mt_goto_image_picker).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setMaxCount(4)
                    .setFileMode(FileMode.IMAGE)
                    .setSelectionMode(FilePicker.SELECT_MODE_MULTI)
                    .setTitle("图片")
                    .builder()
                    .openFilePicker(MainActivity.this);
        });
        findViewById(R.id.mt_goto_video_picker).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setMaxCount(4)
                    .setFileMode(FileMode.VIDEO)
                    .setSelectionMode(FilePicker.SELECT_MODE_MULTI)
                    .setTitle("视屏")
                    .builder()
                    .openFilePicker(MainActivity.this);
        });
        findViewById(R.id.mt_goto_image_video_picker).setOnClickListener(v -> {
            new FilePicker.Builder()
                    .setMaxCount(4)
                    .setFileMode(FileMode.IMAGE_VIDEO)
                    .setSelectionMode(FilePicker.SELECT_MODE_MULTI)
                    .setTitle("图片/视屏")
                    .builder()
                    .openFilePicker(MainActivity.this);
        });
    }
}
