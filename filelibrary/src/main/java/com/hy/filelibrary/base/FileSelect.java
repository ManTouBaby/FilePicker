package com.hy.filelibrary.base;

import com.hy.filelibrary.FileMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/06/08 10:28
 * @desc:
 */
public class FileSelect {
    private static FileSelect mFileSelect;
    private List<FileBean> mSelectFiles;
    private FileBean mFileBean;
    private List<OnSelectItemListener> onSelectItemListenerList = new ArrayList<>();
    private OnSelectFinishListener mOnSelectFinishListener;
    private OnCameraListener mOnCameraListener;

    private FileSelect() {
        mSelectFiles = new ArrayList<>();
    }

    public static FileSelect getInstance() {
        if (mFileSelect == null) {
            synchronized (FileSelect.class) {
                if (mFileSelect == null) {
                    mFileSelect = new FileSelect();
                }
            }
        }
        return mFileSelect;
    }

    public void onDestroy() {
        onSelectItemListenerList.clear();
        if (mSelectFiles != null) mSelectFiles.clear();
        mFileSelect = null;
        mSelectFiles = null;
    }

    public void setOnCameraListener(OnCameraListener mOnCameraListener) {
        this.mOnCameraListener = mOnCameraListener;
    }

    public void setOnSelectFinishListener(OnSelectFinishListener mOnSelectFinishListener) {
        this.mOnSelectFinishListener = mOnSelectFinishListener;
    }

    public void addSelectItemListener(OnSelectItemListener mOnSelectItemListener) {
        this.onSelectItemListenerList.add(mOnSelectItemListener);
    }

    public void removeSelectItemListener(OnSelectItemListener mOnSelectItemListener) {
        this.onSelectItemListenerList.remove(mOnSelectItemListener);
    }

    public void finishSelect(FileMode fileMode) {
        if (mOnSelectFinishListener != null) {
            mOnSelectFinishListener.onSelectFinish(fileMode,new ArrayList<>(mSelectFiles));
        }
    }

    public void setFileBean(FileBean mFileBean) {
        this.mFileBean = mFileBean;
        if (mOnCameraListener != null) {
            mOnCameraListener.onCamera(mFileBean);
        }
    }

    public int getSelectSize() {
        return mSelectFiles.size();
    }

    public void clearSelect() {
        if (mSelectFiles != null) mSelectFiles.clear();
    }

    public List<FileBean> getSelectFiles() {
        return mSelectFiles;
    }

    private void addListener() {
        if (onSelectItemListenerList.size() > 0) {
            for (OnSelectItemListener selectItemListener : onSelectItemListenerList) {
                selectItemListener.onSelectItem(mSelectFiles);
            }
        }
    }


    public void addFileBean(FileBean fileBean) {
        mSelectFiles.add(fileBean);
        fileBean.setSelectIndex(mSelectFiles.size());
        addListener();
    }

    public boolean container(FileBean fileBean) {
        if (mSelectFiles != null) for (FileBean bean : mSelectFiles) {
            if (bean.getFileID() == fileBean.getFileID()) {
                return true;
            }
        }
        return false;
    }

    public FileBean getFileBean(FileBean fileBean) {
        if (mSelectFiles != null) for (FileBean bean : mSelectFiles) {
            if (bean.getFileID() == fileBean.getFileID()) {
                return bean;
            }
        }
        return null;
    }

    public void setSelectFiles(List<FileBean> mSelectFiles) {
        if (mSelectFiles == null) {
            this.mSelectFiles = new ArrayList<>();
        } else {
            this.mSelectFiles = mSelectFiles;
            for (int i = 0; i < mSelectFiles.size(); i++) {
                FileBean bean = mSelectFiles.get(i);
                bean.setSelectIndex((i + 1));
            }
        }
    }

    public void removeFileBean(FileBean fileBean) {
        for (FileBean bean : mSelectFiles) {
            if (fileBean.getFileID() == bean.getFileID()) {
                mSelectFiles.remove(bean);
                break;
            }
        }
        for (int i = 0; i < mSelectFiles.size(); i++) {
            FileBean bean = mSelectFiles.get(i);
            bean.setSelectIndex((i + 1));
        }

        addListener();
    }


}
