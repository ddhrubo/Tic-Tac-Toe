package com.example.dhrubo.tictactoe;

public interface GameActivityConnection {
    public void sendUpdate(int row,int col);
    public void abortGame();
}
