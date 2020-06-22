package com.mt.filelibrary.utils;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mt.filelibrary.base.FileBean;

import java.util.List;

/**
 * @author:MtBaby
 * @date:2020/06/03 15:04
 * @desc:
 */
public class FileModel extends ViewModel {
    private MutableLiveData<List<FileBean>> fileBeanLiveData = new MutableLiveData<>();

    public MutableLiveData<List<FileBean>> getFileBeanLiveData() {
        if (fileBeanLiveData == null) {
            fileBeanLiveData = new MutableLiveData<>();
        }
        return fileBeanLiveData;
    }
}
