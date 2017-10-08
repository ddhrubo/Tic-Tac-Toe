package com.example.dhrubo.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout create_button, join_button;
    LinearLayout joinGame, changeName, help, aboutUs;
    TextView changeNameButtonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setSubtitle("Connect with Players");

        mapElements();

        new CloseConnectionThread().start();
    }

    private void mapElements() {
        //create_button = (LinearLayout)findViewById(R.id.create_button);
        //join_button = (LinearLayout)findViewById(R.id.join_button);
        joinGame = (LinearLayout)findViewById(R.id.join_game_button);
        changeName = (LinearLayout)findViewById(R.id.change_name_button);
        changeNameButtonText = (TextView)findViewById(R.id.change_name_button_text);
        help = (LinearLayout)findViewById(R.id.help_button);
        aboutUs = (LinearLayout)findViewById(R.id.about_us_button);

        setListeners();
    }

    private void setListeners() {
        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Connection.class);
                startActivity(intent);
            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChangeNameActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(intent);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }
}
