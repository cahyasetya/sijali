package com.pln.decki.sijali;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref=getSharedPreferences("sijali", 0);
                if
                        (pref.getBoolean("IsLogedIn", false)==true){
                    startActivity(new Intent(Splash.this, MainActivity.class));
                }else if(pref.getBoolean("IsLogedIn", false)==false){
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                }
                finish();
            }
        },500);
    }
}
