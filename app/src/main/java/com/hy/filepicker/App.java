package com.hy.filepicker;

import android.app.Application;

/**
 * @author:MtBaby
 * @date:2020/06/16 9:46
 * @desc:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 异常处理，不需要处理时注释掉这两句即可！
//        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
//        crashHandler.init(getApplicationContext());
    }
}
