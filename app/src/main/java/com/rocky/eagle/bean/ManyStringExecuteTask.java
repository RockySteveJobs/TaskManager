package com.rocky.eagle.bean;

import android.util.Log;

import com.rocky.eagle.task.ExecuteTask;
import com.rocky.eagle.task.ExecuteTaskManager;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/9/9  19:24
 * PACKAGE_NAME com.rocky.eagle.bean
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class ManyStringExecuteTask extends ExecuteTask implements ExecuteTaskManager.GetExcuteTaskCallback {

    private volatile int size = 0;

    @Override
    public ExecuteTask doTask() {
        for (int i = 0; i < 1000; i++) {
            ExecuteTaskManager.getInstance().getData(new StringExecuteTask(), new ExecuteTaskManager.GetExcuteTaskCallback() {
                @Override
                public void onDataLoaded(ExecuteTask task) {
                    ++size;
                    Log.i("ManyStringExcuteTask", "222222222222222  =" + size);
                }
            });
        }
        return this;
    }

    @Override
    public void onDataLoaded(ExecuteTask task) {

    }
}
