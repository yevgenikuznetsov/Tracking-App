package com.example.readapplication.Service;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.readapplication.Object.Call;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class StatePhoneListener extends PhoneStateListener {

    private Context context;
    private Call call;
    private long start_time, end_time;

    private int lastState = TelephonyManager.CALL_STATE_IDLE;

    private boolean isIncoming = false;
    private String pattern = "MM.dd.yyyy HH:mm:ss";

    public StatePhoneListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        DateFormat df = new SimpleDateFormat(pattern);

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                call = new Call();
                call.setCallNumber(incomingNumber);

                call.setCallStartTime(df.format(new Date()));
                isIncoming = true;
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                start_time = System.currentTimeMillis();
                call.setCallStartTime(df.format(new Date()));
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    call.setDuration("Not Answer");
                } else if (isIncoming){
                    end_time = System.currentTimeMillis();
                    call.setDuration(duration());
                    isIncoming = false;
                }

                call.setExist(checkIfNumberInContactList(context, call.getCallNumber()));

                saveCallToDB(call);
                break;
        }

        lastState = state;
    }

    private String duration() {
        int total_time = (int)((end_time - start_time) / 1000 );

        if(total_time < 60) {
            return total_time + " s";
        } else if(total_time >= 60 && total_time < 3600) {
            return (total_time / 60) + " min " + (total_time % 60) + " s";
        }else {
            return (total_time / 60) + " hour " + (total_time % 60) + "min";
        }
    }

    private void saveCallToDB(Call call) {
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("CALL/").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/").child(mDatabase.push().getKey() + "/").setValue(call);

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
