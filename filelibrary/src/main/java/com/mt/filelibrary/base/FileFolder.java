package com.mt.filelibrary.base;

import java.util.ArrayList;

/**
 * 图片文件夹实体类
 * Create by: chenWei.li
 * Date: 2018/8/23
 * Time: 上午12:56
 * Email: lichenwei.me@foxmail.com
 */
public class FileFolder extends BaseBean {
    //    private int folderId;
    private String folderName;
    private String folderCover;
    private ArrayList<FileBean> fileBeans;


    private boolean isCheck;

    public FileFolder(int folderId, String folderName, String folderCover, ArrayList<FileBean> fileBeans) {
        this.fileID = folderId;
        this.folderName = folderName;
        this.folderCover = folderCover;
        this.fileBeans = fileBeans;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderCover() {
        return folderCover;
    }

    public void setFolderCover(String folderCover) {
        this.folderCover = folderCover;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public ArrayList<FileBean> getFileBeans() {
        return fileBeans;
    }

    public void setFileBeans(ArrayList<FileBean> fileBeans) {
        this.fileBeans = fileBeans;
    }

}
