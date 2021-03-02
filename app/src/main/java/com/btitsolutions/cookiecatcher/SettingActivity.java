package com.btitsolutions.cookiecatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSave;
    RadioButton rdbtnEasy, rdbtnMedium, rdbtnHard;
    RadioButton rdbtnCatcherSlow, rdbtnCatcherMedium, rdbtnCatcherFast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        rdbtnEasy = (RadioButton)findViewById(R.id.rdbtnEasy);
        rdbtnMedium = (RadioButton)findViewById(R.id.rdbtnMedium);
        rdbtnHard = (RadioButton)findViewById(R.id.rdbtnHard);

        rdbtnCatcherSlow = (RadioButton)findViewById(R.id.rdbtnCatcherSlow);
        rdbtnCatcherMedium = (RadioButton)findViewById(R.id.rdbtnCatcherMedium);
        rdbtnCatcherFast = (RadioButton)findViewById(R.id.rdbtnCatcherFast);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        SharedPreferences Game_Setting = getSharedPreferences("Game_Setting", Context.MODE_PRIVATE);
        String SelectedLevel = Game_Setting.getString("SelectedLevel", "Easy");
        String SelectedSensitivity = Game_Setting.getString("SelectedSensitivity", "Slow");

        if(SelectedLevel.equals("Easy")){
            rdbtnEasy.setChecked(true);
        }
        else if(SelectedLevel.equals("Medium")){
            rdbtnMedium.setChecked(true);
        }
        else if(SelectedLevel.equals("Hard")){
            rdbtnHard.setChecked(true);
        }

        if(SelectedSensitivity.equals("Slow")){
            rdbtnCatcherSlow.setChecked(true);
        }
        else if(SelectedSensitivity.equals("Medium")){
            rdbtnCatcherMedium.setChecked(true);
        }
        else if(SelectedSensitivity.equals("Fast")){
            rdbtnCatcherFast.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        SharedPreferences Game_Setting = getSharedPreferences("Game_Setting", Context.MODE_PRIVATE);
        String SelectedLevel = "";
        String SelectedSensitivity = "";

        if(rdbtnEasy.isChecked() == true){
            SelectedLevel = "Easy";
        }
        else if(rdbtnMedium.isChecked() == true){
            SelectedLevel = "Medium";
        }
        else if(rdbtnHard.isChecked() == true){
            SelectedLevel = "Hard";
        }

        if(rdbtnCatcherSlow.isChecked() == true){
            SelectedSensitivity = "Slow";
        }
        else if(rdbtnCatcherMedium.isChecked() == true){
            SelectedSensitivity = "Medium";
        }
        else if(rdbtnCatcherFast.isChecked() == true){
            SelectedSensitivity = "Fast";
        }

        SharedPreferences.Editor editor = Game_Setting.edit();
        editor.putString("SelectedLevel", SelectedLevel);
        editor.putString("SelectedSensitivity", SelectedSensitivity);
        editor.commit();

        Toast.makeText(this, "Saved Successfully.", Toast.LENGTH_SHORT).show();
    }
}
