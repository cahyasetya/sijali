package com.pln.decki.sijali;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.pln.decki.sijali.object.Anggota;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cahya on 9/19/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirebaseIIDService";
    ProgressDialog pd;

    @Override
    public void  onTokenRefresh(){
        // Get updated InstanceID token.
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("sijali",0);
        Gson gson=new Gson();
        Anggota anggota=gson.fromJson(preferences.getString("anggota",null),Anggota.class);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        String username=null;
        try{
            username = anggota.getUsername();
            sendRegistrationToServer(username, refreshedToken);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    private void sendRegistrationToServer(final String username, final String token){
        String url="http://sijali.developer.my.id/updatetoken.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Success update token: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendRegistrationToServer(username,token);
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params=new HashMap<>();
                params.put("username", username);
                params.put("token",token);
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
