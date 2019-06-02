package com.example.xpandit.pushnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button sendPushNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("PUSHNOTIFICATION");
        sendPushNotification = (Button) findViewById(R.id.button_sendPushNotification);
        sendPushNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(
                        "https://fcm.googleapis.com/fcm/send");

                // we already created this model class.
                // we will convert this model class to json object using google gson library.

                NotificationRequestModel notificationRequestModel = new NotificationRequestModel();
                NotificationData notificationData = new NotificationData();

                notificationData.setDetail("this is firebase push notification from java client (server)");
                notificationData.setTitle("Hello Firebase Push Notification");
                notificationRequestModel.setData(notificationData);
                notificationRequestModel.setTo("/topics/PUSHNOTIFICATION");


                Gson gson = new Gson();
                Type type = new TypeToken<NotificationRequestModel>() {
                }.getType();

                final String json = gson.toJson(notificationRequestModel, type);

                StringEntity input = null;
                try {
                    input = new StringEntity(json);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (input != null) {
                    input.setContentType("application/json");
                }
                // server key of your firebase project goes here in header field.
                // You can get it from firebase console.

                postRequest.addHeader("Authorization", "key=AAAASJCGE8g:APA91bEbt1GBFbk8FQ9RRfBNwg-AOUSNtNbook4zs8Pw9Jr0HLrrcNrNGGkAnxII3NkllAqy0rd1lZSK5xz8tw4Al-GwlsraGITlGpqp7CQFq4j0qjuBKDoD_uL5f9SlHpALAl0NDAsH");
                postRequest.setEntity(input);

                System.out.println("reques:" + json);

                String url = "https://fcm.googleapis.com/fcm/send";


                StringRequest jsonObjectRequest = new StringRequest
                        (Request.Method.POST, url, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String,String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "key=AAAASJCGE8g:APA91bEbt1GBFbk8FQ9RRfBNwg-AOUSNtNbook4zs8Pw9Jr0HLrrcNrNGGkAnxII3NkllAqy0rd1lZSK5xz8tw4Al-GwlsraGITlGpqp7CQFq4j0qjuBKDoD_uL5f9SlHpALAl0NDAsH");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return json == null ? null : json.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                            return null;
                        }
                    }
                };
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

}
