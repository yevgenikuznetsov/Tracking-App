package com.example.readapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    MaterialButton main_BTN_start;
    MaterialButton main_BTN_stop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
        askForPermissions();
    }

    private void initViews() {
        main_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionToService(AppService.START_FOREGROUND_SERVICE);
            }
        });

        main_BTN_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionToService(AppService.STOP_FOREGROUND_SERVICE);
            }
        });
    }

    private void actionToService(String action) {
        Intent startIntent = new Intent(MainActivity.this, AppService.class);
        startIntent.setAction(action);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startIntent);
        } else {
            startService(startIntent);
        }
    }

    private void askForPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_CALL_LOG}, 1);
    }

    private void findViews() {
        main_BTN_start = findViewById(R.id.main_BTN_start);
        main_BTN_stop = findViewById(R.id.main_BTN_stop);
    }
}