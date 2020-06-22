package com.mt.filelibrary.base;

import android.view.View;

/**
 * @author:MtBaby
 * @date:2020/05/07 11:15
 * @desc:
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view, T data,int position);
}
