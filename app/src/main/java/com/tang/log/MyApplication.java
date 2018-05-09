package com.tang.log;

import android.app.Application;

import org.xutils.x;

/**
 * Created by zhaoxuan.li on 2015/9/21.
 */
public class MyApplication extends Application {

    private static MyApplication application;

    public static Application getInstence() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
