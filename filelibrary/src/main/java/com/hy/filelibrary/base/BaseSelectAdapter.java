package com.hy.filelibrary.base;

import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hy.filelibrary.FilePicker;
import com.hy.filelibrary.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
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
    public final static int TYPE_CAMERA = 0x100;
    public final static int TYPE_SHOW = 0x200;
    public final static String TAKE_CAMERA = "TAKE_CAMERA";

    //    protected Map<Integer, FileBean> selectMap = new LinkedHashMap<>();
    private int mSelectMode;


    public BaseSelectAdapter(@SelectMode int selectMode) {
        this(new ArrayList<>(), selectMode);
    }

    public BaseSelectAdapter(List<FileBean> mDates) {
        this(mDates, SELECT_MODE_SINGLE);
    }

    public BaseSelectAdapter(List<FileBean> mDates, @SelectMode int selectMode) {
        super(mDates, R.layout.item_type_image);
        this.mSelectMode = selectMode;
        mFileSelect = FileSelect.getInstance();
        mBuilder = FilePicker.getBuilder();
        if (mBuilder.isShowCamera()) addFileBean(mDates);
    }

    private void addFileBean(List<FileBean> mDates) {
        FileBean fileBean = new FileBean();
        fileBean.setFileName(TAKE_CAMERA);
        mDates.add(0, fileBean);
    }

    @Override
    public void setDates(List<FileBean> mDates) {
        if (mBuilder.isShowCamera()) addFileBean(mDates);
        super.setDates(mDates);
    }

    @Override
    public SmartVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_CAMERA) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_camera, null);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_image, null);
        }
        return new SmartVH(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (TAKE_CAMERA.equals(mDates.get(position).getFileName())) {
            return TYPE_CAMERA;
        } else {
            return TYPE_SHOW;
        }
    }

    @Override
    protected void onBindView(SmartVH holder, FileBean data, int position) {
        if (TAKE_CAMERA.equals(data.getFileName())) {
            addChildViewClick(holder.getImage(R.id.iv_show_bg), data, position);
        } else {
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
