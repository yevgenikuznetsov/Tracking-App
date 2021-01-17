package com.example.readapplication;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;

public class StatePhoneListener extends PhoneStateListener {

    private Context context;

    private Date callStartTime;
    private Date callSEndTime;
    private int lastState = TelephonyManager.CALL_STATE_IDLE;
    private boolean isIncoming = false;

    public StatePhoneListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d("pttt", "Ring ");
                isIncoming = true;
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                callStartTime = new Date();
                Log.d("pttt", " answer , number: " + incomingNumber + " time: " + callStartTime);
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    Log.d("pttt", "Ring but no pickup");
                } else if (isIncoming){
                    callSEndTime = new Date();
                    Log.d("pttt", "end , number: " + incomingNumber + " time: " + callSEndTime);
                    isIncoming = false;
                    break;
                }
        }

    }

}
