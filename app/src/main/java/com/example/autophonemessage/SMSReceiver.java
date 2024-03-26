package com.example.autophonemessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;

public class SMSReceiver extends BroadcastReceiver {

    Message message = new Message();

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        MainActivity.baseUrl = sharedPreferences.getString("url", "not found");
        MainActivity.baseUrl1 = sharedPreferences.getString("url1", "not found");
        MainActivity.baseUrl2 = sharedPreferences.getString("url2", "not found");
        MainActivity.replyMsg = sharedPreferences.getString("replyMsg", "not found");

        if (MainActivity.switchOn) {

            Toast.makeText(context, "get msg", Toast.LENGTH_SHORT).show();

            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION) || intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null){
                    String msgGotTime = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(new java.util.Date());
                    try{
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for(int i=0; i<msgs.length; i++){
                            msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();

                            Toast.makeText(context, msg_from + " : " + msgBody, Toast.LENGTH_SHORT).show();

                            message.checkMessageContains(msgBody, context, msg_from, msgGotTime, MainActivity.baseUrl);
                        }
                    } catch(Exception e){
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        getSmsLogs(context, msgGotTime);
                    }
                }
            }

        }

    }

    public void getSmsLogs(Context context, String msgGotTime) {
        if(ContextCompat.checkSelfPermission(context, "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            try {
                Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);
                cursor.moveToFirst();
                String lastMsg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));

                message.checkMessageContains(lastMsg, context, number, msgGotTime, MainActivity.baseUrl);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
