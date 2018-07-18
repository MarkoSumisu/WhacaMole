package com.example.marcosmith.whacamole;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button playButton;
    public TextView streakNumber, scoreNumber;

    public static int highScore = MainActivity.points;
    public static int highstreak = MainActivity.streak;

    private String testsave = MainActivity.stringPointsSaved;
    private String testsave2 = MainActivity.stringStreakSaved;

    public SharedPreferences loadpoints = MainActivity.load;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadpoints = getSharedPreferences(testsave, 0);
        String mString = loadpoints.getString("HighScore", "0");

        loadpoints = getSharedPreferences(testsave2, 0);
        String mString2 = loadpoints.getString("HighStreak", "0");

       // Log.d("logtag", mString);

        scoreNumber = findViewById(R.id.textView12);
        scoreNumber.setText(mString);

        streakNumber = findViewById(R.id.textView11);
        streakNumber.setText(mString2);

        Log.d("logtag", "Main Activity booted");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button){
            Intent homeIntent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(homeIntent);
            finish();
            Log.d("logtag", "Main Activity fired");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();{
        }
    }
}


