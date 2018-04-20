package com.tang.log;

import android.app.Application;

/**
 * Created by zhaoxuan.li on 2015/9/21.
 */
public class MyApplication extends Application {

    private static MyApplication application ;

    public static Application getInstence(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
