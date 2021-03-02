package com.btitsolutions.cookiecatcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.btitsolutions.cookiecatcher.Utilities.RateThisApp;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    TextView lblScore, lblHighScore;
    Button btnRetry, btnExit, btnRateUs;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        context = this;

        lblScore = (TextView)findViewById(R.id.lblScore);
        lblHighScore = (TextView)findViewById(R.id.lblHighScore);

        btnRetry = (Button)findViewById(R.id.btnRetry);
        btnExit = (Button)findViewById(R.id.btnExit);
        btnRateUs = (Button)findViewById(R.id.btnRateUs);

        btnRetry.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnRateUs.setOnClickListener(this);


    }

    public void TryAgain(View view){

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnRetry.getId()){

        }
        else if(view.getId() == btnExit.getId()){

        }
        else if(view.getId() == btnRateUs.getId()){

        }
    }
}
