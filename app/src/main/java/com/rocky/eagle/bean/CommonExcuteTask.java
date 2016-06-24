package com.rocky.eagle.bean;

import com.rocky.eagle.task.ExcuteTask;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/6/15  22:35
 * PACKAGE_NAME com.rocky.eagle.bean
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class CommonExcuteTask extends ExcuteTask {

    @Override
    public ExcuteTask doTask() {

        /**
         * 做一些耗时操作
         */

        try {
            /**
             * 模拟耗时
             */
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return this;
    }
}
