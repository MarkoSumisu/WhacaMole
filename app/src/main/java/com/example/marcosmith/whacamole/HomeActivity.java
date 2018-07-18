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

    private final String SCORE_KEY = "scorekey";
    private final String STREAK_KEY = "streakkey";

    public static String pointsString;
    public String testsave = "yo";

    //public SharedPreferences loadpoints = MainActivity.load;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

/*        loadpoints = getSharedPreferences(pointsString, 0);
        String mString = loadpoints.getString("HighScore", "0");*/

/*        scoreNumber = findViewById(R.id.textView11);
        scoreNumber.setText(mString);*/
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


