package com.example.smsbomber;

import android.telephony.SmsManager;

public class Sms {

    String destination;
    String text;
    int count;

    public Sms(String destination, String text, int count) {
        this.destination = destination;
        this.text = text;
        this.count = count;
    }

    public void send() {
        SmsManager manager = SmsManager.getDefault();
        for (int i = 0; i < this.count; i++) {
            manager.sendTextMessage(this.destination, null, this.text, null, null);
        }
    }
}
