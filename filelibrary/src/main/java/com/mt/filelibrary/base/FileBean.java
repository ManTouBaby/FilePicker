package com.mt.filelibrary.base;

/**
 * @author:MtBaby
 * @date:2020/05/18 11:58
 * @desc:
 */
public class FileBean extends BaseBean {
    private String minType;//文件类型
    private String path;//文件存储路径
    private String mimePath;//文件缩略图
    private String fileName;//文件名
    private long fileSize;//文件大小
    private long modifyMilli;//文件修改时间

    private long duration;//文件时长

    private int fileDirID;//文件对应文件夹ID
    private String fileDirName;//文件对应文件夹名称

    public String getMimePath() {
        return mimePath;
    }

    public void setMimePath(String mimePath) {
        this.mimePath = mimePath;
    }

    public long getModifyMilli() {
        return modifyMilli;
    }

    public void setModifyMilli(long modifyMilli) {
        this.modifyMilli = modifyMilli;
    }

    public String getFileDirName() {
        return fileDirName;
    }

    public void setFileDirName(String fileDirName) {
        this.fileDirName = fileDirName;
    }

    public int getFileDirID() {
        return fileDirID;
    }

    public void setFileDirID(int fileDirID) {
        this.fileDirID = fileDirID;
    }

    public String getMinType() {
        return minType;
    }

    public void setMinType(String minType) {
        this.minType = minType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    public String getMime() {
//        return mime;
//    }
//
//    public void setMime(String mime) {
//        this.mime = mime;
//    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
