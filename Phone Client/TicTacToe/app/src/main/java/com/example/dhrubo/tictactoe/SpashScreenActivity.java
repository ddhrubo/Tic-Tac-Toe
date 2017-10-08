package com.example.dhrubo.tictactoe;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpashScreenActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        getSupportActionBar().hide();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpashScreenActivity.this,MainActivity.class);
                startActivity(intent);
            }
        };
        handler.postDelayed(runnable,1500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
