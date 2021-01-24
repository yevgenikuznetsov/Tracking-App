package com.example.readapplication.Object;

import java.util.Date;

public class Call {
    private String callNumber;
    private Date callStartTime;
    private Date callSEndTime;

    public Call() {

    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public Date getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Date callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Date getCallSEndTime() {
        return callSEndTime;
    }

    public void setCallSEndTime(Date callSEndTime) {
        this.callSEndTime = callSEndTime;
    }
}
