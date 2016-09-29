package com.rocky.eagle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocky.eagle.R;
import com.rocky.eagle.bean.BitmapExecuteTask;
import com.rocky.eagle.bean.CommonExecuteTask;
import com.rocky.eagle.bean.JsonExecuteTask;
import com.rocky.eagle.bean.ManyExceptionExecuteTask;
import com.rocky.eagle.bean.ManyStringExecuteTask;
import com.rocky.eagle.task.ExecuteTask;
import com.rocky.eagle.task.ExecuteTaskManager;
import com.rocky.eagle.utils.LogUtils;


import java.util.HashMap;
import java.util.Map;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2016/6/15  21:36
 * PACKAGE_NAME com.rocky.eagle.activity
 * PROJECT_NAME TaskManager
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class MainActivity extends Activity implements View.OnClickListener, ExecuteTaskManager.GetExcuteTaskCallback {

    private TextView txtv_TestJson, txtv_Json,
            txtv_TestManyTask, txtv_ManyTask,
            txtv_TestBitmap;

    private ImageView imgv_Bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        txtv_TestJson = (TextView) findViewById(R.id.txtv_TestJson);
        txtv_TestJson.setOnClickListener(this);
        txtv_Json = (TextView) findViewById(R.id.txtv_Json);
        txtv_TestManyTask = (TextView) findViewById(R.id.txtv_TestManyTask);
        txtv_TestManyTask.setOnClickListener(this);
        txtv_ManyTask = (TextView) findViewById(R.id.txtv_ManyTask);
        txtv_TestBitmap = (TextView) findViewById(R.id.txtv_TestBitmap);
        txtv_TestBitmap.setOnClickListener(this);
        imgv_Bitmap = (ImageView) findViewById(R.id.imgv_Bitmap);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtv_TestJson:
                testJsonTask();
                break;
            case R.id.txtv_TestManyTask:
                testManyTask();
                break;
            case R.id.txtv_TestBitmap:
                testBitmap();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExecuteTaskManager.getInstance().log();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDataLoaded(ExecuteTask task) {
        if (task == null) {
            return;
        }

        if (task instanceof JsonExecuteTask) {

            if (task.getStatus() >= 0 && task.getJson() != null) {
                txtv_Json.setText(task.getJson());
            } else {
                LogUtils.i("请求失败");
            }

        } else if (task instanceof CommonExecuteTask) {

            reqNum++;
            txtv_ManyTask.setText("第  " + reqNum + "  次请求完成");

        } else if (task instanceof BitmapExecuteTask) {

            BitmapExecuteTask bitmapTask = (BitmapExecuteTask) task;
            if (bitmapTask.bitmap != null) {
                imgv_Bitmap.setImageBitmap(bitmapTask.bitmap);
            }

        }
    }

    private void testJsonTask() {
        JsonExecuteTask jsonTask = new JsonExecuteTask();
        Map<String, Object> map = new HashMap<>();
        map.put("Url", "http://www.google.com");
        map.put("Count", 20);
        jsonTask.setTaskParam(map);
        ExecuteTaskManager.getInstance().getData(jsonTask, this);
    }

    /**
     * 用于记录ManyTask成功请求了多少次
     */
    int reqNum = 0;

    private void testManyTask() {
        for (int i = 0; i < 100; i++) {
            ExecuteTaskManager.getInstance().getData(new CommonExecuteTask(), this);
        }
    }

    private void testNestManyTask() {
        for (int i = 0; i < 4; i++) {
            if (i % 2 == 0) {
                ExecuteTaskManager.getInstance().getData(new ManyExceptionExecuteTask(), this);
            } else {
                ExecuteTaskManager.getInstance().getData(new ManyStringExecuteTask(), this);
            }
        }
    }

    private void testBitmap() {
        ExecuteTaskManager.getInstance().getData(new BitmapExecuteTask(), this);
    }
}
