package com.hy.filelibrary.page;

import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.filelibrary.FilePicker;
import com.hy.filelibrary.R;
import com.hy.filelibrary.base.BaseAdapter;
import com.hy.filelibrary.base.FileBean;
import com.hy.filelibrary.base.FileSelect;
import com.hy.filelibrary.base.OnItemClickListener;
import com.hy.filelibrary.base.SmartVH;
import com.hy.filelibrary.utils.FileUtil;
import com.hy.filelibrary.utils.format.FormatUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/05/18 16:14
 * @desc:
 */
public class FileFragment extends BaseFragment {
    protected RecyclerView mRecyclerView;
    protected RecyclerView mMenuList;

    private BaseAdapter<FileBean> mBaseAdapter;
    private BaseAdapter<FileBean> mMenuAdapter;
    private final static String FOLDER = "FOLDER";
    private LinearLayoutManager mLayoutManager;

    @Override
    protected int getLayout() {
        return R.layout.f_file_show;
    }

    @Override
    protected void init() {
        findViewById(R.id.mt_menu_name).setOnClickListener(v -> {
            File rootFileDir = Environment.getExternalStorageDirectory();
            List<FileBean> fileBeans = file2Beans(rootFileDir.listFiles());
            mBaseAdapter.setDates(fileBeans);
            mMenuAdapter.setDates(new ArrayList<>());
        });

        mMenuList = findViewById(R.id.mt_menu_list);
        mMenuList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mMenuList.setLayoutManager(mLayoutManager);
        mMenuList.setAdapter(mMenuAdapter = new BaseAdapter<FileBean>(new ArrayList<>(), R.layout.item_menu_show) {
            @Override
            protected void onBindView(SmartVH holder, FileBean data, int position) {
                TextView menuName = holder.getText(R.id.mt_menu_name);
                menuName.setText(data.getFileName());
            }
        });
        mMenuAdapter.setOnItemClickListener((OnItemClickListener<FileBean>) (view, data, position) -> callBack(position));

        File rootFileDir = Environment.getExternalStorageDirectory();
        mRecyclerView = findViewById(R.id.mt_file_show_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mBaseAdapter = new BaseAdapter<FileBean>(file2Beans(rootFileDir.listFiles()), R.layout.item_file_show) {
            @Override
            protected void onBindView(SmartVH holder, FileBean data, int position) {
                holder.getText(R.id.mt_file_name).setText(data.getFileName());
                TextView fileSize = holder.getText(R.id.mt_file_size);
                TextView fileDate = holder.getText(R.id.mt_file_date);
                ImageView typeTag = holder.getImage(R.id.iv_file_type);
                View lineTag = holder.getViewById(R.id.v_show);
                ImageView selectTag = holder.getImage(R.id.iv_select_tag);
                fileDate.setText(FileUtil.getDateStringByMilli(data.getModifyMilli()));
                typeTag.setImageResource(FormatUtils.getFileIcon(data.getPath()));
                if (FOLDER.equals(data.getMinType())) {
                    File file = new File(data.getPath());
                    fileSize.setText(file.listFiles().length + "项目");
                    lineTag.setVisibility(View.GONE);
                    selectTag.setImageResource(R.mipmap.icon_go_to);
                } else {
                    FileSelect instance = FileSelect.getInstance();
                    fileSize.setText(FileUtil.getStringByLength(data.getFileSize()));
                    lineTag.setVisibility(View.VISIBLE);
                    selectTag.setImageResource(R.drawable.icon_select);
                    boolean container = instance.container(data);
                    selectTag.setSelected(container);
                    selectTag.setOnClickListener(v -> {
                        int maxCount = FilePicker.getBuilder().getMaxCount();
                        if (!v.isSelected()) {
                            if (instance.getSelectFiles().size()< maxCount){
                                v.setSelected(!v.isSelected());
                                instance.addFileBean(data);
                            }else {
                                Toast.makeText(getContext(), String.format("最多可选%d张", maxCount), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            instance.removeFileBean(data);
                        }
                        notifyItemChanged(position);
                    });
                }
            }
        });
        mBaseAdapter.setOnItemClickListener((OnItemClickListener<FileBean>) (view, data, position) -> {
            if (FOLDER.equals(data.getMinType())) {
                mMenuAdapter.addData(data);
                File file = new File(data.getPath());
                mLayoutManager.scrollToPositionWithOffset(mMenuAdapter.getItemCount() - 1, Integer.MIN_VALUE);
                if (file.listFiles() != null && file.listFiles().length > 0) {
                    mBaseAdapter.setDates(file2Beans(file.listFiles()));
                } else {
                    mBaseAdapter.setDates(new ArrayList<>());
                }
            }
        });
    }

    private List<FileBean> file2Beans(File[] files) {
        List<FileBean> fileBeans = new ArrayList<>();
        if (files != null) for (File file : files) {
            FileBean fileBean = new FileBean();
            fileBean.setFileName(file.getName());
            fileBean.setModifyMilli(file.lastModified());
            fileBean.setFileSize(file.length());
            fileBean.setFileID(file.getPath().hashCode());
            String absolutePath = file.getAbsolutePath();
            fileBean.setPath(absolutePath);
            if (file.isDirectory()) {
                fileBean.setMinType(FOLDER);
            } else {
                String fileType = FormatUtils.getFormatName(absolutePath);
                fileBean.setMinType(fileType);
            }
            fileBeans.add(fileBean);
        }

        List<FileBean> beans = new ArrayList<>();
        int folderCount = -1;
        for (FileBean bean : fileBeans) {
            File file = new File(bean.getPath());
            if (file.isDirectory()) {
                folderCount++;
                beans.add(folderCount, bean);
            } else {
                beans.add(bean);
            }
        }

        return beans;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (mMenuAdapter.getItemCount() == 0) {
                    return false;
                } else {
                    callBack();
                    return true;
                }
            }
            return false;
        });
    }

    private void callBack() {
        callBack(mMenuAdapter.getItemCount() - 1);
    }

    private void callBack(int position) {
        if (position == 0) {
            File rootFileDir = Environment.getExternalStorageDirectory();
            mBaseAdapter.setDates(file2Beans(rootFileDir.listFiles()));
            mMenuAdapter.setDates(new ArrayList<>());
        } else {
            while (mMenuAdapter.getItemCount() != position) {
                mMenuAdapter.remove(mMenuAdapter.getItemCount() - 1);
            }
            FileBean fileBean = mMenuAdapter.getDates().get(mMenuAdapter.getItemCount() - 1);
            File file = new File(fileBean.getPath());
            mBaseAdapter.setDates(file2Beans(file.listFiles()));
        }
    }

}
