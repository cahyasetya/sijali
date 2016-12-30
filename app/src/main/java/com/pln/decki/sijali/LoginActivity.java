package com.pln.decki.sijali;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.pln.decki.sijali.object.Anggota;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signin;
    private Button signup;

    ProgressDialog pd;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        preferences=getSharedPreferences("sijali", 0);

        //cek apakah user sudah log in
        if (preferences.getBoolean("IsLogedIn", false)==true){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        //inisiasi tiap elemen pada layout
        init();

    }

    private void init(){
        usernameEditText=(EditText) findViewById(R.id.username);
        passwordEditText=(EditText) findViewById(R.id.password);
        signin=(Button)findViewById(R.id.signin);
        signup=(Button)findViewById(R.id.signup);
        signin.setOnClickListener(this);
        signup.setOnClickListener(this);

        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.signin:
                //sign in event handler

                //validasi form & kirim request
                FormValidator formValidator=new FormValidator();
                if(!formValidator.validateRequiredEditText(usernameEditText)){
                    usernameEditText.setError("Username harus diisi");
                    usernameEditText.requestFocus();
                    break;
                }else if(!formValidator.validateRequiredEditText(passwordEditText)){
                    passwordEditText.setError("Password harus diisi");
                    passwordEditText.requestFocus();
                    break;
                }else{
                    //buat parameter untuk request login
                    JSONObject params=new JSONObject();
                    try{
                        params.put("username", usernameEditText.getText().toString());
                        params.put("password", passwordEditText.getText().toString());
                        params.put("token", FirebaseInstanceId.getInstance().getToken());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    //kirim request
                    String url="http://sijali.developer.my.id/login.php";//"http://192.168.1.101/sijali/login.php";
                    final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,url , params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                if(response.getString("status").equals("unregistered")){
                                    //tidak terdaftar
                                    Toast.makeText(LoginActivity.this, "Anda belum terdaftar", Toast.LENGTH_LONG).show();
                                }else if(response.getString("status").equals("registered")){
                                    //buat variabel global sebagai identitas user yang sedang login
                                    Anggota anggota=new Anggota();
                                    anggota.setUsername(response.getString("username_anggota"));
                                    anggota.setNamaLengkap(response.getString("nama_lengkap_anggota"));
                                    anggota.setNip(response.getString("nip_anggota"));
                                    anggota.setJabatan(response.getString("jabatan_anggota"));
                                    anggota.setNoHp(response.getString("no_hp_anggota"));
                                    anggota.setStatus(response.getString("status_anggota"));
                                    anggota.setKeterangan(response.getString("keterangan_anggota"));
                                    anggota.setHak_akses_anggota(response.getInt("hak_akses_anggota"));

                                    Gson gson=new Gson();
                                    String json=gson.toJson(anggota);

                                    SharedPreferences.Editor editor=preferences.edit();
                                    editor.putBoolean("IsLogedIn", true);
                                    editor.putString("anggota", json);
                                    editor.putBoolean("pemberitahuan",true);
                                    editor.commit();

                                    //beralih ke layout setelah login
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }catch (JSONException e){
                                try {
                                    e.printStackTrace();
                                }catch (NullPointerException err){
                                    err.printStackTrace();
                                }
                                Toast.makeText(LoginActivity.this,"Error jaringan", Toast.LENGTH_SHORT).show();
                            }
                            pd.dismiss();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //handler untuk error jaringan
                            try {
                                error.printStackTrace();
                                Log.d("errorLoginActivity", String.valueOf(error.networkResponse.statusCode));
                            }catch (NullPointerException e){
                                Log.d("errorLoginActivity", "Error code tidak terdefinisi");
                            }

                            Toast.makeText(LoginActivity.this, "Error jaringan", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });

                    RequestQueue queue= Volley.newRequestQueue(this);
                    queue.add(request);

                    pd=new ProgressDialog(this);
                    pd.setMessage("Loading...");
                    pd.show();
                }
                break;
            case R.id.signup:
                //sign up event handler
                startActivity(new Intent(LoginActivity.this, SignUp.class));
                break;
        }
    }
}
