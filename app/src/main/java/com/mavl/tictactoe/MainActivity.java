package com.mavl.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    byte[][] field = new byte[3][3];
    TextView[][] drField = new TextView[3][3];
    byte player;
    int tmp;
    boolean win = false;
    String[] labels = {"", "X", "O"};

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

    @Override
    public void onClick(View view) {
        Log.d("onClick", "event caught");
        if (win) {
            win = false;
            clearField();
            return;
        }
        tmp = view.getId();
        Log.d("OnClick","View id:"+tmp);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                Log.d("OnClick-looking for","asked: "+tmp+"having now: "+drField[i][j].getId());
                if (tmp == drField[i][j].getId()) {
                    if (field[i][j] == 0) {
                        field[i][j] = player;
                        drField[i][j].setText(labels[player]);
                        if (checkWin(player,i,j)){
                            Toast.makeText(this, "Игрок "+player+" выиграл!", Toast.LENGTH_LONG).show();
                            win = true;
                            return;
                        }
                        if (player == 1)
                            player = 2;
                        else
                            player = 1;

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
            }
    }

    boolean checkWin (byte i, int x, int y) {
            if ((field[x][0] == i) && (field[x][1] == i) && (field[x][2] == i))
                return true;

            if ((field[0][y] == i) && (field[1][y] == 1) && (field[2][y] == i))
                return true;

            if ((field[0][0] == i) && (field[1][1] == 1) && (field[2][2] == i))
                return true;

            if ((field[0][2] == i) && (field[1][1] == 1) && (field[2][0] == i))
                return true;
        return false;
    }


}
