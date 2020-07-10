package com.hy.filelibrary.base;


import com.hy.filelibrary.FileMode;

import java.util.List;

public interface OnSelectFinishListener {
    void onSelectFinish(FileMode fileMode, List<FileBean> fileBeans);
}
