package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

public class NewsContentActivity extends BaseActivity {

    /**
     * 启动 NewsContentActivity 的静态方法，传递新闻标题和内容作为额外数据。
     *
     * @param context    上下文
     * @param newsTitle  新闻标题
     * @param newsContent 新闻内容
     */
    public static void actionStart(Context context, String newsTitle, String newsContent) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("news_title", newsTitle);
        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }
    private static final String NEWS_TITLE_KEY = "news_title";
    private static final String NEWS_CONTENT_KEY = "news_content";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        configureActionBar();

        String newsTitle = getIntent().getStringExtra(NEWS_TITLE_KEY);
        String newsContent = getIntent().getStringExtra(NEWS_CONTENT_KEY);

        if (newsTitle != null && newsContent != null) {
            handleNewsData(newsTitle, newsContent);
        } else {
            handleMissingData();
        }
    }

    private void configureActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");  // Set to an empty title or your custom title
        }
    }


    /**
     * 处理显示新闻数据的方法。尝试找到 NewsContentFragment 并刷新其内容。
     *
     * @param newsTitle   新闻标题
     * @param newsContent 新闻内容
     */
    private void handleNewsData(String newsTitle, String newsContent) {
        // 尝试找到 NewsContentFragment 实例
        NewsContentFragment newsContentFragment = (NewsContentFragment) getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);

        // 如果找到了 NewsContentFragment，刷新其内容
        if (newsContentFragment != null) {
            newsContentFragment.refresh(newsTitle, newsContent);
        } else {
            // 如果未找到 NewsContentFragment，处理缺失 fragment 的情况
            handleMissingFragment();
        }
    }

    /**
     * 处理缺失数据的方法。在新闻标题或内容为 null 时调用。
     * 可以在这里显示错误消息或采取适当的行动。
     */
    private void handleMissingData() {
        // 处理新闻标题或内容为 null 的情况
        // 可以显示错误消息或采取适当的行动
    }

    /**
     * 处理缺失 fragment 的方法。在无法找到 NewsContentFragment 时调用。
     * 可以在这里显示错误消息或采取适当的行动，例如添加新的 fragment。
     */
    private void handleMissingFragment() {
        // 处理无法找到 NewsContentFragment 的情况
        // 可以显示错误消息或采取适当的行动，例如添加新的 fragment
    }
}


