package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;


import jxl.Workbook;
import jxl.Sheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
/*
 *继承BaseActivity的目的在于及时的把活动添加到ArrayList中，便于后期实现强制下线对
 * 所有活动的销毁活动
 */
public class MainActivity extends BaseActivity implements android.view.View.OnClickListener{
    private ViewPager mviewpager;
    private PagerAdapter mpagerAdapter;
    private List<View> mviews=new ArrayList<View>();

    //2 tab,每个tab包含一个按钮
    private LinearLayout mtabmainpage;
    private LinearLayout mtabmine;
    private LinearLayout mtabsearch;

    //2个按钮
    private ImageButton mmainpage;
    private ImageButton mmine;
    private ImageButton msearch;

    //静态标签
    private int flag=0;
    private boolean flag1=true;

    //点击键盘监听
    private boolean flagtab3=true;

    //选择窗口实例
    PopupWindow mpopupwindow=null;

    //填充窗口查找主view
    public View pop2view;
    public ImageView picture;
    private Uri imageUri;

    //滑动菜单
    private DrawerLayout mDrawerLayout;

    //读写
    private SharedPreferences pref=null;
    private SharedPreferences.Editor editor;

    //滑动菜单头
    private NavigationView navView=null;

    //改昵称
    public boolean flagrewrite=false;
    public String tempstr=null;

    //退出监测
    public boolean isExit;
    private TextView textView;
    //查单词
    public boolean flagdesti=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        //滑动菜单
        navView=(NavigationView)findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                if(item.getItemId()== R.id.nav_client){
                    Intent in3=new Intent(MainActivity.this,Myprivacydata.class);
                    startActivity(in3);
                }
                else if(item.getItemId()== R.id.nav_base){
                    Intent in2=new Intent(MainActivity.this,Mywordbase.class);
                    startActivity(in2);
                }
                else if(item.getItemId()== R.id.nav_instruction){
                    Intent in4=new Intent(MainActivity.this,Myinstruction.class);
                    startActivity(in4);
                }
                return true;
            }
        });


        //ImageButton的初始化等
        initView();
        initviewPage();
        initEvent();
    }

private void initEvent() {
    setTabClickListeners();
    setViewPagerChangeListener();
}

    private void setTabClickListeners() {
        mtabmainpage.setOnClickListener(this);
        mtabmine.setOnClickListener(this);
        mtabsearch.setOnClickListener(this);
    }

    private void setViewPagerChangeListener() {
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                handlePageSelected(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Unused, keeping the method to adhere to the interface
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Unused, keeping the method to adhere to the interface
            }
        });
    }

    private void handlePageSelected(int position) {
        resetImg(flag);

        switch (position) {
            case 0:
                flag = 0;
                handlePageZero();
                break;
            case 2:
                flag = 2;
                handlePageTwo();
                break;
            case 1:
                flag = 1;
                handlePageOne();
                break;
            default:
                break;
        }
    }

    private void handlePageZero() {
        mmainpage.setImageResource(R.drawable.select);
        updateWordCount();
    }

    private void handlePageTwo() {
        mmine.setImageResource(R.drawable.select);
        if (flag1) {
            setmonitor();
            flag1 = false;
        }
    }

    private void handlePageOne() {
        msearch.setImageResource(R.drawable.select);
    }

    private void updateWordCount() {
        TextView wordCountTextView = layoutmainpage.findViewById(R.id.word_count);
        if (pref.getInt("count", -1) != -1) {
            wordCountTextView.setText(pref.getInt("count", 0) + "  个");
        }
    }


    private void initView(){
        mviewpager=(ViewPager)findViewById(R.id.viewpager);
        //初始化各个LinearLayour
        mtabmainpage=(LinearLayout)findViewById(R.id.id_1);
        mtabmine=(LinearLayout)findViewById(R.id.id_2);
        mtabsearch=(LinearLayout)findViewById(R.id.id_3);

        //初始化2个按钮
        mmainpage=(ImageButton)findViewById(R.id.id_1img);
        mmainpage.setImageResource(R.drawable.select);//初始指向第一个界面
        mmine=(ImageButton)findViewById(R.id.id_2img);
        msearch=(ImageButton)findViewById(R.id.id_3img);
    }
    private View layoutmainpage=null;
    private View layoutsearch=null;
    private View layoutmine=null;
    //private View layoutmin=null;
    private void initviewPage(){
        //初始化各个布局
        LayoutInflater mlayoutinflater= LayoutInflater.from(this);
        layoutmainpage=mlayoutinflater.inflate(R.layout.tab3,null);
        layoutsearch=mlayoutinflater.inflate(R.layout.tab1,null);
        layoutmine=mlayoutinflater.inflate(R.layout.tab2,null);
        //layoutmin=mlayoutinflater.inflate(R.layout.tab11,null);
        mviews.add(layoutmainpage);
        mviews.add(layoutsearch);
        mviews.add(layoutmine);
       // mviews.add(layoutmin);


        //查单词
        TextView finddes=findViewById(R.id.finddesti);
        finddes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View c){
                if(flagdesti==true){
                    flagdesti=false;
                    finddes.setText("");
                    finddes.setVisibility(View.GONE);
                }
            }
        });

        //添加单词备忘
        TextView addmyword=layoutmainpage.findViewById(R.id.addword);
        addmyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,add_word.class);
                startActivity(in);
            }
        });

        //查看拥有的词库
        TextView checkmydata=layoutmainpage.findViewById(R.id.englishbase);
        checkmydata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in2=new Intent(MainActivity.this,Mywordbase.class);
                startActivity(in2);
            }
        });

        //适配器初始化并配置
        mpagerAdapter=new PagerAdapter(){
            @Override
            public void destroyItem(ViewGroup container,int position,Object object){
                container.removeView(mviews.get(position));
            }
            @NonNull
            @Override
            public Object instantiateItem(ViewGroup container,int position){
                View view=mviews.get(position);
                container.addView(view);
                return view;
            }
            @Override
            public boolean isViewFromObject(View arg0,Object arg1){
                return arg0==arg1;
            }
            @Override
            public int getCount(){
                return mviews.size();
            }
        };
        mviewpager.setAdapter(mpagerAdapter);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        String username=pref.getString("name",null);
        TextView username1=layoutmainpage.findViewById(R.id.name);
        EditText myname=layoutmine.findViewById(R.id.myname);
        ImageView rewrite=layoutmine.findViewById(R.id.rewritename);
        View headerview=navView.getHeaderView(0);//这是导航栏的查找方式
        TextView username2=headerview.findViewById(R.id.username);
        if(username!=null){
            myname.setText(username);
            username1.setText(username);
            username2.setText(username);
        }
        else{
            myname.setText("单词菌");
            username1.setText("单词菌");
            username2.setText("单词菌");
        }
        layoutmine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                HideSoftInput(v2.getWindowToken());
            }
        });
        myname.setCursorVisible(false);
        myname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewrite.setImageDrawable(getResources().getDrawable(R.drawable.yes));
                myname.setCursorVisible(true);
                flagrewrite=true;
            }
        });
        rewrite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(flagrewrite){
                    myname.setCursorVisible(false);
                    rewrite.setImageDrawable(getResources().getDrawable(R.drawable.rewrite));
                    flagrewrite=false;
                    tempstr=myname.getText().toString();
                    if(tempstr.equals("")){
                        ToastUtil.showToast(MainActivity.this,"昵称不能为空");
                    }
                    else{
                        editor=pref.edit();
                        editor.putString("name",tempstr);
                        editor.apply();
                        myname.setText(tempstr);
                        username1.setText(tempstr);
                        username2.setText(tempstr);
                        ToastUtil.showToast(MainActivity.this,"昵称已更改");
                    }
                }
                else{
                    myname.setCursorVisible(true);
                    myname.setSelection(myname.length());
                }
            }
        });
        TextView word_count=layoutmainpage.findViewById(R.id.word_count);
        if(pref.getInt("count",-1)!=-1){
            word_count.setText(pref.getInt("count",0)+"  个");
        }
        setheadphoto();

        //查单词
        Button find=layoutmainpage.findViewById(R.id.search_word);
        EditText searchtext=layoutmainpage.findViewById(R.id.editsearch);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWord(searchtext.getText().toString());
                searchtext.setText("");
            }
        });

        //提醒资料修改的地方
        ImageView changephoto=layoutmainpage.findViewById(R.id.icon_image);
        changephoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ToastUtil.showToast(MainActivity.this,"您可在资料页面更改头像");
            }
        });

        //键盘的显示与隐藏
        if(flagtab3){
            flagtab3=false;
            //键盘
            LinearLayout v1=layoutmainpage.findViewById(R.id.panelmain);
            TextView startmemory=layoutmainpage.findViewById(R.id.startmemory);
            v1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    HideSoftInput(v.getWindowToken());
                }
            });
            startmemory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mviewpager.setCurrentItem(1);
                    resetImg(flag);
                    flag=1;
                    msearch.setImageResource(R.drawable.select);
                }
            });
        }



        //资料查看


    }
    private void findWord(String word) {
        if (word.equals("")) {
            ToastUtil.showToast(MainActivity.this, "请输入单词后再搜索");
            return;
        }

        if (word.startsWith(" ")) {
            ToastUtil.showToast(MainActivity.this, "请不要在开头输入不必要的空格");
            return;
        }
                        // A     B    C    D      E     F     G     H     I     J   K       L      M    N     O     P    Q     R     S      T     U      v       W     X     Y       Z
        int[] wordRanges = {0, 663, 1260, 2362, 3043, 3464, 3975, 4314, 4339, 5247, 5329, 5382, 5753, 6325, 6536, 6830, 7731, 7778, 8434, 9656, 10165, 10439, 10621, 10890, 10892, 10916, 10929};
        int firstChar = word.charAt(0);

        if (firstChar < 97) {
            firstChar += 32;
        } else if (firstChar >= 97) {
            int start = wordRanges[firstChar - 97];
            int end = wordRanges[firstChar - 96];
            searchinfile(word, start, end);
        } else {
            ToastUtil.showToast(MainActivity.this, "功能暂时未完善，敬请期待");
        }
    }


    @SuppressLint("SetTextI18n")
    private void searchinfile(String defiword, int start, int end){
        AssetManager manager=MainActivity.this.getAssets();
        TextView findesti=findViewById(R.id.finddesti);
        try{
            Workbook workbook=Workbook.getWorkbook(manager.open("glossary2.xls"));
            Sheet sheet=workbook.getSheet(0);//第几张表
            //读取
            for(int j=start;j<end;j++){
                if(defiword.equals(sheet.getCell(0,j).getContents())){
                    findesti.setVisibility(View.VISIBLE);
                    flagdesti=true;
                    findesti.setText(defiword+"\n  "+sheet.getCell(1,j).getContents());
                    ToastUtil.showToast(MainActivity.this,defiword+"  "+sheet.getCell(1,j).getContents());
                    break;
                }
                else if(j==end-1){
                    if(flagdesti){
                        findesti.setVisibility(View.GONE);
                        flagdesti=false;
                    }
                    ToastUtil.showToast(MainActivity.this,"当前词库没有此单词");
                }
            }
            workbook.close();
        }
        catch(Exception e){
            if(flagdesti==true){
                findesti.setVisibility(View.GONE);
                flagdesti=false;
            }
            ToastUtil.showToast(MainActivity.this,"查找单词失败，请重试");
        }
    }

    private void setheadphoto(){
        //初始化各个布局
        CircleImageView head=layoutmine.findViewById(R.id.myheadphoto);
        CircleImageView headmain=layoutmainpage.findViewById(R.id.icon_image);
        View headerview=navView.getHeaderView(0);//这是导航栏的查找方式
        CircleImageView headnav=headerview.findViewById(R.id.icon_image2);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        String urlpic=pref.getString("picture",null);
        File pic=null;
        if(urlpic==null){
            pic=new File("");
        }
        else{
            pic=new File(urlpic);
        }
        if(!pic.exists()){
            ToastUtil.showToast(MainActivity.this,"您的头像本地资源已删除，加载失败");
        }
        if(urlpic!=null&&pic.exists()){
            Glide.with(MainActivity.this).load(urlpic).into(head);
            Glide.with(MainActivity.this).load(urlpic).into(headmain);
            Glide.with(MainActivity.this).load(urlpic).into(headnav);
        }
        else{
            ToastUtil.showToast(MainActivity.this,"加载头像失败");
        }
    }

    private void resetImg(int flag1){
        if(flag1==0){
            mmainpage.setImageResource(R.drawable.mainpage);
        }
        else if(flag1==1){
            msearch.setImageResource(R.drawable.search);
        }
        else if(flag1==2){
            mmine.setImageResource(R.drawable.mine);
        }
    }

    private void setmonitor(){
        TextView mydata=(TextView) findViewById(R.id.mydata);
        mydata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in3=new Intent(MainActivity.this,Myprivacydata.class);
                startActivity(in3);
//                ToastUtil.showToast(MainActivity.this,"我的资料设置");
            }
        });

        TextView instruction=(TextView) findViewById(R.id.instructions);
        instruction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in2=new Intent(MainActivity.this,Mywordbase.class);
                startActivity(in2);
                ToastUtil.showToast(MainActivity.this,"切换科目");
            }
        });
        TextView setting=(TextView) findViewById(R.id.settings);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in4=new Intent(MainActivity.this,Myinstruction.class);
                startActivity(in4);
//                ToastUtil.showToast(MainActivity.this,"使用说明");
            }
        });
        ImageView myheadphoto=(ImageView)findViewById(R.id.myheadphoto);
        myheadphoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pop2view=showpopupwindow();
                View tab2panel=(View)findViewById(R.id.tab2panel);
                tab2panel.setBackgroundColor(Color.rgb(192,192,192));
            }
        });
    }
    //作用是屏幕旋转时不重新加载活动
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    //显示弹窗
    public View showpopupwindow() {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popuplayout, null);
        mpopupwindow = new PopupWindow(contentView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT, true);
        mpopupwindow.setContentView(contentView);
        TextView tv1=(TextView)contentView.findViewById(R.id.pop_camera);
        TextView tv2=(TextView)contentView.findViewById(R.id.pop_album);
        TextView cancel=(TextView)contentView.findViewById(R.id.cancel);
        View poppanel=(View)findViewById(R.id.tab2panel);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        cancel.setOnClickListener(this);
        poppanel.setOnClickListener(this);
        View rootview=LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
        mpopupwindow.showAtLocation(rootview, Gravity.BOTTOM,0,0);
        //监测popupwindow的消失或存在状态
        mpopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                View tab2panel=(View)findViewById(R.id.tab2panel);
                tab2panel.setBackgroundColor(Color.WHITE);
            }
        });
        return contentView;
    }
    @Override
    public void onClick(View v){
        if(v.getId()== R.id.id_1){
            mviewpager.setCurrentItem(0);
            resetImg(flag);
            TextView word_count=layoutmainpage.findViewById(R.id.word_count);
            if(pref.getInt("count",-1)!=-1){
                word_count.setText(pref.getInt("count",0)+"  个");
            }
            flag=0;
            mmainpage.setImageResource(R.drawable.select);

        }else if (v.getId()== R.id.id_2) {
            mviewpager.setCurrentItem(2);
            resetImg(flag);
            if(flag1){
                //为按钮添加点击事件
                setmonitor();
                flag1=false;
            }
            flag=2;
            mmine.setImageResource(R.drawable.select);

        } else if (v.getId()== R.id.id_3) {
            mviewpager.setCurrentItem(1);
            resetImg(flag);
            flag=1;
            msearch.setImageResource(R.drawable.select);
        }else if(v.getId()==R.id.cancel){
            mpopupwindow.dismiss();}
        else if (v.getId()==R.id.pop_camera) {
            //changeheadpohto();
            ToastUtil.showToast(MainActivity.this,"相机无法打开");
            mpopupwindow.dismiss();
        } else if (v.getId()==R.id.pop_album) {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            else{
               // openAlbum();
                ToastUtil.showToast(MainActivity.this,"无法打开相册");
            }
            mpopupwindow.dismiss();
        }



        }


    //销毁活动时的资源释放
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    //菜单栏菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (item.getItemId()==R.id.backup) {
            finish();

        } else if (item.getItemId()==R.id.exit) {
            Intent intent = new Intent("com.example.impracticalities.FORCE_OFFLINE");
            MainActivity.this.sendBroadcast(intent);  //发送这个广播

        }else{

        }
        return true;



    }



    /*
    隐藏软键盘
     */
    private void HideSoftInput(IBinder token){
        if(token!=null){
            InputMethodManager manager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        else{
            return super.onKeyDown(keyCode,event);
        }
    }
    public void exit(){
        Handler mHandler=new Handler(){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                isExit=false;
            }
        };
        if(!isExit){
            TextView finddestin=findViewById(R.id.finddesti);
            if(flagdesti==true){
                finddestin.setVisibility(View.GONE);
                flagdesti=false;
                return;
            }
            isExit=true;
            ToastUtil.showToast(MainActivity.this,"再按一次返回退出程序");
            mHandler.sendEmptyMessageDelayed(0,2000);
        }
        else{
            System.exit(0);
        }
    }

}