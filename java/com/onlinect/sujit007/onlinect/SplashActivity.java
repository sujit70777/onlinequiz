package com.onlinect.sujit007.onlinect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    String a = "ha ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        getSupportActionBar().hide();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText("HELLO !!");
//                textView.setTextSize(20);
//                a += a;
//            }
//        }, 000);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText(textView.getText()+"\nI am SUJIT");
//                textView.setTextSize(25);
//                a += a;
//            }
//        }, 1000);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText(textView.getText()+"\nWELCOME TO MY APP...");
//                textView.setTextSize(30);
//                a += a;
//            }
//        }, 2000);
//
//
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, Login.class));
              //  finish();
               overridePendingTransition(R.anim.slide_in , R.anim.slide_out);
                finish();
            }
        }, 4000);


       // startActivity(new Intent(SplashActivity.this, Login.class));

    }
}
