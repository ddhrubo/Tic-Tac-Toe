package com.example.dhrubo.tictactoe;

public class CloseConnectionThread extends Thread {
    public CloseConnectionThread() {}

    @Override
    public void run() {
        try {
            if(Manager.server!=null) {
                Manager.server.close();
            }
            Manager.server = null;
        } catch (Exception ex) {
            Manager.server = null;
        }
    }
}
