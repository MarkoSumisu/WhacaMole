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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    public static int points = 0;
    public static int streak = 0;

    public static int savepoint = 0;
    public static int savestreak = 0;
    public static int finalstreak = 0;
    public int life = 3;

    public static int debounce = 0;
    public int backedout = 0;

    private SoundPool sp;
    private AudioAttributes AA;
    private int mInterval = 3000;
    private int molespeed = 1000;
    public boolean startGame = false;
    private Handler mHandler;

    //sound variables//
    public int moleWhack;
    public int moleHere;
    public int missMole;
    public int streakfail;
    public int gamestart;

    public static String stringStreakSaved = "0";
    public static String stringPointsSaved = "0";

    public long last_pause;

    private ImageButton hole1;
    private ImageButton hole2;
    private ImageButton hole3;
    private ImageButton hole4;
    private ImageButton hole5;
    private ImageButton pausedbutton;
    private ImageView heart1, heart2, heart3;
    private TextView pointCounter;
    private TextView streakCounter;
    public boolean lastMoleWhacked = false;
    public int gamepaused;
    private boolean moleAppear1 = false, moleAppear2 = false, moleAppear3 = false, moleAppear4 = false, moleAppear5 = false;
    private boolean molespawned = false;
    private int chooseHole = 0;
    private Handler handler;
    private MediaPlayer mPlayer;
    public Boolean soundIsPlaying;

    private TextView pausething;

    public static SharedPreferences load;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSounds();
        setButtons();

        pausething = findViewById(R.id.textView6);
        pausething.setVisibility(View.VISIBLE);
        pausething.setText("GAME START!");

        debounce = 0;
        points = 0;
        streak = 0;
        savepoint = 0;
        savestreak = 0;
        finalstreak = 0;
        mInterval = 3000;
        molespeed = 1000;

        load = getSharedPreferences("userdata", 0);
        String mString = load.getString("HighScore", "0");

        load = getSharedPreferences("userdata", 0);
        String mString2 = load.getString("HighStreak", "0");

        stringStreakSaved = mString2;
        stringPointsSaved = mString;

        savepoint = savepoint + Integer.parseInt(mString);
        savestreak = savestreak + Integer.parseInt(mString2);

        Log.d("logtag", "MainActivity onCreate: " + stringPointsSaved);

        mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playSounds(5);
                //playSounds(4);
                pausething.setVisibility(View.INVISIBLE);
                pausething.setText("PAUSED!");
            }
        }, 1000);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pausething.setVisibility(View.INVISIBLE);
                startRepeatingTask();
            }
        }, 3000);
        //startRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (!molespawned){
                    long runTime = System.currentTimeMillis();
                    if (startGame = false){
                        Log.d("logtag", "Game not Started!");
                        return;
                    }
                    if(runTime >= (last_pause + (mInterval))) {
                        testhole();
                        Log.d("logtag", "Mole Spawned!");
                    }
                }else if (molespawned){
                    clearMoles();
                    molespawned = false;
                    Log.d("logtag", "Remove Mole!");
                }
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
                noMole(1);
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
        if (boop.getAction() == MotionEvent.ACTION_DOWN){
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
                    MainActivity.this.finish();
                }
            }
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    public void givePoints(int ptns){
        streak = streak + 1;
        finalstreak = finalstreak + 1;
/*        if (mInterval>500){
            mInterval = mInterval - (streak * 10);
        }else if (mInterval < 500){
            mInterval = 500;
        }*/
        if (molespeed < 800){
            molespeed = 800;
        }else if (molespeed > 800){
            molespeed = molespeed - (streak * 5);
        }
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
    if (mInterval < 500){
        mInterval = 500;
    }
/*    Log.d("logtag", "Spawn Interval : " + mInterval);
    Log.d("logtag", "Despawn Interval : " + molespeed);*/
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

public void clearMoles(){
    moleAppear1 = false;
    hole1.setImageResource(R.drawable.hole);

    moleAppear2 = false;
    hole2.setImageResource(R.drawable.hole);

    moleAppear3 = false;
    hole3.setImageResource(R.drawable.hole);

    moleAppear4 = false;
    hole4.setImageResource(R.drawable.hole);

    moleAppear5 = false;
    hole5.setImageResource(R.drawable.hole);
}

public void UpdateLife(int health){
        if (health == 3){
            heart1.setVisibility(View.VISIBLE);
            heart2.setVisibility(View.VISIBLE);
            heart3.setVisibility(View.VISIBLE);
        } else if (health == 2){
            heart1.setVisibility(View.VISIBLE);
            heart2.setVisibility(View.VISIBLE);
            heart3.setVisibility(View.INVISIBLE);
        } else if (health == 1){
            heart1.setVisibility(View.VISIBLE);
            heart2.setVisibility(View.INVISIBLE);
            heart3.setVisibility(View.INVISIBLE);
        } else if (health == 0){
            heart1.setVisibility(View.INVISIBLE);
            heart2.setVisibility(View.INVISIBLE);
            heart3.setVisibility(View.INVISIBLE);
            backedout = 1;
            Intent homeIntent = new Intent(MainActivity.this,EndActivity.class);
            startActivity(homeIntent);
            MainActivity.this.finish();
        }
}

public void noMole(final int holenumber) {
    handler = new Handler();
    handler.postDelayed(new Runnable() {
        @SuppressLint("SetTextI18n")
        public void run() {
            if (gamepaused == 0){
                if (holenumber == 1 && moleAppear1){
                    moleAppear1 = false;
                    hole1.setImageResource(R.drawable.hole);
                    if (lastMoleWhacked) {
                        if (gamepaused == 0) {
                            finalstreak = streak;
                            streak = 0;
                            lastMoleWhacked = false;
                            streakCounter = findViewById(R.id.StreakValue);
                            streakCounter.setText(String.valueOf(streak));
                            playSounds(3);
                            mInterval = 3000;
                            if(life > 0){
                                life = life - 1;
                                UpdateLife(life);
                            }
                            if(life == 0){
                                UpdateLife(life);
                            }
                        }
                    }
                }else if (holenumber == 2 && moleAppear2){
                    moleAppear2 = false;
                    hole2.setImageResource(R.drawable.hole);
                    if (lastMoleWhacked) {
                        if (gamepaused == 0) {
                            finalstreak = streak;
                            streak = 0;
                            lastMoleWhacked = false;
                            streakCounter = findViewById(R.id.StreakValue);
                            streakCounter.setText(String.valueOf(streak));
                            playSounds(3);
                            mInterval = 3000;
                            if(life > 0){
                                life = life - 1;
                                UpdateLife(life);
                            }
                            if(life == 0){
                                UpdateLife(life);
                            }
                        }
                    }
                }else if (holenumber == 3 && moleAppear3){
                    moleAppear3 = false;
                    hole3.setImageResource(R.drawable.hole);
                    if (lastMoleWhacked) {
                        if (gamepaused == 0) {
                            finalstreak = streak;
                            streak = 0;
                            lastMoleWhacked = false;
                            streakCounter = findViewById(R.id.StreakValue);
                            streakCounter.setText(String.valueOf(streak));
                            playSounds(3);
                            mInterval = 3000;
                            if(life > 0){
                                life = life - 1;
                                UpdateLife(life);
                            }
                            if(life == 0){
                                UpdateLife(life);
                            }
                        }
                    }
                }else if (holenumber == 4 && moleAppear4){
                    moleAppear4 = false;
                    hole4.setImageResource(R.drawable.hole);
                    if (lastMoleWhacked) {
                        if (gamepaused == 0) {
                            finalstreak = streak;
                            streak = 0;
                            lastMoleWhacked = false;
                            streakCounter = findViewById(R.id.StreakValue);
                            streakCounter.setText(String.valueOf(streak));
                            playSounds(3);
                            mInterval = 3000;
                            if(life > 0){
                                life = life - 1;
                                UpdateLife(life);
                            }
                            if(life == 0){
                                UpdateLife(life);
                            }
                        }
                    }
                }else if (holenumber == 5 && moleAppear5){
                    moleAppear5 = false;
                    hole5.setImageResource(R.drawable.hole);
                    if (lastMoleWhacked) {
                        if (gamepaused == 0) {
                            finalstreak = streak;
                            streak = 0;
                            lastMoleWhacked = false;
                            streakCounter = findViewById(R.id.StreakValue);
                            streakCounter.setText(String.valueOf(streak));
                            playSounds(3);
                            mInterval = 3000;
                            if(life > 0){
                                life = life - 1;
                                UpdateLife(life);
                            }
                            if(life == 0){
                                UpdateLife(life);
                            }
                        }
                    }
                }
                molespawned = false;
                Log.d("logtag", "molespawned: " + molespawned);
            }
        }
    }, molespeed);
}

    @SuppressLint("ClickableViewAccessibility")
    public void setButtons(){

        heart1 = findViewById(R.id.Life1);
        heart2 = findViewById(R.id.Life2);
        heart3 = findViewById(R.id.Life3);

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
                    clearMoles();
                    gamepaused = 1;
                    stopRepeatingTask();
                    pausething.setVisibility(View.VISIBLE);
                    pausedbutton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play, null));
                    Log.d("logtag", "PAUSED!");
                    Log.d("logtag", "Spawn Interval : " + mInterval);
                } else if (gamepaused == 1){
                    last_pause = System.currentTimeMillis();
                    gamepaused = 0;
                    startRepeatingTask();
                    pausething.setVisibility(View.INVISIBLE);
                    pausedbutton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause, null));
                    Log.d("logtag", "UNPAUSED!");
                    Log.d("logtag", "Spawn Interval : " + mInterval);
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
        gamestart = sp.load(this, R.raw.startgame, 1);

        playSounds(4);
    }

    public void moleAppearSound(){
        sp.play(moleHere, 1,1,0,0,1);
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
        } else if(soundnumber == 5){
            sp.play(gamestart, 1,1,0,0,1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();{
            stopRepeatingTask();
            if (debounce != 1 && backedout == 0){
                clearMoles();
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
        MainActivity.this.finish();
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
            MainActivity.this.finish();
        }
    }

}