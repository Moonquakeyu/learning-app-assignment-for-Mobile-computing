package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class add_word extends AppCompatActivity {

    private String text = null;  // 用于存储编辑框中的文本
    private SharedPreferences pref = null;  // SharedPreferences 实例，用于存储数据
    private SharedPreferences.Editor editor = null;  // SharedPreferences 编辑器
    private Button save = null;  // 保存按钮变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        // 初始化 SharedPreferences 和编辑器
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        // 获取布局中的控件实例
        EditText mytext = findViewById(R.id.words);
        TextView toast = findViewById(R.id.toast);
        save = findViewById(R.id.save);

        // 从 SharedPreferences 中获取已保存的文本并显示在编辑框中
        String show = pref.getString("note", "");
        mytext.setText(show);
        mytext.setSelection(mytext.length());

        // 设置保存按钮的点击事件监听器
        save.setOnClickListener(view -> handleSaveButtonClick(mytext, toast));
    }

    // 保存按钮点击事件的处理方法
    private void handleSaveButtonClick(EditText mytext, TextView toast) {
        text = mytext.getText().toString();

        // 如果文本为空且按钮显示为“保存”，则显示提示信息
        if (text.isEmpty() && save.getText().equals("保存")) {
            ToastUtil.showToast(add_word.this, "请输入内容再保存");
        } else if (!text.isEmpty() && save.getText().equals("保存")) {
            // 如果文本不为空且按钮显示为“保存”，则保存文本到 SharedPreferences
            editor.putString("note", text);
            editor.apply();
            ToastUtil.showToast(add_word.this, "内容已保存");
        }

        // 如果文本行数超过 20 行且按钮显示为“保存”，则处理超出最大限制情况
        if (mytext.getLineCount() > 20 && save.getText().equals("保存")) {
            handleMaxLimitExceeded(toast);
        } else if (save.getText().equals("清空")) {
            // 如果按钮显示为“清空”，则处理清空按钮点击事件
            handleClearButtonClick(mytext, toast);
        }
    }

    // 文本行数超过最大限制的处理方法
    private void handleMaxLimitExceeded(TextView toast) {
        ToastUtil.showToast(add_word.this, "文本框内容已达最大限制");
        save.setText("清空");
        toast.setVisibility(View.INVISIBLE);
    }

    // 清空按钮点击事件的处理方法
    private void handleClearButtonClick(EditText mytext, TextView toast) {
        mytext.setText("");
        toast.setVisibility(View.VISIBLE);
        save.setText("保存");
        editor.putString("note", "");
        editor.apply();
    }

}
