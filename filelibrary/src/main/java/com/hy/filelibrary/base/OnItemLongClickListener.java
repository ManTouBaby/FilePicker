package com.hy.filelibrary.base;

import android.view.View;

/**
 * @author:MtBaby
 * @date:2020/05/07 11:16
 * @desc:
 */
public interface OnItemLongClickListener<T> {
    void onItemLongClick(View view, T data, int position);
}
