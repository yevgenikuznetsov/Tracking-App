package com.example.readapplication;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;

public class StatePhoneListener extends PhoneStateListener {

    private Context context;
    private Call call;
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
                call = new Call();
                call.setCallNumber(incomingNumber);
                isIncoming = true;
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                call.setCallStartTime(new Date());
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    call.setCallStartTime(new Date());
                    call.setCallSEndTime(call.getCallStartTime());
                } else if (isIncoming){
                    call.setCallSEndTime(new Date());
                    isIncoming = false;
                }

                saveCallToDB(call);
                break;
        }

        lastState = state;
    }

    private void saveCallToDB(Call call) {
        Log.d("pttt", "call number: " + call.getCallNumber() + " start time: " + call.getCallStartTime() + " ent time: " + call.getCallSEndTime());
    }

}
