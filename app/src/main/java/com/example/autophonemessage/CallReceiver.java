package com.example.autophonemessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {

    PhoneCall phoneCall = new PhoneCall();

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        MainActivity.baseUrl = sharedPreferences.getString("url", "not found");
        MainActivity.baseUrl1 = sharedPreferences.getString("url1", "not found");
        MainActivity.baseUrl2 = sharedPreferences.getString("url2", "not found");
        MainActivity.replyMsg = sharedPreferences.getString("replyMsg", "not found");


        if (MainActivity.switchOn) {
            try {
                String number = phoneCall.endCall(context, intent);
                if (number == "" || number.isEmpty() || number == null) {
                    number = getLastNumber(context);
                    if (number == "" || number.isEmpty() || number == null) {
                        Toast.makeText(context, "failed to send msg", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        boolean isContain = phoneCall.doByPhoneNumber(number, context);
                        if (isContain) {
                            phoneCall.sendEndCallMessage(context, MainActivity.baseUrl, number);
                        }
                    }
                }
                else {
                    boolean isContain = phoneCall.doByPhoneNumber(number, context);
                    if (isContain) {
                        phoneCall.sendEndCallMessage(context, MainActivity.baseUrl, number);
                    }
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    String getLastNumber(Context context) {
        String lastNumber;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Uri uriCallLogs = Uri.parse("content://call_log/calls");
            Cursor cursor = context.getContentResolver().query(uriCallLogs, null, null, null);

            cursor.moveToLast();

            lastNumber = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));

            return lastNumber;
        }
        return "";

    }

}
