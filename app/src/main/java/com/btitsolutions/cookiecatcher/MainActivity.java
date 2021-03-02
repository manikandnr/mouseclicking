package com.btitsolutions.cookiecatcher;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.btitsolutions.cookiecatcher.Utilities.RateThisApp;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    TextView lblScore;
    ImageView eater, firstMouse, secondMouse, thirdMouse, fourthMouse, poisonedMouse;
    FrameLayout frame;
    Button btnPause;

    private int score = 0;
    private int missedCounter = -4;

    private int frameHeight;
    private int boxSize;

    private int screenWidth;
    private int screenHeight;

    private int boxY;

    private int firstMouseX;
    private int firstMouseY;

    private int secondMouseX;
    private int secondMouseY;

    private int thirdMouseX;
    private int thirdMouseY;

    private int fourthMouseX;
    private int fourthMouseY;

    private int poisonedMouseX;
    private int poisonedMouseY;

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer soundPlayer;

    private boolean action_flag = false;
    private boolean start_flag = false;

    private int COOKIE_SPEED_LEVEL = 0;
    private int EATER_SPEED_LEVEL = 0;

    Context context_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;
        context_1 = this;

        soundPlayer = new SoundPlayer(this);
        lblScore = (TextView)findViewById(R.id.lblScore);
        lblScore.setText("Score: 0");

        btnPause = (Button)findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timer = null;

                OpenStarterDialog(context, true);
            }
        });

        eater = (ImageView)findViewById(R.id.eater);
        firstMouse = (ImageView)findViewById(R.id.firstMouse);
        secondMouse = (ImageView)findViewById(R.id.secondMouse);
        thirdMouse = (ImageView)findViewById(R.id.thirdMouse);
        fourthMouse = (ImageView)findViewById(R.id.fourthMouse);
        poisonedMouse = (ImageView)findViewById(R.id.poisonedMouse);

        firstMouse.setX(-80);
        firstMouse.setY(-80);

        secondMouse.setX(-80);
        secondMouse.setY(-80);

        thirdMouse.setX(-80);
        thirdMouse.setY(-80);

        fourthMouse.setX(-80);
        fourthMouse.setY(-80);

        fourthMouse.setX(-80);
        fourthMouse.setY(-80);

        poisonedMouse.setX(-80);
        poisonedMouse.setY(-80);

        frame = (FrameLayout)findViewById(R.id.frame);
        frame.setOnTouchListener(this);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        SharedPreferences Game_Setting = getSharedPreferences("Game_Setting", Context.MODE_PRIVATE);
        String level = Game_Setting.getString("SelectedLevel", "Easy");
        if(level.equals("Easy")){
            COOKIE_SPEED_LEVEL = 5;
        }
        else if(level.equals("Medium")){
            COOKIE_SPEED_LEVEL = 10;
        }
        else if(level.equals("Hard")){
            COOKIE_SPEED_LEVEL = 15;
        }

        String sensitivity = Game_Setting.getString("SelectedSensitivity", "Slow");
        if(sensitivity.equals("Slow")){
            EATER_SPEED_LEVEL = 10;
        }
        else if(sensitivity.equals("Medium")){
            EATER_SPEED_LEVEL = 15;
        }
        else if(sensitivity.equals("Fast")){
            EATER_SPEED_LEVEL = 20;
        }

        OpenStarterDialog(context, true);
    }

    private void ChangePosition(){
        HitCheck();

        //check for missed cookies counter
        if(missedCounter >= 100){
            //Game over
            timer.cancel();
            timer = null;

            soundPlayer.PlayGameOverSound();

            finish();
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("LastScore", score);
            startActivity(intent);
        }

        //first mouse
        firstMouseX -= (COOKIE_SPEED_LEVEL);
        if(firstMouseX < 0){
            firstMouseX = screenWidth + 500;
            firstMouseY = (int)Math.floor(Math.random() * (frameHeight - firstMouse.getHeight()));
        }

        firstMouse.setX(firstMouseX);
        firstMouse.setY(firstMouseY);

        //second mouse
        secondMouseX -= (COOKIE_SPEED_LEVEL + 1);
        if(secondMouseX < 0){
            secondMouseX = screenWidth + 1000;
            secondMouseY = (int)Math.floor(Math.random() * (frameHeight - secondMouse.getHeight()));
        }

        secondMouse.setX(secondMouseX);
        secondMouse.setY(secondMouseY);

        //third mouse
        thirdMouseX -= (COOKIE_SPEED_LEVEL + 2);
        if(thirdMouseX < 0){
            thirdMouseX = screenWidth + 1500;
            thirdMouseY = (int)Math.floor(Math.random() * (frameHeight - thirdMouse.getHeight()));
        }

        thirdMouse.setX(thirdMouseX);
        thirdMouse.setY(thirdMouseY);

        //fourth mouse
        fourthMouseX -= (COOKIE_SPEED_LEVEL + 3);
        if(fourthMouseX < 0){
            fourthMouseX = screenWidth + 2000;
            fourthMouseY = (int)Math.floor(Math.random() * (frameHeight - fourthMouse.getHeight()));
        }

        fourthMouse.setX(fourthMouseX);
        fourthMouse.setY(fourthMouseY);

        //poisoned mouse
        poisonedMouseX -= (COOKIE_SPEED_LEVEL + 2);
        if(poisonedMouseX < 0){
            poisonedMouseX = screenWidth + 3000;
            poisonedMouseY = (int)Math.floor(Math.random() * (frameHeight - poisonedMouse.getHeight()));
        }

        poisonedMouse.setX(poisonedMouseX);
        poisonedMouse.setY(poisonedMouseY);

        //eater setting
        if(action_flag == true){
            boxY -= EATER_SPEED_LEVEL;
        }
        else{
            boxY += EATER_SPEED_LEVEL;
        }

        if(boxY < 0) boxY = 0; //top boundary
        if(boxY > frameHeight - boxSize) boxY = frameHeight - boxSize; //bottom boundary

        eater.setY(boxY);

        lblScore.setText("Score: " + score);
    }

    public void HitCheck(){
        //first mouse
        int firstMouseCenterX = firstMouseX + firstMouse.getWidth()/2;
        int firstMouseCenterY = firstMouseY + firstMouse.getHeight()/2;

        if(0 <= firstMouseCenterX && firstMouseCenterX <= boxSize
                && boxY <= firstMouseCenterY && firstMouseCenterY <= boxY + boxSize){

            score += 5;
            firstMouseX = -10;

            soundPlayer.PlayHitSound();
        }
        else if(firstMouseCenterX + firstMouse.getWidth() - 10 <= boxSize){
            //firstMouseX = -10;
            //missedCounter += 1;
        }

        //second mouse
        int secondMouseCenterX = secondMouseX + secondMouse.getWidth()/2;
        int secondMouseCenterY = secondMouseY + secondMouse.getHeight()/2;

        if(0 <= secondMouseCenterX && secondMouseCenterX <= boxSize
                && boxY <= secondMouseCenterY && secondMouseCenterY <= boxY + boxSize){

            score += 10;
            secondMouseX = -10;

            soundPlayer.PlayHitSound();
        }
        else if(secondMouseCenterX + secondMouse.getWidth() - 10 <= boxSize){
            //secondMouseX = -10;
            //missedCounter += 1;
        }

        //third mouse
        int thirdMouseCenterX = thirdMouseX + thirdMouse.getWidth()/2;
        int thirdMouseCenterY = thirdMouseY + thirdMouse.getHeight()/2;

        if(0 <= thirdMouseCenterX && thirdMouseCenterX <= boxSize
                && boxY <= thirdMouseCenterY && thirdMouseCenterY <= boxY + boxSize){

            score += 15;
            thirdMouseX = -10;

            soundPlayer.PlayHitSound();
        }
        else if(thirdMouseCenterX + thirdMouse.getWidth() - 10 <= boxSize){
            //thirdMouseX = -10;
            //missedCounter += 1;
        }

        //fourth mouse
        int fourthMouseCenterX = fourthMouseX + fourthMouse.getWidth()/2;
        int fourthMouseCenterY = fourthMouseY + fourthMouse.getHeight()/2;

        if(0 <= fourthMouseCenterX && fourthMouseCenterX <= boxSize
                && boxY <= fourthMouseCenterY && fourthMouseCenterY <= boxY + boxSize){

            score += 20;
            fourthMouseX = -10;

            soundPlayer.PlayHitSound();
        }
        else if(fourthMouseCenterX + fourthMouse.getWidth() - 10 <= boxSize){
            //fourthMouseX = -10;
            //missedCounter += 1;
        }

        //poisoned mouse
        int poisonedMouseCenterX = poisonedMouseX + poisonedMouse.getWidth()/2;
        int poisonedMouseCenterY = poisonedMouseY + poisonedMouse.getHeight()/2;

        if(0 <= poisonedMouseCenterX && poisonedMouseCenterX <= boxSize
                && boxY <= poisonedMouseCenterY && poisonedMouseCenterY <= boxY + boxSize){

            //Game over
            timer.cancel();
            timer = null;

            soundPlayer.PlayGameOverSound();

            OpenResultDialog();
        }

        SharedPreferences Game_Setting = getSharedPreferences("Game_Setting", Context.MODE_PRIVATE);
        String level = Game_Setting.getString("SelectedLevel", "Easy");

        if(score >= 100 && score < 200){
            if(level.equals("Easy")){
                COOKIE_SPEED_LEVEL = 6;
                EATER_SPEED_LEVEL = 11;
            }
            else if(level.equals("Medium")){
                COOKIE_SPEED_LEVEL = 11;
                EATER_SPEED_LEVEL = 16;
            }
            else if(level.equals("Hard")){
                COOKIE_SPEED_LEVEL = 15;
                EATER_SPEED_LEVEL = 21;
            }
        }
        else if(score >= 200 && score < 300){
            if(level.equals("Easy")){
                COOKIE_SPEED_LEVEL = 7;
                EATER_SPEED_LEVEL = 12;
            }
            else if(level.equals("Medium") ){
                COOKIE_SPEED_LEVEL = 12;
                EATER_SPEED_LEVEL = 17;
            }
            else if(level.equals("Hard") ){
                COOKIE_SPEED_LEVEL = 16;
                EATER_SPEED_LEVEL = 22;
            }
        }
        else if(score >= 300 && score < 400){
            if(level.equals("Easy") ){
                COOKIE_SPEED_LEVEL = 8;
                EATER_SPEED_LEVEL = 13;
            }
            else if(level.equals("Medium") ){
                COOKIE_SPEED_LEVEL = 13;
                EATER_SPEED_LEVEL = 18;
            }
            else if(level.equals("Hard") ){
                COOKIE_SPEED_LEVEL = 17;
                EATER_SPEED_LEVEL = 23;
            }
        }
        else if(score >= 400 && score < 500){
            if(level.equals("Easy") ){
                COOKIE_SPEED_LEVEL = 9;
                EATER_SPEED_LEVEL = 14;
            }
            else if(level.equals("Medium") ){
                COOKIE_SPEED_LEVEL = 14;
                EATER_SPEED_LEVEL = 19;
            }
            else if(level.equals("Hard") ){
                COOKIE_SPEED_LEVEL = 18;
                EATER_SPEED_LEVEL = 24;
            }
        }
        else if(score >= 500 && score < 600){
            if(level.equals("Easy") ){
                COOKIE_SPEED_LEVEL = 10;
                EATER_SPEED_LEVEL = 15;
            }
            else if(level.equals("Medium") ){
                COOKIE_SPEED_LEVEL = 15;
                EATER_SPEED_LEVEL = 20;
            }
            else if(level.equals("Hard") ){
                COOKIE_SPEED_LEVEL = 19;
                EATER_SPEED_LEVEL = 25;
            }
        }
    }

    private void StartGame(){
        start_flag = true;
        frameHeight = frame.getHeight();
        boxY = (int)eater.getY();
        boxSize = eater.getHeight();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ChangePosition();
                    }
                });
            }
        }, 0, 20);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(start_flag == false){
            //
        }
        else{
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                action_flag = true;
            }
            else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                action_flag = false;
            }
        }

        return true;
    }

    public void OpenStarterDialog(Context context, final boolean IsPaused) {
        final Dialog dialog = new Dialog(context); // Context, this, etc.
        dialog.setContentView(R.layout.starter_dialog);
        dialog.setCancelable(false);
        dialog.show();

        Button btnDialogStart = (Button) dialog.findViewById(R.id.btnDialogStart);
        btnDialogStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IsPaused == true){
                    timer = new Timer();
                }

                StartGame();
                dialog.dismiss();
            }
        });

        Button btnDialogRestart = (Button) dialog.findViewById(R.id.btnDialogRestart);
        btnDialogRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences Game_Setting = getSharedPreferences("Game_Setting", Context.MODE_PRIVATE);
                String level = Game_Setting.getString("SelectedLevel", "Easy");
                if(level.equals("Easy")){
                    COOKIE_SPEED_LEVEL = 5;
                }
                else if(level.equals("Medium")){
                    COOKIE_SPEED_LEVEL = 10;
                }
                else if(level.equals("Hard")){
                    COOKIE_SPEED_LEVEL = 15;
                }

                String sensitivity = Game_Setting.getString("SelectedSensitivity", "Slow");
                if(sensitivity.equals("Slow")){
                    EATER_SPEED_LEVEL = 10;
                }
                else if(sensitivity.equals("Medium")){
                    EATER_SPEED_LEVEL = 15;
                }
                else if(sensitivity.equals("Fast")){
                    EATER_SPEED_LEVEL = 20;
                }

                score = 0;
                timer = new Timer();
                StartGame();
                dialog.dismiss();
            }
        });

        Button btnDialogClose = (Button) dialog.findViewById(R.id.btnDialogClose);
        btnDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StarterActivity.class));
            }
        });
    }

    public void OpenResultDialog() {
        final Dialog dialog = new Dialog(context_1); // Context, this, etc.
        dialog.setContentView(R.layout.result_dialog);
        dialog.setCancelable(false);

        TextView lblScore = (TextView)dialog.findViewById(R.id.lblScore);
        TextView lblHighScore = (TextView)dialog.findViewById(R.id.lblHighScore);

        lblScore.setText(String.valueOf(score));

        SharedPreferences Game_Data = getSharedPreferences("Game_Data", Context.MODE_PRIVATE);
        int highScore = Game_Data.getInt("HighScore", 0);

        if(score > highScore){
            lblHighScore.setText("High Score: " + score);

            //Update high score
            SharedPreferences.Editor editor = Game_Data.edit();
            editor.putInt("HighScore", score);
            editor.commit();
        }
        else{
            lblHighScore.setText("High Score: " + highScore);
        }

        dialog.show();

        Button btnRetry = (Button) dialog.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("IsRetry", true);
                startActivity(intent);
            }
        });

        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StarterActivity.class));
            }
        });

        Button btnRateUs = (Button) dialog.findViewById(R.id.btnRateUs);
        btnRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = context_1.getSharedPreferences("apprater", 0);
                if (prefs.getBoolean("dontshowagain", false)) { return ; }
                SharedPreferences.Editor editor = prefs.edit();

                RateThisApp.showRateDialog(context_1, editor);
            }
        });
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
}
