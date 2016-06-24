package com.rocky.eagle.task;

import java.io.Serializable;
import java.util.Map;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/6/15  21:51
 * PACKAGE_NAME com.rocky.eagle.task
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 *
 * 用这个类的时候一定切记切记，
 * doTask 方法返回数据时一定是return this (回调)
 * or return null(不回调) ，不要返回其他的东西
 *
 * Done
 */
public abstract class ExcuteTask implements Runnable, Serializable {
    /**
     * 这个会自动生成，不用自己设置
     */
    protected int uniqueID;
    /**
     * 主要是用来初始化的时候传入参数，
     * 然后根据不用的参数来进行异步操作
     */
    @SuppressWarnings("rawtypes")
    protected Map taskParam;// 内容参数
    /**
     * 异步操作完成之后的状态，失败、成功 or 其他
     */
    protected int status;
    /**
     * 如果是网络请求，并且获取的数据是Json,
     * 则直接可以给此字段赋值，然后在回调中get Json数据
     */
    protected String json;
    /**
     * 这个是异步操作后，如果想把异步数据传到UI线程，
     * 则可以通过此字段赋值，然后再强转得到所要的数据
     */
    protected Object result;

    public ExcuteTask() {
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    @SuppressWarnings("rawtypes")
    public Map getTaskParam() {
        return taskParam;
    }

    @SuppressWarnings("rawtypes")
    public void setTaskParam(Map taskParam) {
        this.taskParam = taskParam;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public void run() {
        doTask();
    }

    /**
     * 专门用来执行耗时的操作，
     * 子类只需要继承此类，实现此方法，
     * 在这个方法中执行所有耗时的操作
     * 用ExcuteTaskManager进行执行，
     * 可以回调， 也可以不回调
     *
     * 在继承此类的时候 doTask
     * return null（不再回调） or  return this(会回调)
     *
     * @return
     */
    public abstract ExcuteTask doTask();
}
