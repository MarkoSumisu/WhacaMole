package com.example.marcosmith.whacamole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
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
                finish();
            }
        });
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
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
    protected void onResume() {
        super.onResume();{
            //FullScreencall();
        }
    }

    @Override
    public void onBackPressed() {
        mPlayer.stop();
        mPlayer.release();
        Intent intent = new Intent(EndActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
