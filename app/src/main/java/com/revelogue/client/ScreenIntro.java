package com.revelogue.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class ScreenIntro extends AppCompatActivity {
    //TextView intro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         //       WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_intro);
        //intro = findViewById(R.id.txtIntro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        checkRedict();

    }


    private void checkRedict() {
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);



                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}