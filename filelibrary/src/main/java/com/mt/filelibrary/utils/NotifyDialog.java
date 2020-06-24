package com.mt.filelibrary.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class NotifyDialog {
    public static void showNotifyDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setMessage("是否放弃已选中的文件？")
                .setTitle("提示")
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("确定", onClickListener)
                .show();
    }

}
