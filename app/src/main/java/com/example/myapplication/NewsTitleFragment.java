package com.example.myapplication;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jxl.Sheet;
import jxl.Workbook;

public class NewsTitleFragment extends Fragment{
    public List<News> newsList=new ArrayList<News>();
    public NewsAdapter adapter=null;
    private boolean isTwoPane;
    public RecyclerView newsTitleRecyclerView=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.news_title_frag,container,false);
        newsTitleRecyclerView=(RecyclerView)view.findViewById(R.id.news_title_recycler_view);
        newsTitleRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL));
        //分割线

        //设置recyclerview的预加载个数，因为recyclerview会重用item
        newsTitleRecyclerView.setItemViewCacheSize(52);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        adapter=new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);

        return view;
    }
    private List<News> getNews(){
        return readxls();
    }

    //读取表格
    private List readxls(){
        //读写
        SharedPreferences pref=null;
        SharedPreferences.Editor editor;
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor=pref.edit();//获取editor实例
        AssetManager manager=getContext().getAssets();
        int num=50;
        int nowcount=pref.getInt("count",0);
        if(nowcount>0){
            nowcount=nowcount-50;
        }
        try{
            Workbook workbook=Workbook.getWorkbook(manager.open("glossary.xls"));
            Sheet sheet=workbook.getSheet(0);//第几张表
            if(nowcount==10929){
                ToastUtil.showToast(getContext(),"恭喜呀，词库已完结，已返回开头");
                nowcount=0;
            }
            if((nowcount+50)>10929){
                num=10929-nowcount;
            }

            News news3=new News();
            news3.setTitle("-----------加载上一页-----------");
            newsList.add(news3);
            //读取
            for(int j=nowcount;j<nowcount+num;j++){
                News news=new News();
                news.setTitle(String.format("%-12s:",sheet.getCell(0,j).getContents())+"\t\t"+sheet.getCell(1,j).getContents());
                news.setContent(sheet.getCell(0,j).getContents()+"\n\t\t释义：\n\t\t\t"+sheet.getCell(1,j).getContents());
                newsList.add(news);
            }
            News news2=new News();
            news2.setTitle("-----------加载下一页-----------");
            newsList.add(news2);
            editor.putInt("count",nowcount+num);
            editor.apply();
            workbook.close();
            return newsList;
        }
        catch(Exception e){
            ToastUtil.showToast(getContext(),"加载词汇书失败，请重试");
            return newsList;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        isTwoPane=false;
//        if(getActivity().findViewById(R.id.flag)!=null){
//            isTwoPane=true;
//        }
//        else{
//            isTwoPane=false;
//        }
    }

    //适配器
    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
        private List<News> mNewsList;
        /**
         * 内部 ViewHolder 类，用于表示 RecyclerView 中的每个项
         */
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitleText;
            public ViewHolder(View view){
                super(view);
                newsTitleText=(TextView)view.findViewById(R.id.news_title);
            }
        }
        /**
         * 构造函数，传入新闻数据列表
         */
        public NewsAdapter(List<News> newsList){
            mNewsList=newsList;
        }
        /**
         * 创建 ViewHolder 实例，加载新闻项布局，并设置点击事件
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            final View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // 获取被点击的新闻项的位置
                    News news=mNewsList.get(holder.getAdapterPosition());
//                    v.setBackgroundColor(Color.parseColor("#f6f5ec"));//选中颜色  这个方法行不通
                    // 处理点击事件，包括加载上一页、加载下一页和跳转到新闻内容界面
                    if(news.getTitle()=="-----------加载下一页-----------"){
                        refreshnextlast(1);
                    }
                    else if(news.getTitle()=="-----------加载上一页-----------"){
                        refreshnextlast(0);
                    }
                    else if(isTwoPane){
                        // 如果是双页模式，更新新闻内容
                        NewsContentFragment newsContentFragment=(NewsContentFragment)getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(),news.getContent());
                    }
                    else{
                        // 启动新闻内容活动
                        NewsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            News news=mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
            if(news.getTitle()=="-----------加载上一页-----------"||news.getTitle()=="-----------加载下一页-----------"){
                holder.newsTitleText.setTextColor(Color.BLUE);
                holder.newsTitleText.setTextSize(14);
                if(Build.VERSION.SDK_INT>=17){
                    holder.newsTitleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
            }
        }

        @Override
        public int getItemCount(){
            return mNewsList.size();
        }
    }
    /**
     * 刷新上一页或下一页的方法
     */
    private void refreshnextlast(int flag){
        //读写
        SharedPreferences pref=null;
        SharedPreferences.Editor editor;
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor=pref.edit();//获取editor实例
        AssetManager manager=getContext().getAssets();
        int num=50;
        int nowcount=pref.getInt("count",0);
        // 根据标志位调整当前位置
        if(flag==0&&nowcount>=50){
            nowcount=nowcount-50;
        }
        if(flag==0&&nowcount==0){
            ToastUtil.showToast(getContext(),"这里是第一页数据了哦");
            return;
        }
        try{
            Workbook workbook=Workbook.getWorkbook(manager.open("glossary.xls"));
            Sheet sheet=workbook.getSheet(0);//第几张表
//            int sheetRows=sheet.getRows();
//            editor.putInt("totalnum",sheetRows);
            if(nowcount==3990){
                ToastUtil.showToast(getContext(),"恭喜呀，词库已完结，已返回开头");
                nowcount=0;
            }
            if((nowcount+50)>3990) {
                num = 3990 - nowcount;
            }

            //读取
            int k=1;
            if(flag==0){
                nowcount=nowcount-50;
            }
            for(int j=nowcount;j<nowcount+num;j++){
                News news=new News();
                news.setTitle(String.format("%-12s:",sheet.getCell(0,j).getContents())+"\t\t"+sheet.getCell(1,j).getContents());
                news.setContent(sheet.getCell(0,j).getContents()+"\n\t\t释义：\n\t\t\t"+sheet.getCell(1,j).getContents());
                newsList.set(k,news);
                k++;
            }
            if(flag==0){
                nowcount=nowcount+50;
            }
            adapter.notifyItemRangeChanged(1,50);
            if(flag==0&&nowcount>0){
                nowcount=nowcount-50;
            }
            editor.putInt("count",nowcount+num);
            editor.apply();
            workbook.close();
            newsTitleRecyclerView.smoothScrollToPosition(0);
        }
        catch(Exception e){
            ToastUtil.showToast(getContext(),"加载词汇数据失败，请重试");
        }
    }
}
