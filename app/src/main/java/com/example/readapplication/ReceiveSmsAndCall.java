package com.example.readapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class ReceiveSmsAndCall extends BroadcastReceiver {
    private StatePhoneListener phoneStateListener;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from = "";
            String msgBody = "";

            Date date = new Date();

            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                    }
                    Log.d("pttt", msgBody);
                    Log.d("pttt", msg_from);
                    Log.d("pttt", String.valueOf(date));
                } catch (Exception e) {
                }
            }
        } else if(intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(context.TELEPHONY_SERVICE);
            if (phoneStateListener == null) {
                phoneStateListener = new StatePhoneListener(context);
                manager.listen(phoneStateListener,
                        android.telephony.PhoneStateListener.LISTEN_CALL_STATE);
            }
         }
    }
}
