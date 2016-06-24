package com.rocky.eagle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocky.eagle.R;
import com.rocky.eagle.bean.BitmapExcuteTask;
import com.rocky.eagle.bean.CommonExcuteTask;
import com.rocky.eagle.bean.JsonExcuteTask;
import com.rocky.eagle.task.ExcuteTask;
import com.rocky.eagle.task.ExcuteTaskManager;
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
public class MainActivity extends Activity implements View.OnClickListener, ExcuteTaskManager.GetExcuteTaskCallback {

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
    public void onDataLoaded(ExcuteTask task) {
        if (task == null) {
            return;
        }

        if (task instanceof JsonExcuteTask) {

            if (task.getStatus() >= 0 && task.getJson() != null) {
                txtv_Json.setText(task.getJson());
            } else {
                LogUtils.i("请求失败");
            }

        } else if (task instanceof CommonExcuteTask) {

            reqNum++;
            txtv_ManyTask.setText("第  " + reqNum + "  次请求完成");

        } else if (task instanceof BitmapExcuteTask) {

            BitmapExcuteTask bitmapTask = (BitmapExcuteTask) task;
            if (bitmapTask.bitmap != null) {
                imgv_Bitmap.setImageBitmap(bitmapTask.bitmap);
            }

        }
    }

    private void testJsonTask() {
        JsonExcuteTask jsonTask = new JsonExcuteTask();
        Map<String, Object> map = new HashMap<>();
        map.put("Url", "http://www.google.com");
        map.put("Count", 20);
        jsonTask.setTaskParam(map);
        ExcuteTaskManager.getInstance().getData(jsonTask, this);
    }

    /**
     * 用于记录ManyTask成功请求了多少次
     */
    int reqNum = 0;

    private void testManyTask() {
        for (int i = 0; i < 100; i++) {
            ExcuteTaskManager.getInstance().getData(new CommonExcuteTask(), this);
        }
    }

    private void testBitmap() {
        ExcuteTaskManager.getInstance().getData(new BitmapExcuteTask(), this);
    }
}
