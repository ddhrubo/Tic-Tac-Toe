package com.example.dhrubo.tictactoe;

public class SendMessageToServer extends Thread {
    String message;

    public SendMessageToServer(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            Manager.toServer.println(message);
        } catch (Exception ex) {}
    }
}
