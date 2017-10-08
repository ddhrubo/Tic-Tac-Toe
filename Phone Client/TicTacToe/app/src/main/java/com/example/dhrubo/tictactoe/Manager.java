package com.example.dhrubo.tictactoe;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Dhrubo on 6/18/2017.
 */

public class Manager {
    // GAME STUFF
    public static int this_player;

    // NETWORK STUFF
    public static Socket server = null;
    public static PrintWriter toServer;
    public static BufferedReader fromServer;

    // PERSONAL STUFF
    public static String opponentName = "abc";
}
