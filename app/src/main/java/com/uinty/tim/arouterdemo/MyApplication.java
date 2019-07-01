package com.uinty.tim.arouterdemo;

import android.app.Application;

import com.uinty.tim.arouter.ARouter;

/**
 * author : Administrator
 * time   : 2019/06/28
 * desc   :
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.getInstance().init(this);
    }
}
