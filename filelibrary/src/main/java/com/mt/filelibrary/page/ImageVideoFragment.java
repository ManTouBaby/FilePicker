package com.mt.filelibrary.page;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mt.filelibrary.FilePicker;
import com.mt.filelibrary.R;
import com.mt.filelibrary.base.BaseAdapter;
import com.mt.filelibrary.base.BaseSelectAdapter;
import com.mt.filelibrary.base.FileBean;
import com.mt.filelibrary.base.FileFolder;
import com.mt.filelibrary.base.FileSelect;
import com.mt.filelibrary.base.OnCameraListener;
import com.mt.filelibrary.base.OnItemChildViewClickListener;
import com.mt.filelibrary.base.OnItemClickListener;
import com.mt.filelibrary.base.SmartVH;
import com.mt.filelibrary.camera.CameraHelper;
import com.mt.filelibrary.camera.CameraOpenType;
import com.mt.filelibrary.utils.DateUtil;
import com.mt.filelibrary.utils.FileLoader;
import com.mt.filelibrary.utils.FileModel;
import com.mt.filelibrary.utils.GlideUtil;
import com.mt.filelibrary.widget.SquareImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/05/18 17:03
 * @desc:
 */
public class ImageVideoFragment extends BaseFragment implements OnCameraListener {
    TextView mSelectName;
    private BaseSelectAdapter mBaseAdapter;
    private List<FileFolder> mMediaFolder;
    private PopupWindow popupWindow;
    private BaseAdapter mFileFolderAdapter;
    RelativeLayout relativeLayout;
    private ArrayList<FileBean> mFileBeans;

    @Override
    protected int getLayout() {
        return R.layout.f_image_video_show;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void init() {
        FilePicker.Builder mBuilder = FilePicker.getBuilder();
        relativeLayout = findViewById(R.id.mi_show_photo_video_container);

        mSelectName = findViewById(R.id.select_name);
        mSelectName.setOnClickListener(v -> showFolders());
        RecyclerView mRecyclerView = findViewById(R.id.mt_image_video_show);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerView.setAdapter(mBaseAdapter = new BaseSelectAdapter(BaseSelectAdapter.SELECT_MODE_MULTI_BY_INDEX) {

            @Override
            protected void onSelectBindView(SmartVH holder, View tagView, FileBean data, int position) {
                LinearLayout videoContainer = holder.getViewById(R.id.video_container);
                SquareImageView squareImageView = holder.getViewById(R.id.iv_show);
                TextView selectTag = holder.getText(R.id.icon_select_tag);
                ImageView selectBGTag = holder.getViewById(R.id.iv_show_bg);
                if (FileSelect.getInstance().container(data)) {
                    selectTag.setText(FileSelect.getInstance().getFileBean(data).getSelectIndex() + "");
                    selectBGTag.setVisibility(View.VISIBLE);
                } else {
                    selectTag.setText("");
                    selectBGTag.setVisibility(View.GONE);
                }
                holder.getText(R.id.mt_time).setText(DateUtil.getStrByLong(data.getDuration()));
                if (data.getDuration() > 0) {//视屏类型
                    videoContainer.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(data.getMimePath())) {
                        GlideUtil.loadImageWithCache(squareImageView, data.getPath());
                    } else {
                        GlideUtil.loadImageWithCache(squareImageView, data.getMimePath());
                    }

                } else {
                    videoContainer.setVisibility(View.GONE);
                    GlideUtil.loadImageWithCache(squareImageView, data.getPath());
                }
                addChildViewClick(squareImageView, data, position);
            }

            @Override
            protected View getTagView(SmartVH holder) {
                return holder.getViewById(R.id.icon_select_container);
            }
        });

        FileModel fileModel = ViewModelProviders.of(this).get(FileModel.class);
        FileLoader fileLoader = new FileLoader(fileModel, mBuilder);
        fileLoader.getFileCursor(getContext());
        fileModel.getFileBeanLiveData().observe(this, fileBeans -> {
            this.mFileBeans = (ArrayList<FileBean>) fileBeans;
            mBaseAdapter.setDates(mFileBeans);
            mMediaFolder = fileLoader.getMediaFolder(mFileBeans);
        });

        mBaseAdapter.setOnItemChildViewClickListener((OnItemChildViewClickListener<FileBean>) (view, data, position) -> {
            if (BaseSelectAdapter.TAKE_CAMERA.equals(data.getFileName())) {
                switch (mBuilder.getFileMode()) {
                    case VIDEO:
                        new CameraHelper.Builder()
                                .setCameraOpenType(CameraOpenType.TAKE_VIDEO)
                                .setDuration(mBuilder.getVideoDuration())
                                .setSavePath(mBuilder.getSavePath())
                                .build()
                                .setOnCameraListener(this)
                                .openCamera(getContext());
                        break;
                    case IMAGE:
                        new CameraHelper.Builder()
                                .setCameraOpenType(CameraOpenType.TAKE_PHOTO)
                                .setSavePath(mBuilder.getSavePath())
                                .build()
                                .setOnCameraListener(this)
                                .openCamera(getContext());
                        break;
                    case IMAGE_VIDEO:
                        new CameraHelper.Builder()
                                .setCameraOpenType(CameraOpenType.TAKE_PHOTO_IMAGE)
                                .setSavePath(mBuilder.getSavePath())
                                .setDuration(mBuilder.getVideoDuration())
                                .build()
                                .setOnCameraListener(this)
                                .openCamera(getContext());
                        break;
                }
            } else {
                Intent intent = new Intent(getContext(), ACPreMediaShow.class);
                intent.putExtra("FileList", mFileBeans);
                intent.putExtra("FileClick", data);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, "ABC");
                ActivityCompat.startActivityForResult(getActivity(), intent, 0x11, options.toBundle());
            }
        });
        mBaseAdapter.setOnItemSelectListener((BaseSelectAdapter.OnItemSelectListener<FileBean>) dates -> FileSelect.getInstance().setSelectFiles(dates));
    }

    @Override
    public void onResume() {
        super.onResume();
        mBaseAdapter.notifyDataSetChanged();
    }

    //打开文件夹列表
    private void showFolders() {
        View view;
        if (popupWindow == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_item_dir_list, null);
            RecyclerView fileFolderList = view.findViewById(R.id.mt_file_folder);
            fileFolderList.setLayoutManager(new LinearLayoutManager(getContext()));
            mMediaFolder.get(0).setCheck(true);
            fileFolderList.setAdapter(mFileFolderAdapter = new BaseAdapter<FileFolder>(mMediaFolder, R.layout.item_recyclerview_folder) {
                int currentClick = 0;

                @Override
                protected void onBindView(SmartVH holder, FileFolder data, int position) {
                    String folderCover = data.getFolderCover();
                    String folderName = data.getFolderName();
                    int imageSize = data.getFileBeans().size();
                    holder.getText(R.id.tv_item_folderName).setText(TextUtils.isEmpty(folderName) ? "" : folderName);
                    ImageView mImageCover = holder.getImage(R.id.iv_item_imageCover);
                    holder.getText(R.id.tv_item_imageSize).setText(String.format(getContext().getString(R.string.image_num), imageSize));
                    View showTag = holder.getViewById(R.id.iv_item_check);
                    showTag.setVisibility(data.isCheck() ? View.VISIBLE : View.GONE);
                    //加载图片
//                    Glide.with(holder.itemView).load(folderCover).into(mImageCover);
                    GlideUtil.loadImageWithCache(mImageCover, folderCover);
                    holder.itemView.setOnClickListener(v -> {
                        if (currentClick != position) {
                            mDates.get(currentClick).setCheck(false);
                            notifyItemChanged(currentClick);
                            currentClick = position;
                            data.setCheck(!data.isCheck());
                            notifyItemChanged(position);

                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(holder.itemView, data, position);
                            }
                        }
                    });
                }
            });

            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            // 设置弹窗外可点击
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setAnimationStyle(R.style.popup_anim_style);
            mFileFolderAdapter.setOnItemClickListener((OnItemClickListener<FileFolder>) (view1, data, position) -> {
                mSelectName.setText(data.getFolderName());
                mBaseAdapter.setDates(data.getFileBeans());
                popupWindow.dismiss();
            });
        }

        view = popupWindow.getContentView();
        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = view.getMeasuredHeight();
        int popupWidth = view.getMeasuredWidth();
        int height = (int) (368 * getResources().getDisplayMetrics().density);
        if (popupHeight > height) {
            popupWindow.setHeight(height);
            popupHeight = height;
        }

        bgAlpha(0.4f);
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        mSelectName.getLocationOnScreen(location);
        mFileFolderAdapter.notifyDataSetChanged();
        //在控件上方显示    向上移动y轴是负数
        popupWindow.showAtLocation(mSelectName, Gravity.NO_GRAVITY, (location[0] + mSelectName.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
        popupWindow.setOnDismissListener(() -> bgAlpha(1.0f));
    }

    /**
     * 设置窗口的背景透明度
     *
     * @param f 0.0-1.0
     */
    private void bgAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = f;
        getActivity().getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onCamera(FileBean fileBean) {
        mFileBeans.add(1, fileBean);
        FileSelect.getInstance().addFileBean(fileBean);
    }
}
