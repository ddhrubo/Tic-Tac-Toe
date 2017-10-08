package com.example.dhrubo.tictactoe;

public class UpdateGameMessageToServer extends Thread {
    int row, col;

    public UpdateGameMessageToServer(int row,int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public void run() {
        try {
            Manager.toServer.println("UPDATE");
            Manager.toServer.println(Integer.toString(row));
            Manager.toServer.println(Integer.toString(col));
        } catch (Exception ex) {}
    }
}
