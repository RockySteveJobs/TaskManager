package com.rocky.eagle.bean;

import com.rocky.eagle.task.ExecuteTask;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/6/15  22:38
 * PACKAGE_NAME com.rocky.eagle.bean
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class JsonExecuteTask extends ExecuteTask {

    @Override
    public ExecuteTask doTask() {

        if (getTaskParam() == null ||
                getTaskParam().get("Count") == null ||
                getTaskParam().get("Url") == null) {
            setStatus(-1);
            return this;
        }

        String url = getTaskParam().get("Url").toString();

        String count = getTaskParam().get("Count").toString();

        /**
         * 在这里添加真正的网络请求
         */
        setJson("{\"status\":0,\"count\":" + count + ",\"url\":\"" + url + "\"}");


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
