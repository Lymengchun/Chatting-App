package com.example.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
//import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_start extends AppCompatActivity {

//    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Delay 2 seconds, then move to MainScreen
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
               startMainActivity();
            }
        },2000);

    }
    public void OnGetStartedClick(View view){
        startMainActivity();
    }

    private void startMainActivity(){
        Intent intent = new Intent(this,activity_sign_in.class);

        startActivity(intent);
        finish();
    }



}
