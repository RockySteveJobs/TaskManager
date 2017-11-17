## 使用线程池(Executor)实现异步任务的添加、执行、回调，完全替代Thread、AsyncTask ，最大程度的解放你的双手!
	
github地址 : [https://github.com/RockySteveJobs](https://github.com/RockySteveJobs)
	
### How to use?

#### 1、first step:

	copy the file "ExcuteTaskManager" and "ExcuteTask"  to your project 

#### 2、second step:

	init the library in your application or activity

	ExcuteTaskManager.getInstance().init();

#### 3、third step:

	write a file extends "ExcuteTask" (eg: JsonExcuteTask)

#### 4、fourth step:

	ExcuteTaskManager.getInstance().newExcuteTask(new JsonExcuteTask());

	but if you want callback , please call the method getData()

	ExcuteTaskManager.getInstance().getData(new JsonExcuteTask(),
	new ExcuteTaskManager.GetExcuteTaskCallback()
	{
		public void onDataLoaded(ExcuteTask task)
		{
			//data has int the task, so you can update your ui;
		}
	});


### welcome to fork 、follow and star , thank you very much !  ^_^

## 核心代码(Core code)

	package com.rocky.eagle.task;

	import android.os.Handler;
	import android.os.Looper;
	import android.os.Message;
	
	
	import com.rocky.eagle.utils.LogUtils;
	
	import java.lang.ref.SoftReference;
	import java.util.concurrent.ConcurrentHashMap;
	import java.util.concurrent.ConcurrentLinkedQueue;
	import java.util.concurrent.ExecutorService;
	import java.util.concurrent.Executors;
	import java.util.concurrent.ScheduledExecutorService;
	import java.util.concurrent.TimeUnit;
	
	/**
	 * Start
	 * <p/>
	 * User:Rocky(email:1247106107@qq.com)
	 * Created by Rocky on 2016/6/15  21:51
	 * PACKAGE_NAME com.rocky.eagle.activity
	 * PROJECT_NAME TaskManager
	 * TODO:
	 * Description:
	 *
	 * 使用的时候务必请先调用init()方法，否则不能使用
	 *
	 * 用最少的代码做尽可能多的事情
	 *
	 * Done
	 */
	public class ExcuteTaskManager implements Runnable {
	
	    /**
	     * 线程执行完事儿后默认的回调类型
	     */
	    private static final int COMMON_EXCUTE_TASK_TYPE = 0;
	    /**
	     * 线程开关
	     */
	    public volatile boolean isRunning = false;
	    /**
	     * 是否初始化完成的开关
	     */
	    private boolean isHasInit = false;
	    /**
	     * 默认线程池的线程数量
	     */
	    private static final int DEFAULT_THREAD_NUM = 5;
	    /**
	     * 初始化时的线程数量
	     */
	    private int threadNum = DEFAULT_THREAD_NUM;
	    /**
	     * 定义一个单线程的线程池，专门用来执行耗时且不需要回调的操作
	     */
	    private static ScheduledExecutorService singlePool = null/*Executors.newSingleThreadScheduledExecutor()*/;
	    /**
	     * 定义一个大小为5的线程池(这个我们比较适合多个图片下载时使用)
	     */
	    private static ExecutorService threadPool = null/*Executors.newFixedThreadPool(threadNum)*/;
	    /**
	     * 任务执行队列
	     */
	    private static ConcurrentLinkedQueue<ExcuteTask> allExcuteTask = null/*new ConcurrentLinkedQueue<ExcuteTask>()*/;
	    /**
	     * 回调接口列表
	     */
	    private static ConcurrentHashMap<Integer, Object> uniqueListenerList = null/*new ConcurrentHashMap<String, Object>()*/;
	
	
	    public Handler getHandler() {
	        return handler;
	    }
	
	    public int getThreadNum() {
	        return threadNum;
	    }
	
	    public boolean isHasInit() {
	        return isHasInit;
	    }
	
	    public boolean isRunning() {
	        return isRunning;
	    }
	
	
	    /**
	     * @author Rocky
	     * @desc 得到普通的 ExcuteTask 对象，
	     * 对外界开放的回调接口
	     */
	    public interface GetExcuteTaskCallback {
	        void onDataLoaded(ExcuteTask task);
	    }
	
	
	    /**
	     * 直接把数据发送到主线程
	     */
	    private static Handler handler = new Handler(Looper.getMainLooper()) {
	        @Override
	        public void handleMessage(Message msg) {
	            long start = System.currentTimeMillis();
	            if (msg != null && msg.obj != null
	                    && msg.obj instanceof ExcuteTask) {
	                switch (msg.what) {
	                    case COMMON_EXCUTE_TASK_TYPE:
	                        ExcuteTaskManager.getInstance().doCommonHandler((ExcuteTask) msg.obj);
	                        break;
	                    /** 如果想要添加其他类型的回调，可以在此加入代码*/
	                }
	            }
	            long end = System.currentTimeMillis();
	
	            LogUtils.i(" handleMessage 总共消耗时间为：" + (end - start));
	        }
	    };
	
	
	    private static ExcuteTaskManager instance = null;
	
	    private ExcuteTaskManager() {
	        LogUtils.i("private ExcuteTaskManager() { 当前的线程Id为：" + Thread.currentThread().getId());
	    }
	
	    public static ExcuteTaskManager getInstance() {
	        if (instance == null) {
	            synchronized (ExcuteTaskManager.class) {
	                if (instance == null) {
	                    instance = new ExcuteTaskManager();
	                }
	            }
	        }
	        return instance;
	    }
	
	    /**
	     * 初始化操作，这个主要是初始化需要执行异步
	     * 回调任务的线程池，默认开启5个线程
	     */
	    public void init() {
	        init(threadNum);
	    }
	
	    /**
	     * 初始化操作，这个主要是初始化需要执行异步
	     * 回调任务的线程池，可以传入线程的个数
	     */
	    public synchronized void init(int initNum) {
	        if (!isHasInit) {
	            /**
	             * 初始化之后就相当于开始了线程次的运行
	             * 只不过如果没有任务处于等待状态
	             */
	            isRunning = true;
	            if (initNum > 0) {
	                threadNum = initNum;
	            }
	            threadPool = Executors.newFixedThreadPool(threadNum);
	            singlePool = Executors.newSingleThreadScheduledExecutor();
	            allExcuteTask = new ConcurrentLinkedQueue<>();
	            uniqueListenerList = new ConcurrentHashMap<>();
	
	            /**
	             * 初始化需要用到的线程
	             */
	            for (int i = 0; i < threadNum; i++) {
	                threadPool.execute(this);
	            }
	            isHasInit = true;
	        } else {
	            LogUtils.d("ExcuteTaskManager 已经初始化完成,不需要重复初始化");
	        }
	    }
	
	
	    /**
	     * 当应用被销毁时，执行清理操作
	     */
	    public void doDestory() {
	        /**
	         * 关闭线程开关
	         */
	        isRunning = false;
	        isHasInit = false;
	        if (allExcuteTask != null) {
	            allExcuteTask.clear();
	            allExcuteTask = null;
	        }
	        if (uniqueListenerList != null) {
	            uniqueListenerList.clear();
	            uniqueListenerList = null;
	        }
	        if (threadPool != null) {
	            threadPool.shutdown();
	            threadPool = null;
	        }
	        if (singlePool != null) {
	            singlePool.shutdown();
	            singlePool = null;
	        }
	    }
	
	    /**
	     * 向任务队列中添加任务对象,添加成功后,
	     * 任务会自动执行,执行完事儿后,不进行任何回调操作
	     *
	     * @param task 可执行的任务对象
	     */
	    public void newExcuteTask(ExcuteTask task) {
	        if (task != null) {
	            allExcuteTask.offer(task);
	            LogUtils.i("ExcuteTaskManager 添加任务成功之后" + "allExcuteTask.size()=" + allExcuteTask.size());
	            long timeOne = System.currentTimeMillis();
	            synchronized (allExcuteTask) {
	                allExcuteTask.notifyAll();
	                LogUtils.i("ExcuteTaskManager =====>处于唤醒状态");
	            }
	            long timeTwo = System.currentTimeMillis();
	            LogUtils.i("ExcuteTaskManager唤醒线程所消耗的时间为：" + (timeTwo - timeOne));
	        } else {
	            LogUtils.w("ExcuteTaskManager====您添加的ExcuteTask为空，请重新添加");
	        }
	    }
	
	    /**
	     * 这个方法主要是获取普通的回调数据,
	     * 获取成功后会把加入的 ExcuteTask 对象回调到用户界面
	     *
	     * @param task     加入的任务Task
	     * @param callback 任务的回调接口GetDataCallback
	     */
	    public void getData(ExcuteTask task, GetExcuteTaskCallback callback) {
	        /**
	         *  把CallBack 接口加入列表中,用完之后移除
	         */
	        try {
	            if (task != null && callback != null) {
	                LogUtils.i("callback的hashcode为：" + callback.hashCode() + "task的hashcode为：" + task.hashCode());
	                if (task.getUniqueID() == 0) {
	                    task.setUniqueID(task.hashCode());
	                }
	                uniqueListenerList.put(task.getUniqueID(), callback);
	
	                /**
	                 * 开始加入任务,执行任务
	                 */
	                newExcuteTask(task);
	            } else {
	                LogUtils.w("Task 或者是 GetDataCallback 为空了,请检查你添加的参数!");
	            }
	        } catch (Exception e) {
	            // TODO: handle exception
	            /**
	             * 其实，这个地方的数据应该写到一个文件中
	             */
	            LogUtils.e("ExcuteTaskManager========getData=====" + e.toString() + " thread id 为：" + Thread.currentThread().getId());
	            e.printStackTrace();
	        }
	    }
	
	    /**
	     * 从任务队列中移除任务对象,使其不再执行(如果任务已经执行,则此方法无效)
	     *
	     * @param task 添加的任务对象
	     */
	    public void removeExcuteTask(ExcuteTask task) {
	        if (task != null) {
	            uniqueListenerList.remove(task.getUniqueID());
	            allExcuteTask.remove(task);
	        } else {
	            LogUtils.w("ExcuteTaskManager====您所要移除的任务为null,移除失败");
	        }
	    }
	
	
	    /**
	     * 清除所有的任务
	     */
	    public void removeAllExcuteTask() {
	        allExcuteTask.clear();
	        uniqueListenerList.clear();
	    }
	
	
	    /**=================================任务执行、回调、分发start============================================*/
	
	    /**
	     * 所有的异步任务都在此执行
	     */
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub
	        while (isRunning) {
	
	            LogUtils.i("ExcuteTaskManager====准备开始执行任务 总任务个数为  allExcuteTask.size()=" + allExcuteTask.size());
	
	            /**
	             * 从allExcuteTask取任务
	             */
	            ExcuteTask lastExcuteTask = allExcuteTask.poll();
	
	            LogUtils.i("ExcuteTaskManager====从allExcuteTask取出了一个任务  allExcuteTask.size()=" + allExcuteTask.size());
	            if (lastExcuteTask != null) {
	                try {
	                    LogUtils.i("ExcuteTaskManager任务ID" + lastExcuteTask.getUniqueID());
	                    /**
	                     * 真正开始执行任务，
	                     * 所有的耗时任务都是在子线程中执行
	                     */
	                    doExcuteTask(lastExcuteTask);
	                } catch (Exception e) {
	                    // TODO: handle exception
	                    LogUtils.e("ExcuteTaskManager=====>执行任务发生了异常，信息为：" + e.getMessage());
	                    e.printStackTrace();
	                }
	                LogUtils.i("任务仍在执行,ExcuteTaskManager线程处于运行状态,当前的线程的ID为：" + Thread.currentThread().getId());
	            } else {
	                LogUtils.i("任务执行完毕,ExcuteTaskManager线程处于等待状态,当前的线程的ID为：" + Thread.currentThread().getId());
	                try {
	                    synchronized (allExcuteTask) {
	                        allExcuteTask.wait();
	                    }
	                } catch (InterruptedException e) {
	                    // TODO Auto-generated catch block
	                    LogUtils.e("ExcuteTaskManager=====>  线程等待时发生了错误，信息为：" + e.getMessage());
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	
	
	    /**
	     * 根据不同的ExcuteTask,执行相应的任务
	     *
	     * 这个是真正开始执行异步任务的地方，
	     * 即调用需要在子线程执行的代码==>task.doTask()
	     *
	     * @param task ExcuteTask对象
	     */
	    private void doExcuteTask(ExcuteTask task) {
	
	        ExcuteTask result = task.doTask();
	
	        /**
	         *
	         * 开始执行的Task和最后得到的Task是同一个的时候，才会进行回调，
	         * 否则不进行回调(保证在回调得到数据的时候知道是哪一个Task,以便进行强转)
	         *
	         *
	         * 没有UniqueID相当于不需要回调
	         *
	         */
	        if (result != null && task == result && result.getUniqueID() != 0) {
	            /**
	             *  发送当前消息,更新UI(把数据回调到界面),
	             *  下面不用做任何的发送消息，
	             *  只在这一个地方发送就行，否者会发生错误！
	             */
	
	            Message msg = Message.obtain();
	            msg.what = COMMON_EXCUTE_TASK_TYPE;
	            msg.obj = result;
	            handler.sendMessage(msg);
	        } else {
	            uniqueListenerList.remove(task.getUniqueID());
	        }
	    }
	
	    /**
	     * 真正的回调操作，所有的任务在这里
	     * 把数据回调到主界面
	     *
	     * @param task ExcuteTask对象
	     */
	    private void doCommonHandler(ExcuteTask task) {
	        long start = System.currentTimeMillis();
	        LogUtils.i("已经进入了private void doCommonHandler(Message msg) {");
	        if (task != null && uniqueListenerList.containsKey(task.getUniqueID())) {
	            try {
	                /**
	                 * 回调整个Task数据
	                 * 然后可以回调方法中去直接更新UI
	                 */
	                ((GetExcuteTaskCallback) uniqueListenerList.get(task.getUniqueID())).onDataLoaded(task);
	                /**
	                 * 回调完成移除CallBack对象
	                 */
	                uniqueListenerList.remove(task.getUniqueID());
	                LogUtils.i("TaskManager========doCommonHandler=====回调成功====task 为：" + task.toString());
	            } catch (Exception e) {
	                // TODO: handle exception
	                LogUtils.e("TaskManager========doCommonHandler=====if (uniqueListenerList.containsKey(task.getUniqueID())) {" + e.toString());
	                e.printStackTrace();
	            }
	        } else {
	            LogUtils.i("TaskManager========doCommonHandler=====已经移除了回调监听");
	        }
	        long end = System.currentTimeMillis();
	        LogUtils.i("执行回调doCommonHandler 耗时：" + (end - start));
	    }
	    /**=================================任务执行、回调、分发end============================================*/
	
	
	    /**=================================单线程池,可以顺序，延迟执行一些任务start============================================*/
	
	    /**
	     * 顺序执行耗时的操作
	     *
	     * @param runnable 对象
	     */
	    public void excute(Runnable runnable) {
	        singlePool.execute(runnable);
	    }
	
	    /**
	     * 顺序执行耗时的操作
	     *
	     * @param runnable 对象
	     * @param delay    延迟执行的时间，单位毫秒
	     */
	    public void excute(Runnable runnable, long delay) {
	        singlePool.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	    }
	
	    /**
	     * 顺序执行耗时的操作
	     *
	     * @param runnable 对象
	     * @param delay    延迟执行的时间
	     * @param timeUnit 时间单位
	     */
	    public void excute(Runnable runnable, long delay, TimeUnit timeUnit) {
	        singlePool.schedule(runnable, delay, timeUnit);
	    }
	
	    public void scheduleAtFixedRate(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
	        singlePool.scheduleAtFixedRate(runnable, delay, period, timeUnit);
	    }
	
	    public void scheduleAtFixedRate(Runnable runnable, long delay, long period) {
	        singlePool.scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
	    }
	
	    public void scheduleAtFixedRate(Runnable runnable, long period) {
	        singlePool.scheduleAtFixedRate(runnable, 0, period, TimeUnit.MILLISECONDS);
	    }
	    /**=================================单线程池,可以顺序，延迟执行一些任务end============================================*/
	}
