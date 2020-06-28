package com.mt.filelibrary.base;


import com.mt.filelibrary.FileMode;

import java.util.List;

public interface OnSelectFinishListener {
    void onSelectFinish(FileMode fileMode, List<FileBean> fileBeans);
}
