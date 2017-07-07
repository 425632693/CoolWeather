package com.example.administrator.coolweather.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/6/22.
 */

/**
 * 自定义的全局Context
 */
public class MyAppliction extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        //由于项目中只能配置一个Appliction,将全局的Context对象通过参数传递给了LitePal
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
