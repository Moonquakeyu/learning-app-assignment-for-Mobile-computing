package com.example.myapplication;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class NewsContentFragment extends Fragment {


        private View rootView;
    /**
     * 创建并返回 fragment 的视图。
     *
     * @param inflater           布局加载器
     * @param container          父视图容器
     * @param savedInstanceState 之前保存的实例状态
     * @return 创建的视图
     */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.news_content_frag, container, false);
            return rootView;
        }
    /**
     * 刷新新闻标题和内容的方法。
     *
     * @param newsTitle   新闻标题
     * @param newsContent 新闻内容
     */
        public void refresh(String newsTitle, String newsContent) {
            // 获取视图中的可见布局
            View visibilityLayout = rootView.findViewById(R.id.visibility_layout);
            // 设置可见性为可见
            visibilityLayout.setVisibility(View.VISIBLE);
// 获取新闻标题和内容的 TextView 实例
            TextView newsTitleText = rootView.findViewById(R.id.news_title);
            TextView newsContentText = rootView.findViewById(R.id.news_content);
            // 设置新闻标题和内容
            newsTitleText.setText(newsTitle);
            newsContentText.setText(newsContent);
        }
    }


