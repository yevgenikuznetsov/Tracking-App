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
    private Message message;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from = "";
            String msgBody = "";

            message = new Message();
            message.setMessageTime(new Date());

            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                    }

                    message.setSender(msg_from);
                    message.setMessage(msgBody);

                    saveMessage();

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

    void saveMessage() {
        if(message.getSender().contains("+972")){
            String newNumber = "0" + message.getSender().substring(4,13);
            message.setSender(newNumber);
        }
    }
}
