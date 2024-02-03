package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast 工具类，用于方便显示短时消息
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 显示 Toast 消息
     *
     * @param context 上下文环境
     * @param text    要显示的文本消息
     */
    public static void showToast(Context context, String text) {
        if (toast == null) {
            // 如果 toast 为 null，则创建新的 Toast
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            // 如果 toast 不为 null，则更新文本和显示时长
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        // 显示 Toast
        toast.show();
    }
}
