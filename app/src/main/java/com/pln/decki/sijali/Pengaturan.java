package com.pln.decki.sijali;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pln.decki.sijali.object.Anggota;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pengaturan extends Fragment implements View.OnClickListener{

    SharedPreferences preferences;
    Button hidup;
    Button mati;
    Button siap;
    Button tidakSiap;
    Button updatebutton;
    EditText keterangan;
    String status;
    Anggota anggota;
    Gson gson;
    ProgressDialog pd;

    public Pengaturan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pengaturan, container, false);

        //inisiasi komponen
        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(false);

        hidup=(Button)v.findViewById(R.id.pemberitahuan_hidup);
        mati=(Button)v.findViewById(R.id.pemberitahuan_mati);
        siap=(Button)v.findViewById(R.id.siapbutton);
        tidakSiap=(Button)v.findViewById(R.id.tidaksiapbutton);
        updatebutton=(Button)v.findViewById(R.id.updatebutton);
        keterangan=(EditText)v.findViewById(R.id.keterangan);

        hidup.setOnClickListener(this);
        mati.setOnClickListener(this);
        siap.setOnClickListener(this);
        tidakSiap.setOnClickListener(this);
        updatebutton.setOnClickListener(this);

        //handler untuk pemberitahuan/notifikasi
        preferences=getContext().getSharedPreferences("sijali",0);
        if (preferences.getBoolean("pemberitahuan",false)==true){
            hidup.setBackgroundResource(R.drawable.kiri_biru);
            mati.setBackgroundResource(R.drawable.kanan_putih);
        }else {
            hidup.setBackgroundResource(R.drawable.kiri_putih);
            mati.setBackgroundResource(R.drawable.kanan_merah);
        }

        //handler untuk kesiapan anggota
        gson=new Gson();
        String anggotaString=preferences.getString("anggota", null);
        anggota=gson.fromJson(anggotaString,Anggota.class);
        status=anggota.getStatus();
        if(anggota.getStatus().equals("Siap")){
            siap.setBackgroundResource(R.drawable.kiri_biru);
            tidakSiap.setBackgroundResource(R.drawable.kanan_putih);
        }else {
            siap.setBackgroundResource(R.drawable.kiri_putih);
            tidakSiap.setBackgroundResource(R.drawable.kanan_merah);
        }

        if(anggota.getKeterangan()==null||anggota.getKeterangan().equals(null)){
            keterangan.setText("Keterangan belum diatur");
        }else keterangan.setText(anggota.getKeterangan());
        return v;
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.pemberitahuan_hidup:
                //handler saat tombol pemberitahuan hidup diklik
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("pemberitahuan", true);
                editor.commit();

                hidup.setBackgroundResource(R.drawable.kiri_biru);
                mati.setBackgroundResource(R.drawable.kanan_putih);

                break;
            case R.id.pemberitahuan_mati:
                //handler saat tombol pemberitahuan mati diklik
                editor=preferences.edit();
                editor.putBoolean("pemberitahuan", false);
                editor.commit();

                hidup.setBackgroundResource(R.drawable.kiri_putih);
                mati.setBackgroundResource(R.drawable.kanan_merah);
                break;
            case R.id.siapbutton:
                //handler saat tombol siap diklik
                status="Siap";

                siap.setBackgroundResource(R.drawable.kiri_biru);
                tidakSiap.setBackgroundResource(R.drawable.kanan_putih);
                break;
            case R.id.tidaksiapbutton:
                //handler saat tombol tidak siap diklik
                status="Tidak Siap";

                siap.setBackgroundResource(R.drawable.kiri_putih);
                tidakSiap.setBackgroundResource(R.drawable.kanan_merah);
                break;
            case R.id.updatebutton:

                //handler untuk update
                //kirim request update ke server
                String url="http://sijali.developer.my.id/updateanggota.php";//"http://192.168.1.101/sijali/updateanggota.php";
                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {
                            //berhasil update data user
                            anggota.setStatus(status);
                            anggota.setKeterangan(keterangan.getText().toString());

                            SharedPreferences.Editor editor1=preferences.edit();
                            String anggotaJson=gson.toJson(anggota);
                            editor1.putString("anggota", anggotaJson);
                            editor1.commit();

                            Toast.makeText(getContext(), "Berhasil memperbarui data", Toast.LENGTH_LONG).show();
                        } else {
                            //gagal update data server
                            Toast.makeText(getContext(), "Gagal memperbarui data", Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handler error jaringan
                        Toast.makeText(getContext(), "Error jaringan", Toast.LENGTH_LONG).show();
                        try{
                            Log.d("errorPengaturan", error.getMessage().toString());
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        pd.dismiss();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params=new HashMap<>();
                        params.put("username", anggota.getUsername());
                        params.put("status", status);
                        params.put("keterangan", keterangan.getText().toString());
                        return params;
                    }
                };

                RequestQueue queue= Volley.newRequestQueue(getContext());
                queue.add(request);
                pd.show();

                break;
        }
    }
}
