package com.example.dhrubo.tictactoe;

public class GameServerThread extends Thread {
    GameActivityConnection gameActivity;
    String message;

    public GameServerThread(GameActivityConnection gameActivity) {
        this.gameActivity = gameActivity;
    }

    @Override
    public void run() {
        while (true) {
            try {
                message = Manager.fromServer.readLine();
                if(message.equals("UPDATE")) {
                    int row = Integer.parseInt(Manager.fromServer.readLine());
                    int col = Integer.parseInt(Manager.fromServer.readLine());

                    gameActivity.sendUpdate(row,col);
                } else if(message.equals("OVER")){
                    break;
                } else {
                    gameActivity.abortGame();
                    break;
                }
            } catch (Exception ex) {
                gameActivity.abortGame();
                break;
            }
        }
    }
}
