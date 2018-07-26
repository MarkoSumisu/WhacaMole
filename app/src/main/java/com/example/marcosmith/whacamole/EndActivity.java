package com.example.marcosmith.whacamole;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    public int endingScore;
    public int endingStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
    }
}
