package com.mt.filelibrary.base;

import android.support.annotation.IntDef;
import android.view.View;
import android.widget.Toast;

import com.mt.filelibrary.FilePicker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/06/09 14:58
 * @desc:
 */
public abstract class BaseSelectAdapter extends BaseAdapter<FileBean> {
    private final FileSelect mFileSelect;
    private FilePicker.Builder mBuilder;
    private OnItemSelectListener mOnItemSelectListener;

    @IntDef({SELECT_MODE_SINGLE, SELECT_MODE_MULTI, SELECT_MODE_MULTI_BY_INDEX})
    @Retention(RetentionPolicy.SOURCE)
    @interface SelectMode {
    }

    public final static int SELECT_MODE_SINGLE = 0;
    public final static int SELECT_MODE_MULTI = 1;
    public final static int SELECT_MODE_MULTI_BY_INDEX = 2;

    //    protected Map<Integer, FileBean> selectMap = new LinkedHashMap<>();
    private int mSelectMode = SELECT_MODE_SINGLE;


    public BaseSelectAdapter(int mResLayout) {
        this(null, mResLayout, SELECT_MODE_SINGLE);
    }


    public BaseSelectAdapter(int mResLayout, @SelectMode int selectMode) {
        this(null, mResLayout, selectMode);
    }

    public BaseSelectAdapter(List<FileBean> mDates, int mResLayout) {
        this(mDates, mResLayout, SELECT_MODE_SINGLE);
    }

    public BaseSelectAdapter(List<FileBean> mDates, int mResLayout, @SelectMode int selectMode) {
        super(mDates, mResLayout);
        this.mSelectMode = selectMode;
        mFileSelect = FileSelect.getInstance();
        mBuilder = FilePicker.getBuilder();
    }

    @Override
    protected void onBindView(SmartVH holder, FileBean data, int position) {
        View viewTag = getTagView(holder);
        viewTag.setOnClickListener(v -> {
            if (mSelectMode == SELECT_MODE_SINGLE) {
                FileSelect.getInstance().clearSelect();
                FileSelect.getInstance().addFileBean(data);
                notifyDataSetChanged();
            }

            if (mSelectMode == SELECT_MODE_MULTI) {
                if (!mFileSelect.container(data)) {
                    FileSelect.getInstance().addFileBean(data);
                } else {
                    FileSelect.getInstance().removeFileBean(data);
                }
                notifyItemChanged(position);
            }

            if (mSelectMode == SELECT_MODE_MULTI_BY_INDEX) {
                if (!mFileSelect.container(data)) {
                    if (mFileSelect.getSelectSize() < mBuilder.getMaxCount()) {
                        FileSelect.getInstance().addFileBean(data);
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(viewTag.getContext(), String.format("最多可选%d张", mBuilder.getMaxCount()), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    FileSelect.getInstance().removeFileBean(data);
                    notifySelectIndex();
                    notifyItemChanged(position);
                }
                if (mOnItemSelectListener != null) {
                    mOnItemSelectListener.OnItemSelect(mFileSelect.getSelectFiles());
                }
            }
        });
        viewTag.setSelected(mFileSelect.container(data));
        onSelectBindView(holder, viewTag, data, position);
    }

    private void notifySelectIndex() {
        for (FileBean fileBean : mFileSelect.getSelectFiles()) {
            for (int j = 0; j < mDates.size(); j++) {
                FileBean bean = mDates.get(j);
                if (fileBean.getFileID() == bean.getFileID()) {
                    notifyItemChanged(j);
                    break;
                }
            }
        }
    }

    protected abstract void onSelectBindView(SmartVH holder, View tagView, FileBean data, int position);

    protected abstract View getTagView(SmartVH holder);


    public void setOnItemSelectListener(OnItemSelectListener mOnItemSelectListener) {
        this.mOnItemSelectListener = mOnItemSelectListener;
    }

    public interface OnItemSelectListener<T> {
        void OnItemSelect(List<FileBean> dates);
    }
}
