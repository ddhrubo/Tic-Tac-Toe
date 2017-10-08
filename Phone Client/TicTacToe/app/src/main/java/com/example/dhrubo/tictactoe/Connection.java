package com.example.dhrubo.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.Socket;

public class Connection extends AppCompatActivity implements ConnectionInfoExchange {

    TextView connectionStatus;
    CreateConnectionThread createConnectionThread;
    boolean normallyLeaving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        getSupportActionBar().setTitle("Connection");
        getSupportActionBar().setSubtitle("Wait patiently.");

        connectionStatus = (TextView)findViewById(R.id.connection_status);

        createConnectionThread = new CreateConnectionThread(Connection.this,Connection.this);
        createConnectionThread.start();

        normallyLeaving = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(!normallyLeaving) {
            try {
                createConnectionThread.stop();
            } catch (Exception ex) {}
            new CloseConnectionThread().start();
        }

        finish();
    }

    // Interface Implementation
    @Override
    public void changeStatus(String statusMessage) {
        runOnUiThread(new ChangeTextViewRunnable(connectionStatus,statusMessage));
    }
    @Override
    public void proceedToGame() {
        normallyLeaving = true;
        runOnUiThread(new GoToGame(Connection.this));
    }
}

class ChangeTextViewRunnable implements Runnable {
    TextView textView;
    String message;

    public ChangeTextViewRunnable(TextView textView,String message) {
        this.textView = textView;
        this.message = message;
    }

    @Override
    public void run() {
        textView.setText(message);
    }
}

class GoToGame implements Runnable {
    Context context;

    public GoToGame(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context,GameActivity.class);
        context.startActivity(intent);
    }
}
