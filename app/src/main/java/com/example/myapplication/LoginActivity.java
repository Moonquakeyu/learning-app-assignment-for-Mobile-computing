package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.bumptech.glide.Glide;

import java.io.File;

public class LoginActivity extends BaseActivity {

    private EditText username;
    private Button login;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox playOrNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideActionBar();

        // Use findViewById directly
        initializeViews();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        checkIfNicknameNeeded();

        ImageView headp = findViewById(R.id.headp);
        TextView usernamet = findViewById(R.id.usernamet);
        String urlpic = getUserProfilePicture();
        String usernametget = getUserNickname();

        handleUserProfilePicture(headp, urlpic);
        handleUserNickname(usernamet, usernametget);

        setLoginClickListener();
        setPanelClickListener();
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initializeViews() {
        username = findViewById(R.id.usernamex);
        login = findViewById(R.id.login);
        playOrNot = findViewById(R.id.playornot);
    }

    private void checkIfNicknameNeeded() {
        if (pref.getString("name", null) != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private String getUserProfilePicture() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getString("picture", null);
    }

    private String getUserNickname() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getString("name", null);
    }

    private void handleUserProfilePicture(ImageView headp, String urlpic) {
        File pic = (urlpic != null) ? new File(urlpic) : new File("");

        // Handle case where the image does not exist
        if (urlpic != null && pic.exists()) {
            Glide.with(LoginActivity.this).load(urlpic).into(headp);
        }
    }

    private void handleUserNickname(TextView usernamet, String usernametget) {
        // Handle case where the username is not available
        if (usernametget != null) {
            usernamet.setText(usernametget);
        }
    }

    private void setLoginClickListener() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginButtonClick();
            }
        });
    }

    private void handleLoginButtonClick() {
        String usernameText = getUsernameText();
        editor.putString("name", usernameText).apply();

        Class<?> destinationClass = (playOrNot.isChecked()) ? videoplay.class : MainActivity.class;
        startActivity(new Intent(LoginActivity.this, destinationClass));
        finish();
    }

    private String getUsernameText() {
        return username.getText().toString().isEmpty() ? "用户名" : username.getText().toString();
    }

    private void setPanelClickListener() {
        View panel = findViewById(R.id.panel);
        panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(v.getWindowToken());
            }
        });
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
