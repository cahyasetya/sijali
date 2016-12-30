package com.pln.decki.sijali;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cahya on 9/8/16.
 */
public class JSONRequest {
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    ProgressDialog pd;

    public JSONRequest(Context context){
        pd=new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
    }

    public void jsonArrayRequest(final Context context, int method, String url, @Nullable JSONArray param, final JSONRequest jsonRequest){
        JsonArrayRequest request=new JsonArrayRequest(method, url, param, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                jsonRequest.setJsonArray(response);
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                Log.d("ErrorAnggotaSiap", error.getMessage().toString());
                new JSONRequest(context).setJsonArray(null);
                pd.dismiss();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(request);

        pd.show();
    }

    public void jsonObjectRequest(final Context context, int method, String url, @Nullable JSONObject params){
        JsonObjectRequest request=new JsonObjectRequest(method, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                new JSONRequest(context).setJsonObject(response);
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new JSONRequest(context).setJsonObject(null);
                pd.dismiss();
            }
        });
    }

    public void setJsonArray(JSONArray jsonArray){
        this.jsonArray=jsonArray;
    }

    public void setJsonObject(JSONObject jsonObject){
        this.jsonObject=jsonObject;
    }

    public JSONArray getJsonArray(){
        return this.jsonArray;
    }

    public JSONObject getJsonObject(){
        return this.jsonObject;
    }

}
