package com.rocky.eagle.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.rocky.eagle.BuildConfig;


/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/6/15  21:42
 * PACKAGE_NAME com.rocky.eagle.utils
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class LogUtils {
    public static final boolean LOG_SWITCH = BuildConfig.DEBUG;

    public static final String LOG_TAG = "TASK_MANAGER";

    public static void i(String msg) {
        i(LOG_TAG, msg);
    }

    public static void e(String msg) {
        e(LOG_TAG, msg);
    }

    public static void d(String msg) {
        Log.d(LOG_TAG, msg);
    }

    public static void w(String msg) {
        Log.w(LOG_TAG, msg);
    }

    public static void v(String msg) {
        Log.v(LOG_TAG, msg);
    }


    public static void i(String tag, String msg) {
        if (LOG_SWITCH)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (LOG_SWITCH)
            Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (LOG_SWITCH)
            Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_SWITCH)
            Log.w(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (LOG_SWITCH)
            Log.v(tag, msg);
    }

    public static void toast(Context context, String msg) {
        if (LOG_SWITCH) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
