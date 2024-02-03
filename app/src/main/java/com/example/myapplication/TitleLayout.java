package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
/**
 * 自定义标题布局，继承自 LinearLayout
 */
public class TitleLayout extends LinearLayout {
    // 强制下线的广播动作字符串
    private static final String FORCE_OFFLINE_ACTION = "com.example.impracticalities.FORCE_OFFLINE";
    /**
     * 构造方法，在代码中创建 TitleLayout 实例时调用
     *
     * @param context 上下文环境
     * @param attrs   属性集
     */
    public TitleLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);

        // 使用布局文件 "R.layout.title" 来填充 TitleLayout
        LayoutInflater.from(context).inflate(R.layout.title, this);

        // 获取返回按钮并设置点击事件
        Button buttonBack = findViewById(R.id.title_back);
        buttonBack.setOnClickListener(v -> {
            // 当返回按钮被点击时，结束当前 Activity
            ((Activity) getContext()).finish();
        });

        // 获取退出按钮并设置点击事件
        Button buttonExit = findViewById(R.id.title_exit);
        buttonExit.setOnClickListener(v -> {
            // 当退出按钮被点击时，发送广播强制用户下线
            Intent intent = new Intent(FORCE_OFFLINE_ACTION);
            context.sendBroadcast(intent);
        });
    }
}

