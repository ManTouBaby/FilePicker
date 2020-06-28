package com.mt.filelibrary.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mt.filelibrary.FilePicker;
import com.mt.filelibrary.R;
import com.mt.filelibrary.base.FileBean;
import com.mt.filelibrary.base.FileSelect;
import com.mt.filelibrary.base.OnSelectItemListener;
import com.mt.filelibrary.base.PhotoVideoBigShowAdapter;
import com.mt.filelibrary.utils.ActivityStatusUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/06/10 10:48
 * @desc:
 */
public class ACPreMediaShow extends AppCompatActivity implements OnSelectItemListener, View.OnClickListener {
    private PhotoVideoBigShowAdapter mShowAdapter;
    private FileBean mFileBean;
    private ArrayList<FileBean> mFileBeans;
    TextView mSelectTag;
    int mCurrentPage = 0;
    private TextView mCompleteBtn;
    private FilePicker.Builder mBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStatusUtils.setStatusColor(this, R.color.mt_black_status, true);
        setContentView(R.layout.ac_pre_show);
        findViewById(R.id.mi_iv_back).setOnClickListener(v -> finish());
        mCompleteBtn = findViewById(R.id.mt_complete_btn);
        mCompleteBtn.setOnClickListener(this);
        mBuilder = FilePicker.getBuilder();
        mFileBeans = (ArrayList<FileBean>) getIntent().getSerializableExtra("FileList");
        if (mBuilder.isShowCamera())mFileBeans.remove(0);
        mFileBean = (FileBean) getIntent().getSerializableExtra("FileClick");
        for (int i = 0; i < mFileBeans.size(); i++) {
            FileBean bean = mFileBeans.get(i);
            if (bean.getFileID() == mFileBean.getFileID()) {
                mCurrentPage = i;
                break;
            }
        }

        List<FileBean> selectFiles = FileSelect.getInstance().getSelectFiles();
        initCompleteBtn(selectFiles);
        FileSelect.getInstance().addSelectItemListener(this);

        TextView mTitle = findViewById(R.id.mt_page_title);
        mSelectTag = findViewById(R.id.mt_select_tag);
        mTitle.setText(mBuilder.getTitle());
        if (FileSelect.getInstance().container(mFileBean)) {
            mSelectTag.setSelected(true);
            mSelectTag.setText(FileSelect.getInstance().getFileBean(mFileBean).getSelectIndex() + "");
        } else {
            mSelectTag.setSelected(false);
            mSelectTag.setText("");
        }
        mSelectTag.setOnClickListener(v -> {
            if (v.isSelected() || FileSelect.getInstance().getSelectFiles().size() < mBuilder.getMaxCount()) {
                FileBean bean = mFileBeans.get(mCurrentPage);
                v.setSelected(!v.isSelected());
                String label = "";
                if (v.isSelected()) {
                    FileSelect.getInstance().addFileBean(bean);
                    label = bean.getSelectIndex() + "";
                } else {
                    FileSelect.getInstance().removeFileBean(bean);
                }
                mSelectTag.setText(label);
            } else {
                Toast.makeText(this, String.format("最多可选%d张", mBuilder.getMaxCount()), Toast.LENGTH_SHORT).show();
            }
        });
        initRecyclerView();
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

    private void initRecyclerView() {
        // PagerSnapHelper
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper() {
            // 在 Adapter的 onBindViewHolder 之后执行
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                // TODO 找到对应的Index
                int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                if (targetPos <= (mFileBeans.size() - 1)) {
                    mCurrentPage = targetPos;
                    FileBean bean = mFileBeans.get(targetPos);
                    String label = "";
                    System.out.println("-----------------findTargetSnapPosition");
                    if (FileSelect.getInstance().container(bean)) {
                        System.out.println("-----------------FileSelect.getInstance().container(bean)");
                        FileBean fileBean = FileSelect.getInstance().getFileBean(bean);
                        label = fileBean.getSelectIndex() + "";
                        mSelectTag.setSelected(true);
                    } else {
                        System.out.println("-----------------FileSelect.getInstance().container(bean)---else");
                        mSelectTag.setSelected(false);
                    }
                    System.out.println("-----------------RESULT");
                    mSelectTag.setText(label);
                    System.out.println("-----------------setText");
                }
                return targetPos;
            }
        };
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        RecyclerView mShowPhotoVideo = findViewById(R.id.mi_show_photo_video);
        mShowPhotoVideo.setHasFixedSize(true);
        mShowPhotoVideo.setLayoutManager(layout);
        mShowPhotoVideo.setAdapter(mShowAdapter = new PhotoVideoBigShowAdapter(mFileBeans));
        pagerSnapHelper.attachToRecyclerView(mShowPhotoVideo);
        mShowAdapter.showPosition(mFileBean);
        ViewCompat.setTransitionName(mShowPhotoVideo, "ABC");
    }

    @Override
    protected void onDestroy() {
        mShowAdapter.onDestroy();
        FileSelect.getInstance().removeSelectItemListener(this);
        super.onDestroy();
    }

    @Override
    public void onSelectItem(List<FileBean> fileBeans) {
        initCompleteBtn(fileBeans);
    }

    @Override
    public void onClick(View v) {
        FileSelect.getInstance().finishSelect(mBuilder.getFileMode());
        finish();
        setResult(RESULT_OK);
    }
}
