package com.example.marcosmith.whacamole;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


