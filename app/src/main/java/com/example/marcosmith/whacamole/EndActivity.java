package com.example.marcosmith.whacamole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    public int endingScore = MainActivity.points;
    public int endingStreak = MainActivity.finalstreak;

    public TextView finalscore, finalstreak;
    private ImageButton returnbutton;

    private SoundPool sp;
    private AudioAttributes AA;
    private MediaPlayer mPlayer;

    public int gameover;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        finalscore = findViewById(R.id.endingScore);
        finalstreak = findViewById(R.id.endingStreak);

        finalscore.setText("Score: " + String.valueOf(endingScore));
        finalstreak.setText("Streak: " + String.valueOf(endingStreak));

        loadsound();

        returnbutton = findViewById(R.id.backToHome);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.stop();
                mPlayer.release();
                Intent intent = new Intent(EndActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //Intent homeIntent = new Intent(EndActivity.this,HomeActivity.class);
                //startActivity(homeIntent);
                finish();
                //android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    public void loadsound(){
        final  int MAX_VOLUME = 100;
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 50) / Math.log(MAX_VOLUME)));
        mPlayer = MediaPlayer.create(this, R.raw.gameover);
        mPlayer.setVolume(volume, volume);
        mPlayer.start();
        Log.d("logtag", "Gameover!");
    }

    @Override
    public void onBackPressed() {
        mPlayer.stop();
        mPlayer.release();
        Intent intent = new Intent(EndActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //Intent homeIntent = new Intent(EndActivity.this,HomeActivity.class);
        //startActivity(homeIntent);
        finish();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();{
            sp.release();
            sp = null;
            mPlayer.stop();
            mPlayer.release();
        }
    }*/
}
