package com.example.dhrubo.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CreateConnectionThread extends Thread {
    ConnectionInfoExchange connectionActivity;
    Context context;
    String message;

    public CreateConnectionThread(ConnectionInfoExchange connectionActivity, Context context) {
        this.connectionActivity = connectionActivity;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            connectionActivity.changeStatus("Connecting to server.");

            // get preference
            SharedPreferences readPref = context.getSharedPreferences("TicTacToe_pref",context.MODE_PRIVATE);
            String myName = readPref.getString("myName","unknown");
            String desiredIP = readPref.getString("desiredIP","192.168.0.108");

            String server_ip;
            Manager.server = new Socket(desiredIP,6255);
            Manager.fromServer = new BufferedReader(new InputStreamReader(Manager.server.getInputStream()));
            Manager.toServer = new PrintWriter(Manager.server.getOutputStream(),true);
            // ............ send name
            Manager.toServer.println(myName);

            connectionActivity.changeStatus("Connected! Wait for opponent.");

            message = Manager.fromServer.readLine();    // mandatory message from server

            if(message.equals("STATUS")) {
                Manager.toServer.println("YES");

                // starting game
                message = Manager.fromServer.readLine();    // this message will have player number
                Manager.this_player = Integer.parseInt(message);
                message = Manager.fromServer.readLine();    // this message will have opponent name
                Manager.opponentName = message;
                connectionActivity.proceedToGame();

                return;
            }else {
                // starting game
                // the message already has player number
                Manager.this_player = Integer.parseInt(message);
                message = Manager.fromServer.readLine();    // this message will have opponent name
                Manager.opponentName = message;
                connectionActivity.proceedToGame();
            }
        } catch (Exception ex) {

        }
    }
}
