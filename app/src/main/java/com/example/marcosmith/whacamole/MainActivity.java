package com.example.marcosmith.whacamole;


import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static int points = 0;
    public static int streak = 0;

    public static int savepoint = 0;
    public static int savestreak = 0;

    private SoundPool sp;
    private AudioAttributes AA;
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private int molespeed;
    private Handler mHandler;

    //sound variables//
    public int moleWhack;
    public int moleHere;
    public int missMole;
    public int streakfail;

    public static String stringStreakSaved;
    public static String stringPointsSaved;

    private ImageButton hole1, hole2, hole3, hole4, hole5;
    private TextView pointCounter;
    private TextView streakCounter;
    private boolean lastMoleWhacked = false;
    private boolean moleAppear1 = false, moleAppear2 = false, moleAppear3 = false, moleAppear4 = false, moleAppear5 = false;
    private boolean molespawned = false;
    private int chooseHole = 0;
    private Handler handler;
    private MediaPlayer mPlayer;

    public static SharedPreferences load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load = getSharedPreferences(stringPointsSaved, 0);
        String mString = load.getString("HighScore", "0");

        load = getSharedPreferences(stringStreakSaved, 0);
        String mString2 = load.getString("HighStreak", "0");

        stringStreakSaved = mString2;
        stringPointsSaved = mString;

        savepoint = savepoint + Integer.parseInt(mString);
        savestreak = savestreak + Integer.parseInt(mString2);

        Log.d("logtag", "Second Activity booted");
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.molehole1) {
           //Log.d("logtag", String.valueOf(moleAppear1));
            if (moleAppear1){
                playSounds(1);
                givePoints(1);
                moleAppear1 = false;
                hole1.setImageResource(R.drawable.hole);
            }else if (!moleAppear1) {
                playSounds(2);
                //Log.d("logtag", "sound fired");
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

        }

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
            Log.d("logtag", "High Score Beaten! Streak Stats Saved!");
        }
        if (points > savepoint){
            stringPointsSaved = String.valueOf(points);
            SharedPreferences.Editor save = load.edit();
            save.putString("HighScore", stringPointsSaved).apply();
            Log.d("logtag", "High Score Beaten! Point Stats Saved!");
        }
    }

public void testhole() {
    //moleAppear1 = false; moleAppear2 = false; moleAppear3= false; moleAppear4 = false; moleAppear5 = false;
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
                    streak = 0;
                    lastMoleWhacked = false;
                    streakCounter = findViewById(R.id.StreakValue);
                    streakCounter.setText(String.valueOf(streak));
                    playSounds(3);
                    mInterval = 5000;
                }
            }else if (num == 2 && moleAppear2){
                moleAppear2 = false;
                hole2.setImageResource(R.drawable.hole);
                if (lastMoleWhacked) {
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
                if (lastMoleWhacked) {
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
                if (lastMoleWhacked) {
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
                if (lastMoleWhacked) {
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
            //moleAppear1 = false; moleAppear2 = false; moleAppear3 = false; moleAppear4 = false; moleAppear5 = false;
        }
    }, molespeed);

}

    public void setButtons(){
        hole1 = findViewById(R.id.molehole1);
        hole1.setOnClickListener(this);

        hole2 = findViewById(R.id.molehole2);
        hole2.setOnClickListener(this);

        hole3 = findViewById(R.id.molehole3);
        hole3.setOnClickListener(this);

        hole4 = findViewById(R.id.molehole4);
        hole4.setOnClickListener(this);

        hole5 = findViewById(R.id.molehole5);
        hole5.setOnClickListener(this);
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
        //backgroundMusic = sp.load(this, R.raw.bg_music,1);

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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();{
            stopRepeatingTask();
            mPlayer.pause();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();{
            startRepeatingTask();
            playSounds(4);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();{
            Log.d("logtag", "App Closed!");
            Log.d("logtag", stringPointsSaved);
            stopRepeatingTask();
            sp.release();
            sp = null;
            mPlayer.release();
            if (streak>savestreak){
                stringStreakSaved = String.valueOf(streak);
                SharedPreferences.Editor savestreak = load.edit();
                savestreak.putString("HighStreak", stringStreakSaved).apply();
                Log.d("logtag", "High Score Beaten! Streak Stats Saved!");
            }
            if (points > savepoint){
                stringPointsSaved = String.valueOf(points);
                SharedPreferences.Editor save = load.edit();
                save.putString("HighScore", stringPointsSaved).apply();
                Log.d("logtag", "High Score Beaten! Point Stats Saved!");
            }
        }
    }

}