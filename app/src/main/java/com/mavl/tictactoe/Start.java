package com.mavl.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void go(View view) {
        byte mode = 0;
        switch (view.getId()) {
            case (R.id.btFriend):
                mode = 1;
                break;
            case (R.id.brComp):
                mode = 2;
                break;
        }
        Intent in = new Intent(this, MainActivity.class);
        in.putExtra("mode", mode);
        startActivity(in);
    }
}
