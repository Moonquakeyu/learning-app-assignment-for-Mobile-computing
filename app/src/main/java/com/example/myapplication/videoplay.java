package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import android.Manifest;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class videoplay extends AppCompatActivity {

    private VideoView videoPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        // Hide the action bar
        hideActionBar();

        // Check and request external storage permission
        checkStoragePermission();
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(videoplay.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request storage permission if not granted
            requestStoragePermission();
        } else {
            // Permission already granted, initialize video path
            initVideoPath();
        }
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(videoplay.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void initVideoPath() {
        // Video URI (Replace this with your actual video path)
        String uri = "android.resource://" + getPackageName() + "/"/* + R.raw.movie */;

        // Initialize VideoView
        initializeVideoView(uri);
    }

    private void initializeVideoView(String uri) {
        videoPlayer = findViewById(R.id.video_view);
        videoPlayer.setVideoURI(Uri.parse(uri));
        videoPlayer.start();

        // Set completion listener to start MainActivity when video playback is complete
        setVideoCompletionListener();
    }

    private void setVideoCompletionListener() {
        videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Start MainActivity when video playback is complete
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(videoplay.this, MainActivity.class);
        startActivity(intent);
        finish(); // Destroy the videoplay activity
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handlePermissionResult(requestCode, grantResults);
    }

    private void handlePermissionResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, initialize video path
                    initVideoPath();
                } else {
                    // Permission denied, show a toast and finish the activity
                    showToastAndFinish("您拒绝了该权限");
                }
                break;
            default:
                break;
        }
    }

    private void showToastAndFinish(String message) {
        ToastUtil.showToast(videoplay.this, message);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseVideoResources();
    }

    private void releaseVideoResources() {
        if (videoPlayer != null) {
            videoPlayer.suspend(); // Release video resources
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
