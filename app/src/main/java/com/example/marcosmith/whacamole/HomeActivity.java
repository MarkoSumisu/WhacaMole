package com.example.marcosmith.whacamole;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    public TextView streakNumber, scoreNumber;

    public static int highScore;
    public static int highstreak;

    //private String testsave = MainActivity.stringPointsSaved;
    //private String testsave2 = MainActivity.stringStreakSaved;

    public SharedPreferences loadpoints = MainActivity.load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //FullScreencall();


        highScore = MainActivity.savepoint;
        highstreak = MainActivity.savestreak;

        loadpoints = getSharedPreferences("userdata", 0);

        String mString = loadpoints.getString("HighScore", "0");
        scoreNumber = findViewById(R.id.textView12);
        scoreNumber.setText(mString);

        String mString2 = loadpoints.getString("HighStreak", "0");
        streakNumber = findViewById(R.id.textView11);
        streakNumber.setText(mString2);

        Log.d("logtag", "HomeActivity onCreate: " + mString);
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();{
            FullScreencall();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button){
            Log.d("logtag", "Game Launched");
            Intent homeIntent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //android.os.Process.killProcess(android.os.Process.myPid());
        moveTaskToBack(true);
        //HomeActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();{
        }
    }
}


