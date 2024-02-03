package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

public class Myprivacydata extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprivacydata);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        ImageView myp = findViewById(R.id.myheadphoto);
        TextView text = findViewById(R.id.mydata);

        displayUserName(text);
        displayUserProfilePicture(myp);
    }

    /**
     * 在TextView中显示用户名。
     */
    private void displayUserName(TextView textView) {
        String name = pref.getString("name", "单词菌");
        textView.setText("我的昵称：" + name);
    }

    /**
     * 使用滑翔在ImageView中显示用户的个人资料图片。
     */
    private void displayUserProfilePicture(ImageView imageView) {
        String urlPic = pref.getString("picture", null);

        if (urlPic != null) {
            loadProfilePictureWithGlide(imageView, urlPic);
        } else {
            handleFailedImageLoad();
        }
    }

    /**
     * 使用Glide加载用户个人资料图片。
     */
    private void loadProfilePictureWithGlide(ImageView imageView, String url) {
        File pic = new File(url);
        if (pic.exists()) {
            loadImageWithGlide(imageView, url);
        } else {
            handleFailedImageLoad();
        }
    }

    /**
     * 使用滑动加载图像到ImageView。
     */
    private void loadImageWithGlide(ImageView imageView, String url) {
        Glide.with(Myprivacydata.this).load(url).into(imageView);
    }

    /**
     * 处理加载图像失败的情况。
     */
    private void handleFailedImageLoad() {
        ToastUtil.showToast(Myprivacydata.this, "加载头像失败");
    }
}
