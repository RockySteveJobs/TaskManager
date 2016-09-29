package com.rocky.eagle.activity;

import android.app.Application;

import com.rocky.eagle.task.ExecuteTaskManager;
import com.rocky.eagle.utils.LogUtils;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/6/15  21:38
 * PACKAGE_NAME com.rocky.eagle.activity
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class MainApplication extends Application {
    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        long start = System.currentTimeMillis();
        /**
         *首先初始化执行线程，默认初始化5个
         */
        ExecuteTaskManager.getInstance().init();
        /*ExcuteTaskManager.getInstance().init(10);*/

        LogUtils.i("初始化消耗的时间为：" + (System.currentTimeMillis() - start));
    }
}
