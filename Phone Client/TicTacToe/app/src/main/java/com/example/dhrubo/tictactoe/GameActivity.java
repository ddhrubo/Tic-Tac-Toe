package com.example.dhrubo.tictactoe;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements GameActivityConnection {

    // components
    TextView win_text, draw_text, you_text, opponent_text;
    ImageView you_box, opponent_box, winner_box, draw_box_1, draw_box_2;
    ImageView[][] game_cell;

    // control variables
    int[][] player_mark;    // input record in each cell, 1 is 'X' and 2 is 'O'
    int turn_player;        // which player's turn now, 1 is 'X' and 2 is 'O'
    int this_player;        // identification of the player running the app. 1/2
    boolean active;         // true = game is running, false = win/draw state reached

    // network
    GameServerThread gameServerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().setTitle("Opponent: "+Manager.opponentName);
        getSupportActionBar().setSubtitle("Best of luck!");

        player_mark = new int[3][3];
        turn_player = 1;
        this_player = Manager.this_player;
        active = true;

        mapElements();
        resetHud();
        changeHud();

        gameServerThread = new GameServerThread(GameActivity.this);
        gameServerThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            gameServerThread.stop();
        } catch (Exception ex) {}

        new CloseConnectionThread().start();
    }

    /*
        * Initializes 'You' and 'Opponent' pictures based
        * on create/join game selected.
        * Creator is 'X' and joining player is 'O'*/
    private void resetHud() {
        if(this_player==1) {
            you_box.setImageResource(R.drawable.x_icon);
            opponent_box.setImageResource(R.drawable.o_icon);
        } else {
            you_box.setImageResource(R.drawable.o_icon);
            opponent_box.setImageResource(R.drawable.x_icon);
        }
    }

    /*
    * Shows who's turn it is*/
    private void changeHud() {
        if(!active) {
            /*you_box.setVisibility(View.INVISIBLE);
            you_text.setVisibility(View.INVISIBLE);
            opponent_box.setVisibility(View.INVISIBLE);
            opponent_text.setVisibility(View.INVISIBLE);*/
            you_box.setVisibility(View.VISIBLE);
            you_text.setVisibility(View.VISIBLE);
            opponent_box.setVisibility(View.VISIBLE);
            opponent_text.setVisibility(View.VISIBLE);
        }
        else if(turn_player==this_player) {
            you_box.setVisibility(View.VISIBLE);
            you_text.setVisibility(View.VISIBLE);
            opponent_box.setVisibility(View.INVISIBLE);
            opponent_text.setVisibility(View.INVISIBLE);
        } else {
            you_box.setVisibility(View.INVISIBLE);
            you_text.setVisibility(View.INVISIBLE);
            opponent_box.setVisibility(View.VISIBLE);
            opponent_text.setVisibility(View.VISIBLE);
        }
    }

    /*
    * Shows winners or draw match.*/
    private void showResult(int winner) {
        active = false;

        if(winner==0) {
            draw_box_1.setVisibility(View.VISIBLE);
            draw_box_2.setVisibility(View.VISIBLE);
            draw_text.setVisibility(View.VISIBLE);
            return;
        }

        if(winner==1) {
            winner_box.setImageResource(R.drawable.x_icon);
        } else if(winner==2) {
            winner_box.setImageResource(R.drawable.o_icon);
        }

        winner_box.setVisibility(View.VISIBLE);
        win_text.setVisibility(View.VISIBLE);
    }

    /*
    * At win, highlights the three winning cells by grey-scaling
    * all the other cells.*/
    private void greyOutCells(int r1,int c1,int r2,int c2,int r3,int c3) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter greyscale_filter = new ColorMatrixColorFilter(matrix);
        for(int r=0; r<3; r++) {
            for(int c=0; c<3; c++) {
                if(!(r==r1&&c==c1) && !(r==r2&&c==c2) && !(r==r3&&c==c3)) {
                    game_cell[r][c].setColorFilter(greyscale_filter);
                }
            }
        }
    }

    /*
    * Binds all the UI elements*/
    private void mapElements() {
        // hud
        you_box = (ImageView)findViewById(R.id.you_box);
        you_text = (TextView)findViewById(R.id.you_text);
        opponent_box = (ImageView)findViewById(R.id.opponent_box);
        opponent_text = (TextView)findViewById(R.id.opponent_text);

        // result panel
        winner_box = (ImageView)findViewById(R.id.winner_box);
        win_text = (TextView)findViewById(R.id.win_text);
        draw_box_1 = (ImageView)findViewById(R.id.draw_box_1);
        draw_box_2 = (ImageView)findViewById(R.id.draw_box_2);
        draw_text = (TextView)findViewById(R.id.draw_text);

        // game cells
        game_cell = new ImageView[3][3];
        // row 0
        game_cell[0][0] = (ImageView)findViewById(R.id.box0);
        game_cell[0][1] = (ImageView)findViewById(R.id.box1);
        game_cell[0][2] = (ImageView)findViewById(R.id.box2);
        // row 1
        game_cell[1][0] = (ImageView)findViewById(R.id.box3);
        game_cell[1][1] = (ImageView)findViewById(R.id.box4);
        game_cell[1][2] = (ImageView)findViewById(R.id.box5);
        // row 2
        game_cell[2][0] = (ImageView)findViewById(R.id.box6);
        game_cell[2][1] = (ImageView)findViewById(R.id.box7);
        game_cell[2][2] = (ImageView)findViewById(R.id.box8);

        setListeners();
    }

    /*
    * Sets listeners to necessary ui elements*/
    private void setListeners() {
        // clickable game cells
        for(int row=0; row<3; row++) {
            for(int col=0; col<3; col++) {
                game_cell[row][col].setOnClickListener(new GameCellClickListener(row,col));
            }
        }
    }

    /*
    * Implement Interface*/
    @Override
    public void sendUpdate(int row,int col) {
        runOnUiThread(new UpdateGameThread(row,col));
    }
    class UpdateGameThread implements Runnable {
        int row, col;
        public UpdateGameThread(int row,int col) {
            this.row = row;
            this.col = col;
        }
        @Override
        public void run() {
            gameCellClick(row,col);
        }
    }
    @Override
    public void abortGame() {
        runOnUiThread(new AbortGameThread(GameActivity.this));
    }
    class AbortGameThread implements Runnable {
        GameActivity gameActivity;
        public AbortGameThread(GameActivity gameActivity) {this.gameActivity=gameActivity;}
        @Override
        public void run() {
            try {
                Toast.makeText(gameActivity,"Game Cancelled!",Toast.LENGTH_LONG).show();
                gameServerThread.stop();
            } catch (Exception ex) {}
            new CloseConnectionThread().start();
            gameActivity.finish();
        }
    }

    /*
    * Extends OnClickListener to hold a Row+Column pair.
    * The row column data is needed to identify which cell
    * is clicked.*/
    class GameCellClickListener implements View.OnClickListener {
        private int row_data, column_data;
        public GameCellClickListener(int row, int col) {
            row_data = row;
            column_data = col;
        }
        @Override
        public void onClick(View v) {
            if(this_player!=turn_player) {
                return;
            }
            gameCellClick(row_data,column_data);
        }
    }

    /*
    * SEUPER IMPORTANT STUFF!!!
    * The main action of the games mechanics.
    * This function needs to be called in 2 ways.
    *   1. When the player clicks a cell, his move is processed. His click's
    *       row and column is passed in this function.
    *   2. When the other end (server/client) has sent data about the opponent's
    *       move. The opponent's click's row and column is used to call this function.*/
    private void gameCellClick(int row, int col) {
        // ######################################################### LATER CHANGE NEEDED HERE ################################
        if(!active || player_mark[row][col]!=0) {
            return;
        }

        if(turn_player==this_player) {
            // send message to server --------------------------------------------------------------------------
            new UpdateGameMessageToServer(row,col).start();
        }

        // view changes
        if(turn_player==1) {
            game_cell[row][col].setImageResource(R.drawable.x_icon);
        } else if(turn_player==2) {
            game_cell[row][col].setImageResource(R.drawable.o_icon);
        }

        // ######################################################### LATER CHANGE NEEDED HERE ################################
        // mark the move
        player_mark[row][col] = turn_player;

        // check for winning combination
        boolean endFound = checkForWinner();
        if(endFound) {
            // send message to end everything ..............................................................................
            new SendMessageToServer("OVER").start();
        }

        // change turns
        if(turn_player==1) {
            turn_player=2;
        } else if(turn_player==2) {
            turn_player=1;
        }

        // update hud
        changeHud();
    }

    /*
    * Checks if the game has reached a winning/draw state.*/
    private boolean checkForWinner() {
        for(int i=0; i<3; i++) {
            if(player_mark[i][0]==player_mark[i][1] && player_mark[i][1]==player_mark[i][2] && player_mark[i][0]!=0) {
                showResult(player_mark[i][0]);
                greyOutCells(i,0,i,1,i,2);
                return true;
            } else if(player_mark[0][i]==player_mark[1][i] && player_mark[1][i]==player_mark[2][i] && player_mark[0][i]!=0) {
                showResult(player_mark[0][i]);
                greyOutCells(0,i,1,i,2,i);
                return true;
            }
        }

        if(player_mark[0][0]==player_mark[1][1] && player_mark[1][1]==player_mark[2][2] && player_mark[0][0]!=0) {
            showResult(player_mark[0][0]);
            greyOutCells(0,0,1,1,2,2);
            return true;
        }

        if(player_mark[0][2]==player_mark[1][1] && player_mark[1][1]==player_mark[2][0] && player_mark[0][2]!=0) {
            showResult(player_mark[0][2]);
            greyOutCells(0,2,1,1,2,0);
            return true;
        }

        // check draw
        int cnt=0;
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                if(player_mark[i][j]==0) {
                    cnt++;
                }
            }
        }
        if(cnt==0) {
            showResult(0);
            return true;
        }

        return false;
    }
}
