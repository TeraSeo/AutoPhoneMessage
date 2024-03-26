package com.example.autophonemessage;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

public class GoogleSheetOperations {


    void writeDataOnSheets(boolean isReplied, String myNum, String othersNum, String msgArrivalTime, String repliedMsg, String msgGot, String msgSentTime, Context context, String baseUrl) {

        String url = baseUrl + "?action=create";

        try {
            String repliedCheck;

            if (isReplied) {
                repliedCheck = "O";
            }
            else {
                repliedCheck = "X";
            }

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("myPhoneNum", myNum);
            jsonBody.put("othersNum", othersNum);
            jsonBody.put("smsArrivalTime", msgArrivalTime);
            jsonBody.put("msgGot", msgGot);
            jsonBody.put("isReplied", repliedCheck);
            jsonBody.put("repliedMsg", repliedMsg);
            jsonBody.put("msgSentTime", msgSentTime);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

//            writeOperationTime(MainActivity.myNum, currentTime, context);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void writeOperationTime(String myNum, String operatedTime, Context context) {

        String url = MainActivity.baseUrl + "?action=createOperationTime";

        try {

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("myPhoneNum", myNum);
            jsonBody.put("operatedTime", operatedTime);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void writePhoneCallStatus(String myNum, String othersNum, Context context, boolean isSuccess, String baseUrl) {

        String url = baseUrl + "?action=writePhoneCallStatus";
//        String currentTime = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(new java.util.Date());

        try {

            JSONObject jsonBody = new JSONObject();

            if (isSuccess) {
                jsonBody.put("myPhoneNum", myNum);
                jsonBody.put("othersNum", othersNum);
                jsonBody.put("isSuccess", "O");
                jsonBody.put("operatedTime", new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(new java.util.Date()));
            }
            else {
                jsonBody.put("myPhoneNum", myNum);
                jsonBody.put("othersNum", othersNum);
                jsonBody.put("isSuccess", "X");
                jsonBody.put("operatedTime", new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(new java.util.Date()));
            }

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

//            writeOperationTime(MainActivity.myNum, currentTime, context);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
