package com.btitsolutions.cookiecatcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.btitsolutions.cookiecatcher.Utilities.RateThisApp;

public class StarterActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSetting, btnStart, btnRateUs;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        context = this;

        btnSetting = (Button)findViewById(R.id.btnSetting);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnRateUs = (Button)findViewById(R.id.btnRateUs);

        btnSetting.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnRateUs.setOnClickListener(this);
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
        if(view.getId() == btnSetting.getId()){
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
        }
        else if(view.getId() == btnStart.getId()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        else if(view.getId() == btnRateUs.getId()){
            SharedPreferences prefs = context.getSharedPreferences("apprater", 0);
            if (prefs.getBoolean("dontshowagain", false)) { return ; }
            SharedPreferences.Editor editor = prefs.edit();

            RateThisApp.showRateDialog(context, editor);
        }
    }
}
