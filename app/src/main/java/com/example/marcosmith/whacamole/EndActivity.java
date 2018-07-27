package com.example.marcosmith.whacamole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    public int endingScore = MainActivity.points;
    public int endingStreak = MainActivity.finalstreak;

    public TextView finalscore, finalstreak;
    private ImageButton returnbutton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        finalscore = findViewById(R.id.endingScore);
        finalstreak = findViewById(R.id.endingStreak);

        finalscore.setText("Score: " + String.valueOf(endingScore));
        finalstreak.setText("Streak: " + String.valueOf(endingStreak));

        returnbutton = findViewById(R.id.backToHome);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(EndActivity.this,HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });
    }
}
