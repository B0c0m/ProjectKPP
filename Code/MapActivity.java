package com.example.firstapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MapActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private View decorView;
    public int countEnergy = 10;
    public int minutes;
    public int seconds;
    private Handler customHandler = new Handler();
    long timeStartEnergyTimer = 0;
    long currentTimeEnergyTimer;


    // Array for energy
    final int[] energy = {
            R.id.battery1, R.id.battery2,R.id.battery3,R.id.battery4,R.id.battery5,R.id.battery6,R.id.battery7,R.id.battery8,R.id.battery9,R.id.battery10
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);



        ImageButton buttonEnergy = (ImageButton) findViewById(R.id.imageButton1);
        buttonEnergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (countEnergy == 0) {
                        countEnergy = 10;
                        TextView textCountEnergy = findViewById(R.id.countEnergy);
                        textCountEnergy.setText(countEnergy + "/10");
                        for(int i = 0; i < 10; i++) {
                            TextView eP = findViewById(energy[i]);
                            eP.setBackgroundResource(R.drawable.style_battery);
                        }
                    }
                    else {
                        countEnergy--;
                        for(int i = 0; i < 10; i++) {
                            TextView eP = findViewById(energy[i]);
                            eP.setBackgroundResource(R.drawable.style_battery);
                        }


                        for(int i = countEnergy; i < 10; i++) {
                            TextView eP = findViewById(energy[i]);
                            eP.setBackgroundResource(R.drawable.style_battery_empty);
                            TextView textCountEnergy = findViewById(R.id.countEnergy);
                            textCountEnergy.setText(" "+countEnergy + "/10");
                        }


                    }

                } catch (Exception e) {
                    //Exception
                }
            }


        });



        Runnable runnableEnergyTimer = new Runnable() {
            @Override
            public void run() {

                currentTimeEnergyTimer = System.currentTimeMillis();


                if(countEnergy < 10 && currentTimeEnergyTimer - timeStartEnergyTimer > 10000) {
                    timeStartEnergyTimer = System.currentTimeMillis();
                    CountDownTimer energyTimer = new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            minutes = ((int)millisUntilFinished / 1000) / 60;
                            seconds = ((int)millisUntilFinished / 1000) % 60;
                            TextView eT = findViewById(R.id.energyTimer);
                            eT.post(new Runnable() {
                                @Override
                                public void run() {
                                    eT.setText(minutes + ":" + seconds);
                                }
                            });
                        }

                        @Override
                        public void onFinish() {
                            countEnergy++;
                            TextView eP = findViewById(energy[countEnergy - 1]);
                            eP.setBackgroundResource(R.drawable.style_battery);
                            TextView textCountEnergy = findViewById(R.id.countEnergy);
                            textCountEnergy.post(new Runnable() {
                                @Override
                                public void run() {
                                    textCountEnergy.setText(" "+countEnergy + "/10");
                                }
                            });

                        }
                    }.start();
                }
                customHandler.postDelayed(this, 0);
            }
        };

        Thread threadEnergyTimer = new Thread(runnableEnergyTimer);
        threadEnergyTimer.start();



        // Implementation for hiding the bar
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });


    }






    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBar());

        }
    }


    // Parameter for bar hiding method
    private int hideSystemBar() {
        return    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    // Double tap for exit
    @Override
    public void onBackPressed() {


        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        }
        else {
            backToast = Toast.makeText(getBaseContext(),"Click again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }

    

}

