package com.example.autophonemessage;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class Message {

    String msgGotTime = "";
    String msgRepliedTime = "";

    GoogleSheetOperations googleSheetOperations = new GoogleSheetOperations();
    void sendMessage(String number, String message) {

        try {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(number,null, message,null,null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void checkMessageContains(String message, Context context, String othersNum, String msgGotTime, String baseUrl) {

        this.msgGotTime = msgGotTime;

        String url = baseUrl + "?action=getClients";

        try {

            boolean isContained = false;

            int i = 0;
            int size = MainActivity.clientNameNumbers.size();

            for ( String key : MainActivity.clientNameNumbers.keySet() ) {
                i++;
                if (key != "" && key != null) {
                    if (message.contains(key)) {
                        isContained = true;
                        String clientNum = MainActivity.clientNameNumbers.get(key);
                        Toast.makeText(context, clientNum, Toast.LENGTH_SHORT).show();
                        sendClientNumber(key, othersNum, clientNum, message, true, context);
                    }
                }
                if (i == size) {
                    if (!isContained) sendClientNumber("", othersNum, "", message,false, context);
                }
            }

        } catch (Exception e) {

            checkMessageContains(message, context, othersNum, msgGotTime, MainActivity.baseUrl1);
            checkMessageContains(message, context, othersNum, msgGotTime, MainActivity.baseUrl2);
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();

        }

//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                boolean isContained = false;
//
//                try {
//                    for (int i = 0; i < response.length(); i++) {
//                        JSONObject jsonObject = response.getJSONObject(i);
//
//                        String name = jsonObject.getString("name");
//
//                        if (name == null || name == "") {
//                        }
//                        else {
//                            if (message.contains(name)) {
//                                isContained = true;
//                                String clientNum = jsonObject.getString("number");
//                                Toast.makeText(context, clientNum, Toast.LENGTH_SHORT).show();
//                                sendClientNumber(name, othersNum, clientNum, message, true, context);
//                            }
//                        }
//                        if (i == response.length() - 1) {
//                            if (!isContained) sendClientNumber("", othersNum, "", message,false, context);
//                        }
//                    }
//                } catch (JSONException e) {
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                checkMessageContains(message, context, othersNum, msgGotTime, MainActivity.baseUrl1);
//                checkMessageContains(message, context, othersNum, msgGotTime, MainActivity.baseUrl2);
//                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        queue.add(jsonArrayRequest);

    }

    void sendClientNumber(String name, String othersNum, String clientNum, String msgGot, boolean isReplied, Context context) {

        String myNum = MainActivity.myNum;
        String url = MainActivity.baseUrl + "?action=getReplyMessage";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject jsonObject = response.getJSONObject(0);

                    String reply = jsonObject.getString("reply");

                    String form = reply.replace("홍길동", name);
                    form = form.replace("000-0000-0000", clientNum);

                    Toast.makeText(context, form, Toast.LENGTH_SHORT).show();

                    if (isReplied) {
                        sendMessage(othersNum, form);
                        msgRepliedTime = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(new java.util.Date());

//                        final String finalForm = form;
//                        Thread t1 = new Thread(new Runnable() {
//                            @Override
//                            public void run() {

                        googleSheetOperations.writeDataOnSheets(true, myNum, othersNum, msgGotTime , form, msgGot, msgRepliedTime, context, MainActivity.baseUrl);
//
//                            }
//                        });

//                        Thread t2 = new Thread(new Runnable() {
//                            @Override
//                            public void run() {

                        googleSheetOperations.writeDataOnSheets(true, myNum, othersNum, msgGotTime ,form, msgGot, msgRepliedTime, context, MainActivity.baseUrl1);
//
//                            }
//                        });

//                        Thread t3 = new Thread(new Runnable() {
//                            @Override
//                            public void run() {

                        googleSheetOperations.writeDataOnSheets(true, myNum, othersNum, msgGotTime ,form, msgGot, msgRepliedTime, context, MainActivity.baseUrl2);

//                            }
//                        });
//
//                        t1.start();
//                        t2.start();
//                        t3.start();
                    }
                    else {
//                        Thread t1 = new Thread(new Runnable() {
//                            @Override
//                            public void run() {

                        googleSheetOperations.writeDataOnSheets(false, myNum, othersNum, msgGotTime,"", msgGot, "", context, MainActivity.baseUrl);
//
//                            }
//                        });
//
//                        Thread t2 = new Thread(new Runnable() {
//                            @Override
//                            public void run() {

                        googleSheetOperations.writeDataOnSheets(false, myNum, othersNum, msgGotTime,"", msgGot, "", context, MainActivity.baseUrl);
//
//                            }
//                        });
//
//                        Thread t3 = new Thread(new Runnable() {
//                            @Override
//                            public void run() {

                        googleSheetOperations.writeDataOnSheets(false, myNum, othersNum, msgGotTime,"", msgGot, "", context, MainActivity.baseUrl);

//                            }
//                        });
//
//                        t1.start();
//                        t2.start();
//                        t3.start();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);

        queue.add(jsonArrayRequest);

    }

}
