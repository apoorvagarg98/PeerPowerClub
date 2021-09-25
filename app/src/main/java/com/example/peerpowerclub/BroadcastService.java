package com.example.peerpowerclub;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BroadcastService extends Service {
    private String TAG = "BroadcastService";
    public static String COUNTDOWN_BR = "com.example.peerpowerclub";
    Intent intent = new Intent(COUNTDOWN_BR);
    CountDownTimer countDownTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("theend","startting timer");
        countDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
Log.i("theend","seconds remaining" + l);
intent.putExtra("countdown",l);
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
