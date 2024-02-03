package com.example.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 用于管理和处理强制用户注销的基础活动。
 * 当应用程序接收到特定广播时，它将提示用户是否要重新启动应用。
 * 如果用户同意，它将销毁所有活动并重新启动MainActivity。
 */
public class BaseActivity extends AppCompatActivity {

    private ForceOfflineReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerForceOfflineReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterForceOfflineReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * 注册强制下线广播接收器
     */
    private void registerForceOfflineReceiver() {
        if (receiver == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.example.impracticalities.FORCE_OFFLINE");
            receiver = new ForceOfflineReceiver();
            registerReceiver(receiver, intentFilter);
        }
    }

    /**
     * 注销强制下线广播接收器
     */
    private void unregisterForceOfflineReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    /**
     * 用于处理强制离线场景的广播接收器。
     */
    class ForceOfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            showForceOfflineDialog(context);
        }

        /**
         * 显示强制离线对话框
         */
        private void showForceOfflineDialog(final Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("通知");
            builder.setMessage("您确定要重新启动应用吗？");
            builder.setCancelable(true);
            builder.setNegativeButton("取消", (dialog, which) -> {
                // 用户点击了取消
                // 如果需要，可以添加额外的逻辑
            });
            builder.setPositiveButton("重新启动", (dialog, which) -> restartApplication(context));
            builder.show();
        }

        /**
         * 重新启动应用
         */
        private void restartApplication(Context context) {
            ActivityCollector.finishAll(); // 销毁所有活动
            Intent restartIntent = new Intent(context, MainActivity.class);
            context.startActivity(restartIntent);
        }
    }
}
