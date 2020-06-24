package com.mt.filelibrary.page;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.transition.BitmapTransitionFactory;
import com.mt.filelibrary.FileMode;
import com.mt.filelibrary.FilePicker;
import com.mt.filelibrary.R;
import com.mt.filelibrary.base.FileBean;
import com.mt.filelibrary.base.FileSelect;
import com.mt.filelibrary.base.OnSelectItemListener;
import com.mt.filelibrary.utils.ActivityStatusUtils;
import com.mt.filelibrary.utils.NotifyDialog;

import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/05/18 11:35
 * @desc:
 */
public class ACFileShow extends AppCompatActivity implements OnSelectItemListener, View.OnClickListener {

    private FilePicker.Builder mBuilder;
    private TextView mCompleteBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_file_show);
        View view = findViewById(R.id.mt_toolbar_container);
        mCompleteBtn = findViewById(R.id.mt_complete_btn);
        mCompleteBtn.setOnClickListener(this);
        TextView mTitle = findViewById(R.id.mt_page_title);
        ImageView mBack = findViewById(R.id.mi_iv_back);
        mBack.setOnClickListener(this);
        mBuilder = FilePicker.getBuilder();
        FileSelect.getInstance().setSelectFiles(mBuilder.getFileSelect());
        List<FileBean> selectFiles = FileSelect.getInstance().getSelectFiles();
        initCompleteBtn(selectFiles);
        FileSelect.getInstance().addSelectItemListener(this);
        mTitle.setText(mBuilder.getTitle());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mBuilder.getFileMode() == FileMode.FILE) {
            transaction.replace(R.id.f_holder, new FileFragment()).commit();
            ActivityStatusUtils.setStatusColor(this, R.color.mt_white_status, false);
            view.setBackgroundColor(getResources().getColor(R.color.mt_white_status));
            mCompleteBtn.setBackground(getResources().getDrawable(R.drawable.icon_complete_corner));
            mTitle.setTextColor(getResources().getColor(R.color.mt_text_black));
            mBack.setImageResource(R.mipmap.icon_black_back);
        } else {
            transaction.replace(R.id.f_holder, new ImageVideoFragment()).commit();
            ActivityStatusUtils.setStatusColor(this, R.color.mt_black_status, true);
            view.setBackgroundColor(getResources().getColor(R.color.mt_black_bg));
            mCompleteBtn.setBackground(getResources().getDrawable(R.drawable.icon_complete_image_video_corner));
            mTitle.setTextColor(getResources().getColor(R.color.mt_text_white));
            mBack.setImageResource(R.mipmap.icon_width_back);
        }

    }

    private void initCompleteBtn(List<FileBean> fileBeans) {
        if (fileBeans != null && fileBeans.size() > 0) {
            mCompleteBtn.setEnabled(true);
            mCompleteBtn.setText(fileBeans.size() + "/" + mBuilder.getMaxCount() + "  完成");
        } else {
            mCompleteBtn.setEnabled(false);
            mCompleteBtn.setText("完成");
        }
    }

    @Override
    protected void onDestroy() {
        FileSelect.getInstance().onDestroy();
        FileSelect.getInstance().removeSelectItemListener(this);
        super.onDestroy();
    }

    @Override
    public void onSelectItem(List<FileBean> fileBeans) {
        initCompleteBtn(fileBeans);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.mt_complete_btn) {
            FileSelect.getInstance().finishSelect();
            finish();
        } else if (id == R.id.mi_iv_back) {
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        if (FileSelect.getInstance().getSelectFiles().size() > 0) {
            NotifyDialog.showNotifyDialog(this, (dialog, which) -> {
                dialog.dismiss();
                super.onBackPressed();
            });
        } else {
            super.onBackPressed();
        }
    }
}
