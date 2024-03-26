package com.example.autophonemessage;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static String baseUrl = "";
    static String baseUrl1 = "";
    static String baseUrl2 = "";

    static String myNum = "";

    static String replyMsg = "";

    static Map<String, String> clientNameNumbers = new HashMap<>();

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 300000;

    public static boolean switchOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, baseUrl, Toast.LENGTH_SHORT).show();

        Button urlBtn = findViewById(R.id.urlBtn);
        Button urlBtn1 = findViewById(R.id.urlBtn1);
        Button urlBtn2 = findViewById(R.id.urlBtn2);
        Button urlBtn3 = findViewById(R.id.urlBtn3);

        EditText urlText = findViewById(R.id.urlText);
        EditText urlText1 = findViewById(R.id.urlText1);
        EditText urlText2 = findViewById(R.id.urlText2);
        EditText urlText3 = findViewById(R.id.urlText3);

        baseUrl = sharedPreferences.getString("url", "not found");
        baseUrl1 = sharedPreferences.getString("url1", "not found");
        baseUrl2 = sharedPreferences.getString("url2", "not found");

        String[] readPhoneNumber_Permission = {
                Manifest.permission.READ_PHONE_NUMBERS
        };

        requestPermissions(readPhoneNumber_Permission, 102);

        Switch sw = findViewById(R.id.switch1);
        Switch sw1 = findViewById(R.id.switch2);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    MainActivity.switchOn = true;

                    checkPhoneCallStatePermission();
                    checkPhoneCallEndPermission();
                    checkPhoneCallReadLog();
                    checkInternet();
                    checkNetworkState();
                    checkSendSms();
                    checkReceiveSms();
                    checkReadSms();

                    setThisPhoneNumber();

                    baseUrl = sharedPreferences.getString("url", "not found");
                    baseUrl1 = sharedPreferences.getString("url1", "not found");
                    baseUrl2 = sharedPreferences.getString("url2", "not found");

                    setClientNumbers(baseUrl, true, false);



                }
                else {
                    MainActivity.switchOn = false;

                }
            }
        });

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    baseUrl = sharedPreferences.getString("url", "not found");
                    baseUrl1 = sharedPreferences.getString("url1", "not found");
                    baseUrl2 = sharedPreferences.getString("url2", "not found");
                    replyMsg = sharedPreferences.getString("replyMsg", "not found");

                    urlText.setText(baseUrl);
                    urlText1.setText(baseUrl1);
                    urlText2.setText(baseUrl2);
                    urlText3.setText(replyMsg);


                }
            }
        });

        urlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("url", urlText.getText().toString()).apply();

            }
        });

        urlBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("url1", urlText1.getText().toString()).apply();

            }
        });

        urlBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("url2", urlText2.getText().toString()).apply();


            }
        });

        urlBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("replyMsg", urlText3.getText().toString()).apply();
            }
        });
    }

//    @Override
//    protected void onResume() {
//
//        super.onResume();
//
//        handler.postDelayed(runnable = new Runnable() {
//            public void run() {
//                handler.postDelayed(runnable, delay);
//
//                try {
//
//                    getURL();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, delay);
//
//    }

    void checkPhoneCallStatePermission() {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

    }

    void checkPhoneCallEndPermission() {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, 1000);


        }

    }

    void checkPhoneCallReadLog() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, 1000);

        }

    }

    void checkInternet() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1000);

        }

    }

    void checkNetworkState() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1000);

        }

    }

    void checkSendSms() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1000);

        }

    }

    void checkReceiveSms() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }
        else {
            Toast.makeText(this, "receive allowed", Toast.LENGTH_SHORT).show();
        }
    }

    void checkReadSms() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED );
        }
        else {
            Toast.makeText(this, "receive allowed", Toast.LENGTH_SHORT).show();
        }
    }

    void setThisPhoneNumber() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myNum = telephonyManager.getLine1Number();

        Toast.makeText(this, myNum, Toast.LENGTH_SHORT).show();
    }

    void setClientNumbers(String baseUrl, boolean isFirst, boolean isSecond) {

        String url = baseUrl + "?action=getClients";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String name = jsonObject.getString("name");
                        String clientNum = jsonObject.getString("number");

                        clientNameNumbers.put(name, clientNum);

                    }

                    Toast.makeText(MainActivity.this, response.length() + "개의 전화번호가 저장됐습니다.", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "not work again", Toast.LENGTH_LONG).show();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFirst) {
                    Toast.makeText(MainActivity.this, "first url failed", Toast.LENGTH_LONG).show();
                    setClientNumbers(baseUrl1, false, true);
                }
                else if (isSecond) {
                    Toast.makeText(MainActivity.this, "second url failed", Toast.LENGTH_LONG).show();
                    setClientNumbers(baseUrl2, false, false);
                }
                else {
                    Toast.makeText(MainActivity.this, "failed all", Toast.LENGTH_LONG).show();
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        queue.add(jsonArrayRequest);

    }
}