package com.mt.filelibrary.base;

import android.view.View;

/**
 * @author:MtBaby
 * @date:2020/06/10 10:08
 * @desc:
 */
public interface OnItemChildViewClickListener<T> {
    void onItemChildViewClick(View view, T data, int position);
}
