package com.sheng.one_sheng.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/15.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
