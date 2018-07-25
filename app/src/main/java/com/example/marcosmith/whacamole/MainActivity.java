package com.example.marcosmith.whacamole;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    public static int points = 0;
    public static int streak = 0;

    public static int savepoint = 0;
    public static int savestreak = 0;

    public static int debounce = 0;
    public int backedout = 0;

    private SoundPool sp;
    private AudioAttributes AA;
    private int mInterval = 5000;
    private int molespeed;
    private Handler mHandler;

    //sound variables//
    public int moleWhack;
    public int moleHere;
    public int missMole;
    public int streakfail;

    public static String stringStreakSaved = "0";
    public static String stringPointsSaved = "0";

    private ImageButton hole1;
    private ImageButton hole2;
    private ImageButton hole3;
    private ImageButton hole4;
    private ImageButton hole5;
    private ImageButton pausedbutton;
    private TextView pointCounter;
    private TextView streakCounter;
    private boolean lastMoleWhacked = false;
    public int gamepaused;
    private boolean moleAppear1 = false, moleAppear2 = false, moleAppear3 = false, moleAppear4 = false, moleAppear5 = false;
    private boolean molespawned = false;
    private int chooseHole = 0;
    private Handler handler;
    private MediaPlayer mPlayer;
    public Boolean soundIsPlaying;

    private TextView pausething;

    public static SharedPreferences load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pausething = findViewById(R.id.textView6);

        debounce = 0;
        points = 0;
        streak = 0;
        savepoint = 0;
        savestreak = 0;

        load = getSharedPreferences("userdata", 0);
        String mString = load.getString("HighScore", "0");

        load = getSharedPreferences("userdata", 0);
        String mString2 = load.getString("HighStreak", "0");

        stringStreakSaved = mString2;
        stringPointsSaved = mString;

        savepoint = savepoint + Integer.parseInt(mString);
        savestreak = savestreak + Integer.parseInt(mString2);

        Log.d("logtag", "MainActivity onCreate: " + stringPointsSaved);

        loadSounds();
        setButtons();
        mHandler = new Handler();
        startRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                testhole();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public boolean onTouch(View view, MotionEvent boop) {
        int id = view.getId();
        if (id == R.id.molehole1) {
            if (moleAppear1){
                playSounds(1);
                givePoints(1);
                moleAppear1 = false;
                hole1.setImageResource(R.drawable.hole);
            }else if (!moleAppear1) {
                playSounds(2);
            }
        } else if (id == R.id.molehole2) {
            if (moleAppear2){
                playSounds(1);
                givePoints(1);
                moleAppear2 = false;
                hole2.setImageResource(R.drawable.hole);
            }else if(!moleAppear2) {
                playSounds(2);
            }
        } else if (id == R.id.molehole3) {
            if (moleAppear3){
                playSounds(1);
                givePoints(1);
                moleAppear3 = false;
                hole3.setImageResource(R.drawable.hole);
            }else if (!moleAppear3){
                playSounds(2);
            }

        } else if (id == R.id.molehole4){
            if (moleAppear4){
                playSounds(1);
                givePoints(1);
                moleAppear4 = false;
                hole4.setImageResource(R.drawable.hole);
            }else if (!moleAppear4){
                playSounds(2);
            }

        } else if (id == R.id.molehole5){
            if (moleAppear5){
                playSounds(1);
                givePoints(1);
                moleAppear5 = false;
                hole5.setImageResource(R.drawable.hole);
            }else if (!moleAppear5){
                playSounds(2);
            }

        } else if (id == R.id.imageButton){
            if (debounce == 0) {
                debounce = 1;
                Log.d("logtag", "if ID is imagebutton: " + stringPointsSaved);

                if (streak>savestreak){
                    stringStreakSaved = String.valueOf(streak);
                    SharedPreferences.Editor savestreak = load.edit();
                    savestreak.putString("HighStreak", stringStreakSaved).apply();
                }
                if (points > savepoint){
                    stringPointsSaved = String.valueOf(points);
                    SharedPreferences.Editor save = load.edit();
                    save.putString("HighScore", stringPointsSaved).apply();
                }
                Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }
        return false;
    }





    @SuppressLint("SetTextI18n")
    public void givePoints(int ptns){
        streak = streak + 1;
        mInterval = mInterval - (streak * 50);
        streakCounter = findViewById(R.id.StreakValue);
        streakCounter.setText(String.valueOf(streak));
        lastMoleWhacked = true;
        points = points + ptns;
        pointCounter = findViewById(R.id.textView5);
        pointCounter.setText(String.valueOf(points));
        if (streak>savestreak){
            stringStreakSaved = String.valueOf(streak);
            SharedPreferences.Editor savestreak = load.edit();
            savestreak.putString("HighStreak", stringStreakSaved).apply();
        }
        if (points > savepoint){
            stringPointsSaved = String.valueOf(points);
            SharedPreferences.Editor save = load.edit();
            save.putString("HighScore", stringPointsSaved).apply();
        }
    }

public void testhole() {
    Random rnd = new Random();
    chooseHole = rnd.nextInt(5);
    if (chooseHole == 0 && !molespawned){
        moleAppearSound();
        moleAppear1 = true;
        hole1.setImageResource(R.drawable.mole);
        noMole(1);
        molespawned = true;
    } else if (chooseHole == 1 && !molespawned){
        moleAppearSound();
        moleAppear2 = true;
        hole2.setImageResource(R.drawable.mole);
        noMole(2);
        molespawned = true;
    } else if(chooseHole == 2 && !molespawned){
        moleAppearSound();
        moleAppear3= true;
        hole3.setImageResource(R.drawable.mole);
        noMole(3);
        molespawned = true;
    }else if (chooseHole == 3 && !molespawned){
        moleAppearSound();
        moleAppear4 = true;
        hole4.setImageResource(R.drawable.mole);
        noMole(4);
        molespawned = true;
    }else if (chooseHole == 4 && !molespawned){
        moleAppearSound();
        moleAppear5 = true;
        hole5.setImageResource(R.drawable.mole);
        noMole(5);
        molespawned = true;
    }
}

public void noMole(final int holenumber) {
    handler = new Handler();
    handler.postDelayed(new Runnable() {
        @SuppressLint("SetTextI18n")
        public void run() {
            int num = holenumber;
            if (num == 1 && moleAppear1){
                moleAppear1 = false;
                hole1.setImageResource(R.drawable.hole);
                if (lastMoleWhacked) {
                    if (gamepaused == 0) {
                        streak = 0;
                        lastMoleWhacked = false;
                        streakCounter = findViewById(R.id.StreakValue);
                        streakCounter.setText(String.valueOf(streak));
                        playSounds(3);
                        mInterval = 5000;
                    }
                }
            }else if (num == 2 && moleAppear2){
                moleAppear2 = false;
                hole2.setImageResource(R.drawable.hole);
                if (gamepaused == 0) {
                    streak = 0;
                    lastMoleWhacked = false;
                    streakCounter = findViewById(R.id.StreakValue);
                    streakCounter.setText(String.valueOf(streak));
                    playSounds(3);
                    mInterval = 5000;
                }
            }else if (num == 3 && moleAppear3){
                moleAppear3 = false;
                hole3.setImageResource(R.drawable.hole);
                if (gamepaused == 0) {
                    streak = 0;
                    lastMoleWhacked = false;
                    streakCounter = findViewById(R.id.StreakValue);
                    streakCounter.setText(String.valueOf(streak));
                    playSounds(3);
                    mInterval = 5000;
                }
            }else if (num == 4 && moleAppear4){
                moleAppear4 = false;
                hole4.setImageResource(R.drawable.hole);
                if (gamepaused == 0) {
                    streak = 0;
                    lastMoleWhacked = false;
                    streakCounter = findViewById(R.id.StreakValue);
                    streakCounter.setText(String.valueOf(streak));
                    playSounds(3);
                    mInterval = 5000;
                }
            }else if (num == 5 && moleAppear5){
                moleAppear5 = false;
                hole5.setImageResource(R.drawable.hole);
                if (gamepaused == 0) {
                    streak = 0;
                    lastMoleWhacked = false;
                    streakCounter = findViewById(R.id.StreakValue);
                    streakCounter.setText(String.valueOf(streak));
                    playSounds(3);
                    mInterval = 5000;
                }
            }
            molespawned = false;
            molespeed = 1000 - (streak * 5);
            if (molespeed < 100){
                molespeed = 100;
            }else {
                molespeed = 1000 - (streak * 5);
            }
        }
    }, molespeed);

}

    @SuppressLint("ClickableViewAccessibility")
    public void setButtons(){
        hole1 = findViewById(R.id.molehole1);
        hole1.setOnTouchListener(this);

        hole2 = findViewById(R.id.molehole2);
        hole2.setOnTouchListener(this);

        hole3 = findViewById(R.id.molehole3);
        hole3.setOnTouchListener(this);

        hole4 = findViewById(R.id.molehole4);
        hole4.setOnTouchListener(this);

        hole5 = findViewById(R.id.molehole5);
        hole5.setOnTouchListener(this);

        ImageButton returnbutton = findViewById(R.id.imageButton);
        returnbutton.setOnTouchListener(this);

        pausedbutton = findViewById(R.id.pausebutton);
        pausedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gamepaused == 0){
                    gamepaused = 1;
                    stopRepeatingTask();
                    pausething.setVisibility(View.VISIBLE);
                    pausedbutton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play, null));
                    Log.d("logtag", "PAUSED!");
                } else if (gamepaused == 1){
                    gamepaused = 0;
                    startRepeatingTask();
                    pausething.setVisibility(View.INVISIBLE);
                    pausedbutton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause, null));
                    Log.d("logtag", "UNPAUSED!");
                }
            }
        });
    }

    public void loadSounds(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AA = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(AA)
                    .build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        }

        moleWhack = sp.load(this, R.raw.mole_whack,1);
        missMole = sp.load(this, R.raw.mole_swing,1);
        moleHere = sp.load(this, R.raw.mole_boop,1);
        streakfail = sp.load(this, R.raw.streak_ended,1);

        playSounds(4);
    }

    public void moleAppearSound(){
        sp.play(moleHere, 3,3,0,0,1);
    }


    public void playSounds(int soundnumber){
        if (soundnumber == 1) {
            sp.play(moleWhack, 1,1,0,0,1);
        } else if(soundnumber == 2) {
            sp.play(missMole, 1,1,0,0,1);
        } else if (soundnumber == 3){
            sp.play(streakfail, 1,1,0,0,1);
        } else if(soundnumber == 4){
            final  int MAX_VOLUME = 100;
            final float volume = (float) (1 - (Math.log(MAX_VOLUME - 50) / Math.log(MAX_VOLUME)));
            mPlayer = MediaPlayer.create(this, R.raw.bg_music);
            mPlayer.setVolume(volume, volume);
            mPlayer.start();
            mPlayer.setLooping(true);
            Log.d("logtag", "BG Music Started!");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();{
            stopRepeatingTask();
            if (debounce != 1 && backedout == 0){
                mPlayer.pause();
                gamepaused = 1;
                pausething.setVisibility(View.VISIBLE);
                pausedbutton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play, null));
                Log.d("logtag", "PAUSED from onPause!");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();{
            //startRepeatingTask();
            soundIsPlaying = mPlayer.isPlaying();
            Log.d("logtag", String.valueOf(soundIsPlaying));
            if(!soundIsPlaying){
                playSounds(4);
                Log.d("logtag", "Sound Played From onResume!");
            }
        }
    }


    @Override
    public void onBackPressed() {
        backedout = 1;
        Log.d("logtag", "Back button pressed : " + stringPointsSaved);
        stopRepeatingTask();
        if (streak>savestreak){
            stringStreakSaved = String.valueOf(streak);
            SharedPreferences.Editor savestreak = load.edit();
            savestreak.putString("HighStreak", stringStreakSaved).apply();
        }
        if (points > savepoint){
            stringPointsSaved = String.valueOf(points);
            SharedPreferences.Editor save = load.edit();
            save.putString("HighScore", stringPointsSaved).apply();
        }
        Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();{
            stopRepeatingTask();
            sp.release();
            sp = null;
            mPlayer.stop();
            mPlayer.release();
            if (streak>savestreak){
                stringStreakSaved = String.valueOf(streak);
                SharedPreferences.Editor savestreak = load.edit();
                savestreak.putString("HighStreak", stringStreakSaved).apply();
            }
            if (points > savepoint){
                stringPointsSaved = String.valueOf(points);
                SharedPreferences.Editor save = load.edit();
                save.putString("HighScore", stringPointsSaved).apply();
            }
        }
    }

}