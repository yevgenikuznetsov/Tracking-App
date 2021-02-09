package com.example.readapplication.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.example.readapplication.Aid.AidFunction;
import com.example.readapplication.Object.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiveSmsAndCall extends BroadcastReceiver {
    private String pattern = "dd.MM.yyyy HH:mm:ss";

    private StatePhoneListener phoneStateListener;
    private Message message;
    private AidFunction aidFunction;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Receive incoming SMS
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();

            SmsMessage[] msgs = null;
            String msg_from = "";
            String msgBody = "";

            message = new Message();
            aidFunction = new AidFunction();

            DateFormat df = new SimpleDateFormat(pattern);
            message.setMessageTime(df.format(new Date()));

            if (bundle != null) {
                try {
                    // Read the contents of the message
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                    }

                    message.setSenderNumber(msg_from);
                    message.setMessage(msgBody);

                    String name = aidFunction.checkIfNumberInContactList(context, message.getSenderNumber());

                    message.setExist(!name.equals("null"));
                    message.setSenderName(name);

                    // Save incoming SMS to DB
                    saveMessage();

                } catch (Exception e) {
                }
            }
            // Receive incoming Call
        } else if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(context.TELEPHONY_SERVICE);

            // Open only one phone listener
            if (phoneStateListener == null) {
                phoneStateListener = new StatePhoneListener(context);
                manager.listen(phoneStateListener,
                        android.telephony.PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }

    private void saveMessage() {
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (message.getSenderNumber().contains("+972")) {
            String newNumber = "0" + message.getSenderNumber().substring(4, 13);
            message.setSenderNumber(newNumber);
        }

        // Save to DB
        mDatabase.child("SMS/").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/").child(mDatabase.push().getKey() + "/").setValue(message);
    }
}
