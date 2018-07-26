package com.example.marcosmith.whacamole;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    public int endingScore = MainActivity.points;
    public int endingStreak = MainActivity.streak;

    public TextView finalscore, finalstreak;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        finalscore = findViewById(R.id.endingScore);
        finalstreak = findViewById(R.id.endingStreak);

        finalscore.setText("Score: " + String.valueOf(endingScore));
        finalstreak.setText("Streak: " + String.valueOf(endingStreak));
    }
}
