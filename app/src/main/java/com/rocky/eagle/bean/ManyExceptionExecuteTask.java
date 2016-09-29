package com.rocky.eagle.bean;

import android.util.Log;

import com.rocky.eagle.task.ExecuteTask;
import com.rocky.eagle.task.ExecuteTaskManager;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/9/9  19:20
 * PACKAGE_NAME com.rocky.eagle.bean
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class ManyExceptionExecuteTask extends ExecuteTask implements ExecuteTaskManager.GetExcuteTaskCallback {

    private int size = 0;

    @Override
    public ExecuteTask doTask() {

        for (int i = 0; i < 1000; i++) {
            ExecuteTaskManager.getInstance().getData(new ExceptionTask(), new ExecuteTaskManager.GetExcuteTaskCallback() {
                @Override
                public void onDataLoaded(ExecuteTask task) {
                    ++size;
                    Log.i("ManyExceptionExcuteTask", "11111111111111 =" + size);
                }
            });
        }

        return this;
    }

    @Override
    public void onDataLoaded(ExecuteTask task) {

    }
}
