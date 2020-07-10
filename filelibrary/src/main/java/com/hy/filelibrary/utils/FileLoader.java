package com.hy.filelibrary.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.hy.filelibrary.FilePicker;
import com.hy.filelibrary.base.FileBean;
import com.hy.filelibrary.base.FileFolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author:MtBaby
 * @date:2020/05/18 17:51
 * @desc:
 */
public class FileLoader {
    public static final int ALL_MEDIA_FOLDER = -1;//全部媒体
    private FileModel mFileModel;
    private FilePicker.Builder mBuilder;

    public FileLoader(FileModel fileModel, FilePicker.Builder builder) {
        this.mFileModel = fileModel;
        this.mBuilder = builder;
    }

    /**
     * 对查询到的图片和视频进行聚类（相册分类）
     *
     * @param fileBeans
     * @return
     */
    public List<FileFolder> getMediaFolder(List<FileBean> fileBeans) {
        //根据媒体所在文件夹Id进行聚类（相册）
        SparseArray<FileFolder> mediaFolderMap = new SparseArray<>();
        //全部图片、视频文件
        ArrayList<FileBean> mediaFileList = new ArrayList<>();
        if (fileBeans != null) mediaFileList.addAll(fileBeans);
        //对媒体数据进行排序
        Collections.sort(mediaFileList, (o1, o2) -> {
            if (o1.getModifyMilli() > o2.getModifyMilli()) {
                return -1;
            } else if (o1.getModifyMilli() < o2.getModifyMilli()) {
                return 1;
            } else {
                return 0;
            }
        });

        //全部图片或视频
        if (!mediaFileList.isEmpty()) {
            FileFolder allMediaFolder = new FileFolder(ALL_MEDIA_FOLDER, "全部", mediaFileList.get(0).getPath(), mediaFileList);
            allMediaFolder.setCheck(true);
            mediaFolderMap.put(ALL_MEDIA_FOLDER, allMediaFolder);
        }

        //对文件进行文件夹分类
        if (fileBeans != null && !fileBeans.isEmpty()) {
            //添加其他的图片文件夹
            for (int i = 0; i < fileBeans.size(); i++) {
                FileBean mediaFile = fileBeans.get(i);
                int imageFolderId = mediaFile.getFileDirID();
                FileFolder mediaFolder = mediaFolderMap.get(imageFolderId);
                if (mediaFolder == null) {
                    mediaFolder = new FileFolder(imageFolderId, mediaFile.getFileDirName(), mediaFile.getPath(), new ArrayList<>());
                }
                ArrayList<FileBean> imageList = mediaFolder.getFileBeans();
                imageList.add(mediaFile);
                mediaFolder.setFileBeans(imageList);
                mediaFolderMap.put(imageFolderId, mediaFolder);
            }
        }

        //整理聚类数据
        List<FileFolder> mediaFolderList = new ArrayList<>();
        for (int i = 0; i < mediaFolderMap.size(); i++) {
            int key = mediaFolderMap.keyAt(i);
            FileFolder fileFolder = mediaFolderMap.get(key);
            mediaFolderList.add(fileFolder);
        }

        //按照图片文件夹的数量排序
        Collections.sort(mediaFolderList, (o1, o2) -> {
            if (o1.getFileBeans().size() > o2.getFileBeans().size()) {
                return -1;
            } else if (o1.getFileBeans().size() < o2.getFileBeans().size()) {
                return 1;
            } else {
                return 0;
            }
        });
        return mediaFolderList;
    }

    public void getFileCursor(Context context) {
        Observable.create((Observable.OnSubscribe<Cursor>) subscriber -> {
            String selection = null;
            String[] selectionArgs = null;
            Uri uri = MediaStore.Files.getContentUri("external");
            switch (mBuilder.getFileMode()) {
                case IMAGE:
                    selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";
                    selectionArgs = new String[]{MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + ""};
                    break;
                case VIDEO:
                    selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";
                    selectionArgs = new String[]{MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO + ""};
                    break;
                default:
                    selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=? or " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";
                    selectionArgs = new String[]{MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + "", MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO + ""};
            }

            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, MediaStore.MediaColumns.DATE_ADDED + " desc");
            subscriber.onNext(cursor);
        }).map(cursor -> {
            List<FileBean> fileBeans = new ArrayList<>();
            // 扫描files文件库
            try {
                int fileColumnID = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);//文件数据库中ID
                int fileColumnPath = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);//文件磁盘路径
                int fileColumnSize = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);//文件大小
                int fileColumnTitle = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE);//txt文件类型的标题
                int fileColumnName = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);//文件名称
                int fileColumnTypeName = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);//文件类型名称
//                int fileColumnCreate = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);//文件创建时间
                int fileColumnModify = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED);//文件修改时间
                int fileColumnWidth = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH);//视屏、图片文件宽度
                int fileColumnHeight = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT);//视屏、图片文件高度
                int fileColumnDuration = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION);//视屏播放长度
                int fileColumnDirID = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_ID);//对于文件夹ID
                int fileColumnDirName = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);//对于文件夹名称
                int fileColumnAlbum = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.ALBUM);//对于文件夹名称
//                Cursor query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Video.Media.ALBUM + "=?", new String[]{SyncStateContract.Constants.DIRECTORY_VIDEO}, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(fileColumnPath);
                    String fileName = cursor.getString(fileColumnName);
                    String fileType = cursor.getString(fileColumnTypeName);
                    String fileDirName = cursor.getString(fileColumnDirName);
                    long fileDuration = cursor.getLong(fileColumnDuration);
                    long fileModify = cursor.getLong(fileColumnModify);
                    int fileID = cursor.getInt(fileColumnID);
                    int fileDirID = cursor.getInt(fileColumnDirID);
                    int fileSize = cursor.getInt(fileColumnSize);
                    //视频缩略图路径
                    String albumPath = "";
                    Cursor thumbCursor = null;
                    try {
//                        if (fileDuration > 0) {
//                            thumbCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, null, MediaStore.Video.Thumbnails.VIDEO_ID + "=" + fileID, null, null);
//                            if (thumbCursor.moveToFirst()) {
//                                albumPath = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
//                            }
//                        } else {
//                            thumbCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Thumbnails.IMAGE_ID + "=" + fileID, null, null);
//                            if (thumbCursor.moveToFirst()) {
//                                albumPath = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
//                            }
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (thumbCursor != null) thumbCursor.close();
                    }


                    FileBean info = new FileBean();
                    info.setFileName(fileName);
                    info.setMinType(albumPath);
                    info.setFileDirID(fileDirID);
                    info.setFileDirName(fileDirName);
                    info.setMinType(fileType);
                    info.setPath(filePath);
                    info.setFileSize(fileSize);
                    info.setFileID(fileID);
//                    System.out.println("媒体ID：" + fileID);
                    info.setModifyMilli(fileModify);
//                    info.setCreateMilli(fileCreate);
                    info.setDuration(fileDuration);
                    fileBeans.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return fileBeans;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FileBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<FileBean> fileBeans) {
//                        System.out.println("得到的数据:" + fileBeans.toString());
                        mFileModel.getFileBeanLiveData().postValue(fileBeans);
                    }
                });
    }
}
