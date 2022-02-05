package com.example.smsbomber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    String message;
    String sender;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String str = "";

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                sender = msgs[i].getOriginatingAddress();
                message = msgs[i].getMessageBody().toString();
                str += "SMS de " + sender + " :" + message + "\n";
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            Sms sms = new Sms(sender, "Ce message est une réponse automatique.", 1);
            sms.send();
        }
    }
}
