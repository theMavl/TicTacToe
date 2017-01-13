package com.mavl.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    byte[][] field = new byte[3][3];
    TextView[][] drField = new TextView[3][3];
    TextView tvStatus;
    byte player;
    int tmp;
    boolean win = false;
    byte clicks = 0;
    String[] labels = {"", "X", "O"};
    byte gMode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Intent in = getIntent();
        gMode = in.getByteExtra("mode", (byte)-1);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                Log.d("getting",("f"+i+j));
                tmp = getResources().getIdentifier(("f"+i+j), "id", getPackageName());
                Log.d("got",tmp+"");
                drField[i][j] = (TextView) findViewById(tmp);
            }
        tvStatus = (TextView)findViewById(R.id.tvStatus);
        player = 1;
        refreshStatus(player);
        clearField();
        Log.d("onCreate", "i'm done");
    }

    public void onClick(View view) {
        Log.d("onClick", "event caught");
        if ((win) || clicks >= 9) {
            win = false;
            clearField();
            refreshStatus(player);
            return;
        }
        tmp = view.getId();
        Log.d("OnClick","View id:"+tmp);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (tmp == drField[i][j].getId()) {
                    if (field[i][j] == 0) {
                        field[i][j] = player;
                        clicks ++;
                        drField[i][j].setText(labels[player]);
                        if (checkWin(player,i,j,false)){
                            if (gMode == 1)
                                tvStatus.setText("Игрок "+player+" выиграл!");
                            else
                                tvStatus.setText("Вы выиграли!");
                            win = true;
                            return;
                        }
                        if (clicks == 9) {
                            tvStatus.setText("Ничья");
                            return;
                        }
                        if (gMode == 1) {
                            if (player == 1)
                                player = 2;
                            else
                                player = 1;
                            refreshStatus(player);
                        }
                        else {
                            aiMove();
                        }
                    }
                    return;
                }
            }
    }

    void clearField() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                field[i][j] = 0;
                drField[i][j].setText(labels[0]);
                drField[i][j].setTextColor(Color.BLACK);
            }
        clicks = 0;
    }

    void aiMove() {
        byte act = 0; // 0 - nothing, 1 - win, 2 - interrupt
        byte USER = 1;
        byte COMP = 2;
        boolean COMPwins = false;
        boolean USERwins = false;
        int[] assum = new int[2];

        bump: {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j] > 0)
                        continue;
                    COMPwins = checkWin(COMP,i,j,true);
                    //Log.d("AI", String.format("Let's check if COMP wins at %d %d - %s", i,j, ((COMPwins)? "YES" : "NO")));
                    USERwins = checkWin(USER,i,j,true);
                    //Log.d("AI", String.format("Let's check if USER wins at %d %d - %s", i,j, ((USERwins)? "YES" : "NO")));
                    if (((act == 0) || (act == 2)) && (COMPwins)) {
                        act = 1;
                        assum[0] = i;
                        assum[1] = j;
                        Log.d("AI", String.format("I found win position at %d %d", i,j));
                        break bump;
                    }
                    if ((act == 0) && (USERwins)) {
                        act = 2;
                        assum[0] = i;
                        assum[1] = j;
                        Log.d("AI", String.format("I found interruption position at %d %d", i,j));
                    }
                }
            }
        }

        if (act == 0) { // random move
            do {
                assum[0] = (int)(Math.random()*3);
                assum[1] = (int)(Math.random()*3);
            } while (field[assum[0]][assum[1]] != 0);
            Log.d("AI", String.format("I found nothing, let's just set rand position %d %d", assum[0],assum[1]));
            clicks++;
            drField[assum[0]][assum[1]].setText(labels[COMP]);
            field[assum[0]][assum[1]] = COMP;
            return;
        }
        if (act == 1) {
            field[assum[0]][assum[1]] = COMP;
            drField[assum[0]][assum[1]].setText(labels[COMP]);
            win = true;
            checkWin(COMP, assum[0],assum[1], false);
            tvStatus.setText("ИИ выиграл!");
            return;
        }
        if (act == 2) {
            field[assum[0]][assum[1]] = COMP;
            drField[assum[0]][assum[1]].setText(labels[COMP]);
            clicks++;
            return;
        }


    }

    boolean checkWin (byte i, int x, int y, boolean assume) {
        //Log.d("checkWin", String.format("Checking if player %d wins at %d %d", i, x, y));
            if (((field[x][0] == i) || ((y == 0) && assume)) && ((field[x][1] == i) || ((y == 1) && assume)) && ((field[x][2] == i) || ((y == 2) && assume))) {
                if (!(assume)) {
                    setRed(x,0, x,1, x,2);
                }
                //Log.d("checkWin", "Player wins at horiz");
                return true;
            }

            if (((field[0][y] == i || ((x == 0) && assume))) && ((field[1][y] == i) || ((x == 1) && assume)) && ((field[2][y] == i) || ((x == 2) && assume))) {
                if (!(assume)) {
                    setRed(0,y, 1,y, 2,y);
                }
                //Log.d("checkWin", "Player wins at vert");
                return true;
            }

            if (((field[0][0] == i || ((x == 0) && (y == 0) && assume))) && ((field[1][1] == i || ((x == 1) && (y == 1) && assume))) && ((field[2][2] == i) || ((x == 2) && (y == 2) && assume))) {
                if (!(assume)) {
                    setRed(0,0, 1,1, 2,2);
                }
                //Log.d("checkWin", "Player wins at right diag");
                return true;
            }

            if (((field[0][2] == i || ((x == 0) && (y == 2) && assume))) && ((field[1][1] == i || ((x == 1) && (y == 1) && assume))) && ((field[2][0] == i) || ((x == 2) && (y == 0) && assume))) {
                if (!(assume)) {
                    setRed(0,2, 1,1, 2,0);
                }
                //Log.d("checkWin", "Player wins at left diag");
                return true;
            }
        //Log.d("checkWin", "Nope, he won't");
        return false;
    }

    void setRed (int x1, int y1, int x2, int y2, int x3, int y3) {
        drField[x1][y1].setTextColor(Color.RED);
        drField[x2][y2].setTextColor(Color.RED);
        drField[x3][y3].setTextColor(Color.RED);
    }

    void refreshStatus(byte i) {
        if (i == 1)
            if (gMode == 2)
                tvStatus.setText("Ваша очередь делать ход");
            else
                tvStatus.setText("Игрок 1 делает ход");
        else
            tvStatus.setText("Игрок 2 делает ход");
    }


}
