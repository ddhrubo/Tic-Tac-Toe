package tictactoeserver;
import java.io.*;
import java.net.*;

public class GameThread extends Thread {
    Socket[] player;
    BufferedReader[] fromPlayer;
    PrintWriter[] toPlayer;
    String message, name[];
    
    public GameThread(String name1,Socket client1,BufferedReader fromClient1,PrintWriter toClient1,String name2,Socket client2,BufferedReader fromClient2,PrintWriter toClient2) {
        player = new Socket[2];
        fromPlayer = new BufferedReader[2];
        toPlayer = new PrintWriter[2];
        name = new String[2];
        
        name[0] = name1;
        player[0] = client1;
        fromPlayer[0] = fromClient1;
        toPlayer[0] = toClient1;
        
        name[1] = name2;
        player[1] = client2;
        fromPlayer[1] = fromClient2;
        toPlayer[1] = toClient2;
    }
    
    private void closeConnections() {
        try {
            player[0].close();
        } catch(Exception ex) {}
        
        try {
            player[1].close();
        } catch(Exception ex) {}
    }
    
    @Override
    public void run() {
        toPlayer[0].println("1");
        toPlayer[0].println(name[1]);
        
        toPlayer[1].println("2");
        toPlayer[1].println(name[0]);
        
        int now = 0, other;
        
        while(true) {
            // other
            other = 1 - now;
            
            try {
                message = fromPlayer[now].readLine();
                if(message.equals("UPDATE")) {
                    // get update
                    String row = fromPlayer[now].readLine();
                    String col = fromPlayer[now].readLine();
                    
                    // send update
                    toPlayer[other].println("UPDATE");
                    toPlayer[other].println(row);
                    toPlayer[other].println(col);
                } else if(message.equals("OVER")) {
                    toPlayer[now].println("OVER");
                    toPlayer[other].println("OVER");
                    closeConnections();
                    break;
                }
            } catch(Exception ex) {
                try {
                    toPlayer[now].println("ABORT");
                } catch(Exception exa) {}
                
                try {
                    toPlayer[other].println("ABORT");
                } catch(Exception exa) {}
                
                closeConnections();
                break;
            }
            
            // change turn
            now = other;
        }
    }
}
