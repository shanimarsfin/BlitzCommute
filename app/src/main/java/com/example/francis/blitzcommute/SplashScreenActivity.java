package com.example.francis.blitzcommute;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION_SEC = 1;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread splash = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_DURATION_SEC * 1000);
                } catch (InterruptedException e) {
                    Log.e("InterruptedException", e.toString());
                } finally {
                    Intent i = new Intent(context, MainTerminalActivity.class);
                    startActivity(i);
                }
            }
        };

        splash.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //this.finish();
    }
}
