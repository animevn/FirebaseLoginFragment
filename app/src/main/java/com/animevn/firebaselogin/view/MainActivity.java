package com.animevn.firebaselogin.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.animevn.firebaselogin.R;

public class MainActivity extends AppCompatActivity {

    public void hideActionBar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    public void showActionBar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().show();
        }
    }

    public void setScreenOrientationPortrait(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void setScreenOrientationUser(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }






}
