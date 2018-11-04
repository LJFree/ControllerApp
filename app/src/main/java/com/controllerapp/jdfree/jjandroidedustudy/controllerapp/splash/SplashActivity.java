package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity.MainActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
