package com.example.dhrubo.tictactoe;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeNameActivity extends AppCompatActivity {
    EditText changeNameText, changeIPText;
    Button changeButton, changeIPButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setSubtitle("Change name and desired server ip.");

        changeNameText = (EditText)findViewById(R.id.change_name_edit_text);
        changeIPText = (EditText)findViewById(R.id.change_ip_edit_text);

        // get preference
        SharedPreferences readPref = getSharedPreferences("TicTacToe_pref",MODE_PRIVATE);
        changeNameText.setText(readPref.getString("myName","unknown"));
        changeIPText.setText(readPref.getString("desiredIP","192.168.0.108"));

        changeButton = (Button) findViewById(R.id.change_name_save_button);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set preference
                SharedPreferences.Editor writePref = getSharedPreferences("TicTacToe_pref",MODE_PRIVATE).edit();
                writePref.putString("myName",changeNameText.getText().toString());
                writePref.apply();
                Toast.makeText(ChangeNameActivity.this,"Name changed.",Toast.LENGTH_SHORT).show();
            }
        });

        changeIPButton = (Button) findViewById(R.id.change_ip_save_button);
        changeIPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set preference
                SharedPreferences.Editor writePref = getSharedPreferences("TicTacToe_pref",MODE_PRIVATE).edit();
                writePref.putString("desiredIP",changeIPText.getText().toString());
                writePref.apply();
                Toast.makeText(ChangeNameActivity.this,"Server IP changed.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
