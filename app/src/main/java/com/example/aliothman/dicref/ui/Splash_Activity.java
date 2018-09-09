package com.example.aliothman.dicref.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.example.aliothman.dicref.R;
import com.example.aliothman.dicref.utils.AppUtils;

public class Splash_Activity extends Activity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onStart() {
        super.onStart();
        // method for Set Color For Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppUtils.setStatusBarColor(Splash_Activity.this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash_Activity.this,Home_Activity.class);
                Splash_Activity.this.startActivity(mainIntent);
                Splash_Activity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
