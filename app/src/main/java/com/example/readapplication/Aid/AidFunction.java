package com.example.readapplication.Aid;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class AidFunction {

    public AidFunction() {
    }

    // Check if incoming phone number exist in contact list
    public String checkIfNumberInContactList(Context context, String phoneNumber) {
        String name = "null";

        if (phoneNumber.isEmpty()) {
            return name;
        }

        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);

        if(cur.moveToFirst()) {
            // Get contact name from contact list
            name = cur.getString(2);
        }

        return name;
    }

    // Calculate call duration
    public String duration(long start_time, long end_time) {
        int total_time = (int) ((end_time - start_time) / 1000);

        if (total_time < 60) {
            return total_time + " s";
        } else if (total_time >= 60 && total_time < 3600) {
            return (total_time / 60) + " min " + (total_time % 60) + " s";
        } else {
            return (total_time / 60) + " hour " + (total_time % 60) + " min";
        }
    }
}
