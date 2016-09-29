package com.rocky.eagle.bean;

import com.rocky.eagle.task.ExecuteTask;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/9/9  19:19
 * PACKAGE_NAME com.rocky.eagle.bean
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class ExceptionTask extends ExecuteTask {

    @Override
    public ExecuteTask doTask() {

        Integer.parseInt("fdsafasdf");

        return this;
    }
}
