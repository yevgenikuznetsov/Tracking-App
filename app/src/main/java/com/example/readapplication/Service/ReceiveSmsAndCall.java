package com.example.readapplication.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.readapplication.Object.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiveSmsAndCall extends BroadcastReceiver {
    private String pattern = "MM.dd.yyyy HH:mm:ss";
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

            DateFormat df = new SimpleDateFormat(pattern);
            message.setMessageTime(df.format(new Date()));

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
                    message.setExist(checkIfNumberInContactList(context, message.getSender()));

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

    private void saveMessage() {
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(message.getSender().contains("+972")){
            String newNumber = "0" + message.getSender().substring(4,13);
            message.setSender(newNumber);
        }

        mDatabase.child("SMS/").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/").child(mDatabase.push().getKey() + "/").setValue(message);
    }

    private boolean checkIfNumberInContactList(Context context, String phoneNumber) {
        if(phoneNumber.isEmpty()) {
            return false;
        }

        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);

        return cur.moveToFirst();
    }


}
