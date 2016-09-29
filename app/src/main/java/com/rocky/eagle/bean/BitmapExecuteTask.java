package com.rocky.eagle.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rocky.eagle.R;
import com.rocky.eagle.activity.MainApplication;
import com.rocky.eagle.task.ExecuteTask;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/6/15  22:32
 * PACKAGE_NAME com.rocky.eagle.bean
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class BitmapExecuteTask extends ExecuteTask {

    public Bitmap bitmap;

    @Override
    public ExecuteTask doTask() {
        /**
         * 执行 Decode 或者压缩等耗时操作
         */
        bitmap = BitmapFactory.decodeResource(MainApplication.getInstance().getResources(), R.mipmap.common_test);
        try {
            /**
             * 模拟耗时
             */
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
