package com.mt.filelibrary.base;

import java.io.Serializable;

/**
 * @author:MtBaby
 * @date:2020/06/08 10:58
 * @desc:
 */
public class BaseBean implements Serializable {
    protected int fileID;
    protected int selectIndex;

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }
}
