package com.example.autophonemessage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhoneCall {

    Message message = new Message();

    GoogleSheetOperations googleSheetOperations = new GoogleSheetOperations();

    String endCall(Context context, Intent intent) throws InterruptedException {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {

            TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

            if (tm != null) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {

                    String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                    tm.endCall();

                    if (number == "" || number.isEmpty() || number == null) {
                        Toast.makeText(context, "number is null", Toast.LENGTH_SHORT).show();
                        return "";
                    }
                    else {
                        Toast.makeText(context, number + "My Number", Toast.LENGTH_SHORT).show();
                        return number;
//                        doByPhoneNumber(number, context);
                    }
                }
                else {
                    return "";
                }
            }
            else {
                return "";
            }
        }
        else {
            return "";
        }
    }

    boolean doByPhoneNumber(String phoneNumber, Context context) {

        if (phoneNumber != null) {

            String[] split = phoneNumber.split("");
            String number = split[0] + split[1] + split[2] + split[3] + split[4] + split[5] + split[6];

            Toast.makeText(context, number, Toast.LENGTH_SHORT).show();

            if (number.contains("010") || number.contains("*77010") || number.contains("8210") || number.contains("82010") || number.contains("10") || number.contains("+821")) {
                Toast.makeText(context, "execute", Toast.LENGTH_SHORT).show();

                return true;
            }
            else {
                Log.d("PhoneCall", "just end call");
                return false;
            }

        }
        else {
            Toast.makeText(context, "number is null", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    void sendEndCallMessage(Context context, String baseUrl, String othersPhoneNumber) {

        String url = baseUrl + "?action=getEndMessage";

        try {

            message.sendMessage(othersPhoneNumber, MainActivity.replyMsg);

//            Thread t1 = new Thread(new Runnable() {
//                @Override
//                public void run() {
            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, true, MainActivity.baseUrl);
//                }
//            });
//            Thread t2 = new Thread(new Runnable() {
//                @Override
//                public void run() {
            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, true, MainActivity.baseUrl1);
//                }
//            });
//            Thread t3 = new Thread(new Runnable() {
//                @Override
//                public void run() {
            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, true, MainActivity.baseUrl2);
//                }
//            });
//
//            t1.start();
//            t2.start();
//            t3.start();

        } catch (Exception e) {

//            Thread t1 = new Thread(new Runnable() {
//                @Override
//                public void run() {
            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, false, MainActivity.baseUrl);
//                }
//            });
//            Thread t2 = new Thread(new Runnable() {
//                @Override
//                public void run() {
            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, false, MainActivity.baseUrl1);
//                }
//            });
//            Thread t3 = new Thread(new Runnable() {
//                @Override
//                public void run() {
            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, false, MainActivity.baseUrl2);
//                }
//            });
//
//            t1.start();
//            t2.start();
//            t3.start();

        }

//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                try {
//                    JSONObject jsonObject = response.getJSONObject(0);
//
//                    String reply = jsonObject.getString("reply");
//
//                    Toast.makeText(context, reply, Toast.LENGTH_SHORT).show();
//
//                    message.sendMessage(othersPhoneNumber, reply);
//
//                    Thread t1 = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, true, MainActivity.baseUrl);
//                        }
//                    });
//                    Thread t2 = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, true, MainActivity.baseUrl1);
//                        }
//                    });
//                    Thread t3 = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, true, MainActivity.baseUrl2);
//                        }
//                    });
//
//                    t1.start();
//                    t2.start();
//                    t3.start();
//
//                } catch (JSONException e) {
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
//                    Thread t1 = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, false, MainActivity.baseUrl);
//                        }
//                    });
//                    Thread t2 = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, false, MainActivity.baseUrl1);
//                        }
//                    });
//                    Thread t3 = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            googleSheetOperations.writePhoneCallStatus(MainActivity.myNum, othersPhoneNumber, context, false, MainActivity.baseUrl2);
//                        }
//                    });
//
//                    t1.start();
//                    t2.start();
//                    t3.start();
//                }
//
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        queue.add(jsonArrayRequest);

    }

}