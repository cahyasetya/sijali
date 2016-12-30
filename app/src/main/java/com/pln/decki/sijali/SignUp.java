package com.pln.decki.sijali;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private EditText namaLengkap;
    private EditText nip;
    private Spinner jabatan;
    private EditText noHp;
    private Spinner supervisorgi;
    private Button daftar;
    private EditText namaPanggilan;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //inisiasi elemen pada layout
        init();
    }

    private void init(){
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        namaLengkap=(EditText)findViewById(R.id.namalengkap);
        namaPanggilan=(EditText)findViewById(R.id.namapanggilan);
        jabatan=(Spinner)findViewById(R.id.jabatan);
        supervisorgi=(Spinner)findViewById(R.id.supervisorgi);
        nip=(EditText)findViewById(R.id.nip);
        noHp=(EditText)findViewById(R.id.nohp);
        daftar=(Button)findViewById(R.id.daftar);
        daftar.setOnClickListener(this);

        //sembunyikan password
        password.setTransformationMethod(new PasswordTransformationMethod());

        //handler untuk dropdown jabaan
        jabatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("Supervisor GI")){
                    supervisorgi.setVisibility(View.VISIBLE);
                }else{
                    supervisorgi.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.daftar:
                //daftar event handler

                //deklarasi variabel anggota yang akan mendaftar
                final String usernameString;
                final String passwordString;
                final String namalengkapString;
                final String namaPanggilanString;
                final String nipString;
                final String jabatanString;
                final String nohpString;

                //validasi form
                FormValidator formValidator=new FormValidator();
                if(!formValidator.validateNamaLengkap(namaLengkap)){
                    break;
                }else if(!formValidator.validateNamaLengkap(namaPanggilan)){
                    break;
                }else if(!formValidator.validateRequiredEditText(nip)){
                    break;
                }else if(!formValidator.validateNoHp(noHp)){
                    break;
                }else if(!formValidator.validateUsername(username)){
                    break;
                }else if(!formValidator.validatePassword(password)){
                    break;
                }


                //assign nilai ke variabel
                usernameString=username.getText().toString();
                passwordString=password.getText().toString();
                namalengkapString=namaLengkap.getText().toString();
                namaPanggilanString=namaPanggilan.getText().toString();
                nipString=nip.getText().toString();
                nohpString=noHp.getText().toString();

                if(jabatan.getSelectedItem().equals("Supervisor GI")) jabatanString=supervisorgi.getSelectedItem().toString();
                else jabatanString=jabatan.getSelectedItem().toString();


                //kirim request ke server
                String url="http://sijali.developer.my.id/register.php";//"http://192.168.1.10/sijali/register.php";

                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")){
                            //username sudah terpakai
                            Toast.makeText(SignUp.this, "Username yang dipilih sudah digunakan pengguna lain", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }else if(response.equals("1")){
                            //berhasil mendaftar
                            pd.dismiss();
                            SharedPreferences preferences=getSharedPreferences("sijali", 0);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putBoolean("pemberitahuan", true);
                            editor.commit();
                            startActivity(new Intent(SignUp.this, BerhasilDaftar.class));
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error handler
                        try {
                            error.printStackTrace();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            Log.d("errorSignUp", "Error volley");
                        }
                        Toast.makeText(SignUp.this, "Error jaringan", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }){
                    @Override
                    //parameter yang dikirim ke server
                    protected Map<String, String> getParams(){

                        Map<String,String> params=new HashMap<>();
                        params.put("username", usernameString);
                        params.put("password", passwordString);
                        params.put("namalengkap", namalengkapString);
                        params.put("namapanggilan", namaPanggilanString);
                        params.put("nip", nipString);
                        params.put("jabatan", jabatanString);
                        params.put("nohp", nohpString);
                        params.put("status", "Siap");
                        return params;
                    }
                };

                RequestQueue queue= Volley.newRequestQueue(this);
                queue.add(request);

                pd=new ProgressDialog(this);
                pd.setMessage("Loading...");
                pd.show();

                break;
        }
    }
}
