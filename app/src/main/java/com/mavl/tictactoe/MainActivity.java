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
    byte player;
    int tmp;
    boolean win = false;
    byte clicks = 0;
    String[] labels = {"", "X", "O"};
    Intent in = getIntent();
    byte gMode;
    gMode = in.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                Log.d("getting",("f"+i+j));
                tmp = getResources().getIdentifier(("f"+i+j), "id", getPackageName());
                Log.d("got",tmp+"");
                drField[i][j] = (TextView) findViewById(tmp);
            }
        player = 1;
        clearField();
        Log.d("onCreate", "i'm done");
    }

    public void onClick(View view) {
        Log.d("onClick", "event caught");
        if ((win) || clicks >= 9) {
            win = false;
            clearField();
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
                        if (checkWin(player,i,j)){
                            Toast.makeText(this, "Игрок "+player+" выиграл!", Toast.LENGTH_LONG).show();
                            win = true;
                            return;
                        }
                        if (clicks == 9) {
                            Toast.makeText(this, "Ничья", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (mode == 1) {
                            if (player == 1)
                                player = 2;
                            else
                                player = 1;
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
                    COMPwins = checkWin(COMP,i,j);
                    USERwins = checkWin(USER,i,j);
                    if (((act == 0) || (act == 2)) && (COMPwins)) {
                        act = 1;
                        assum[0] = i;
                        assum[1] = j;
                        break bump;
                    }
                    else if ((act == 0) && (USERwins)) {
                        act = 2;
                        assum[0] = i;
                        assum[1] = j;
                        break bump;
                    }
                }
            }
        }

        if (act == 0) { // random move
            do {
                assum[0] = (int)(Math.random()*3);
                assum[1] = (int)(Math.random()*3);
            } while (field[assum[0]][assum[1]] == 1);
            clicks++;
            drField[assum[0]][assum[1]].setText(labels[COMP]);
            return;
        }
        if (act == 1) {
            drField[assum[0]][assum[1]].setText(labels[COMP]);
            win = true;
            Toast.makeText(this, "Компуктер выиграл!", Toast.LENGTH_LONG).show();
            return;
        }
        if (act == 2) {
            drField[assum[0]][assum[1]].setText(labels[COMP]);
            clicks++;
            return;
        }


    }

    boolean checkWin (byte i, int x, int y) {
            if ((field[x][0] == i) && (field[x][1] == i) && (field[x][2] == i)) {
                drField[x][0].setTextColor(Color.RED);
                drField[x][1].setTextColor(Color.RED);
                drField[x][2].setTextColor(Color.RED);
                return true;
            }

            if ((field[0][y] == i) && (field[1][y] == i) && (field[2][y] == i)) {
                drField[0][y].setTextColor(Color.RED);
                drField[1][y].setTextColor(Color.RED);
                drField[2][y].setTextColor(Color.RED);
                return true;
            }

            if ((field[0][0] == i) && (field[1][1] == i) && (field[2][2] == i)) {
                drField[0][0].setTextColor(Color.RED);
                drField[1][1].setTextColor(Color.RED);
                drField[2][2].setTextColor(Color.RED);
                return true;
            }

            if ((field[0][2] == i) && (field[1][1] == i) && (field[2][0] == i)) {
                drField[0][2].setTextColor(Color.RED);
                drField[1][1].setTextColor(Color.RED);
                drField[2][0].setTextColor(Color.RED);
                return true;
            }

        return false;
    }


}
