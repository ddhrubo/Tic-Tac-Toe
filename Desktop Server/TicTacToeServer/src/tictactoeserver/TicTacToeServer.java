package tictactoeserver;
import java.io.*;
import java.net.*;

public class TicTacToeServer {
    static String message, name1, name2;
    static ServerSocket welcome;
    static Socket client1, client2;
    static PrintWriter toClient1, toClient2;
    static BufferedReader fromClient1, fromClient2;
    
    public static void main(String[] args) throws Exception {
        welcome = new ServerSocket(6255);
        
        client1 = null;
        client2 = null;
        
        while(true) {
            if(client1==null) {
                // GET CLIENT 1
                System.out.println("Waiting for client 1");
                client1 = welcome.accept();
                toClient1 = new PrintWriter(client1.getOutputStream(),true);
                fromClient1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
                // ............ get name
                name1 = fromClient1.readLine();
                System.out.println("Client 1 connected. Name: ");
            }
            
            // GET CLIENT 2
            System.out.println("Waiting for client 2");
            client2 = welcome.accept();
            toClient2 = new PrintWriter(client2.getOutputStream(),true);
            fromClient2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
            // ............ get name
            name2 = fromClient2.readLine();
            System.out.println("Client 2 connected. Name: ");
            
            // Check if client 1 still alive
            System.out.println("Checking client 1 status.");
            toClient1.println("STATUS");
            boolean clientIsOk = false;
            try {
                message = fromClient1.readLine();
                if(message.equals("YES")) {
                    clientIsOk = true;
                }
            } catch(Exception ex) {
                clientIsOk = false;
            }
            
            // client 1 has left
            if(!clientIsOk) {
                System.out.println("Lost client 1. Moving client 2 to 1.");
                client1 = client2;
                fromClient1 = fromClient2;
                toClient1 = toClient2;
                continue;
            }
            
            // creating game for client1 and client2
            System.out.println("Starting Game for \'"+name1+"\' and \'"+name2+"\'.");
            new GameThread(name1,client1,fromClient1,toClient1,name2,client2,fromClient2,toClient2).start();
            
            // clearing client1 and client2 for next iteration
            client1 = null;
            client2 = null;
        }
    }    
}
