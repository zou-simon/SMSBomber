package com.example.smsbomber;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SmsService extends Service {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(SMS_RECEIVED);
        registerReceiver(new SmsReceiver(), filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
